package com.dkh.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("tb_film")

public class Film implements Serializable {
    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", filmName='" + filmName + '\'' +
                ", director='" + director + '\'' +
                ", performer='" + performer + '\'' +
                ", boxOffice=" + boxOffice +
                ", region='" + region + '\'' +
                ", releaseTime=" + releaseTime +
                ", duration=" + duration +
                ", synopsis='" + synopsis + '\'' +
                ", poster='" + poster + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 电影名称
     */
    @TableField("film_name")
    private String filmName;

    /**
     * 导演
     */
    @TableField("director")
    private String director;

    /**
     * 演员
     */
    @TableField("performer")
    private String performer;

    /**
     * 票房
     */
    @TableField("box_office")
    private Double boxOffice;

    /**
     * 地区
     */
    @TableField("region")
    private String region;

    /**
     * 上映时间
     */
    @TableField("release_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseTime;

    /**
     * 时长
     */
    @TableField("duration")
    private Integer duration;

    /**
     * 剧情简介
     */
    @TableField("synopsis")
    private String synopsis;

    /**
     * 海报地址
     */
    @TableField("poster")
    private String poster;

    /**
     * 电影状态
     */
    @TableField("status")
    private Integer status;
}
