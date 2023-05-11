package com.dkh.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
@Data
@TableName("tb_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 用户
     */
    @TableField(exist = false)
    private User user;
    /**
     * 电影id
     */
    @TableField("film_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long filmId;
    /**
     * 电影
     */
    @TableField(exist = false)
    private Film film;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 评分
     */
    @TableField("score")
    private int score;

    /**
     * 评论时间
     */
    @TableField("comment_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;

    /**
     * 点赞数量
     */
    @TableField("liked")
    private Integer liked;

    /**
     * 是否点过赞
     */
    @TableField(exist = false)
    private Boolean isLiked;
}
