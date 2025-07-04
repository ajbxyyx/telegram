package com.ajbxyyx.service.Imp;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.dao.GroupDao;
import com.ajbxyyx.dao.MemberDao;
import com.ajbxyyx.entity.po.Member;
import com.ajbxyyx.entity.vo.Websocket.WebsocketBaseVO;
import com.ajbxyyx.service.WebsocketService;
import com.ajbxyyx.utils.Websocket.WebsocketUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebsocketServiceImp implements WebsocketService {
    @Resource
    private MemberDao memberDao;
    /**
     * Personal push
     * @param channel
     * @param data
     */
    @Override
    public void pushMessage(Channel channel,Long chatId, String data,Integer type) {
        WebsocketBaseVO websocketBaseVO = new WebsocketBaseVO(type,chatId,data);
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(websocketBaseVO)));
    }




    @Override
    public void pushMessage(Long sender ,Long receiveId, String data, Integer type) {
        if(receiveId < 0){
            List<Member> receive = memberDao.lambdaQuery()
                    .eq(Member::getGroupId, receiveId)
                    .select(Member::getUid)
                    .list();
            receive.forEach(member->{
                Long uid = member.getUid();
                Channel onlineChannel = WebsocketUtil.getOnlineChannel(uid);
                if(onlineChannel != null){
                    pushMessage(onlineChannel,receiveId,data,type);
                }
            });
        }else{
            Channel receiveChannel = WebsocketUtil.getOnlineChannel(receiveId);
            if(receiveChannel != null){
                pushMessage(receiveChannel,sender,data,type);//chatId is sender's uid for receiver
            }
            Channel senderChannel = WebsocketUtil.getOnlineChannel(sender);
            if(senderChannel != null){
                pushMessage(senderChannel,receiveId,data,type);//chat id is receiver's uid for sender
            }

        }

    }











}






















