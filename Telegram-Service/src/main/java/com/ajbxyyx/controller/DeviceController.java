package com.ajbxyyx.controller;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.common.entities.ApiResult;
import com.ajbxyyx.entity.po.DeviceRecord;
import com.ajbxyyx.entity.vo.DeviceRecordVO;
import com.ajbxyyx.service.DeviceRecordService;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Resource
    private DeviceRecordService deviceRecordService;

    @GetMapping("")
    public ApiResult<List<DeviceRecordVO>> getDeviceRecord() throws BusinessException {
        Long uid = ThreadLocalUtil.getUid();
        List<DeviceRecordVO> result = deviceRecordService.getAllDeviceRecord(uid);
        return ApiResult.success(result);
    }

    @PostMapping("/terminate/{id}")
    public ApiResult<Void> terminateSession(@PathVariable Long id) throws BusinessException {
        Long uid = ThreadLocalUtil.getUid();
        deviceRecordService.terminateSession(id,uid);
        return ApiResult.success();
    }



}
