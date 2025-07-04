package com.ajbxyyx.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum StickerTypeEnums {

    DUCK(1,"鴨子"),
    APPLE(2,"蘋果");


    private Integer type;
    private String desc;


    public static StickerTypeEnums of(Integer level){
        List<StickerTypeEnums> list = Arrays.stream(StickerTypeEnums.values()).toList();
        if(level >= list.size() || level <0){
            return null;
        }
        return list.get(level-1);
    }


}
