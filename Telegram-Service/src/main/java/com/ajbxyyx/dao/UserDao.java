package com.ajbxyyx.dao;

import com.ajbxyyx.dao.mapper.UserMapper;
import com.ajbxyyx.entity.po.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class UserDao extends ServiceImpl<UserMapper, User> {
}
