package com.ajbxyyx.controller;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.common.entities.ApiResult;
import com.ajbxyyx.entity.dto.SignInDTO;
import com.ajbxyyx.service.SignInService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signin")
public class SigninController {

    @Resource
    private SignInService signInService;

    /**
     * 注冊賬號
     * @param mobileCountry
     * @param mobile
     * @return
     * @throws BusinessException
     */
    @PostMapping("")
    public ApiResult<Void> signIn(@RequestBody SignInDTO req) throws BusinessException {
        signInService.dealSignIn(req.getMobileCountry(),req.getMobile());
        return ApiResult.success();
    }
}
