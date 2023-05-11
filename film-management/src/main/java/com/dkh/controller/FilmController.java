package com.dkh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dkh.dto.Result;
import com.dkh.pojo.Film;
import com.dkh.service.FilmService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/film")
public class FilmController {

    @Resource
    private FilmService filmService;

    @GetMapping("/getFilmList/{current}/{size}")
    public Result getFilmList(@PathVariable Integer current, @PathVariable Integer size) {
        return filmService.getFilmList(current, size);
    }

    @GetMapping("/getFilmTotal")
    public Result getFilmTotal() {
        return filmService.getFilmTotal();
    }

    @PutMapping("/updateFilm")
    public Result updateFilm(@RequestBody Film Film) {
        return filmService.updateFilm(Film);
    }

    @PostMapping("/searchFilm/{current}/{size}")
    public Result searchFilm(Long id, String filmName, Integer status,
                             @PathVariable Integer current, @PathVariable Integer size) {
        //判断是否全为空
        if (id == null && StringUtils.isBlank(filmName) && status == null) {
            return filmService.getFilmList(current, size);
        }
        System.out.println("status = " + status);
        //查询数据库
        int currentPage = (current - 1) * size;
        List<Film> filmList = filmService.searchByCondition(id, filmName, status, currentPage, size);
        return new Result(200, filmList);
    }

    @PostMapping("/getCountByCondition")
    public Result getCountByCondition(Long id, String filmName, Integer status) {
        return filmService.getCountByCondition(id, filmName, status);
    }

    @PostMapping("/addFilm")
    public Result addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @GetMapping("getFilmById/{id}")
    public Result getFilmById(@PathVariable Long id) {
        LambdaQueryWrapper<Film> lqw = new LambdaQueryWrapper();
        lqw.eq(Film::getId, id);
        Film film = filmService.getOne(lqw);
        return new Result(200, film);
    }

    /**
     * 获取正在上映的电影
     *
     * @return
     */
    @GetMapping("getShowFilm")
    public Result getShowFilm() {
        return filmService.getShowFilm();
    }

    /**
     * 获取正在上映的电影数量
     *
     * @return
     */
    @GetMapping("getShowFilmCount")
    public Result getShowFilmCount() {
        return filmService.getShowFilmCount();
    }

    /**
     * 获取历史前10票房
     *
     * @return
     */
    @GetMapping("getRanking")
    public Result getRanking() {
        return filmService.getRanking();
    }

    /**
     * 获取即将上映的电影
     *
     * @return
     */
    @GetMapping("getFutureFilm")
    public Result getFutureFilm() {
        return filmService.getFutureFilm();
    }

    /**
     * 获取即将上映的电影数量
     *
     * @return
     */
    @GetMapping("getFutureFilmCount")
    public Result getFutureFilmCount() {
        return filmService.getFutureFilmCount();
    }

    @GetMapping("getPoster")
    public Result getPoster() {
        String[] poster = {"https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/f7116bf540ca4a289319f0ccfd0d6099.jpeg",
                "https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/2c0bf900f23d46b19d66229dbfba777b.jpeg",
                "https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/c1b4615b727047c3a40494765b815777.png",
                "https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/7c190b5f249d4c6ab92614ccfd3a903c.jpeg"};
        return new Result(200, poster);
    }

    /**
     * 增加票房
     * @param id 电影id
     * @param price 钱
     * @return
     */
    @PutMapping("addBoxOffice")
    public Result addBoxOffice(Long id, BigDecimal price){
        return filmService.addBoxOffice(id, price);
    }

    /**
     * 查找电影
     * @param context 查询内容
     * @return
     */
    @GetMapping("/search/{context}")
    public Result searchFilm(@PathVariable String context){
        return filmService.searchFilm(context);
    }
}
