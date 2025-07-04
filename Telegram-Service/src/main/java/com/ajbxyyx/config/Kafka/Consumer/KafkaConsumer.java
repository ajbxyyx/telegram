package com.ajbxyyx.config.Kafka.Consumer;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.entity.enums.PushTargetTypeEnum;
import com.ajbxyyx.entity.enums.WebsocketPushTypeEnum;
import com.ajbxyyx.entity.vo.Kafka.KafkaMessageVO;
import com.ajbxyyx.service.WebsocketService;
import com.ajbxyyx.utils.Websocket.WebsocketUtil;
import io.netty.channel.Channel;
import jakarta.annotation.Resource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {


    @Resource
    private WebsocketService websocketService;

    @KafkaListener(topics = "PUSH_MESSAGE",groupId = "TELEGRAM")
    public void PUSH_MESSAGE(ConsumerRecord<String,String> record){
        try {
            KafkaMessageVO msg = JSONUtil.toBean(record.value(), KafkaMessageVO.class);
            websocketService.pushMessage(msg.getSender(),msg.getReceive(), msg.getJson(),WebsocketPushTypeEnum.PUSH_MESSAGE_TYPE.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "DELETE_MESSAGE",groupId = "TELEGRAM")
    public void DELETE_MESSAGE(ConsumerRecord<String,String> record){
        try {
            KafkaMessageVO msg = JSONUtil.toBean(record.value(), KafkaMessageVO.class);
            websocketService.pushMessage(msg.getSender(),msg.getReceive(), msg.getJson(),WebsocketPushTypeEnum.DELETE_MESSAGE_TYPE.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "TYPING_MESSAGE",groupId = "TELEGRAM")
    public void TYPING_MESSAGE(ConsumerRecord<String,String> record){
        try {
            KafkaMessageVO msg = JSONUtil.toBean(record.value(), KafkaMessageVO.class);
            websocketService.pushMessage(msg.getSender(),msg.getReceive(), msg.getJson(),WebsocketPushTypeEnum.TYPING_MESSAGE_TYPE.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "REACT_MESSAGE",groupId = "TELEGRAM")
    public void REACT_MESSAGE(ConsumerRecord<String,String> record){
        try {
            KafkaMessageVO msg = JSONUtil.toBean(record.value(), KafkaMessageVO.class);
            websocketService.pushMessage(msg.getSender(),msg.getReceive(), msg.getJson(),WebsocketPushTypeEnum.REACT_MESSAGE_TYPE.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "PIN_MESSAGE",groupId = "TELEGRAM")
    public void PIN_MESSAGE(ConsumerRecord<String,String> record){
        try {
            KafkaMessageVO msg = JSONUtil.toBean(record.value(), KafkaMessageVO.class);
            websocketService.pushMessage(msg.getSender(),msg.getReceive(), msg.getJson(),WebsocketPushTypeEnum.PIN_MESSAGE_TYPE.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









}
