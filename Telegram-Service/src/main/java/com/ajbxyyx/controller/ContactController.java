package com.ajbxyyx.controller;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.common.entities.ApiResult;
import com.ajbxyyx.entity.dto.AddContactDTO;
import com.ajbxyyx.entity.dto.DeleteContactDTO;
import com.ajbxyyx.entity.dto.UpdateContactDTO;
import com.ajbxyyx.entity.po.Contact;
import com.ajbxyyx.entity.vo.UserVO;
import com.ajbxyyx.service.ContactService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Resource
    private ContactService contactService;
    /**
     * 獲取聯係人列表
     * @return
     * @throws BusinessException
     */
    @GetMapping("/list")
    public ApiResult<List<Long>> getContacts()  {
        List<Long> result = contactService.getContactList();
        return ApiResult.success(result);
    }
    /**
     * todo
     * @param firstName
     * @param lastName
     * @param mobileCountry
     * @param mobile
     * @return
     * @throws BusinessException
     */
    @PostMapping("/new")
    public ApiResult<Void> newContact(
            @RequestBody String firstName,@RequestBody(required = false) String lastName,
            @RequestBody Integer mobileCountry,@RequestBody Integer mobile
    ) throws BusinessException {
        Long uid = 123L;
        Contact contact = new Contact();
        contact.setUid(uid);
        contact.setMobileCountry(mobileCountry);
        contact.setMobile(mobile);
        contact.setFirstName(firstName);
        if(lastName != null){
            contact.setLastName(lastName);
        }

        //填補targetUid
        contactService.newContact(contact);
        return ApiResult.success();
    }


    /**
     * 新增
     * @param req
     * @return
     */
    @PostMapping("/add")
    public ApiResult<Void> addContact(@RequestBody @Valid AddContactDTO req) throws BusinessException {
        contactService.addNewContact(req);
        return ApiResult.success();
    }

    /**
     * 更新联系人
     * @param req
     * @return
     * @throws BusinessException
     */
    @PostMapping("/update")
    public ApiResult<Void> updateContact(@RequestBody @Valid UpdateContactDTO req) throws BusinessException {
        contactService.updateContact(req);
        return ApiResult.success();
    }

    /**
     * 刪除聯係人
     * @param req
     * @return
     * @throws BusinessException
     */
    @PostMapping("/delete")
    public ApiResult<Void> deleteContact(@RequestBody @Valid DeleteContactDTO req) throws BusinessException {
        contactService.deleteContact(req);
        return ApiResult.success();
    }




}
