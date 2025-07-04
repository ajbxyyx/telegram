package com.ajbxyyx.entity.vo.Message.History;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryMusicMessageVO extends BaseHistoryMessageVO{

    private String musicUrl;
    private String musicName;
    private String musicDuration;




    public HistoryMusicMessageVO(Long msgId, Long sender, Long time, String musicUrl, String musicName,Boolean isLast) {
        this.msgId = msgId;
        this.sender = sender;
        this.time = time;
        this.isLast = isLast;

        this.musicUrl = musicUrl;
        this.musicName = musicName;

    }
}
