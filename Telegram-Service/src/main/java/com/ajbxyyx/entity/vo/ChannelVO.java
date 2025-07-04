package com.ajbxyyx.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ChannelVO {
    Long id;//channel id
//    Long memberListId;//成員列表id
    Long memberId;//好友id
    String avatar;//頻道頭像
    String name; //頻道名稱

    Date date;//最後一條消息時間
    String displayMessageUser;//最後一條消息發送者
    Boolean isSystem;//是否是系統發送的

    String displayMessage;//最後一條消息
    List<String> displayPhoto;//最後一條消息包含的圖片
    List<String> displayVideo;//最後一條消息包含的影片
    Boolean muteStatus = false;//禁言狀態
    Boolean pinStatus = false;//置頂狀態

    Integer unread;//未讀數


}
