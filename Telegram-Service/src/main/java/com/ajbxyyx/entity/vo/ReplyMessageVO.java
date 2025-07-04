package com.ajbxyyx.entity.vo;

import lombok.Data;

@Data
public class ReplyMessageVO {
    private Long msgId;
    private Long senderId;


    private String text;
    private String thumbnail;
}
