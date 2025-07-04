package com.ajbxyyx.controller;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.common.entities.ApiResult;
import com.ajbxyyx.entity.dto.LogInDTO;
import com.ajbxyyx.entity.vo.LoginUserVO;
import com.ajbxyyx.entity.vo.VerifyLoginDTO;
import com.ajbxyyx.service.AuthService;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Resource
    private AuthService authService;

    /**
     * 登入 請求驗證碼
     * @param req
     * @return
     * @throws BusinessException
     */
    @PostMapping("")
    public ApiResult<String> login(@RequestBody @Valid LogInDTO req) throws BusinessException {
        String uuid = authService.login(req);
        return ApiResult.success(uuid);
    }
    /**
     * 驗證 驗證碼
     * @param req
     * @return
     * @throws BusinessException
     */
    @PostMapping("/verify")
    public ApiResult<LoginUserVO> verify(@RequestBody @Valid VerifyLoginDTO req, HttpServletRequest request) throws BusinessException {
        LoginUserVO loginUserVO = authService.checkVerificationCode(req,request);
        return ApiResult.success(loginUserVO);
    }












}
