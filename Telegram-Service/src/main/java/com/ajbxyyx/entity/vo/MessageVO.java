package com.ajbxyyx.entity.vo;

import lombok.Data;

/**
 * 用於MQ
 */
@Data
public class MessageVO {


    Long id;
    Integer read = 0;
    Long sender;
    String type;
    Long receive;
    Long time;
    Long reply;
    Long replySenderId;
    String replyContent;

    String reaction;
    String content;
    String sticker;
    String photo;
    String video;
    String file;
    String voice;



//    {  id: 1, sender: 2, content: '今晚一起吃饭？', time: '15:00' ,  file: [],video: [],photo: []  }

}
