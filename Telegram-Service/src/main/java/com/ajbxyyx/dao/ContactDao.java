package com.ajbxyyx.dao;

import com.ajbxyyx.dao.mapper.ContactMapper;
import com.ajbxyyx.entity.po.Contact;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class ContactDao  extends ServiceImpl<ContactMapper, Contact> {




}
