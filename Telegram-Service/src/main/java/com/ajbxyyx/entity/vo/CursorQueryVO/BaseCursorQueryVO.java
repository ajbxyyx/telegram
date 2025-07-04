package com.ajbxyyx.entity.vo.CursorQueryVO;

import lombok.Data;

import java.util.List;

@Data
public class BaseCursorQueryVO<Result> {


    Boolean isLast;
    Long cursor;
    List<Result> result;

}
