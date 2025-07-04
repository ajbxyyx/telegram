package com.ajbxyyx.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class MessageReactionVO {

    private Long msgId;
    private Long chatId;

    private Boolean cancel;//only require in pushing MQ
    private Integer reactType;//as key
    private String src;
    private List<User> user;


    @AllArgsConstructor
    @Data
    public static class User{
        private Long uid;//uid操作了相对于uid的chatId
        private Long date;
    }

    public MessageReactionVO switchChatId(Long chatId){
        MessageReactionVO newVO = new MessageReactionVO();
        BeanUtils.copyProperties(this,newVO);
        newVO.chatId = chatId;
        return newVO;
    }

}
