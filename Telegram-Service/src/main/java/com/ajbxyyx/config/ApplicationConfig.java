package com.ajbxyyx.config;

import com.ajbxyyx.entity.po.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${telegram.serverno}")
    private Integer serverNo;

    @Bean
    public Server initServer(){
        Server server = new Server();
        server.setServerNo(serverNo);
        return server;
    }

}
