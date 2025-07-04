package com.ajbxyyx.entity.vo;

import com.ajbxyyx.entity.vo.Message.PinnedMessage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatVO {

    private Long id;
    private String type;
    private Long unreadCount;

    private Integer notification;
    private Integer pin;
    private List<Long> typing = new ArrayList<>();

    private Integer position;

    private List<MessageHistoryVO> messages;

    private List<PinnedMessage> pinnedMessages;


}
