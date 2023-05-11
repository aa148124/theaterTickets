package com.dkh.service;

import com.dkh.dto.Result;
import com.dkh.pojo.Arrange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Component
@FeignClient(value = "film-management-service")
public interface FilmService {
    @GetMapping("/arrange/getArrangeById/{id}")
    public Result getArrangeById(@PathVariable("id") Long id);
    @PutMapping("/arrange/updateArrange")
    public Result updateArrange(@RequestBody Arrange arrange);

}
