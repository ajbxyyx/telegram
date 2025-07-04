package com.ajbxyyx.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PushTargetTypeEnum {


    GROUP(0,"push all group member"),
    PERSON(1,"push specified person");


    private Integer type;
    private String desc;



    public static PushTargetTypeEnum of(Integer type) {
        for (PushTargetTypeEnum e : PushTargetTypeEnum.values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }


}
