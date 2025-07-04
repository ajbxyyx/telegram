package com.ajbxyyx.dao;

import com.ajbxyyx.dao.mapper.ChatMaper;
import com.ajbxyyx.entity.po.Chat;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class ChatDao extends ServiceImpl<ChatMaper, Chat> {
}
