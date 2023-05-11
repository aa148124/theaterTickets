package com.dkh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkh.dto.Result;
import com.dkh.pojo.Comment;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
public interface CommentService extends IService<Comment> {
    List<Comment> getCommentList(Integer current, Integer size);
    Result getCommentListByPage(Integer current, Integer size);

    Result getTotal();

    Result deleteCommentById(Long id);

    Result getHotComment(Long filmId, HttpServletRequest request);

    Result likeComment(Long id, Long userId);

    Result addComment(Comment comment);
}
