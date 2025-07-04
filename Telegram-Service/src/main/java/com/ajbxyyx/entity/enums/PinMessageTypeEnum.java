package com.ajbxyyx.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum PinMessageTypeEnum {
    FOR_ME(1),
    FOR_ALL(2);

    private Integer type;
}
