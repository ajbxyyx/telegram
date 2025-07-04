package com.ajbxyyx.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class Chat {

    private Long id;
    private Long uid;
    private Long chatId;
    private Date lastReadTime;
    private Integer mute;
    private Integer pin;

    private Boolean block;

    private String pinnedMessages;

}
