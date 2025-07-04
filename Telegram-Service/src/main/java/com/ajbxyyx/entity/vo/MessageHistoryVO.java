package com.ajbxyyx.entity.vo;


import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MessageHistoryVO {

    private Long id;//消息id
    private Integer read;//是否已讀
    private Long sender;//發送者Id
    private String content;//文本内容
    private Long time;

    private Map<Integer,MessageReactionVO> reaction;


    private Long reply;


    private List<FileVO> file;
    private List<PhotoVO> photo;
    private List<VideoVO> video;
    private List<VoiceVO> voice;
    private List<MusicVO> music;


    private String sticker;

}
