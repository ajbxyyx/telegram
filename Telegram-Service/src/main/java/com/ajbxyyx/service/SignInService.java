package com.ajbxyyx.service;

import com.ajbxyyx.common.Exception.BusinessException;

public interface SignInService{


    /**
     * 處理注冊
     * @param mobileCountry
     * @param mobile
     */
    void dealSignIn(String mobileCountry,String mobile) throws BusinessException;

}
