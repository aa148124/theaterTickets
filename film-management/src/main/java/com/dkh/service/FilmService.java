package com.dkh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkh.dto.Result;
import com.dkh.pojo.Film;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
public interface FilmService extends IService<Film> {

    Result getFilmList(Integer current, Integer size);

    Result getFilmTotal();

    Result updateFilm(Film film);

    List<Film> searchByCondition(Long id, String filmName, Integer status, Integer current, Integer size);

    Result getCountByCondition(Long id, String filmName, Integer status);

    Result addFilm(Film film);

    Result getShowFilm();

    Result getShowFilmCount();

    Result getRanking();

    Result getFutureFilm();

    Result getFutureFilmCount();

    Result addBoxOffice(Long id, BigDecimal price);

    Result searchFilm(String context);
}
