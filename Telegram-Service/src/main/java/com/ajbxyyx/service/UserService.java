package com.ajbxyyx.service;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.dto.SaveProfileDTO;
import com.ajbxyyx.entity.enums.PrivacyEnum;
import com.ajbxyyx.entity.enums.PrivacyLevelEnum;
import com.ajbxyyx.entity.po.User;
import com.ajbxyyx.entity.vo.LoginUserVO;
import com.ajbxyyx.entity.vo.UserPrivacyVO;
import com.ajbxyyx.entity.vo.UserVO;

import java.util.List;
import java.util.Map;

public interface UserService {


    void updateLastSeenTime(Long userId, Long lastSeenTime);

    User queryByMobileNumber(String country, String phone );

    /**
     * 獲取用戶基礎信息 通過用戶關係
     * 查詢 uidList.size()次[關係]  + 1次[批量信息]
     * @param uidList
     * @param uid
     * @return
     */
    Map<Long, UserVO> getUserBaseInfo(List<Long> uidList, Long uid);


    /**
     * 修改隱私設置
     *
     * @param privacyEnum
     * @param privacyLevelEnum
     */
    void privacyModify(PrivacyEnum privacyEnum, PrivacyLevelEnum privacyLevelEnum) throws BusinessException;

    /**
     * 檢測Username是否被占用
     * @param username
     */
    void checkUsernameTaken(String username) throws BusinessException;

    void saveProfile(SaveProfileDTO req);

    LoginUserVO getMyLoginInfo(Long uid) throws BusinessException;

    UserPrivacyVO getUserPrivacySettins(Long uid) throws BusinessException;

    List<Long> searchByUsername(String username);


}
