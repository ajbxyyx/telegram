package com.ajbxyyx.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum PrivacyLevelEnum {


    EVERYBODY(1,"所有人"),     //對於好友關係 -> 無任何關係
    MY_CONTACTS(2,"聯係人"),   //對於好友關係 -> 聯係人關係
    NOBODY(3,"禁止所有人");




    private Integer level;
    private String desc;

    public static PrivacyLevelEnum of(Integer level){
        List<PrivacyLevelEnum> list = Arrays.stream(PrivacyLevelEnum.values()).toList();
        return list.get(level-1);
    }
}
