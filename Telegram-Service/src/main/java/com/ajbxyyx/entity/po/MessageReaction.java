package com.ajbxyyx.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class MessageReaction {

    private Long id;
    private Long uid;
    private Long messageId;
    private Integer reactType;
    private Date createTime;



}
