package com.ajbxyyx.config.Kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum KafkaTopic {

    PUSH_MESSAGE("PUSH_MESSAGE"),
    TYPING_MESSAGE("TYPING_MESSAGE"),
    DELETE_MESSAGE("DELETE_MESSAGE"),
    REACT_MESSAGE("REACT_MESSAGE"),
    PIN_MESSAGE("PIN_MESSAGE");

    private String topic;

}
