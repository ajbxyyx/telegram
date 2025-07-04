package com.ajbxyyx.entity.vo;

import lombok.Data;

@Data
public class GroupVO {

    private Long groupId;
    private String color;

    private String name;
    private String avatar;

    private String description;

    private Integer memberNum;

}
