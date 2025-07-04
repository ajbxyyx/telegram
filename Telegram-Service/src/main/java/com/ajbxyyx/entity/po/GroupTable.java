package com.ajbxyyx.entity.po;

import lombok.Data;

@Data
public class GroupTable {
    Long id;


    String avatar;//頭像
    String name;//昵稱
    String color;

    String description;//群組描述

    Integer members;//成員數量
    Long memberId;//成員表id

    private String pinnedMessages;




}
