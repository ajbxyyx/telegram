package com.ajbxyyx.service.Imp;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.dao.ContactDao;
import com.ajbxyyx.dao.UserDao;
import com.ajbxyyx.entity.dto.AddContactDTO;
import com.ajbxyyx.entity.dto.DeleteContactDTO;
import com.ajbxyyx.entity.dto.UpdateContactDTO;
import com.ajbxyyx.entity.po.Contact;
import com.ajbxyyx.entity.po.User;
import com.ajbxyyx.entity.vo.UserVO;
import com.ajbxyyx.service.ContactService;
import com.ajbxyyx.service.UserService;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContactServiceImp implements ContactService {
    private static final Logger log = LoggerFactory.getLogger(ContactServiceImp.class);
    @Resource
    private UserService userService;
    @Resource
    private ContactDao contactDao;
    @Resource
    private UserDao userDao;



    @Override
    public Contact getContactByUid(Long uid, Long targetUid){
        Contact result = contactDao.lambdaQuery()
                .eq(Contact::getUid, uid)
                .eq(Contact::getTargetUid, targetUid)
                .select(Contact::getUid,Contact::getFirstName,Contact::getLastName)
                .one();
        return result;
    }




    @Override
    public List<Long> getContactList() {
        List<Contact> list = contactDao.lambdaQuery()
                .eq(Contact::getUid, ThreadLocalUtil.getUid())
                .select(Contact::getTargetUid)
                .list();
        List<Long> result = list.stream().map(o -> {//聯係人uid集合
            return o.getTargetUid();
        }).collect(Collectors.toList());


        return result;
    }

    @Override
    public void newContact(Contact contact) throws BusinessException {
        User user = userDao.lambdaQuery()
                .eq(User::getPhone, contact.getMobile())
                .eq(User::getPhoneCountry, contact.getMobileCountry())
                .one();
        if(user == null){
            throw new BusinessException(500,"找不到該用戶");
        }else{
            contact.setTargetUid(user.getId());
            contactDao.save(contact);
        }
    }

    @Override
    public void addNewContact(AddContactDTO req) throws BusinessException {
        if(req.getId() == ThreadLocalUtil.getUid()){
            throw new BusinessException(500,"add yourself to a contact is not allow");
        }
        Contact one = contactDao.lambdaQuery()
                .eq(Contact::getUid, ThreadLocalUtil.getUid())
                .eq(Contact::getTargetUid, req.getId())
                .select(Contact::getUid)
                .one();
        if(one == null){
            Contact contact = new Contact();
            contact.setUid(ThreadLocalUtil.getUid());
            contact.setTargetUid(req.getId());
            contact.setFirstName(req.getFirstName());
            contact.setLastName(req.getLastName());
            contactDao.save(contact);
        }else{
            throw new BusinessException(500,"already a contact");
        }
    }

    @Override
    public void updateContact(UpdateContactDTO req) {
        contactDao.lambdaUpdate()
                .eq(Contact::getUid,ThreadLocalUtil.getUid())
                .eq(Contact::getTargetUid,req.getId())
                .set(Contact::getFirstName,req.getFirstName())
                .set(Contact::getLastName,req.getLastName())
                .update();
    }

    @Override
    public void deleteContact(DeleteContactDTO req) {
        contactDao.lambdaUpdate()
                .eq(Contact::getUid,ThreadLocalUtil.getUid())
                .eq(Contact::getTargetUid,req.getId())
                .remove();
    }
}
