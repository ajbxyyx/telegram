package com.ajbxyyx.service;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.dto.AddContactDTO;
import com.ajbxyyx.entity.dto.DeleteContactDTO;
import com.ajbxyyx.entity.dto.UpdateContactDTO;
import com.ajbxyyx.entity.po.Contact;
import com.ajbxyyx.entity.vo.UserVO;

import java.util.List;

public interface ContactService {

    /**
     * 根據uid獲取對應聯係人對象
     * @param uid
     * @param targetUid
     * @return
     */
    Contact getContactByUid(Long uid, Long targetUid);

    /**
     * 獲取聯係人 uid 集合 並封裝為UserVO集合
     * @param uid
     * @return
     */
    List<Long> getContactList();

    void newContact(Contact contact) throws BusinessException;

    void addNewContact(AddContactDTO req) throws BusinessException;

    void updateContact(UpdateContactDTO req);

    void deleteContact(DeleteContactDTO req);
}
