package com.dkh.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
@Getter
@Setter
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", icon='" + icon + '\'' +
                '}';
    }

    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 注册时间
     */
    @TableField("create_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 头像地址
     */
    @TableField("icon")
    private String icon;


}
