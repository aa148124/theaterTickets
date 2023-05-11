package com.dkh.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkh.dto.Result;
import com.dkh.mapper.ArrangeMapper;
import com.dkh.pojo.Arrange;
import com.dkh.pojo.Film;
import com.dkh.service.ArrangeService;
import com.dkh.service.FilmService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
@Service
public class ArrangeServiceImpl extends ServiceImpl<ArrangeMapper, Arrange> implements ArrangeService {

    @Resource
    private ArrangeMapper arrangeMapper;


    @Resource
    private FilmService filmService;

    @Override
    public List<Arrange> getArrangePage(Integer current, Integer size) {
        current = (current - 1) * size;
        return arrangeMapper.getArrangePage(current, size);
    }

    @Override
    public Result getArrangeTotal() {
        int count = count();
        return new Result(200, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addArrange(Arrange arrange) {
        //查询是否存在该电影
        Long filmId = arrange.getFilmId();
        Film film = filmService.query().eq("id", filmId).one();
        if (film == null) {
            return new Result(400, null, "该电影不存在");
        }
        //判断电影的状态
        Integer status = film.getStatus();
        if (status != 0) {
            //如果不在上映则返回
            return new Result(400, null, "该电影不在上映状态");
        }
        //判断放映时间是否在上映时间后
        LocalDateTime releaseTime = film.getReleaseTime();
        int i = releaseTime.compareTo(arrange.getPlayTime());
        if (i > 0) {
            return new Result(400, null, "该电影还未上映");
        }
        //保证票价>0
        if (arrange.getPrice().compareTo(new BigDecimal("0")) <= 0) {
            return new Result(400, null, "票价必须大于0");
        }
        //该影厅是否被占用
        //查询该影厅最近一场的电影
        QueryWrapper<Arrange> qw = new QueryWrapper<>();
        qw.eq("room_id",arrange.getRoomId()).orderByDesc("play_time").last("limit 1");
        Arrange arrangeLast = arrangeMapper.selectOne(qw);
        arrange.setStock(144);
        if (arrangeLast == null){
            //如果该影厅是第一次排片，则直接保存到数据库
            //更新数据库
            int save = arrangeMapper.insert(arrange);
            return new Result(200, null, "添加成功");
        }
        Long filmIdLast = arrangeLast.getFilmId();
        Film filmLast = filmService.query().eq("id", filmIdLast).one();
        //获取电影时长
        Integer durationLast = filmLast.getDuration();
        //结束时间 = 放映时间 + 电影时长 + 30分钟
        long playTimeLast = arrangeLast.getPlayTime().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long end = playTimeLast + (durationLast + 30) * 60 * 1000;
        long playTime = arrange.getPlayTime().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        //判断上映时间是否在结束时间之后
        if (playTime - end >= 0) {
            //创建位置数组
            int[][] seat = new int[12][12];
            arrange.setSeat(JSONUtil.toJsonStr(seat));
            //更新数据库
            int save = arrangeMapper.insert(arrange);
            return new Result(200, null, "添加成功");
        }
        return new Result(400, null, "该影厅已被占用");
    }

    @Override
    public Result getArrangeByFilmId(Long filmId) {
        List<Arrange> arranges = arrangeMapper.getArrangeByFilmId(filmId, LocalDateTime.now());
        return new Result(200, arranges);
    }

    @Override
    public Arrange getArrangeById(Long id) {
        Arrange arrange = arrangeMapper.getArrangeById(id);
        return arrange;
    }

    @Override
    public Result updateArrange(Arrange arrange) {
        System.out.println("============================================");
        if (arrange.getStock() < 0) {
            return new Result(400, null, "库存不足");
        }
        //获取版本号
        int version = arrange.getVersion();
        arrange.setVersion(version + 1);
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("version", version);
        uw.eq("id", arrange.getId());
        boolean isSuccess = update(arrange, uw);
        System.out.println("更新完成");
        if (isSuccess) {
            System.out.println("更新成功");
            return new Result(200, null);
        }
        return new Result(400, null);
    }
}
