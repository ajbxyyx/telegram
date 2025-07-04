package com.ajbxyyx.config;



import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.common.entities.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice
@Slf4j
@Order
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(value = BusinessException.class)
    public ApiResult<String> Throwable(BusinessException e) {

        return ApiResult.fail(e.getCode(),e.getError());

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<String> e(MethodArgumentNotValidException e) {

        return ApiResult.fail(500,e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

    }



}
