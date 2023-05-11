package com.dkh.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkh.dto.CommentDTO;
import com.dkh.dto.Result;
import com.dkh.mapper.CommentMapper;
import com.dkh.pojo.Comment;
import com.dkh.pojo.User;
import com.dkh.service.CommentService;
import com.dkh.service.UserService;
import com.dkh.utils.JWTUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.dkh.utils.RedisConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserService userService;

    @Override
    public List<Comment> getCommentList(Integer current, Integer size) {
        return commentMapper.getCommentList(current, size);
    }

    /**
     * 点赞
     *
     * @param id
     * @param userId
     * @return
     */
    @Override
    public Result likeComment(Long id, Long userId) {
        //判断用户是否点过赞
        String key = "comment:liked:" + id;
        Boolean member = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        if (BooleanUtil.isFalse(member)) {
            //如果未点赞，可以点赞
            //数据库点赞+1
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            //保存用户到redis
            if (isSuccess) {
                stringRedisTemplate.opsForSet().add(key, userId.toString());
            }
        } else {
            //如果已点赞，不能
            //数据库点赞-1
            boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
            //把用户从redis中移除
            if (isSuccess) {
                stringRedisTemplate.opsForSet().remove(key, userId.toString());
            }
        }
        return Result.success(null);
    }

    /**
     * 发表评论
     *
     * @param comment
     * @return
     */
    @Override
    public Result addComment(Comment comment) {
        //设置评论时间
        comment.setCommentTime(LocalDateTime.now());
        //更新数据库
        boolean isSuccess = save(comment);
        if (!isSuccess) {
            return Result.fail(400, "评论失败");
        }
        //将评论添加到redis
        String key = "comment:liked:" + comment.getId();
        stringRedisTemplate.opsForSet().add(key, "");
        return Result.success("评论成功");
    }

    /**
     * 分页查询
     *
     * @param current
     * @param size
     * @return
     */
    @Override
    public Result getCommentListByPage(Integer current, Integer size) {
        //如果开始查询的位置大于总数，则返回
        int start = (current - 1) * size + 1;
        //先查询Redis的count
        String countStr = stringRedisTemplate.opsForValue().get(CACHE_COMMENT_COUNT_KEY);
        //判断Redis中的是否有数据，没有则查找数据库
        int count = StringUtils.isBlank(countStr) ? (int) getTotal().getData() : Integer.valueOf(countStr);

        if (start > count) {
            return Result.fail(400, "无数据");
        }

        //先查询redis中是否有缓存，有则直接返回,无则查询数据库
        String data = stringRedisTemplate.opsForValue().get(CACHE_COMMENT_KEY + current + ":" + size);
        if (StringUtils.isNotBlank(data)) {
            //将JSON数据转成对象数组
            List<CommentDTO> commentDTOList = JSONUtil.parseArray(data).toList(CommentDTO.class);
            return Result.success(commentDTOList);
        }
        List<Comment> commentList = getCommentList((current - 1) * size, size);
        //将Comment转成CommentDTO
        List<CommentDTO> commentDTOList = toCommentDTO(commentList);
        //将信息存到Redis
        //将commentList转成JSON字符串
        data = JSONUtil.toJsonStr(commentDTOList);
        stringRedisTemplate.opsForValue().set(CACHE_COMMENT_KEY + current + ":" + size, data, CACHE_COMMENT_TTL, TimeUnit.MINUTES);

        return Result.success(commentDTOList);
    }

    private List<CommentDTO> toCommentDTO(List<Comment> commentList) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setScore(comment.getScore());
            commentDTO.setLiked(comment.getLiked());
            commentDTO.setIsLiked(comment.getIsLiked());
            commentDTO.setCommentTime(comment.getCommentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            commentDTO.setUserId(comment.getUserId());
            commentDTO.setIcon(comment.getUser().getIcon());
            commentDTO.setNickName(comment.getUser().getNickName());
            if (comment.getFilm() != null) {
                commentDTO.setFilmName(comment.getFilm().getFilmName());
            }
            commentDTO.setLiked(comment.getLiked());
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    /**
     * 获取个数
     *
     * @return
     */
    @Override
    public Result getTotal() {
        //先查询Redis
        String count = stringRedisTemplate.opsForValue().get(CACHE_COMMENT_COUNT_KEY);
        if (StringUtils.isNotBlank(count)) {
            return Result.success(Integer.valueOf(count));
        }

        count = String.valueOf(count());
        stringRedisTemplate.opsForValue().set(CACHE_COMMENT_COUNT_KEY, count, CACHE_COMMENT_COUNT_TTL, TimeUnit.MINUTES);

        return Result.success(Integer.valueOf(count));
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @Override
    public Result deleteCommentById(Long id) {
        //删除数据库数据
        boolean remove = removeById(id);
        if (!remove) {
            return Result.fail(400, "删除失败");
        }
        //删除redis中的数据
        Set<String> keys = stringRedisTemplate.keys(CACHE_COMMENT_KEY + "*");
        stringRedisTemplate.delete(CACHE_COMMENT_COUNT_KEY);
        stringRedisTemplate.delete(keys);
        return Result.success("OK");
    }

    /**
     * 获取点赞前10的评论
     *
     * @param filmId
     * @param request
     * @return
     */
    @Override
    public Result getHotComment(Long filmId, HttpServletRequest request) {
        //查询点赞前10的评论
        QueryWrapper<Comment> qw = new QueryWrapper();
        qw.eq("film_id", filmId);
        qw.orderByDesc("liked");
        qw.last("limit 10");
        List<Comment> comments = list(qw);
        //查询评论的用户信息
        for (Comment comment : comments) {
            User user = userService.getById(comment.getUserId());
            comment.setUser(user);
            //判断该评论是否点赞过
            String key = "comment:liked:" + comment.getId();
            Long userId = getUserId(request);
            System.out.println("userId = " + userId);
            //判断是否已经登录
            if (userId != null) {
                Boolean member = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
                comment.setIsLiked(BooleanUtil.isTrue(member));
            }

        }
        //封装数据
        List<CommentDTO> commentDTOList = toCommentDTO(comments);
        return Result.success(commentDTOList);
    }

    /**
     * 获取当前登录用户id
     *
     * @param request
     * @return
     */
    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            return JWTUtils.getUserId(token);
        }
        return null;
    }


}
