package com.ajbxyyx.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum ReactionEnum {

    LOVE(0L,"/src/emoji/normal/love.png"),
    UP_THUMB(1L,"/src/emoji/normal/upThumb.png"),
    DOWN_THUMB(2L,"/src/emoji/normal/downThumb.png"),
    FIRE(3L,"/src/emoji/normal/fire.png"),
    LOVE_FACE(4L,"/src/emoji/normal/love_face.png"),
    APPLAUD(5L,"/src/emoji/normal/applaud.png"),
    LAUGH(6L,"/src/emoji/normal/laugh.png");

    private Long type;
    private String src;



    public static ReactionEnum of(Integer type){
        List<ReactionEnum> list = Arrays.stream(ReactionEnum.values()).toList();
        int index = Integer.parseInt(String.valueOf(type));
        return list.get(index);
    }



}
