package com.dkh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dkh.pojo.Film;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface FilmMapper extends BaseMapper<Film> {
    List<Film> searchByCondition(@Param("id") Long id, @Param("filmName") String filmName,
                                 @Param("status") Integer status, @Param("current") Integer current,
                                 @Param("size") Integer size);
    int getCountByCondiction(@Param("id") Long id, @Param("filmName") String filmName,
                     @Param("status") Integer status);
}
