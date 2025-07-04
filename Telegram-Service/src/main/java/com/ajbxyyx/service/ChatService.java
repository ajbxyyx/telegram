package com.ajbxyyx.service;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.dao.ChatDao;
import com.ajbxyyx.dao.MemberDao;
import com.ajbxyyx.entity.dto.Chat.*;
import com.ajbxyyx.entity.dto.MsgHistoryCursorQueryDTO;
import com.ajbxyyx.entity.dto.message.PinMessageDTO;
import com.ajbxyyx.entity.po.Chat;
import com.ajbxyyx.entity.po.GroupTable;
import com.ajbxyyx.entity.po.Member;
import com.ajbxyyx.entity.po.Message;
import com.ajbxyyx.entity.vo.ChatVO;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.Message.PinnedMessage;
import com.ajbxyyx.entity.vo.MessageHistoryVO;
import com.ajbxyyx.entity.vo.MessageVO;
import com.ajbxyyx.utils.CursorQueryUtil;
import com.ajbxyyx.utils.Test;
import com.ajbxyyx.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {


    @Resource
    private CursorQueryUtil msgHistoryCursorQueryHandler;
    @Resource
    private ChatDao chatDao;
    @Resource
    private MemberDao memberDao;
    @Resource
    @Lazy
    private MessageService messageService;
    @Resource
    private GroupService groupService;

    /**
     * cursor query a channel's history of message
     * @param id -> channel id
     * @return
     */
    public BaseCursorQueryVO<MessageVO> cursorQueryMsgHistory(Long id, MsgHistoryCursorQueryDTO req) {
        LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Message::getReceive,id)
                .orderByDesc(Message::getDate);
        BaseCursorQueryVO<MessageVO> result = msgHistoryCursorQueryHandler.CursorQuery(req, lambdaQueryWrapper);
        return result;
    }

    public LinkedHashMap<Long, ChatVO> getChatVOList() {
        //單聊
        List<Chat> list1 = chatDao.lambdaQuery()
                .eq(Chat::getUid, ThreadLocalUtil.getUid()).list();
        //群聊
        List<Member> list2 = memberDao.lambdaQuery()
                .eq(Member::getUid, ThreadLocalUtil.getUid())
                .list();
        //for getting group pinned messages
        Map<Long, GroupTable> groupsMap = groupService.getGroupByIds(list2.stream().map(o -> o.getGroupId()).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(k -> k.getId(), v -> v));

        //單聊
        List<ChatVO> chatList = list1.stream().map(o -> {
            ChatVO vo = new ChatVO();
            //基礎信息
            vo.setId(o.getChatId());
            vo.setType("user");
            vo.setNotification(o.getMute());
            vo.setPin(o.getPin());
            //查聊天記錄
            List<MessageHistoryVO> msgHistory = messageService.getChatHistoryMessage(new MsgHistoryCursorQueryDTO(o.getChatId()));
            vo.setMessages(msgHistory);
            //查未读
            Long count = messageService.getChatUnreadCount(o.getChatId(), o.getUid(), o.getLastReadTime());
            vo.setUnreadCount(count);
            //set pinned messages
            vo.setPinnedMessages(JSONUtil.toList(o.getPinnedMessages(), PinnedMessage.class));
            return vo;
        }).collect(Collectors.toList());
        //群聊
        List<ChatVO> groupList = list2.stream().map(o -> {
            ChatVO vo = new ChatVO();
            vo.setId(o.getGroupId());
            vo.setType("group");
            vo.setPin(o.getPin());
            vo.setNotification(o.getNotification());
            vo.setPosition(o.getPosition());
            List<MessageHistoryVO> msgHistory = messageService.getGroupHistoryMessage(new MsgHistoryCursorQueryDTO(o.getGroupId()));
            vo.setMessages(msgHistory);
            //查未读
            Long count = messageService.getGroupUnreadCount(o.getGroupId(), o.getUid(), o.getLastReadTime());
            vo.setUnreadCount(count);
            //set pinned message
            vo.setPinnedMessages(JSONUtil.toList(groupsMap.get(o.getGroupId()).getPinnedMessages(),PinnedMessage.class));
            return vo;
        }).collect(Collectors.toList());
        //合并 轉map
        groupList.addAll(chatList);
        LinkedHashMap<Long, ChatVO> result = groupList.stream().sorted(
                ((o1,o2)->{
                    Long t1 = getLastMessageTime(o1);
                    Long t2 = getLastMessageTime(o2);
                    if(t1 < t2){
                        return -1;
                    }else{
                        return 1;
                    }
                })
        ).collect(Collectors.toMap(k -> k.getId(), v -> v, (e1,e2)->e1,LinkedHashMap::new));
        return result;
    }


    private Long getLastMessageTime(ChatVO o){
        if(o.getMessages() == null || o.getMessages().size() == 0){
            return 0L;
        }else{
            Long time = o.getMessages().get(o.getMessages().size() - 1).getTime();
            return time;
        }

    }










    public void newChat(Long uid, Long chatId) {
        newChat(uid,new NewChatDTO(chatId));
    }
    public void newChat(Long uid, NewChatDTO req) {
        Chat chat = buildChatPO(uid, req);
        chatDao.save(chat);
    }

    public void deleteChat(Long uid, DeleteChatDTO req) {
        chatDao.lambdaQuery()
                .eq(Chat::getChatId,req.getChatId())
                .one();
    }

    /**
     * receive是否有sender的chat
     * @return
     */
    public Boolean checkChat(Long receive,Long sender){
        Chat chat = chatDao.lambdaQuery()
                .eq(Chat::getUid, receive)
                .eq(Chat::getChatId, sender)
                .one();
        if(chat != null){
            return true;
        }
        return false;
    }











    private static Chat buildChatPO(Long uid, NewChatDTO req) {
        Chat chat = new Chat();
        chat.setId(Test.testGetId());
        chat.setUid(uid);
        chat.setChatId(req.getChatId());
        chat.setLastReadTime(new Date(0L));
        chat.setMute(0);
        chat.setPin(0);
        chat.setBlock(false);
        return chat;
    }


    public void readChat(ReadChatDTO req, Long uid) {
        if(req.getChatId() < 0){
            memberDao.lambdaUpdate()
                    .eq(Member::getUid,uid)
                    .eq(Member::getGroupId,req.getChatId())
                    .set(Member::getLastReadTime,new Date())
                    .update();
        }else{
            chatDao.lambdaUpdate()
                    .eq(Chat::getChatId,req.getChatId())
                    .eq(Chat::getUid,uid)
                    .set(Chat::getLastReadTime,new Date())
                    .update();
        }

    }

    public void pinChat(PinChatDTO req) {
        if(req.getChatId() < 0){
            memberDao.lambdaUpdate()
                    .eq(Member::getUid, ThreadLocalUtil.getUid())
                    .eq(Member::getGroupId,req.getChatId())
                    .setSql("pin = pin ^ 1")
                    .update();
        }else{
            chatDao.lambdaUpdate()
                    .eq(Chat::getUid, ThreadLocalUtil.getUid())
                    .eq(Chat::getChatId,req.getChatId())
                    .setSql("pin = pin ^ 1")
                    .update();
        }

    }

    public void muteChat(MuteChatDTO req) {
        if(req.getChatId() < 0){
            memberDao.lambdaUpdate()
                    .eq(Member::getUid, ThreadLocalUtil.getUid())
                    .eq(Member::getGroupId,req.getChatId())
                    .setSql("mute = mute ^ 1")
                    .update();
        }else{
            chatDao.lambdaUpdate()
                    .eq(Chat::getUid, ThreadLocalUtil.getUid())
                    .eq(Chat::getChatId,req.getChatId())
                    .setSql("mute = mute ^ 1")
                    .update();
        }
    }

    public PinnedMessage pinMessage(PinnedMessage newPinnedMessage,PinMessageDTO req) {
        List<Chat> list = chatDao.lambdaQuery()
                .and(warp -> warp
                        .and(w1 -> w1.eq(Chat::getUid, ThreadLocalUtil.getUid()).eq(Chat::getChatId, req.getChatId()))
                        .or(w2 -> w2.eq(Chat::getUid, req.getChatId()).eq(Chat::getChatId, ThreadLocalUtil.getUid()))
                ).list();
        Chat chat = list.get(0);
        List<PinnedMessage> originalPinnedMessageIds = JSONUtil.toList(chat.getPinnedMessages(), PinnedMessage.class);
        originalPinnedMessageIds = messageService.checkAndAddNewPinnedMessage(originalPinnedMessageIds, newPinnedMessage);
        String newPinnedMessageIdsJson = JSONUtil.toJsonStr(originalPinnedMessageIds);
        chatDao.lambdaUpdate()
                .and(warp -> warp
                        .and(w1 -> w1.eq(Chat::getUid, ThreadLocalUtil.getUid()).eq(Chat::getChatId, req.getChatId()))
                        .or(w2 -> w2.eq(Chat::getUid, req.getChatId()).eq(Chat::getChatId, ThreadLocalUtil.getUid()))
                        ).set(Chat::getPinnedMessages,newPinnedMessageIdsJson).update();
        return newPinnedMessage;
    }
}
