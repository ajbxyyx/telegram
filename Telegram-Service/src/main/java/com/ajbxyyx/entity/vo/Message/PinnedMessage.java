package com.ajbxyyx.entity.vo.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinnedMessage {

    private Long msgId;

    private Long date;
    private Long uid;

    private String msgContent;

    public PinnedMessage(Long msgId, Long date, Long uid) {
        this.msgId = msgId;
        this.date = date;
        this.uid = uid;
    }
}
