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
 * @since 2023-01-18
 */
@Getter
@Setter
@TableName("tb_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", arrangeId=" + arrangeId +
                ", seat='" + seat + '\'' +
                ", orderTime=" + orderTime +
                ", status=" + status +
                ", price=" + price +
                ", paymentTime=" + paymentTime +
                '}';
    }

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
     * 排片id
     */
    @TableField("arrange_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long arrangeId;
    /**
     * 排片信息
     */
    @TableField(exist = false)
    private Arrange arrange;
    /**
     * 座位
     */
    @TableField("seat")
    private String seat;

    /**
     * 下单时间
     */
    @TableField("order_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    /**
     * 订单状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 支付金额
     */
    @TableField("price")
    private BigDecimal price;
    /**
     * 支付时间
     */
    @TableField("payment_time")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;


}
