package com.ajbxyyx.dao;

import com.ajbxyyx.dao.mapper.MessageMapper;
import com.ajbxyyx.dao.mapper.MessageReactionMapper;
import com.ajbxyyx.entity.po.Message;
import com.ajbxyyx.entity.po.MessageReaction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class MessageReactionDao  extends ServiceImpl<MessageReactionMapper, MessageReaction> {
}
