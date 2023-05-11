package com.dkh.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@TableName("tb_room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 影厅名称
     */
    @TableField("room_name")
    private String roomName;

    /**
     * 影厅容量
     */
    @TableField("room_capacity")
    private String roomCapacity;
}
