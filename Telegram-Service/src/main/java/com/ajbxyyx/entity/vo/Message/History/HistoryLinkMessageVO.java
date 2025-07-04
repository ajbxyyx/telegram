package com.ajbxyyx.entity.vo.Message.History;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryLinkMessageVO extends BaseHistoryMessageVO{

    private String link;
    private String text;
    private String title;
    private String description;


    public HistoryLinkMessageVO(Long msgId, Long sender, Long time, String link,String text,String title,String description,Boolean isLast) {
        this.msgId = msgId;
        this.sender = sender;
        this.time = time;
        this.isLast = isLast;

        this.link = link;
        this.text = text;
        this.title = title;
        this.description = description;

    }
}
