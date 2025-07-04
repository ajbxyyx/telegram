package com.ajbxyyx.utils.Redis.CacheHandlers;


import com.ajbxyyx.constant.RedisKey;
import com.ajbxyyx.dao.UserDao;
import com.ajbxyyx.entity.po.User;
import com.ajbxyyx.entity.vo.UserVO;
import com.ajbxyyx.utils.Redis.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RedisKey 是uid  即id
 */
@Component
public class UserCacheHandler extends CacheUtil<UserVO> {

    @Autowired
    private UserDao userDao;



    @Override
    public Map<Long, UserVO> queryFromDB(List<Long> ids) {
        List<User> list = userDao.lambdaQuery()
                .in(User::getId,ids).list();
        Map<Long,UserVO> result = list.stream().map(o -> {
            UserVO vo = new UserVO();
            vo.setUid(o.getId());
            vo.setColor(o.getColor());
            vo.setFirstName(o.getFirstName());
            vo.setLastName(o.getLastName());

            vo.setUsername(o.getUsername());
            vo.setAvatar(o.getAvatar());
            vo.setBio(o.getBio());
            vo.setDateOfBirth(o.getDateOfBirth());
            vo.setMobile(o.getPhone());
            vo.setMobileCountry(o.getPhoneCountry());
            vo.setLastSeenTime(o.getLastSeen());
            return vo;
        }).collect(Collectors.toMap(k->k.getUid(),v->v));
        return result;
    }


    @Override
    public Long getExpireTime() {
        return 60L;
    }

    @Override
    public String getRedisKey(Long uid) {
        return RedisKey.BaseUserInfoKey(uid);
    }
}
