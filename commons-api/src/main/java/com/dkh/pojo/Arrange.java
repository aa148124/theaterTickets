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
import java.math.BigDecimal;
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
@TableName("tb_arrange")
public class Arrange implements Serializable {
    @Override
    public String toString() {
        return "Arrange{" +
                "id=" + id +
                ", filmId=" + filmId +
                ", film=" + film +
                ", roomId=" + roomId +
                ", room=" + room +
                ", playTime=" + playTime +
                ", price=" + price +
                ", seat='" + seat + '\'' +
                ", stock=" + stock +
                ", version=" + version +
                '}';
    }

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 电影id
     */
    @TableField("film_id")
    private Long filmId;
    /**
     * 电影
     */
    @TableField(exist = false)
    private Film film;
    /**
     * 影厅id
     */
    @TableField("room_id")
    private Long roomId;
    /**
     * 影厅
     */
    @TableField(exist = false)
    private Room room;

    /**
     * 放映时间
     */
    @TableField("play_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime playTime;

    /**
     * 票价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 座位
     */
    @TableField("seat")
    private String seat;

    /**
     * 库存
     */
    @TableField("stock")
    private int stock;

    /**
     * 版本号
     */
    @TableField("version")
    private int version;
}
