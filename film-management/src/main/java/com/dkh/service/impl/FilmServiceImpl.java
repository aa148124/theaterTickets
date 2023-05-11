package com.dkh.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkh.dto.Result;
import com.dkh.mapper.FilmMapper;
import com.dkh.pojo.Film;
import com.dkh.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.dkh.utils.RabbitMQConstants.DELAYED_EXCHANGE_NAME;
import static com.dkh.utils.RabbitMQConstants.FILM_KEY;
import static com.dkh.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
@Service
@Slf4j
public class FilmServiceImpl extends ServiceImpl<FilmMapper, Film> implements FilmService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FilmMapper filmMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateFilm(Film film) {
        System.out.println("\nfilm = " + film);
        //更新数据
        updateById(film);
        //删除缓存
        Set<String> keys = stringRedisTemplate.keys(CACHE_FILM_KEY + "*");
        stringRedisTemplate.delete(keys);
        stringRedisTemplate.delete(CACHE_FILM_SHOW_COUNT_KEY);
        stringRedisTemplate.delete(CACHE_FILM_SHOW_KEY);
        return new Result(200, null, "OK");
    }

    @Override
    public List<Film> searchByCondition(Long id, String filmName, Integer status, Integer current, Integer size) {
        return filmMapper.searchByCondition(id, filmName, status, current, size);
    }

    @Override
    public Result getCountByCondition(Long id, String filmName, Integer status) {
        return new Result(200, filmMapper.getCountByCondiction(id, filmName, status));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addFilm(Film film) {
        boolean save = save(film);
        if (!save){
            return new Result(400, null,"添加失败");
        }
        //发送电影id到mq中
        //计算此时到上映的时间
        long releaseTime = film.getReleaseTime().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long now = Instant.now().toEpochMilli();
        long ttl = releaseTime - now;
        send(film.getId().toString(), (int) ttl);
        //删除缓存
        Set<String> keys = stringRedisTemplate.keys(CACHE_FILM_KEY + "*");
        stringRedisTemplate.delete(keys);
        stringRedisTemplate.delete(CACHE_FILM_COUNT_KEY);
        return new Result(200, null);
    }
    public void send(String message, Integer delayTime) {
        log.info("发送一条消息："+message);
        // 三个参数
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME,
                FILM_KEY,message,
                message1 -> {
                    message1.getMessageProperties().setDelay(delayTime);
                    return message1;
                });
    }
    @Override
    public Result getShowFilm() {
        //先查询Redis的count
        String countStr = stringRedisTemplate.opsForValue().get(CACHE_FILM_SHOW_COUNT_KEY);
        //判断Redis中的是否有数据，没有则查找数据库
        int count = StringUtils.isBlank(countStr) ? (int) getShowFilmCount().getData() : Integer.valueOf(countStr);
        //先查询redis中是否有缓存，有则直接返回,无则查询数据库
        String data = stringRedisTemplate.opsForValue().get(CACHE_FILM_SHOW_KEY);
        if (StringUtils.isNotBlank(data)) {
            //将JSN数据转成对象数组
            List<Film> filmList = JSONUtil.parseArray(data).toList(Film.class);
            return new Result(200, filmList, "OK");
        }
        //查询数据库
        List<Film> filmList = query().eq("status", 0).list();
        //将信息存到Redis
        //将filmList转成JSON字符串
        data = JSONUtil.toJsonStr(filmList);
        stringRedisTemplate.opsForValue().set(CACHE_FILM_SHOW_KEY, data, CACHE_FILM_SHOW_TTL, TimeUnit.MINUTES);
        return new Result(200, filmList);
    }

    @Override
    public Result getShowFilmCount() {
        //先查询Redis
        String count = stringRedisTemplate.opsForValue().get(CACHE_FILM_SHOW_COUNT_KEY);
        if (StringUtils.isNotBlank(count)) {
            return new Result(200, Integer.valueOf(count), "OK");
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("status", 0);
        count = String.valueOf(count(qw));
        stringRedisTemplate.opsForValue().set(CACHE_FILM_SHOW_COUNT_KEY, count, CACHE_FILM_SHOW_COUNT_TTL, TimeUnit.MINUTES);
        return new Result(200, Integer.valueOf(count), "OK");
    }

    @Override
    public Result getRanking() {
        //查询redis,判断是否存在
        String data = stringRedisTemplate.opsForValue().get(CACHE_RANKING_KEY);
        if (StringUtils.isNotBlank(data)) {
            //存在则直接返回
            //将数据转成对象集合
            List<Film> filmList = JSONUtil.parseArray(data).toList(Film.class);
            return new Result(200, filmList, "OK");
        }
        //不存在查询则数据库
        //设置查询页的数据,根据状态排序
        IPage pageSet = new Page(1, 10).addOrder(OrderItem.desc("box_office"));
        page(pageSet);
        List<Film> filmList = pageSet.getRecords();
        //将数据存入redis,并设置有效期为五分钟
        stringRedisTemplate.opsForValue().set(CACHE_RANKING_KEY, JSONUtil.toJsonStr(filmList), CACHE_RANKING_TTL, TimeUnit.MINUTES);
        //返回结果
        return new Result(200, filmList);
    }

    @Override
    public Result getFutureFilm() {
        //先查询Redis的count
        String countStr = stringRedisTemplate.opsForValue().get(CACHE_FILM_FUTURE_COUNT_KEY);
        //判断Redis中的是否有数据，没有则查找数据库
        int count = StringUtils.isBlank(countStr) ? (int) getShowFilmCount().getData() : Integer.valueOf(countStr);
        //先查询redis中是否有缓存，有则直接返回,无则查询数据库
        String data = stringRedisTemplate.opsForValue().get(CACHE_FILM_FUTURE_KEY);
        if (StringUtils.isNotBlank(data)) {
            //将JSN数据转成对象数组
            List<Film> filmList = JSONUtil.parseArray(data).toList(Film.class);
            return new Result(200, filmList, "OK");
        }
        //查询数据库
        List<Film> filmList = query().eq("status", 1).list();
        //将信息存到Redis
        //将filmList转成JSON字符串
        data = JSONUtil.toJsonStr(filmList);
        stringRedisTemplate.opsForValue().set(CACHE_FILM_FUTURE_KEY, data, CACHE_FILM_FUTURE_TTL, TimeUnit.MINUTES);
        return new Result(200, filmList);
    }

    @Override
    public Result getFutureFilmCount() {
        //先查询Redis
        String count = stringRedisTemplate.opsForValue().get(CACHE_FILM_FUTURE_COUNT_KEY);
        if (StringUtils.isNotBlank(count)) {
            return new Result(200, Integer.valueOf(count), "OK");
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("status", 1);
        count = String.valueOf(count(qw));
        stringRedisTemplate.opsForValue().set(CACHE_FILM_FUTURE_COUNT_KEY, count, CACHE_FILM_FUTURE_COUNT_TTL, TimeUnit.MINUTES);
        return new Result(200, Integer.valueOf(count), "OK");
    }

    @Override
    public Result addBoxOffice(Long id, BigDecimal price) {
        System.out.println("id = " + id);
        System.out.println("price = " + price);
        update().eq("id", id).setSql("set box_office = box_office + " + price).update();
        return new Result(200, null);
    }

    @Override
    public Result searchFilm(String context) {
        //如果搜索内容为空，则查询当前正在上映的电影
        if (StringUtils.isBlank(context)){
            return getShowFilm();
        }
        List<Film> list = query().like("film_name", context).or().like("performer", context).list();
        return new Result(200, list);
    }


    @Override
    public Result getFilmList(Integer current, Integer size) {
        //如果开始查询的位置大于总数，则返回
        int start = (current - 1) * size + 1;
        //先查询Redis的count
        String countStr = stringRedisTemplate.opsForValue().get(CACHE_FILM_COUNT_KEY);
        //判断Redis中的是否有数据，没有则查找数据库
        int count = StringUtils.isBlank(countStr) ? (int) getFilmTotal().getData() : Integer.valueOf(countStr);

        if (start > count) {
            return new Result(400, null, "无数据");
        }

        //先查询redis中是否有缓存，有则直接返回,无则查询数据库
        String data = stringRedisTemplate.opsForValue().get(CACHE_FILM_KEY + current + ":" + size);
        if (StringUtils.isNotBlank(data)) {
            //将JSN数据转成对象数组
            List<Film> filmDTOList = JSONUtil.parseArray(data).toList(Film.class);
            return new Result(200, filmDTOList, "OK");
        }

        //设置查询页的数据,根据状态排序
        IPage pageSet = new Page(current, size).addOrder(OrderItem.asc("status"));
        page(pageSet);
        List<Film> filmList = pageSet.getRecords();

        //将信息存到Redis
        //将filmList转成JSON字符串
        data = JSONUtil.toJsonStr(filmList);
        stringRedisTemplate.opsForValue().set(CACHE_FILM_KEY + current + ":" + size, data, CACHE_FILM_TTL, TimeUnit.MINUTES);

        return new Result(200, filmList, "OK");
    }

    @Override
    public Result getFilmTotal() {
        //先查询Redis
        String count = stringRedisTemplate.opsForValue().get(CACHE_FILM_COUNT_KEY);
        if (StringUtils.isNotBlank(count)) {
            return new Result(200, Integer.valueOf(count), "OK");
        }

        count = String.valueOf(count());
        stringRedisTemplate.opsForValue().set(CACHE_FILM_COUNT_KEY, count, CACHE_FILM_COUNT_TTL, TimeUnit.MINUTES);

        return new Result(200, Integer.valueOf(count), "OK");
    }
}
