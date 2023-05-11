package com.dkh;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CodeGenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ticket?useUnicode=true&useSSL=false&characterEncoding=utf8";
        String username = "root";
        String password = "123456";


        //表名集合
        List<String> tables = new ArrayList<>();
//        tables.add("tb_film");
//        tables.add("tb_arrange");
//        tables.add("tb_admin");
        tables.add("tb_comment");
        tables.add("tb_order");
//        tables.add("tb_user");

        FastAutoGenerator.create(url, username, password)
                //全局配置
                .globalConfig(builder -> {
                    builder.fileOverride()   //	开启覆盖之前生成的文件
                            .disableOpenDir()  //禁止打开输出目录
                            .outputDir(System.getProperty("user.dir") + "\\loginAndRegister\\src\\main\\java")   //指定输出目录
                            .author("dkh")   //作者名
//                            .enableKotlin()      //开启 kotlin 模式
//                            .enableSwagger()     //开启 swagger 模式
                            .dateType(DateType.TIME_PACK)   //时间策略
                            .commentDate("yyyy-MM-dd");   //注释日期
                })
                //包配置
                .packageConfig(builder -> {
                    builder.parent("com.dkh")     //父包名
                            .entity("pojo")                 //Entity 包名
                            .service("service")             //	Service 包名
                            .serviceImpl("service.impl")    //Service Impl 包名
                            .mapper("mapper")               //Mapper 包名
                            .xml("mapper.xml")              //	Mapper XML 包名
                            .controller("controller")       //Controller 包名
                            .other("config")                //自定义文件包名	输出自定义文件时所用到的包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir")
                                    + "\\loginAndRegister\\src\\main\\resources\\mapper"));//指定xml位置
                })
                //策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .addTablePrefix("tb_")//表名前缀，配置后生成的代码不会有此前缀
                            .serviceBuilder()
                            .formatServiceFileName("%sService")//服务层接口名后缀
                            .formatServiceImplFileName("%sServiceImpl")//服务层实现类名后缀
                            .entityBuilder()
                            .enableLombok()//实体类使用lombok,需要自己引入依赖
                            //.logicDeleteColumnName("status")//逻辑删除字段，使用delete方法删除数据时会将status设置为1。调用update方法时并不会将该字段放入修改字段中，而是在条件字段中
                            .enableTableFieldAnnotation()//加上字段注解@TableField
                            .controllerBuilder()
                            .formatFileName("%sController")//控制类名称后缀
                            .enableRestStyle()
                            .mapperBuilder()
                            .superClass(BaseMapper.class)
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                })
                .execute();

    }
}

