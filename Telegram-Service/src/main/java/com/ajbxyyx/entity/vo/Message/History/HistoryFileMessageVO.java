package com.ajbxyyx.entity.vo.Message.History;

import lombok.Data;

@Data
public class HistoryFileMessageVO extends BaseHistoryMessageVO{

    private String fileName;
    private String fileSize;
    private String fileUrl;

    public HistoryFileMessageVO(Long msgId,Long sender,Long time,String fileUrl,String fileName,String fileSize,Boolean isLast) {
        this.msgId = msgId;
        this.sender = sender;
        this.time = time;
        this.isLast = isLast;

        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;

    }
}
