package com.ajbxyyx.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {


    //base info
    private Long uid;
    private Integer color;

    private String firstName;
    private String lastName;

    private String username;

    //privacy info
    private String avatar;//隱私設置
    private String bio;//隱私設置
    private Date dateOfBirth;//隱私設置
    private String mobile;//隱私設置
    private String mobileCountry;


    private Integer relationship;
    //last seen time
    private Long lastSeenTime;



}




























