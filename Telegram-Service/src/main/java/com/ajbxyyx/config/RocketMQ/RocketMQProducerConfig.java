package com.ajbxyyx.config.RocketMQ;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQProducerConfig {

    @Value("${telegram.mq.name-server}")
    private String nameServer;
    @Value("${telegram.mq.access-key}")
    private String accessKey;
    @Value("${telegram.mq.secret-key}")
    private String secretKey;

    @Bean
    public DefaultMQProducer DefaultMQProducer() throws MQClientException {
        //ACL验证
        AclClientRPCHook aclClientRPCHook = new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));

        //订阅消费组
        DefaultMQProducer producer = new DefaultMQProducer("PUSH_MESSAGE",aclClientRPCHook);
        producer.setNamesrvAddr(nameServer);//设置nameserver地址
        producer.start();

        return producer;
    }

}
