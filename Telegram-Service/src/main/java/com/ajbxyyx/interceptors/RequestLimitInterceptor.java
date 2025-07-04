package com.ajbxyyx.interceptors;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.constant.RedisKey;
import com.ajbxyyx.utils.Redis.Annotation.RequestLimit;
import com.ajbxyyx.utils.Redis.RedisUtil;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 限流拦截器
 */
@Component
@Slf4j
public class RequestLimitInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod handlerMethod) {
            // 只处理控制器方法
            Method method = ((HandlerMethod) handler).getMethod();
            if(!method.isAnnotationPresent(RequestLimit.class)) {//没有注解 不限流
                return true;
            }
            String token = ThreadLocalUtil.getToken();
            if (token == null) {  //不带token的请求 不处理限流
                return true;
            }
            RequestLimit annotation = method.getAnnotation(RequestLimit.class);
            int limit = annotation.limit();
            long ms = annotation.ms();

            String path = request.getRequestURI().split("\\?")[0];
            String pathToken = path +":"+ token;
            String redisKey = RedisKey.RequestLimitKey(pathToken);

            Integer current = RedisUtil.get(redisKey, Integer.class);
            if(current == null || current == 0){
                RedisUtil.set(redisKey, 1, ms, TimeUnit.MILLISECONDS);
            }else if(current < limit){
                RedisUtil.set(redisKey, current+1, ms, TimeUnit.MILLISECONDS);
            }else{
                throw new BusinessException(200, annotation.msg());
            }
            return true;
        } else {
            // 静态资源直接放行
            return true;
        }

    }
}









