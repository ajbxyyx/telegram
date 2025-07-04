package com.ajbxyyx.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    FILE(0,"file"),
    AUDIO(1,"audio"),
    VIDEO(2,"video"),
    IMAGE(3,"image"),
    VOICE(4,"voice");

    private Integer type;
    private String desc;
}
