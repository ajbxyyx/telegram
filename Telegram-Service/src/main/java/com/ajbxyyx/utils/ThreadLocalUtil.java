package com.ajbxyyx.utils;

public class ThreadLocalUtil {


    public static ThreadLocal<Long> threadLocalUid = new ThreadLocal<>();
    public static ThreadLocal<String> threadLocalToken = new ThreadLocal<>();

    public static void setUid(Long uid,String token) {
        threadLocalUid.set(uid);
        threadLocalToken.set(token);
    }

    public static Long getUid() {
        return threadLocalUid.get();
    }
    public static String getToken() {
        return threadLocalToken.get();
    }


    public static void delUid() {
        threadLocalUid.remove();
    }



}
