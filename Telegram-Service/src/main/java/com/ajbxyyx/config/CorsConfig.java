package com.ajbxyyx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // 允許所有來源
        config.addAllowedHeader("*");         // 允許所有頭部
        config.addAllowedMethod("*");         // 允許所有方法 (GET, POST, PUT, DELETE...)
        config.setAllowCredentials(true);     // 允許攜帶 cookie（如果有的話）

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 對所有路徑生效
        return new CorsFilter(source);
    }
}
