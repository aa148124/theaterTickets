package com.dkh.oss.controller;

import com.dkh.dto.Result;
import com.dkh.oss.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file){
        //获取上传文件
        String url = uploadService.upload(file);
        return new Result(200, url, "上传成功");
    }
}
