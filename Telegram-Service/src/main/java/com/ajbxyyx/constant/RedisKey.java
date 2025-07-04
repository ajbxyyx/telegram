package com.ajbxyyx.constant;

public class RedisKey {

    private static final String PREFIX = "Telegram:";

    /**
     * 登入/注冊 驗證
     * @param mobileCountry
     * @param mobile
     * @return
     */
    public static String LoginKey(String UUID){
        return PREFIX + "SIGN_IN:"+UUID;
    }


    public static String OnlineKey(Long uid){
        return PREFIX + "ONLINE:" + String.valueOf(uid);
    }

    public static String BaseUserInfoKey(Long uid){
        return PREFIX + "BASE_USER_INFO:" + String.valueOf(uid);
    }
    public static String BaseGroupInfoKey(Long groupId){
        return PREFIX + "BASE_GROUP_INFO:" + String.valueOf(groupId);
    }


    public static String TokenKey(Long uid){
        return PREFIX + "TOKEN:" + uid;
    }

    public static String RequestLimitKey(String pathToken){
        return PREFIX + "REQUEST_LIMIT" + pathToken;
    }

    public static String UrlParseKey(String url){
        return PREFIX + "URL_PARSE" + url;
    }






}
