package com.ajbxyyx.config.Netty.Handler.Service;

import com.ajbxyyx.constant.RedisKey;
import com.ajbxyyx.service.UserService;
import com.ajbxyyx.utils.Redis.RedisUtil;
import com.ajbxyyx.utils.Websocket.WebsocketUtil;
import io.netty.channel.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class UnconnectedHandler {

    @Resource
    private UserService userService;

    public void handler(Channel channel) {
        Long offlineUid = WebsocketUtil.remOnline(channel);
        if(offlineUid == null){
            return;
        }

        userService.updateLastSeenTime(offlineUid,new Date().getTime());

        RedisUtil.del(RedisKey.OnlineKey(offlineUid));
        log.warn("uid = " + offlineUid + "  下綫!");
    }
}
