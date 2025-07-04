package com.ajbxyyx.service.Imp;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.constant.RedisKey;
import com.ajbxyyx.dao.UserDao;
import com.ajbxyyx.entity.po.User;
import com.ajbxyyx.service.SignInService;
import com.ajbxyyx.utils.Redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.ajbxyyx.constant.en_US.SIGN_IN_PHONE_USED;

@Service
public class SignInServiceImp implements SignInService {

    @Resource
    private UserDao userDao;

    @Override
    public void dealSignIn(String mobileCountry, String mobile) throws BusinessException {
//        User result = userDao.lambdaQuery()
//                .eq(User::getPhoneCountry, mobileCountry)
//                .eq(User::getPhone, mobile)
//                .one();
//        if(result != null){
//            throw new BusinessException(200,SIGN_IN_PHONE_USED);//手機號已經注冊過了
//        }
//        String verificationCode = generateVerificationCode(6);
//        RedisUtil.set(RedisKey.LoginKey(mobileCountry,mobile),verificationCode,1000L*60*10);//ten mins expire

    }







    private String generateVerificationCode(Integer length){
        if(length<=0){
            return null;
        }
        return String.valueOf(new Random().nextInt((int) (9 * Math.pow(10, length-1)) + (int) (Math.pow(10, length-1))));
    }


}
