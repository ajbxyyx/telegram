package com.ajbxyyx.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseCursorQueryDTO {

    Boolean isLast;//是否查詢到底了
    Integer pageSize = 20;//一次查詢多少頁
    @JsonProperty("cursor")
    public Long cursor;//游標

}
