package com.ajbxyyx.controller;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.common.entities.ApiResult;
import com.ajbxyyx.entity.dto.Group.CommonGroupCursorQueryDTO;
import com.ajbxyyx.entity.dto.Group.GroupMemberCursorQueryDTO;
import com.ajbxyyx.entity.dto.Group.NewGroupDTO;
import com.ajbxyyx.entity.vo.CommonGroupVO;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.GroupMemberVO;
import com.ajbxyyx.entity.vo.GroupVO;
import com.ajbxyyx.entity.vo.UserVO;
import com.ajbxyyx.service.GroupService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Resource
    private GroupService groupService;

    /**
     * 新建群聊
     * @param req
     * @param avatar
     * @return
     * @throws BusinessException
     */
    @PostMapping("/new")
    public ApiResult<Void> newGroup(@RequestBody @Validated NewGroupDTO req,
                                    @RequestParam(required = false) MultipartFile avatar)
            throws BusinessException {
        groupService.newGroup(req,avatar);
        return ApiResult.success();
    }

    @GetMapping("/groupinfo")
    public ApiResult<GroupVO> getGroupBaseInfoByGroupIp(@RequestParam String groupIds){
        List<Long> groupIdList = Arrays.stream(groupIds.split(",")).map(o -> Long.parseLong(o)).collect(Collectors.toList());
        Map<Long, GroupVO> result =  groupService.getGroupBaseInfo(groupIdList);
        return ApiResult.success(result.values().stream().collect(Collectors.toList()).get(0));
    }

    @PostMapping("/member")
    public ApiResult<BaseCursorQueryVO<GroupMemberVO>> getGroupMember(@RequestBody GroupMemberCursorQueryDTO req){
        BaseCursorQueryVO<GroupMemberVO> result =  groupService.getGroupMember(req);
        return ApiResult.success(result);
    }
    @PostMapping("/member/common")
    public ApiResult<BaseCursorQueryVO<CommonGroupVO>> getGroupMember(@RequestBody CommonGroupCursorQueryDTO req){
        BaseCursorQueryVO<CommonGroupVO> result =  groupService.getCommonGroup(req);
        return ApiResult.success(result);
    }













}
