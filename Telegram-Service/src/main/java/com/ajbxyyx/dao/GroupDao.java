package com.ajbxyyx.dao;

import com.ajbxyyx.dao.mapper.GroupMapper;
import com.ajbxyyx.entity.po.GroupTable;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class GroupDao extends ServiceImpl<GroupMapper, GroupTable> {
}
