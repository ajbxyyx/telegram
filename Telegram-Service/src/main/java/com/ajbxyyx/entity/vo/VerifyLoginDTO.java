package com.ajbxyyx.entity.vo;

import lombok.Data;

@Data
public class VerifyLoginDTO {


    private String verificationCode;
    private String uuid;
}
