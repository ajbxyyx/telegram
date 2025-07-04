package com.ajbxyyx.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class Message {

    Long id;
    @TableField("`read`")
    Integer read;
    Long sender;//发送者
    Long receive;
    Date date;
    Long reply;

    String link;
    String text;

    String sticker;
    String photo;
    String video;
    String file;
    String voice;
    String music;




    Boolean isSystem;

    Boolean isFile;
    Boolean isPhoto;
    Boolean isVideo;
    Boolean isVoice;
    Boolean isLink;
    Boolean isMusic;

    String reaction;


}
