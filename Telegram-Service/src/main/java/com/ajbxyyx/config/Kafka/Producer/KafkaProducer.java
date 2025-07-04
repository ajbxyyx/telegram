package com.ajbxyyx.config.Kafka.Producer;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.entity.vo.Kafka.KafkaMessageVO;
import jakarta.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {



    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String TOPIC,KafkaMessageVO msg){
        kafkaTemplate.send(TOPIC, JSONUtil.toJsonStr(msg));
    }


}
