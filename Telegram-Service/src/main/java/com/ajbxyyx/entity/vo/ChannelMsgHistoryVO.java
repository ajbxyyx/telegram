package com.ajbxyyx.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class ChannelMsgHistoryVO {

    Boolean isLast;//是否到底
    Long cursorId;//游標 -> 消息id
    List<MsgHistoryVO> msgHistory;//消息查詢結果集


}
