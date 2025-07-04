package com.ajbxyyx.controller;

import com.ajbxyyx.common.entities.ApiResult;
import com.ajbxyyx.utils.JwtUtil;
import com.ajbxyyx.utils.Redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("")
    public ApiResult<Void> test(){
        String token = jwtUtil.createToken(123L);
        RedisUtil.set("test",token);
        return ApiResult.success();
    }
}
