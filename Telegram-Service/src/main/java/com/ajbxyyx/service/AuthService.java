package com.ajbxyyx.service;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.dto.LogInDTO;
import com.ajbxyyx.entity.vo.LoginUserVO;
import com.ajbxyyx.entity.vo.VerifyLoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface AuthService {
    String login(LogInDTO req) throws BusinessException;

    String register(LogInDTO req) throws BusinessException;


    LoginUserVO checkVerificationCode(@Valid VerifyLoginDTO req, HttpServletRequest request) throws BusinessException;
}
