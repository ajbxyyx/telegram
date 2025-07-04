package com.ajbxyyx.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class Member {


    private Long id;
    private Long uid;
    private Integer position;
    private Date mute;
    private Long groupId;

    private Date lastReadTime;

    private Integer pin;
    private Integer notification;

}
