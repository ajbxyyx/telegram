package com.ajbxyyx.entity.po;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MessageReactionOld {

    //who react
    private List<WhoReact> uid;
    //react type
    private Long reactionId;
    //react msgId
    private Long msgId;

    @Data
    public static class WhoReact{
        Date date;
        Long uid;
    }
}
