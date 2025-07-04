package com.ajbxyyx.interceptors;


import com.ajbxyyx.entity.po.DeviceRecord;
import com.ajbxyyx.entity.vo.IpParseVO;
import com.ajbxyyx.utils.IpParseUtil;
import com.ajbxyyx.utils.JwtUtil;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 获取网关鉴权的uid
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private IpParseUtil ipParseUtil;
    @Resource
    private JwtUtil jwtUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {


        //解析Token
        String token = request.getHeader("Authorization");
        Long uidOrNull = jwtUtil.getUidOrNull(token);

        if(uidOrNull == null){
            return false;
        }
        ThreadLocalUtil.setUid(uidOrNull,token);


//        //存儲登入設備和IP信息
//        String ip = request.getHeader("X-Real-IP");
        String deviceInfo = parseSecChUa(request.getHeader("sec-ch-ua"));
//        DeviceRecord deviceRecord = buildDeviceRecord(ip, deviceInfo, uidOrNull);
//
//        ThreadLocalUtil.setUid(uidOrNull);//存入綫程map

        return true;
    }





    private DeviceRecord buildDeviceRecord(String ip, String deviceInfo, Long uidOrNull) {
        IpParseVO ipParseVO = ipParseUtil.parseIp(ip);

        DeviceRecord deviceRecord = new DeviceRecord();
        deviceRecord.setDate(new Date());
        deviceRecord.setIp(ip);
        deviceRecord.setCity(ipParseVO.getCity() + " " + ipParseVO.getCity());
        deviceRecord.setDevice(deviceInfo);
        deviceRecord.setUid(uidOrNull);
        return deviceRecord;
    }


    private String parseSecChUa(String secChUa) {

        if (secChUa == null || secChUa.isEmpty()) return "Unknow";

        String[] split = secChUa.split(",");
        if(split.length != 3) return "Unknow";
        secChUa = split[1];
        Pattern pattern = Pattern.compile("\"([^\"]+)\";v=\"(\\d+)\"");
        Matcher matcher = pattern.matcher(secChUa);


        while (matcher.find()) {
            String brand = matcher.group(1);
            String version = matcher.group(2);
            return brand + " " + version;
        }
        return "Unknow";
    }
}
