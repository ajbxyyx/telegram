package com.ajbxyyx.entity.vo.Message.History;

import lombok.Data;

@Data
public class BaseHistoryMessageVO {

    public Long msgId;
    public Long sender;
    public Long time;

    public Boolean isLast;
}
