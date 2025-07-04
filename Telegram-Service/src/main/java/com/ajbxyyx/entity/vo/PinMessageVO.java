package com.ajbxyyx.entity.vo;

import lombok.Data;

@Data
public class PinMessageVO {
    private Long msgId;
    private String msgContent;

    private Long uid;
    private Long date;
}
