package com.dkh.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
@Getter
@Setter
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String icon;
    /**
     * 电影名
     */
    private String filmName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评分
     */
    private int score;

    /**
     * 评论时间
     */
    private String commentTime;
    /**
     * 点赞数量
     */
    private Integer liked;

    /**
     * 是否点过赞
     */
    private Boolean isLiked;

}
