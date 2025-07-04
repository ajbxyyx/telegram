package com.ajbxyyx.entity.vo.Message.History;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class HistoryMediaMessageVO extends BaseHistoryMessageVO{



    private String mediaUrl;

    public HistoryMediaMessageVO(Long msgId,Long sender,Long time,String mediaUrl,Boolean isLast) {
        this.msgId = msgId;
        this.sender = sender;
        this.time = time;
        this.isLast = isLast;

        this.mediaUrl = mediaUrl;

    }




}
