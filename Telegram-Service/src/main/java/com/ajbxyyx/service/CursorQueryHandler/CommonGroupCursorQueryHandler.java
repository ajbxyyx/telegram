package com.ajbxyyx.service.CursorQueryHandler;

import com.ajbxyyx.dao.MemberDao;
import com.ajbxyyx.entity.dto.Group.CommonGroupCursorQueryDTO;
import com.ajbxyyx.entity.dto.Group.GroupMemberCursorQueryDTO;
import com.ajbxyyx.entity.po.Member;
import com.ajbxyyx.entity.vo.CommonGroupVO;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.GroupMemberVO;
import com.ajbxyyx.utils.CursorQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommonGroupCursorQueryHandler extends CursorQueryUtil<CommonGroupCursorQueryDTO, Member, CommonGroupVO> {

    @Resource
    private MemberDao memberDao;

    @Override
    public Page<Member> getPage(Page page, LambdaQueryWrapper<Member> lambdaQueryWrapper) {
        return memberDao.page(page, lambdaQueryWrapper);
    }

    @Override
    public BaseCursorQueryVO<CommonGroupVO> getCursorQueryRespData(BaseCursorQueryVO<CommonGroupVO> resp, List<Member> members) {
        List<CommonGroupVO> result = new ArrayList<>();

        HashMap<Long, Member> map = new HashMap<>();
        List<Member> commonGroup = new ArrayList<>();
        members.forEach(member -> {
            if(map.containsKey(member.getGroupId())) {
                commonGroup.add(member);
            }else{
                map.put(member.getGroupId(),member);
            }

        });

        for (Member member : commonGroup) {
            CommonGroupVO vo = new CommonGroupVO();
            vo.setGroupId(member.getGroupId());
            result.add(vo);
        }
        resp.setResult(result);
        return resp;
    }

    @Override
    public Long getDataLastCursor(List<CommonGroupVO> data) {
        return data.size() == 0?null:data.get(data.size() - 1).getGroupId();
    }
}
