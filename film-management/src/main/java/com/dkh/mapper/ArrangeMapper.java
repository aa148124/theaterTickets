package com.dkh.mapper;

import com.dkh.pojo.Arrange;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
@Mapper
public interface ArrangeMapper extends BaseMapper<Arrange> {
    List<Arrange> getArrangePage(@Param("current")Integer current, @Param("size") Integer size);
    Arrange getArrangeById(@Param("id")Long id);
    List<Arrange> getArrangeByFilmId(@Param("filmId")Long filmId, @Param("playTime")LocalDateTime playTime);

}
