package com.ajbxyyx.entity.vo;

import com.ajbxyyx.entity.po.User;
import lombok.Data;

@Data
public class LoginUserVO {



    private Long uid;
    private String username;
    private String avatar;
    private String firstName;
    private String lastName;
    private Integer color;
    private String phoneCountry;
    private String phone;

    private Long dateOfBirth;
    private String bio;
    private String logEmail;



    private Long deviceId;

    private String token;



    public static LoginUserVO buildLoginUserVO(User user, String token, Long deviceId){
        LoginUserVO vo = new LoginUserVO();
        vo.setUid(user.getId());
        vo.setUsername(user.getUsername());
        vo.setAvatar(user.getAvatar());
        vo.setFirstName(user.getUsername());
        vo.setLastName(user.getLastName());
        vo.setColor(user.getColor());
        vo.setPhoneCountry(user.getPhoneCountry());
        vo.setPhone(user.getPhone());
        vo.setDateOfBirth(user.getDateOfBirth().getTime());
        vo.setBio(user.getBio());
        vo.setLogEmail(user.getLogEmail());

        vo.setToken(token);
        vo.setDeviceId(deviceId);

        return vo;
    }

    public static LoginUserVO buildLoginUserVoWithoutToken(User user,Long deviceId){
        LoginUserVO vo = new LoginUserVO();
        vo.setUid(user.getId());
        vo.setUsername(user.getUsername());
        vo.setAvatar(user.getAvatar());
        vo.setFirstName(user.getFirstName());
        vo.setLastName(user.getLastName());
        vo.setColor(user.getColor());
        vo.setPhoneCountry(user.getPhoneCountry());
        vo.setPhone(user.getPhone());
        vo.setDateOfBirth(user.getDateOfBirth().getTime());
        vo.setBio(user.getBio());
        vo.setLogEmail(user.getLogEmail());

        vo.setDeviceId(deviceId);

        return vo;
    }


}
