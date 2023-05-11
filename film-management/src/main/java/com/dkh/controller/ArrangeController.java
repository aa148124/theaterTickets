package com.dkh.controller;

import com.dkh.dto.Result;
import com.dkh.pojo.Arrange;
import com.dkh.service.ArrangeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping("/arrange")
public class ArrangeController {

    @Resource
    private ArrangeService arrangeService;


    @GetMapping("/getArrangeList/{current}/{size}")
    public Result getArrangeList(@PathVariable Integer current, @PathVariable Integer size) {
        List<Arrange> arranges = arrangeService.getArrangePage(current, size);
        return new Result(200, arranges);
    }
    @GetMapping("/getArrangeById/{id}")
    public Result getArrangeById(@PathVariable Long id) {
        System.out.println("id = " + id);
        Arrange arrange = arrangeService.getArrangeById(id);
        return new Result(200, arrange);
    }
    @GetMapping("/getArrangeTotal")
    public Result getArrangeTotal() {
        return arrangeService.getArrangeTotal();
    }

    @PostMapping("/addArrange")
    public Result addArrange(@RequestBody Arrange arrange) {
        return arrangeService.addArrange(arrange);
    }

    /**
     * 根据电影id获取排片
     */
    @GetMapping("/getArrangeByFilmId/{filmId}")
    public Result getArrangeByFilmId(@PathVariable Long filmId){
        return arrangeService.getArrangeByFilmId(filmId);
    }

    /**
     * 下单锁库存
     * @param arrange
     * @return
     */
    @PutMapping("/updateArrange")
    public Result updateArrange(@RequestBody Arrange arrange){
        return arrangeService.updateArrange(arrange);
    }
}
