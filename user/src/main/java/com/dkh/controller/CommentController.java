package com.dkh.controller;

import com.dkh.dto.Result;
import com.dkh.pojo.Comment;
import com.dkh.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    
    @Resource
    private CommentService commentService;
    
    @GetMapping("/getCommentList/{current}/{size}")
    public Result getCommentList(@PathVariable Integer current, @PathVariable Integer size) {
        return commentService.getCommentListByPage(current, size);
    }
    @GetMapping("/getCommentTotal")
    public Result getCommentTotal() {

        return commentService.getTotal();
    }
    @DeleteMapping("/deleteComment/{id}")
    public Result deleteCommentById(@PathVariable Long id){
        return commentService.deleteCommentById(id);
    }

    /**
     * 获取电影点赞数量前10的评论
     */
    @GetMapping("/getHotComment/{filmId}")
    public Result getHotComment(@PathVariable Long filmId, HttpServletRequest request){
        return commentService.getHotComment(filmId, request);
    }

    /**
     * 点赞
     * @param id
     * @param userId
     * @return
     */
    @PutMapping("/likeComment/{id}/{userId}")
    public Result likeComment(@PathVariable("id") Long id, @PathVariable("userId") Long userId){
        return commentService.likeComment(id, userId);
    }

    /**
     *发表评论
     * @return
     */
    @PostMapping("/addComment")
    public Result addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
}
