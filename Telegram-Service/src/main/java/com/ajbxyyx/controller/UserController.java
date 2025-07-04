package com.ajbxyyx.controller;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.common.entities.ApiResult;
import com.ajbxyyx.entity.dto.SaveProfileDTO;
import com.ajbxyyx.entity.dto.SetPrivacyDTO;
import com.ajbxyyx.entity.enums.PrivacyEnum;
import com.ajbxyyx.entity.enums.PrivacyLevelEnum;
import com.ajbxyyx.entity.vo.LoginUserVO;
import com.ajbxyyx.entity.vo.UserPrivacyVO;
import com.ajbxyyx.entity.vo.UserVO;
import com.ajbxyyx.service.Imp.UserServiceImp;
import com.ajbxyyx.utils.Redis.Annotation.RequestLimit;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserServiceImp userServiceImp;

    /**
     * 通過uid 獲取用戶基礎信息
     * @param uids
     * @return
     */
    @GetMapping("/userinfo")
    public ApiResult< UserVO> getUserBaseInfoByUid(@RequestParam String uids){
        Long uid = ThreadLocalUtil.getUid();
        List<Long> uidList = Arrays.stream(uids.split(",")).map(o -> Long.parseLong(o)).collect(Collectors.toList());
        Map<Long, UserVO> result = userServiceImp.getUserBaseInfo(uidList,uid);
        return ApiResult.success(result.values().stream().collect(Collectors.toList()).get(0));
    }

    /**
     * 通過uid獲取群聊信息
     * @param uids
     * @return
     */
    @GetMapping("/group/userinfo")
    public ApiResult<UserVO> getGroupUserInfoByUid(@RequestParam String uids,@RequestParam Long groupId){
        List<Long> uidList = Arrays.stream(uids.split(",")).map(o -> Long.parseLong(o)).collect(Collectors.toList());

        return null;
    }




    /**
     * 修改隱私設置
     * @param privacy
     * @param val
     * @return
     * @throws BusinessException
     */
    @PostMapping("/privacy/set")
    public ApiResult<Void> privacyModify(@RequestBody @Valid SetPrivacyDTO req) throws BusinessException {
        PrivacyEnum privacyEnum = PrivacyEnum.of(req.getPrivacy());
        PrivacyLevelEnum privacyLevelEnum = PrivacyLevelEnum.of(req.getVal());
        userServiceImp.privacyModify(privacyEnum,privacyLevelEnum);
        return ApiResult.success();
    }

    @GetMapping("/privacy")
    public ApiResult<UserPrivacyVO> getUserPrivacySettings() throws BusinessException {
        Long uid = ThreadLocalUtil.getUid();
        UserPrivacyVO result = userServiceImp.getUserPrivacySettins(uid);
        return ApiResult.success(result);
    }



    /**
     * 根據username查詢用戶
     * @param username
     * @return
     */
    @GetMapping("/search/username")
    public ApiResult<List<Long>> searchByUsername(@RequestParam String username){
        List<Long> result = userServiceImp.searchByUsername(username);
        return ApiResult.success(result);
    }
    /**
     * 獲取自己的登入信息 不帶token屬性的LoginUserVO
     * @return
     */
    @GetMapping("/info")
    public ApiResult<LoginUserVO> getLoginUserVO() throws BusinessException {
        Long uid = ThreadLocalUtil.getUid();
        LoginUserVO result = userServiceImp.getMyLoginInfo(uid);
        return ApiResult.success(result);
    }
    /**
     * 檢測username是否被占用
     * @param username
     * @return
     * @throws BusinessException
     */
    @PostMapping("username/check/{username}")
    public ApiResult<Void> checkUsernameTaken(@PathVariable String username) throws BusinessException {
        userServiceImp.checkUsernameTaken(username);
        return ApiResult.success();
    }
    /**
     * 保存用戶個人信息
     * @param req
     * @return
     */
    @RequestLimit(ms = 1000L, limit = 1)
    @PostMapping("/profile/save")
    public ApiResult<Void> saveProfile(@RequestBody @Valid SaveProfileDTO req){
        userServiceImp.saveProfile(req);
        return ApiResult.success();
    }

















//
//
//
//
//
//    @GetMapping("/channels")
//    public ApiResult<List<ChannelVO>> getChannels(){
//        Long uid = 1L;
//        User one = userDao.lambdaQuery()
//                .eq(User::getId, uid)
//                .one();
//        if(one != null){
//            fillNullAttribute(one);
//            List<ChannelVO> userChannels = userServiceImp.getUserChannels(one);
//            return ApiResult.success(userChannels);
//        }
//        return ApiResult.fail(403,"非法请求");
//    }
//
//
//
//
//
//
//
//
//
//
//
//    private void fillNullAttribute(User one) {
//
//        if(one.getPinChannels() == null){
//            one.setPinChannels(new ArrayList<>());
//        }
//        if(one.getMuteChannels() == null){
//            one.setMuteChannels(new ArrayList<>());
//        }
//        if(one.getArchiveChannels() == null){
//            one.setArchiveChannels(new ArrayList<>());
//        }
//
//
//
//    }
}
