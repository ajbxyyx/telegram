package com.ajbxyyx.dao;

import com.ajbxyyx.dao.mapper.DeviceRecordMapper;
import com.ajbxyyx.dao.mapper.MessageMapper;
import com.ajbxyyx.entity.po.DeviceRecord;
import com.ajbxyyx.entity.po.Message;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class DeviceRecordDao  extends ServiceImpl<DeviceRecordMapper, DeviceRecord> {
}
