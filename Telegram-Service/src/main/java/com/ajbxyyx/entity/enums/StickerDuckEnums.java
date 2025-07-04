package com.ajbxyyx.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum StickerDuckEnums {

    LOL(1,"/src/emoji/lol.gif"),
    LOVE(2,"/src/emoji/love.gif"),
    GOOD(3,"/src/emoji/good.gif"),
    SCARE(4,"/src/emoji/scare.gif"),
    BYE(5,"/src/emoji/bye.gif"),
    SAD(6,"/src/emoji/sad.gif"),
    WHAT(7,"/src/emoji/what.gif"),
    BOIL(8,"/src/emoji/boil.gif"),
    BAD(9,"/src/emoji/bad.gif"),
    COOL(10,"/src/emoji/cool.gif"),
    WHATEVER(11,"/src/emoji/whatever.gif"),
    RAINBOW(12,"/src/emoji/rainbow.gif"),
    SO(13,"/src/emoji/so.gif"),
    AAAAAA(14,"/src/emoji/aaaaaa.gif"),
    CLASSIC(15,"/src/emoji/classic.gif"),
    KILL(16,"/src/emoji/kill.gif"),
    THINK(17,"/src/emoji/think.gif"),
    MONEY(18,"/src/emoji/money.gif"),
    DRINK(19,"/src/emoji/drink.gif"),
    DIE(20,"/src/emoji/die.gif"),
    COLD(21,"/src/emoji/cold.gif"),
    BOOM(22,"/src/emoji/boom.gif"),
    UNACCEPTABLE(23,"/src/emoji/unacceptable.gif"),
    VERYGOOD(24,"/src/emoji/verylove.gif"),
    SLEEP(25,"/src/emoji/sleep.gif"),
    SHOWER(26,"/src/emoji/shower.gif"),
    FLY(27,"/src/emoji/fly.gif"),
    DELETE(28,"/src/emoji/delete.gif"),
    CUTE(29,"/src/emoji/cute.gif"),
    WIN(30,"/src/emoji/win.gif");




    private Integer detailType;
    private String value;

    public static StickerDuckEnums of(Integer detailType){
        List<StickerDuckEnums> list = Arrays.stream(StickerDuckEnums.values()).toList();
        return list.get(detailType-1);
    }

}
