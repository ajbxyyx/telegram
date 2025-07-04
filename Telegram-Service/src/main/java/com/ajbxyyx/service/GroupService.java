package com.ajbxyyx.service;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.dto.Group.CommonGroupCursorQueryDTO;
import com.ajbxyyx.entity.dto.Group.GroupMemberCursorQueryDTO;
import com.ajbxyyx.entity.dto.Group.NewGroupDTO;
import com.ajbxyyx.entity.dto.message.PinMessageDTO;
import com.ajbxyyx.entity.po.GroupTable;
import com.ajbxyyx.entity.vo.CommonGroupVO;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.GroupMemberVO;
import com.ajbxyyx.entity.vo.GroupVO;
import com.ajbxyyx.entity.vo.Message.PinnedMessage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface GroupService {
    /**
     * 新建群聊
     * @param req
     * @param avatar
     * @throws BusinessException
     */
    void newGroup(NewGroupDTO req,MultipartFile avatar) throws BusinessException;


    /**
     * 緩存獲取群聊基礎信息
     * @param groupIdList
     * @return
     */
    Map<Long, GroupVO> getGroupBaseInfo(List<Long> groupIdList);

    BaseCursorQueryVO<GroupMemberVO> getGroupMember(GroupMemberCursorQueryDTO req);

    BaseCursorQueryVO<CommonGroupVO> getCommonGroup(CommonGroupCursorQueryDTO req);

    PinnedMessage pinMessage(PinnedMessage newPinnedMessage,PinMessageDTO req);

    List<GroupTable> getGroupByIds(List<Long> collect);
}
