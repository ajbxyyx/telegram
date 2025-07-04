package com.ajbxyyx.dao;

import com.ajbxyyx.dao.mapper.MemberMapper;
import com.ajbxyyx.dao.mapper.MessageMapper;
import com.ajbxyyx.entity.po.Member;
import com.ajbxyyx.entity.po.Message;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MemberDao extends ServiceImpl<MemberMapper, Member> {

}
