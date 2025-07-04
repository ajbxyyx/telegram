package com.ajbxyyx.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class DeviceCheckInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {




//        //存儲登入設備和IP信息
//        String ip = request.getHeader("X-Real-IP");
//        String ip = "38.165.7.48";
//        String deviceInfo = parseSecChUa(request.getHeader("sec-ch-ua"));
//        DeviceRecord deviceRecord = buildDeviceRecord(ip, deviceInfo, 123L);
//
//
//        deviceRecordDao.save(deviceRecord);

        return true;
    }










}
