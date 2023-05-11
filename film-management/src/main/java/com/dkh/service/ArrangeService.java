package com.dkh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkh.dto.Result;
import com.dkh.pojo.Arrange;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
public interface ArrangeService extends IService<Arrange> {
    List<Arrange> getArrangePage(Integer current, Integer size);

    Result getArrangeTotal();

    Result addArrange(Arrange arrange);

    Result getArrangeByFilmId(Long filmId);

    Arrange getArrangeById(Long id);

    Result updateArrange(Arrange arrange);
}
