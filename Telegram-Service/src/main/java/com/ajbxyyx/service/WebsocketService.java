package com.ajbxyyx.service;

import io.netty.channel.Channel;

import java.util.List;

public interface WebsocketService {


    void pushMessage(Channel channel,Long chatId, String data,Integer type);



    void pushMessage(Long sender,Long receiveId, String data,Integer type);










}


























