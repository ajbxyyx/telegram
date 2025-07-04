package com.ajbxyyx.service.Imp;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.dao.GroupDao;
import com.ajbxyyx.dao.MemberDao;
import com.ajbxyyx.entity.dto.Group.CommonGroupCursorQueryDTO;
import com.ajbxyyx.entity.dto.Group.GroupMemberCursorQueryDTO;
import com.ajbxyyx.entity.dto.Group.NewGroupDTO;
import com.ajbxyyx.entity.dto.message.PinMessageDTO;
import com.ajbxyyx.entity.enums.GroupPositionEnum;
import com.ajbxyyx.entity.po.GroupTable;
import com.ajbxyyx.entity.po.Member;
import com.ajbxyyx.entity.vo.CommonGroupVO;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.GroupMemberVO;
import com.ajbxyyx.entity.vo.GroupVO;
import com.ajbxyyx.entity.vo.Message.PinnedMessage;
import com.ajbxyyx.service.CursorQueryHandler.CommonGroupCursorQueryHandler;
import com.ajbxyyx.service.CursorQueryHandler.GroupMemberCursorQueryHandler;
import com.ajbxyyx.service.GroupService;
import com.ajbxyyx.service.MessageService;
import com.ajbxyyx.utils.AliOssUtil;
import com.ajbxyyx.utils.Redis.CacheHandlers.GroupCacheHandler;
import com.ajbxyyx.utils.Test;
import com.ajbxyyx.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GroupServiceImp implements GroupService {

    @Resource
    private AliOssUtil aliOssUtil;
    @Resource
    private MemberDao memberDao;
    @Resource
    private GroupDao groupDao;
    @Resource
    private GroupCacheHandler groupCacheHandler;
    @Resource
    private GroupMemberCursorQueryHandler groupMemberCursorQueryHandler;
    @Resource
    private CommonGroupCursorQueryHandler commonGroupCursorQueryHandler;
    @Resource
    @Lazy
    private MessageService messageService;


    /**
     * 新建群聊
     * @param req
     * @param avatar
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void newGroup(NewGroupDTO req, MultipartFile avatar) throws BusinessException {
        String name = req.getName();
        String avatarUrl = null;
        try {
            //上傳頭像
            if(avatar != null)
                avatarUrl = aliOssUtil.upload(avatar.getBytes(), avatar.getOriginalFilename());
        }catch (Exception e){
            throw new BusinessException(500,"oss error");
        }
        GroupTable groupTable = buildGroupPO(avatarUrl, name);
        Member member = buildOwnerMember(groupTable);
        groupTable.setMemberId(member.getId());

        try {
            //事務保存
            ((GroupServiceImp)AopContext.currentProxy()).transactionalSaveGroup(groupTable,member);
        }catch (Exception e){
            throw new BusinessException(500,"save mysql data error");
        }

        if(req.getInviteUidList() != null && req.getInviteUidList().size() != 0){
            //todo deal invite member
        }
    }

    @Override
    public Map<Long, GroupVO> getGroupBaseInfo(List<Long> groupIdList) {
        Map<Long, GroupVO> result = groupCacheHandler.getFromCache(groupIdList);
        return result;
    }

    @Override
    public BaseCursorQueryVO<GroupMemberVO> getGroupMember(GroupMemberCursorQueryDTO req) {
        LambdaQueryWrapper<Member> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Member::getGroupId, req.getGroupId());
        if(req.getCursor() != null){
            lambdaQueryWrapper.gt(Member::getId,req.getCursor());
        }
        BaseCursorQueryVO<GroupMemberVO> result = groupMemberCursorQueryHandler.CursorQuery(req, lambdaQueryWrapper);

        return result;
    }

    /**
     * query two users common group
     * @param req
     * @return
     */
    @Override
    public BaseCursorQueryVO<CommonGroupVO> getCommonGroup(CommonGroupCursorQueryDTO req) {
        LambdaQueryWrapper<Member> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Member::getUid, req.getUid(), ThreadLocalUtil.getUid());
        if(req.getCursor() != null){
            lambdaQueryWrapper.lt(Member::getId,req.getCursor());
        }

        BaseCursorQueryVO<CommonGroupVO> result = commonGroupCursorQueryHandler.CursorQuery(req, lambdaQueryWrapper);
        return result;
    }

    /***
     * pin a group message   [Transactional]
     * @param req
     */
    @Override
    @Transactional
    public PinnedMessage pinMessage(PinnedMessage newPinnedMessage,PinMessageDTO req) {
        GroupTable group = groupDao.lambdaQuery()
                .eq(GroupTable::getId, req.getChatId())
                .one();
        List<PinnedMessage> originalPinnedMessageIds = JSONUtil.toList(group.getPinnedMessages(), PinnedMessage.class);


        originalPinnedMessageIds = messageService.checkAndAddNewPinnedMessage(originalPinnedMessageIds, newPinnedMessage);
        String newPinnedMessageIdsJson = JSONUtil.toJsonStr(originalPinnedMessageIds);
        groupDao.lambdaUpdate()
                .eq(GroupTable::getId,req.getChatId())
                .set(GroupTable::getPinnedMessages,newPinnedMessageIdsJson).update();
        return newPinnedMessage;
    }

    @Override
    public List<GroupTable> getGroupByIds(List<Long> groupIds) {
        return groupDao.lambdaQuery()
                .in(GroupTable::getId,groupIds).list();
    }


    @Transactional
    protected void transactionalSaveGroup(GroupTable groupTable, Member member){
        groupDao.save(groupTable);
        memberDao.save(member);
    }

    private Member buildOwnerMember(GroupTable groupTable) {
        Member member = new Member();
        member.setGroupId(groupTable.getId());
        member.setId(Test.testGetId());
        member.setPosition(GroupPositionEnum.OWNER.getPosition());
        member.setUid(ThreadLocalUtil.getUid());
        return member;
    }
    private GroupTable buildGroupPO(String avatarUrl, String name) {
        GroupTable groupTable = new GroupTable();
        groupTable.setId(0-Test.testGetId());
        groupTable.setAvatar(avatarUrl);
        groupTable.setName(name);
        return groupTable;
    }

}
