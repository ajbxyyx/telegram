package com.ajbxyyx.config.Netty.Handler.Service;


import com.ajbxyyx.constant.RedisKey;
import com.ajbxyyx.service.UserService;
import com.ajbxyyx.utils.Redis.RedisUtil;
import com.ajbxyyx.utils.Websocket.WebsocketUtil;
import io.netty.channel.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class ConnectedHandler {



    @Resource
    private UserService userService;
    @Value("${telegram.serverno}")
    private Integer serverNo;


    /**
     * 上綫
     * @param channel
     * @param uid
     */
    public void handler(Channel channel,Long uid){
        Date onlineTime = new Date();
        WebsocketUtil.addOnline(channel,uid,onlineTime);


        //todo router
//        RedisUtil.set();

        userService.updateLastSeenTime(uid,new Date().getTime());
        RedisUtil.del(RedisKey.OnlineKey(uid));
        log.warn("uid = " + uid + "  上綫!");
    }


}
