package com.ajbxyyx.service.Imp;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.constant.RedisKey;
import com.ajbxyyx.dao.UserDao;
import com.ajbxyyx.entity.dto.LogInDTO;
import com.ajbxyyx.entity.po.DeviceRecord;
import com.ajbxyyx.entity.po.User;
import com.ajbxyyx.entity.vo.LoginUserVO;
import com.ajbxyyx.entity.vo.VerifyLoginDTO;
import com.ajbxyyx.service.AuthService;
import com.ajbxyyx.service.DeviceRecordService;
import com.ajbxyyx.utils.JwtUtil;
import com.ajbxyyx.utils.Redis.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

import static com.ajbxyyx.entity.vo.LoginUserVO.buildLoginUserVO;

@Service
public class AuthServiceImp implements AuthService {

    @Resource
    private DeviceRecordService deviceRecordService;
    @Resource
    private UserDao userDao;
    @Resource
    private JwtUtil jwtUtil;


    @Override
    public String login(LogInDTO req) throws BusinessException {
        User user = userDao.lambdaQuery()
                .eq(User::getPhoneCountry, req.getMobileCountry())
                .eq(User::getPhone, req.getMobile())
                .one();
        if(user == null){
            return register(req);
        }
        //設置驗證碼到Redis
        String verificationCode = randomCode();
        String uuid = UUID.randomUUID().toString();
        RedisUtil.set(RedisKey.LoginKey(uuid)
                ,verificationCode+"&"+req.getMobileCountry()+"&"+req.getMobile(),180);
        //TODO  發送驗證碼

        return uuid;
    }

    @Override
    public String register(LogInDTO req) throws BusinessException {
        User user = new User();
//        user.setPhoneCountry(req.getMobileCountry());
//        user.setPhone(req.getMobile());
        return null;
    }

    @Override
    public LoginUserVO checkVerificationCode(@Valid VerifyLoginDTO req, HttpServletRequest request) throws BusinessException {
        //驗證參數合法性
        String uuid = req.getUuid();
        String verificationCode = req.getVerificationCode();
        if(verificationCode == null || uuid == null){
            throw new BusinessException(500,"paramas error");
        }
        //讀Redis 匹配Code
        String redisStr = RedisUtil.getStr(RedisKey.LoginKey(req.getUuid()));
        String[] split = redisStr.split("&");
        String code = split[0];
        String phoneCountry = split[1];
        String phone = split[2];
        if(!code.equals(verificationCode)){
            throw new BusinessException(200,"verification code is expired or incorrect");
        }

        //匹配成功 登入成功
        User user = userDao.lambdaQuery()
                .eq(User::getPhoneCountry, phoneCountry)
                .eq(User::getPhone, phone)
                .one();
        if(user == null){
            throw new BusinessException(500,"unknow error!");
        }

        //建token
        String token = jwtUtil.createToken(user.getId());
        //記錄設備
        DeviceRecord deviceRecord = deviceRecordService.recordDevice(request, user.getId(), token);
        return buildLoginUserVO(user,token,deviceRecord.getId());
    }




    private String randomCode(){
        Random random = new Random();
        // 生成一个6位的随机数，范围在0到999999之间
        int randomNumber = random.nextInt(1000000);
        // 使用String.format确保结果是6位数字，不足的部分会补充前导零
        String formattedNumber = String.format("%06d", randomNumber);
        return formattedNumber;
    }


}
