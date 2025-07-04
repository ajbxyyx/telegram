package com.ajbxyyx.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WebsocketPushTypeEnum {

    PUSH_MESSAGE_TYPE(1),
    DELETE_MESSAGE_TYPE(2),
    TYPING_MESSAGE_TYPE(3),
    REACT_MESSAGE_TYPE(4),
    PIN_MESSAGE_TYPE(5);

    private Integer type;

}
