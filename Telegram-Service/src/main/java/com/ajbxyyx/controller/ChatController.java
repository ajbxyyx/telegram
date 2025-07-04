package com.ajbxyyx.controller;

import com.ajbxyyx.common.entities.ApiResult;
import com.ajbxyyx.entity.dto.Chat.*;
import com.ajbxyyx.entity.vo.ChatVO;
import com.ajbxyyx.service.ChatService;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    /**
     * 查詢所有對話
     * @return
     */
    @GetMapping("/list")
    public ApiResult<LinkedHashMap<Long, ChatVO>> getChatVOList(){
        LinkedHashMap<Long, ChatVO> result =chatService.getChatVOList();

        return ApiResult.success(result);
    }




    /**
     * 已读對話
     * @return
     */
    @PostMapping("/read")
    public ApiResult<Void> readChat(@RequestBody ReadChatDTO req){
        chatService.readChat(req,ThreadLocalUtil.getUid());
        return ApiResult.success();
    }

    /**
     * 新建一個對話
     * @param req
     * @return
     */
    @PostMapping("/new")
    public ApiResult<Void> newChat(@RequestBody NewChatDTO req){
        chatService.newChat(ThreadLocalUtil.getUid(),req);
        return ApiResult.success();
    }

    /**
     * 刪除一個chat
     * @param req
     * @return
     */
    @PostMapping("/delete")
    public ApiResult<Void> deleteChat(@RequestBody DeleteChatDTO req){
        chatService.deleteChat(ThreadLocalUtil.getUid(),req);
        return ApiResult.success();
    }

    /**
     * Pin a chat
     * @param req
     * @return
     */
    @PostMapping("/pin")
    public ApiResult<Void> pinChat(@RequestBody PinChatDTO req){
        chatService.pinChat(req);
        return ApiResult.success();
    }

    /**
     * mute a chat
     * @param req
     * @return
     */
    @PostMapping("/mute")
    public ApiResult<Void> muteChat(@RequestBody MuteChatDTO req){
        chatService.muteChat(req);
        return ApiResult.success();
    }
























//    /**
//     * get history message by a channel id
//     * @param id
//     * @param cursor
//     * @return
//     */
//    @GetMapping("/msg/history")         //查詢頻道               //查詢游標
//    public ApiResult<BaseCursorQueryVO> getChannelHistoryMsg(@RequestParam Long id, @RequestParam(required = false) Long cursor){
//        Long uid = 1L;
//        User user = userDao.lambdaQuery()
//                .eq(User::getId, uid)
//                .select(User::getChannels)
//                .one();
//        //this user even didn't or be kicked from this channel ,refuse this request
//        if(!user.getChannels().contains("\"channelId\": "+id+",")){
//            return ApiResult.fail(403,"非法請求");
//        }
//
//        MsgHistoryCursorQueryDTO req = new MsgHistoryCursorQueryDTO();
//        req.setCursor(cursor);
//
//        BaseCursorQueryVO<MessageVO> result = chatService.cursorQueryMsgHistory(id,req);
//        return ApiResult.success(result);
//    }


}
