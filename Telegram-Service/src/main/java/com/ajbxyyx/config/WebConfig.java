package com.ajbxyyx.config;


import com.ajbxyyx.interceptors.AuthInterceptor;
import com.ajbxyyx.interceptors.DeviceCheckInterceptor;
import com.ajbxyyx.interceptors.RequestLimitInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;
    @Resource
    private DeviceCheckInterceptor deviceCheckInterceptor;
    @Resource
    private RequestLimitInterceptor requestLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，并指定需要拦截的 URL 路径
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")  // 拦截所有路径
                .excludePathPatterns("/login","/login/verify","/test")
                .excludePathPatterns("/user/username/check/**");  // 排除不需要拦截的路径
        registry.addInterceptor(requestLimitInterceptor)
                .addPathPatterns("/**");
    }
}

































