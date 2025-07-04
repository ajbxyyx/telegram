package com.ajbxyyx.utils.Websocket;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashMap;

public class WebsocketUtil {



    static HashMap<Channel, WebsocketOnline> channelOnlineMap = new HashMap<>();
    static HashMap<Long, Channel> uidOnlineMap = new HashMap<>();


    public static void addOnline(Channel channel,Long uid,Date date){
        channelOnlineMap.put(channel,new WebsocketOnline(date,uid) );
        uidOnlineMap.put(uid,channel);
    }
    public static Long remOnline(Channel channel){
        Long uid = null;
        if(channelOnlineMap.containsKey(channel)){
            WebsocketOnline remove = channelOnlineMap.remove(channel);
            uid = remove.getUid();
            if(uidOnlineMap.containsKey(remove.getUid())){
                uidOnlineMap.remove(remove.getUid());
            }
        }
        return uid;
    }

    public static Channel getOnlineChannel(Long uid){
        return uidOnlineMap.get(uid);
    }

    @AllArgsConstructor
    @Data
    public static class WebsocketOnline{
        Date onlineDate;
        Long uid;
    }

}
