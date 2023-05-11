package com.dkh.mapper;

import com.dkh.pojo.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    List<Order> searchByCondition(@Param("id") Long id, @Param("userId") Long userId, @Param("arrangeId") Long arrangeId,
                                  @Param("current") Integer current, @Param("size") Integer size);
}
