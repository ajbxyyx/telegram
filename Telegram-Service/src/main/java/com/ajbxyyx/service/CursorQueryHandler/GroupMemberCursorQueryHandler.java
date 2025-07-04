package com.ajbxyyx.service.CursorQueryHandler;

import com.ajbxyyx.dao.MemberDao;
import com.ajbxyyx.entity.dto.Group.GroupMemberCursorQueryDTO;
import com.ajbxyyx.entity.po.Member;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.GroupMemberVO;
import com.ajbxyyx.utils.CursorQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GroupMemberCursorQueryHandler extends CursorQueryUtil<GroupMemberCursorQueryDTO, Member, GroupMemberVO> {

    @Resource
    private MemberDao memberDao;

    @Override
    public Page<Member> getPage(Page page, LambdaQueryWrapper<Member> lambdaQueryWrapper) {
        return memberDao.page(page, lambdaQueryWrapper);
    }

    @Override
    public BaseCursorQueryVO<GroupMemberVO> getCursorQueryRespData(BaseCursorQueryVO<GroupMemberVO> resp, List<Member> members) {
        List<GroupMemberVO> result = new ArrayList<>();
        for (Member member : members) {
            GroupMemberVO vo = new GroupMemberVO();
            vo.setId(member.getId());
            vo.setUid(member.getUid());
            vo.setPosition(member.getPosition());
            result.add(vo);
        }
        resp.setResult(result);
        return resp;
    }

    @Override
    public Long getDataLastCursor(List<GroupMemberVO> data) {
        return data.size() == 0?null:data.get(data.size() - 1).getId();
    }
}
