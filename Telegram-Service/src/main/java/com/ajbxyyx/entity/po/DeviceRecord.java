package com.ajbxyyx.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceRecord {

    private Long id;
    private String device;
    private String systemName;

    private String ip;
    private String latitude;
    private String longitude;
    private String city;
    private String country;


    private Long uid;
    private Date date;
    private String token;




}
