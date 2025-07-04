package com.ajbxyyx.utils.Redis.CacheHandlers;

import com.ajbxyyx.constant.RedisKey;
import com.ajbxyyx.dao.GroupDao;
import com.ajbxyyx.entity.po.GroupTable;
import com.ajbxyyx.entity.vo.GroupVO;
import com.ajbxyyx.utils.Redis.CacheUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GroupCacheHandler  extends CacheUtil<GroupVO> {

    @Resource
    private GroupDao groupDao;

    @Override
    public Map<Long, GroupVO> queryFromDB(List<Long> ids) {
        List<GroupTable> list = groupDao.lambdaQuery()
                .in(GroupTable::getId, ids).list();
        Map<Long, GroupVO> result = list.stream().map(o -> {
            GroupVO vo = new GroupVO();
            vo.setGroupId(o.getId());
            vo.setColor(o.getColor());
            vo.setName(o.getName());
            vo.setAvatar(o.getAvatar());
            vo.setDescription(o.getDescription());
            vo.setMemberNum(o.getMembers());
            return vo;
        }).collect(Collectors.toMap(k -> k.getGroupId(), v -> v));
        return result;
    }

    @Override
    public Long getExpireTime() {
        return 50L;
    }

    @Override
    public String getRedisKey(Long id) {
        return RedisKey.BaseGroupInfoKey(id);
    }
}
