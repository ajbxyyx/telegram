package com.ajbxyyx.service;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.po.DeviceRecord;
import com.ajbxyyx.entity.vo.DeviceRecordVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface DeviceRecordService {


    DeviceRecord recordDevice(HttpServletRequest request,Long uid,String token) throws BusinessException;

    List<DeviceRecordVO> getAllDeviceRecord(Long uid) throws BusinessException;

    void terminateSession(Long id, Long uid) throws BusinessException;
}
