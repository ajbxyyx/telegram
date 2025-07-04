package com.ajbxyyx.config.RocketMQ;


import com.ajbxyyx.service.WebsocketService;
import com.ajbxyyx.utils.Websocket.WebsocketUtil;
import io.netty.channel.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RocketMQConsumerDeleteMessageConfig {

    @Value("${telegram.mq.name-server}")
    private String nameServer;
    @Value("${telegram.mq.access-key}")
    private String accessKey;
    @Value("${telegram.mq.secret-key}")
    private String secretKey;

    @Resource
    private WebsocketService websocketService;

    @Bean
    public void DELETE_MESSAGE_TOPIC() throws MQClientException {
        //ACL验证
        AclClientRPCHook aclClientRPCHook = new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("",aclClientRPCHook,new AllocateMessageQueueAveragely());

        consumer.subscribe("DELETE_MESSAGE","*");//订阅主题
        consumer.setConsumerGroup("DELETE_MESSAGE");

        consumer.setNamesrvAddr(nameServer);
        consumer.setPullBatchSize(500);
        consumer.setConsumeMessageBatchMaxSize(500);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, consumeConcurrentlyContext) -> {
            //监听处理
            try {
                msgs.forEach(
                        msg->{
                            log.warn("消费del-message消息");
                            long receiveUid = Long.parseLong(msg.getTags());
                            String messageVOJsonStr = new String(msg.getBody());

                            Channel onlineChannel = WebsocketUtil.getOnlineChannel(receiveUid);
                            if(onlineChannel != null){
//                                websocketService.deleteMessage(onlineChannel, messageVOJsonStr);
                            }
                        }
                );
            }catch (Exception e){
                e.printStackTrace();
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.warn("[消息发送]监听已经开启 ->  Group:"+"DELETE_MESSAGE"+"    Subscribe:"+"DELETE_MESSAGE");
    }

}
