package com.ajbxyyx.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum PrivacyEnum {

    PHONE_NUMBER(1,"手機號"),
    LAST_SEEN_ONLINE(2,"手機號"),
    PROFILE_PHOTOS(3,"頭像"),
    BIO(4,"BIO"),
    DATE_OF_BIRTH(5,"生日"),
    CALLS(6,"打電話"),
    VOICE_VIDEO_MESSAGE(7,"音頻&視頻消息"),
    MESSAGES(8,"發消息"),
    INVITES(9,"邀請進群");





    private Integer id;
    private String desc;


    public static PrivacyEnum of(Integer privacy){
        List<PrivacyEnum> list = Arrays.stream(PrivacyEnum.values()).toList();
        return list.get(privacy-1);
    }

}
