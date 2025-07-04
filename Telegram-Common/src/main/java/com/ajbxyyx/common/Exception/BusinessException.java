package com.ajbxyyx.common.Exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BusinessException extends Exception{

    private Integer code;
    private String error;

    public BusinessException(Integer code, String error) {
        this.code = code;
        this.error = error;
    }
}
