package com.ajbxyyx.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ColorEnum {

    BLUD(0,"#eaeff5","#3b6f9d"),
    GREEN(1,"#e9f3e8","#478436"),
    ORANGE(2,"#f6ede8","#a1652b"),
    RED(3,"#f4eaeb","#9a483d"),
    PURPLE(4,"#efecf5","#784ab1"),
    PINK(5,"#f5eaf0","#9e4267");


    private Integer type;
    private String edge;
    private String main;


}
