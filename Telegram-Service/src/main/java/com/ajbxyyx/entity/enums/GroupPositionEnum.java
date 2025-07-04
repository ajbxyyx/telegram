package com.ajbxyyx.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupPositionEnum {

    OWNER(1,"owner"),
    ADMIN(2,"admin"),
    MEMBER(3,"normal member");

    private Integer position;
    private String desc;

}
