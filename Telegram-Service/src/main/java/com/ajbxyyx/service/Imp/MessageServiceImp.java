package com.ajbxyyx.service.Imp;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.config.Kafka.KafkaTopic;
import com.ajbxyyx.config.Kafka.Producer.KafkaProducer;
import com.ajbxyyx.dao.*;
import com.ajbxyyx.entity.dto.MsgHistoryCursorQueryDTO;
import com.ajbxyyx.entity.dto.StickerDTO;
import com.ajbxyyx.entity.dto.message.*;
import com.ajbxyyx.entity.enums.*;
import com.ajbxyyx.entity.po.*;
import com.ajbxyyx.entity.vo.*;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.Kafka.KafkaMessageVO;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.*;
import com.ajbxyyx.entity.vo.Message.History.HistoryFileMessageVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryLinkMessageVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryMediaMessageVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryMusicMessageVO;
import com.ajbxyyx.entity.vo.Message.PinnedMessage;
import com.ajbxyyx.service.*;
import com.ajbxyyx.service.CursorQueryHandler.*;
import com.ajbxyyx.utils.Test;
import com.ajbxyyx.utils.ThreadLocalUtil;
import com.ajbxyyx.utils.UrlParseUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MessageServiceImp implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceImp.class);
    @Resource
    private MessageDao messageDao;
    @Resource
    private OssService ossService;
    @Resource
    private ChatService chatService;
    @Resource
    private GroupService groupService;
    @Resource
    private WebsocketService websocketService;
    @Resource
    private UserDao userDao;
    @Resource
    private ContactDao contactDao;
    @Resource
    private ContactService contactService;
    @Resource
    private MessageReactionDao messageReactionDao;
    @Resource
    private MemberDao memberDao;
    @Resource
    private DefaultMQProducer producer;
    @Resource
    private UrlParseUtil urlParseUtil;
    @Resource
    private MsgHistoryCursorQueryHandler msgHistoryCursorQueryHandler;
    @Resource
    private MediaMessageCursorQueryHandler mediaMessageCursorQueryHandler;
    @Resource
    private FileMessageCursorQueryHandler fileMessageCursorQueryHandler;
    @Resource
    private LinkMessageCursorQueryHandler linkMessageCursorQueryHandler;
    @Resource
    private MusicMessageCursorQueryHandler musicMessageCursorQueryHandler;


    @Resource
    private KafkaProducer kafkaProducer;


    @Override
    public SendMessageVO dealMessage(String text,
                                     List<MultipartFile> file,
                                     Long uid,
                                     Long replayId,
                                     Long receive,
                                     String uuid,
                                     String sticker
                            ) throws BusinessException {
        //判斷能不能發送
        checkSendLegal(uid,receive);

        StickerTypeEnums stickerType = null;StickerDuckEnums stickerDetail = null;

        List<PhotoVO> photoVOs = new ArrayList<>();
        List<VideoVO> videoVOs = new ArrayList<>();
        List<VoiceVO> voiceVOs = new ArrayList<>();
        List<LinkVO> linkVOS = new ArrayList<>();
        List<FileVO> fileVOs =new ArrayList<>();
        List<MusicVO> musicVOs =new ArrayList<>();


        CompletableFuture<List<BaseFileVO>> fileFuture = null;
        List<BaseFileVO> files =new ArrayList<>();
        //handle file async
        if(file != null && file.size() != 0)//上傳文件
            fileFuture = ossService.uploadFile(file);
        //handle link
        if(text != null){
            linkVOS = urlParseUtil.parseLinkVO(text);
        }
        //handle sticker
        if(sticker != null){
            StickerDTO req = JSONUtil.toBean(sticker, StickerDTO.class);
            stickerType = StickerTypeEnums.of(req.getType());
            stickerDetail = StickerDuckEnums.of(req.getTypeDetail());
            if(stickerType== null ||  stickerDetail == null){
                throw new BusinessException(500,"unlegal sticker");
            }
        }
        //get async handle file result
        try {
            files = fileFuture==null?files:fileFuture.get();
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(500,"upload to OSS fail");
        }
        for (BaseFileVO base : files) {
            switch (base.getType()){
                case FILE -> fileVOs.add(new FileVO(base.getName(), base.getSize(), base.getUrl()));
                case VIDEO -> videoVOs.add(new VideoVO(base.getUrl(),base.getThumb()));
                case VOICE -> voiceVOs.add(new VoiceVO(base.getUrl(),base.getDuration()));
                case AUDIO -> musicVOs.add(new MusicVO(base.getName(),base.getSize(), base.getDuration(),base.getUrl()));
                case IMAGE -> photoVOs.add(new PhotoVO(base.getUrl(),base.getWidth(),base.getHeight()));

            }
        }

        Message msg = buildMessagePO(text, uid, replayId, receive, photoVOs, videoVOs, fileVOs, voiceVOs,musicVOs,linkVOS,sticker);
//        msg.setSystem(false);
        messageDao.save(msg);//持久化
        //構建SendMessageVO
        SendMessageVO sendMessageVO = buildSendMessageVO(receive, uuid, msg, photoVOs, videoVOs, fileVOs, voiceVOs,musicVOs);

        MessageVO messageVO = null;
        if(receive >=0){
            //構建MessageVO
            messageVO = buildChatMessageVO(uid, receive, msg,stickerDetail);
            dealNewChat(uid, receive);
        }else{
            messageVO = buildGroupMessageVO(uid,receive,msg,stickerDetail);
        }

        //MQ推送
        try {
            kafkaProducer.sendMessage(KafkaTopic.PUSH_MESSAGE.getTopic(),
                    new KafkaMessageVO(ThreadLocalUtil.getUid(),receive,JSONUtil.toJsonStr(messageVO)));
//            producer.send(new org.apache.rocketmq.common.message.Message("PUSH_MESSAGE", String.valueOf(receive), JSONUtil.toJsonStr(messageVO).getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendMessageVO;
    }


    private void checkSendLegal(Long uid, Long receive) throws BusinessException {
        //群聊  -> 1.是否在群 2.是否被禁言
        if(receive < 0){
            Member one = memberDao.lambdaQuery()
                    .eq(Member::getGroupId, receive)
                    .eq(Member::getUid, uid)
                    .select(Member::getMute)
                    .one();
            if(one.getMute().getTime() >= new Date().getTime()){
                throw new BusinessException(200,"you are muted now");
            }
        }else{//單聊 -> 1.接收方是否允許接收
            User one = userDao.lambdaQuery()
                    .eq(User::getId, receive)
                    .one();
            if(PrivacyLevelEnum.of(one.getMessagesPrivacy()) == PrivacyLevelEnum.NOBODY){
                throw new BusinessException(200,"receiver unallow receive message");
            }
            if(PrivacyLevelEnum.of(one.getMessagesPrivacy()) == PrivacyLevelEnum.MY_CONTACTS){
                //接收方 對 發送方 是聯係人關係
                Contact contact = contactService.getContactByUid(receive, uid);
                if(contact == null){
                    throw new BusinessException(200,"receiver unallow receive message");
                }
            }
        }
    }

    private void dealNewChat(Long uid, Long receive) {
        //sender  uid    receive  receive receive是否有sender的chat
        Boolean haveChat1 = chatService.checkChat(receive, uid);
        Boolean haveChat2 = chatService.checkChat(uid, receive);
        if(!haveChat1)
            chatService.newChat(receive, uid);
        if(!haveChat2)
            chatService.newChat(uid, receive);
    }

    private MessageVO buildChatMessageVO(Long uid, Long receive, Message msg,StickerDuckEnums sticker) {
        MessageVO messageVO = new MessageVO();
        messageVO.setType("user");
        messageVO.setId(msg.getId());
        if(sticker != null){
            messageVO.setSticker(sticker.getValue());
        }

        messageVO.setReceive(receive);
        messageVO.setSender(uid);
        messageVO.setTime(new Date().getTime());
        messageVO.setReply(msg.getReply());
        if(msg.getReply() != null){
            Message replyMsg = messageDao.lambdaQuery()
                    .eq(Message::getId, msg.getReply())
                    .one();
            messageVO.setReplySenderId(replyMsg.getSender());
            messageVO.setReplyContent(replyMsg.getText());
        }
        messageVO.setContent(msg.getText());
        messageVO.setFile(msg.getFile());
        messageVO.setVideo(msg.getVideo());
        messageVO.setPhoto(msg.getPhoto());
        messageVO.setVoice(msg.getVoice());
        return messageVO;
    }


    private static SendMessageVO buildSendMessageVO(
            Long receive, String uuid, Message msg, List<PhotoVO> photos,
            List<VideoVO> videos, List<FileVO> files, List<VoiceVO> voices, List<MusicVO> musics) {
        SendMessageVO sendMessageVO = new SendMessageVO();

        sendMessageVO.setDate(new Date().getTime());
        sendMessageVO.setMsgId(msg.getId());
        sendMessageVO.setUuid(uuid);
        sendMessageVO.setReceiveId(receive);
        sendMessageVO.setPhotoUrl(photos);
        sendMessageVO.setVideoUrl(videos.stream().map(o->o.getVideoUrl()).collect(Collectors.toList()));
        sendMessageVO.setFileUrl(files.stream().map(o->o.getFileUrl()).collect(Collectors.toList()));
        sendMessageVO.setVoiceUrl(voices.stream().map(o->o.getVoiceUrl()).collect(Collectors.toList()));
        sendMessageVO.setMusicUrl(musics.stream().map(o->o.getMusicUrl()).collect(Collectors.toList()));

        return sendMessageVO;
    }

    private static Message buildMessagePO(String text, Long uid, Long replayId, Long receive, List<PhotoVO> photos,
                                          List<VideoVO> videos, List<FileVO> files, List<VoiceVO> voices, List<MusicVO> musics,
                                          List<LinkVO> links ,String sticker) {
        Message msg = new Message();
        if(files.size() > 0) msg.setIsFile(true);
        if(musics.size() != 0) msg.setIsMusic(true);
        if(videos.size() != 0) msg.setIsVideo(true);
        if(voices.size() != 0) msg.setIsVoice(true);
        if(photos.size() != 0) msg.setIsPhoto(true);
        if(links.size() != 0) msg.setIsLink(true);

        msg.setId(Test.testGetId());
        msg.setText(text);
        msg.setSticker(sticker);
        msg.setPhoto(JSONUtil.toJsonStr(photos));
        msg.setVideo(JSONUtil.toJsonStr(videos));
        msg.setFile(JSONUtil.toJsonStr(files));
        msg.setVoice(JSONUtil.toJsonStr(voices));
        msg.setLink(JSONUtil.toJsonStr(links));
        msg.setMusic(JSONUtil.toJsonStr(musics));

        msg.setSender(uid);
        msg.setDate(new Date());
        msg.setReply(replayId);
        msg.setReceive(receive);
        return msg;
    }



    /**
     * 獲取一個chat的聊天記錄
     */
    @Override
    public List<MessageHistoryVO> getChatHistoryMessage(MsgHistoryCursorQueryDTO cursorQueryDTO) {

        LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .in(Message::getSender,cursorQueryDTO.getChatId(),ThreadLocalUtil.getUid())
                .in(Message::getReceive,cursorQueryDTO.getChatId(),ThreadLocalUtil.getUid())
                .orderByDesc(Message::getId);
        if(cursorQueryDTO.getCursor() != null){//添加游标条件
            lambdaQueryWrapper.lt(Message::getId,cursorQueryDTO.getCursor());
        }

        //游标查询
        BaseCursorQueryVO<MessageHistoryVO> cursorQueryResult = msgHistoryCursorQueryHandler.CursorQuery(cursorQueryDTO, lambdaQueryWrapper);
        return cursorQueryResult.getResult().stream().sorted(Comparator.comparing(MessageHistoryVO::getId)).collect(Collectors.toList());
    }

    @Override
    public List<MessageHistoryVO> getGroupHistoryMessage(MsgHistoryCursorQueryDTO cursorQueryDTO) {

        LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Message::getReceive, cursorQueryDTO.getChatId())
                .orderByDesc(Message::getId);
        if(cursorQueryDTO.getCursor() != null){//添加游标条件
            lambdaQueryWrapper.lt(Message::getId,cursorQueryDTO.getCursor());
        }
        //游标查询
        BaseCursorQueryVO<MessageHistoryVO> cursorQueryResult = msgHistoryCursorQueryHandler.CursorQuery(cursorQueryDTO, lambdaQueryWrapper);
        return cursorQueryResult.getResult().stream().sorted(Comparator.comparing(MessageHistoryVO::getId)).collect(Collectors.toList());
    }

    @Override
    public Long getChatUnreadCount(Long chatId, Long uid, Date lastReadTime) {
        Long count = messageDao.lambdaQuery()
                .eq(Message::getSender, chatId)
                .eq(Message::getReceive, uid)
                .gt(Message::getDate,lastReadTime)
                .count();
        return count;
    }

    @Override
    public void typingMessage(TypingMessageDTO req) throws BusinessException {
        Long receiveId = req.getReceiveId();
        TypingMessageVO vo = new TypingMessageVO();
        vo.setTypingUid(ThreadLocalUtil.getUid());//who is typing?

        try{
            kafkaProducer.sendMessage(KafkaTopic.TYPING_MESSAGE.getTopic(),
                    new KafkaMessageVO(receiveId,JSONUtil.toJsonStr(vo)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ReplyMessageVO getReplyMessageInfo(ReplyMessageDTO req) {
        Message msg = messageDao.lambdaQuery()
                .eq(Message::getId, req.getMsgId())
                .one();
        Long senderId = msg.getSender();
        if(senderId < 0){// group msg  check wheather in that group

        }else{//personal msg  check wheather allow

        }
        ReplyMessageVO result =buildReplyMessageVO(msg);
        return result;
    }

    @Override
    @Transactional
    public void reactMessage(ReactMessageDTO req) throws BusinessException {
        Message msg = messageDao.lambdaQuery()
                .eq(Message::getId, req.getMsgId())
                .one();
        if(msg == null){
            throw new BusinessException(500,"message doesn't exist");
        }
        Long receiveId = msg.getReceive().equals(ThreadLocalUtil.getUid())?msg.getSender():msg.getReceive();
        Long senderId = ThreadLocalUtil.getUid();

        //check wheather allow access the message
        checkMessageLegalAccess(msg);

        Boolean cancelReaction = false;
        MessageReaction reaction = messageReactionDao.lambdaQuery()
                .eq(MessageReaction::getUid, ThreadLocalUtil.getUid())
                .eq(MessageReaction::getMessageId, req.getMsgId())
                .one();
        if(reaction == null){
            //react
            reaction = buildMessageReactionPO(req);
            messageReactionDao.save(reaction);
        }else if(reaction.getReactType() == req.getReactType()){
            //cancel react
            cancelReaction = true;
            messageReactionDao.removeById(reaction);
        }else{
            //change react
            reaction.setReactType(req.getReactType());
            messageReactionDao.updateById(reaction);
        }

        MessageReactionVO vo = buildMessageReactionVO(reaction,msg,cancelReaction);
        //mq
        try {
            kafkaProducer.sendMessage(KafkaTopic.REACT_MESSAGE.getTopic(),
                    new KafkaMessageVO(receiveId,JSONUtil.toJsonStr(vo)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static PushTargetVO getPushTargetVO(Long receiveId, List<Long> receiveUids) {
        PushTargetVO target;
        if(receiveId < 0){
            target = PushTargetVO.buildPushTargetVO(receiveId);
        }else{
            target = PushTargetVO.buildPushTargetVO(receiveUids);
        }
        return target;
    }

    private static MessageReactionVO buildMessageReactionVO(MessageReaction reaction, Message msg, Boolean cancelReaction) {
        MessageReactionVO vo = new MessageReactionVO();

        vo.setCancel(cancelReaction);
        vo.setMsgId(reaction.getMessageId());
        //if group getReceive() must less than 0 don't care
        //this only deal personal chat
        if(msg.getReceive() < 0){
            vo.setChatId(msg.getReceive());
        }else{
            vo.setChatId(ThreadLocalUtil.getUid());
        }


        vo.setReactType(reaction.getReactType());
        vo.setSrc(ReactionEnum.of(reaction.getReactType()).getSrc());
        vo.setUser(List.of(new MessageReactionVO.User(ThreadLocalUtil.getUid(),reaction.getCreateTime().getTime())));
        return vo;
    }

    /**
     * add message reaction to message
     */
    @Transactional
    @Override
    public void addMessageReaction(Long msgId, List<MessageReactionOld> messageReactionOlds) {
        Message msg = messageDao.lambdaQuery()
                .eq(Message::getId, msgId).one();
        String json = msg.getReaction();
        //db data
        List<MessageReactionOld> reactionList = JSONUtil.toList(json, MessageReactionOld.class);
        //k: reactionId  v: MessageReaction
        Map<Long, MessageReactionOld> reactionMap = reactionList.stream().collect(Collectors.toMap(k -> k.getReactionId(), v -> v));
        //combine old reactionList'uid and new reactionList'uid or add new PO
        messageReactionOlds.forEach(o->{
          if(reactionMap.containsKey(o.getReactionId()) ){
              reactionMap.get(o.getReactionId()).getUid().addAll(o.getUid());
          }else{
              reactionMap.put(o.getReactionId(),o);
          }
        });

        messageDao.lambdaUpdate()
                .eq(Message::getId,msgId)
                .set(Message::getReaction,JSONUtil.toJsonStr(reactionMap.values()))
                .update();
    }

    /**
     * Query media message
     *
     * @param req
     * @return
     */
    @Override
    public BaseCursorQueryVO<HistoryMediaMessageVO> getMediaMessage(MediaMessageCursorQueryDTO req) {
        LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(req.getCursor() != null){
            lambdaQueryWrapper
                    .lt(Message::getId,req.getCursor());
        }
        lambdaQueryWrapper
                .and(warp -> warp.eq(Message::getIsPhoto, 1).or().eq(Message::getIsVideo, 1))
                .and(warp -> warp
                        .and(w1 -> w1.eq(Message::getSender, ThreadLocalUtil.getUid()).eq(Message::getReceive, req.getChatId()))
                        .or(w2 -> w2.eq(Message::getSender, req.getChatId()).eq(Message::getReceive,  ThreadLocalUtil.getUid()))
                );
        lambdaQueryWrapper.orderByDesc(Message::getId);
        //cursor query
        BaseCursorQueryVO<HistoryMediaMessageVO> result = mediaMessageCursorQueryHandler.CursorQuery(req, lambdaQueryWrapper);
        return result;
    }

    /**
     * Query file message
     * @param req
     * @return
     */
    @Override
    public BaseCursorQueryVO<HistoryFileMessageVO> getFileMessage(FileMessageCursorQueryDTO req) {
        LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(req.getCursor() != null){
            lambdaQueryWrapper.lt(Message::getId,req.getCursor());
        }
        lambdaQueryWrapper
                .and(warp->warp.eq(Message::getIsFile,1))
                .and(warp -> warp
                        .and(w1 -> w1.eq(Message::getSender, ThreadLocalUtil.getUid()).eq(Message::getReceive, req.getChatId()))
                        .or(w2 -> w2.eq(Message::getSender, req.getChatId()).eq(Message::getReceive,  ThreadLocalUtil.getUid()))
                );
        lambdaQueryWrapper.orderByDesc(Message::getId);
        BaseCursorQueryVO<HistoryFileMessageVO> result = fileMessageCursorQueryHandler.CursorQuery(req, lambdaQueryWrapper);
        return result;
    }

    /**
     * query link message
     * @param req
     * @return
     */
    @Override
    public BaseCursorQueryVO<HistoryLinkMessageVO> getLinkMessage(LinkMessageCursorQueryDTO req) {

        LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(req.getCursor() != null){
            lambdaQueryWrapper.lt(Message::getId,req.getCursor());
        }
        lambdaQueryWrapper
                .and(warp->warp.eq(Message::getIsLink,1))
                .and(warp -> warp
                        .and(w1 -> w1.eq(Message::getSender, ThreadLocalUtil.getUid()).eq(Message::getReceive, req.getChatId()))
                        .or(w2 -> w2.eq(Message::getSender, req.getChatId()).eq(Message::getReceive,  ThreadLocalUtil.getUid()))
                );
        lambdaQueryWrapper.orderByDesc(Message::getId);

        BaseCursorQueryVO<HistoryLinkMessageVO> result = linkMessageCursorQueryHandler.CursorQuery(req, lambdaQueryWrapper);
        List<String> urlList = result.getResult().stream().map(o -> o.getLink()).collect(Collectors.toList());
        List<UrlParseVO> urlParseVOS = parseUrl(new ParseUrlDTO(urlList));

        for(int i = 0;i<urlParseVOS.size();i++){
            HistoryLinkMessageVO vo = result.getResult().get(i);
            vo.setTitle(urlParseVOS.get(i).getTitle()==null?vo.getLink():urlParseVOS.get(i).getTitle());
            vo.setDescription(urlParseVOS.get(i).getDescription()==null?vo.getText():urlParseVOS.get(i).getDescription());
        }
        return result;
    }

    @Override
    public BaseCursorQueryVO<HistoryMusicMessageVO> getMusicMessage(MusicMessageCursorQueryDTO req) {
        LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(req.getCursor() != null){
            lambdaQueryWrapper.lt(Message::getId,req.getCursor());
        }
        lambdaQueryWrapper
                .and(warp->warp.eq(Message::getIsMusic,1))
                .and(warp -> warp
                        .and(w1 -> w1.eq(Message::getSender, ThreadLocalUtil.getUid()).eq(Message::getReceive, req.getChatId()))
                        .or(w2 -> w2.eq(Message::getSender, req.getChatId()).eq(Message::getReceive,  ThreadLocalUtil.getUid()))
                );
        BaseCursorQueryVO<HistoryMusicMessageVO> result = musicMessageCursorQueryHandler.CursorQuery(req, lambdaQueryWrapper);
        return result;
    }

    /**
     * pin a message
     * @param req
     */
    @Override
    public void pinMessage(PinMessageDTO req) throws BusinessException {
        Message msg = messageDao.lambdaQuery()
                .eq(Message::getId, req.getMsgId())
                .one();
        //check legal?  and  get receiver
        Long receiver = checkMessageLegalAccess(msg);
        req.setChatId(receiver);
        //build vo
        PinnedMessage newPinnedMessage = new PinnedMessage(req.getMsgId(), new Date().getTime(),ThreadLocalUtil.getUid());
        newPinnedMessage = parsePinnedMessageContent(newPinnedMessage,msg);
        if(msg.getReceive() < 0){//group message
            newPinnedMessage = groupService.pinMessage(newPinnedMessage,req);
        }else{
            newPinnedMessage = chatService.pinMessage(newPinnedMessage,req);
        }

        //push MQ
        try {
            kafkaProducer.sendMessage(KafkaTopic.PIN_MESSAGE.getTopic(),
                    new KafkaMessageVO(receiver,JSONUtil.toJsonStr(newPinnedMessage)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(500,"MQ push error!");
        }
    }

    private PinnedMessage parsePinnedMessageContent(PinnedMessage newPinnedMessage, Message msg) {
        String msgContent = "";
        if(msg.getIsFile()){
            msgContent += "\uD83D\uDCCE";
            if(msg.getText() == null){
                msgContent += JSONUtil.toList(msg.getFile(), FileVO.class).get(0).getFileName();
            }
        }

        msgContent+=msg.getText();
        newPinnedMessage.setMsgContent(msgContent);
        return newPinnedMessage;
    }

    private static MessageReaction buildMessageReactionPO(ReactMessageDTO req) {
        MessageReaction reaction = new MessageReaction();
        reaction.setId(Test.testGetId());
        reaction.setMessageId(req.getMsgId());
        reaction.setReactType(req.getReactType());
        reaction.setUid(ThreadLocalUtil.getUid());
        reaction.setCreateTime(new Date());
        return reaction;
    }

    /**
     * msg -> receive , sender
     *
     * case GROUP: 1.belong this group?  2.mute?  3.have permession?
     * case USER: 1.allow send?
     * @param msg
     */
    private Long checkMessageLegalAccess(Message msg){
        Long receive = null;
        if(msg.getReceive()<0){//group
            receive = msg.getReceive();

        }else{//person
            receive = msg.getReceive()==ThreadLocalUtil.getUid()?msg.getSender():msg.getReceive();

        }
        return receive;
    }

    private ReplyMessageVO buildReplyMessageVO(Message msg) {
        ReplyMessageVO vo = new ReplyMessageVO();
        vo.setMsgId(msg.getId());
        vo.setSenderId(msg.getSender());

        vo.setText(setReplyMessageVoText(msg));
        vo.setThumbnail(setReplyMessageVoThumbnail(msg));

        return vo;
    }

    private static String setReplyMessageVoThumbnail(Message msg) {
        //todo
        return null;
    }

    private static String setReplyMessageVoText(Message msg) {
        String result = "";
        //sticker
        if(msg.getSticker() != null){
            result = "Sticker";
        }
        //file
        if(!msg.getFile().equals("[]")){
            List<FileVO> list = JSONUtil.toList(msg.getFile(), FileVO.class);
            if(list.size() == 1){
                result = list.get(0).getFileName();
            }else{
                result = list.size() + "files";
            }
        }
        //photo
        if(!msg.getPhoto().equals("[]")){
            List<PhotoVO> list = JSONUtil.toList(msg.getPhoto(), PhotoVO.class);
            if(list.size() == 1){
                result = "Photo";
            }else{
                result = list.size() + "photos";
            }
        }
        //video
        if(!msg.getVideo().equals("[]")){
            List<VideoVO> list = JSONUtil.toList(msg.getVideo(), VideoVO.class);
            if(list.size() == 1){
                result = "Video";
            }else{
                result = list.size() + "videos";
            }
        }
        //voice
        if(!msg.getVoice().equals("[]")){
            System.out.println(msg.getVoice());
            result = "Voice";
        }
        if(msg.getText() != null){
            if(result == ""){
                result = msg.getText();
            }else{
                result = result + "," + msg.getText();
            }
        }
        return result;
    }

    private static String addSuffixText(String originalText, Message msg) {
        if(msg.getText() != null){
            return originalText + ", " + msg.getText();
        }else{
            return originalText;
        }
    }


    @Override
    public Long getGroupUnreadCount(Long groupId, Long uid, Date lastReadTime) {
        Long count = messageDao.lambdaQuery()
                .eq(Message::getReceive, groupId)
                .gt(Message::getDate,lastReadTime)
                .count();
        return count;
    }




    @Override
    public void deleteMessage(DeleteMessageDTO req) throws BusinessException {
        Long deleteMessageId = req.getDeleteMessageId();
        Message delMessage = messageDao.lambdaQuery()
                .eq(Message::getId, deleteMessageId)
                .one();
        if(delMessage == null){
            throw new BusinessException(500,"unlegal message id");
        }
        if(!delMessage.getSender().equals(ThreadLocalUtil.getUid())){
            throw new BusinessException(500,"unlegal request,not your sent message");
        }
        messageDao.removeById(req.getDeleteMessageId());
        DeleteMessageVO deleteMessageVO = buildDeleteMessageVO(delMessage);
        try {
            kafkaProducer.sendMessage(KafkaTopic.DELETE_MESSAGE.getTopic(),
                    new KafkaMessageVO(delMessage.getReceive(),JSONUtil.toJsonStr(deleteMessageVO)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DeleteMessageVO buildDeleteMessageVO(Message delMessage) {
        DeleteMessageVO deleteMessageVO = new DeleteMessageVO();
        deleteMessageVO.setMsgId(delMessage.getId());
        return deleteMessageVO;
    }


    /**
     * 解析url
     * @param req
     * @return
     * @throws BusinessException
     */
    @Override
    public List<UrlParseVO> parseUrl(ParseUrlDTO req){
        List<CompletableFuture<UrlParseVO>> futures = req.getUrl().stream().map(url -> {
            return urlParseUtil.parseURL(url);
        }).collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allFutures.get();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return futures.stream()
                    .map(future -> {
                        try {
                            return future.join();
                        } catch (Exception e) {
                            return new UrlParseVO();
                        }
                    }).collect(Collectors.toList());
        }
    }

    private MessageVO buildGroupMessageVO(Long uid, Long receive, Message msg,StickerDuckEnums sticker) {
        MessageVO messageVO = new MessageVO();
        messageVO.setType("group");
        messageVO.setId(msg.getId());
        if(sticker != null){
            messageVO.setSticker(sticker.getValue());
        }

        messageVO.setReceive(receive);
        messageVO.setSender(uid);
        messageVO.setTime(new Date().getTime());
        messageVO.setReply(msg.getReply());
        if(msg.getReply() != null){
            Message replyMsg = messageDao.lambdaQuery()
                    .eq(Message::getId, msg.getReply())
                    .one();
            messageVO.setReplySenderId(replyMsg.getSender());
            messageVO.setReplyContent(replyMsg.getText());
        }
        messageVO.setContent(msg.getText());
        messageVO.setFile(msg.getFile());
        messageVO.setVideo(msg.getVideo());
        messageVO.setPhoto(msg.getPhoto());
        messageVO.setVoice(msg.getVoice());
        return messageVO;
    }

    public List<PinnedMessage> checkAndAddNewPinnedMessage(List<PinnedMessage> originalPinnedMessageIds, PinnedMessage newPinnedMessage) {
        //check size reached max and update pined message list
        if(originalPinnedMessageIds.size() == 50){
            originalPinnedMessageIds.remove(0);
            originalPinnedMessageIds.add(newPinnedMessage);
        }else{
            originalPinnedMessageIds.add(newPinnedMessage);
        }
        return originalPinnedMessageIds;
    }


}
