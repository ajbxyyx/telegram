package com.ajbxyyx.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    Long id;
    String username;

    /**
     * 頭像
     */
    String avatar;//頭像
    Integer avatarPrivacy;//頭像顯示範圍

    /**
     * 用戶昵稱
     */
    String firstName;
    String lastName;

    Integer color;//用戶顔色

    /**
     * 手機號
     */
    String phone;
    String phoneCountry;
    Integer phonePrivacy;//手機號顯示範圍

    /**
     * 生日
     */
    Date dateOfBirth;
    Integer dateOfBirthPrivacy;// 生日顯示範圍

    /**
     * 在綫狀態
     */
    Long lastSeen;
    Integer lastSeenPrivacy;//在綫狀態顯示範圍


    /**
     * BIO
     */
    String bio;
    Integer bioPrivacy;

    /**
     * 登入郵箱
     */
    String logEmail;


    //隱私權限
    Integer callsPrivacy;
    Integer voiceVideoMessagesPrivacy;
    Integer messagesPrivacy;
    Integer invitesPrivacy;


}
