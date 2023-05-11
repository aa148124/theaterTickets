package com.dkh.sms.controller;

import cn.hutool.core.util.RandomUtil;
import com.dkh.dto.Result;
import com.dkh.sms.service.SmsService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.dkh.utils.RedisConstants.LOGIN_CODE_KEY;
import static com.dkh.utils.RedisConstants.LOGIN_CODE_TTL;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Resource
    private SmsService smsService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/send/{phone}")
    public Result sendCode(@PathVariable String phone) {
/*        //从redis获取验证码，如果获取获取到，返回ok
        // key 手机号  value 验证码
        String code = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        if (!StringUtils.isEmpty(code)) {
            return new Result(200, null, "ok");
        }
        //如果从redis获取不到，*/
        // 生成验证码，
        String code = RandomUtil.randomNumbers(6);
        //调用service方法，通过整合短信服务进行发送
        boolean isSend = smsService.send(phone, code);
        //生成验证码放到redis里面，设置有效时间
        if (isSend) {
            stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
            return new Result(200, null, "ok");
        } else {
            return new Result(400, null, "fail");
        }
    }
}
