package com.ajbxyyx.entity.vo;

import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.PhotoVO;
import lombok.Data;

import java.util.List;

@Data
public class SendMessageVO {

    private String uuid;
    private Long date;
    private Long msgId;
    private Long receiveId;


    private List<String> fileUrl;
    private List<String> videoUrl;
    private List<PhotoVO> photoUrl;
    private List<String> voiceUrl;
    private List<String> musicUrl;





}
