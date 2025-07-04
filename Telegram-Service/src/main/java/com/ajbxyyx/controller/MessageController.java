package com.ajbxyyx.controller;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.common.entities.ApiResult;
import com.ajbxyyx.entity.dto.MsgHistoryCursorQueryDTO;
import com.ajbxyyx.entity.dto.message.*;
import com.ajbxyyx.entity.vo.*;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryFileMessageVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryLinkMessageVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryMediaMessageVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryMusicMessageVO;
import com.ajbxyyx.service.MessageService;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * 發送消息
     * @param sticker
     * @param text
     * @param files
     * @param replayId
     * @param receive
     * @param uuid
     * @return
     * @throws BusinessException
     */
    @PostMapping("/send")
    public ApiResult<SendMessageVO> uploadFiles(
            @RequestParam(required = false) String sticker,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<MultipartFile> files,
            @RequestParam(required = false) Long replayId,
            @RequestParam Long receive,
            @RequestParam String uuid) throws BusinessException {
        if(files == null && text == null && sticker == null){
            throw new BusinessException(500,"Send content empty!");
        }
        //單獨處理表情消息
        SendMessageVO result = messageService.dealMessage(text, files, ThreadLocalUtil.getUid(), replayId, receive, uuid,sticker);
        return ApiResult.success(result);
    }

    /**
     * parse url info
     * @param req
     * @return
     * @throws BusinessException
     */
    @PostMapping("/url/parse")
    public ApiResult<List<UrlParseVO>> parseUrl(@RequestBody ParseUrlDTO req) throws BusinessException {
        List<UrlParseVO> result = messageService.parseUrl(req);
        return ApiResult.success(result);
    }
    /**
     * 刪除消息
     * @param req
     * @return
     * @throws BusinessException
     */
    @PostMapping("/delete")
    public ApiResult<Void> deleteMessage(@RequestBody DeleteMessageDTO req) throws BusinessException {
        messageService.deleteMessage(req);
        return ApiResult.success();
    }
    @PostMapping("/typing")
    public ApiResult<Void> deleteMessage(@RequestBody TypingMessageDTO req) throws BusinessException {
        messageService.typingMessage(req);
        return ApiResult.success();
    }


    /**
     * 游標查詢歷史消息
     * @throws BusinessException
     */
    @PostMapping("/history")
    public ApiResult<List<MessageHistoryVO>> getHistoryMessage(@RequestBody MsgHistoryCursorQueryDTO req) throws BusinessException {
        List<MessageHistoryVO> result;
        if(req.getChatId() < 0){//处理群聊
            result = messageService.getGroupHistoryMessage(req);
        }else{//处理单聊
            result = messageService.getChatHistoryMessage(req);
        }
        return ApiResult.success(result);
    }

    /**
     * 获取回复消息详细
     * @param req
     * @return
     */
    @PostMapping("/replymessage")
    public ApiResult<ReplyMessageVO> getReplyMessageInfo(@RequestBody ReplyMessageDTO req){
        ReplyMessageVO result = messageService.getReplyMessageInfo(req);
        return ApiResult.success(result);
    }

    /**
     * react message
     * @param req
     * @return
     * @throws BusinessException
     */
    @PostMapping("/react")
    public ApiResult<Void> reactMessage(@RequestBody ReactMessageDTO req) throws BusinessException{
        messageService.reactMessage(req);
        return ApiResult.success();
    }



    /**
     * query history media message
     * @param req
     * @return
     */
    @PostMapping("/history/media")
    public ApiResult<BaseCursorQueryVO<HistoryMediaMessageVO>> getMediaMessage(@RequestBody MediaMessageCursorQueryDTO req){
        BaseCursorQueryVO<HistoryMediaMessageVO> result = messageService.getMediaMessage(req);
        return ApiResult.success(result);
    }
    /**
     * query history file message
     * @param req
     * @return
     */
    @PostMapping("/history/file")
    public ApiResult<BaseCursorQueryVO<HistoryFileMessageVO>> getFileMessage(@RequestBody FileMessageCursorQueryDTO req){
        BaseCursorQueryVO<HistoryFileMessageVO> result = messageService.getFileMessage(req);
        return ApiResult.success(result);
    }

    /**
     * query history link message
     * @param req
     * @return
     */
    @PostMapping("/history/link")
    public ApiResult<BaseCursorQueryVO<HistoryLinkMessageVO>> getLinkMessage(@RequestBody LinkMessageCursorQueryDTO req){
        BaseCursorQueryVO<HistoryLinkMessageVO> result = messageService.getLinkMessage(req);
        return ApiResult.success(result);
    }
    /**
     * query history link message
     * @param req
     * @return
     */
    @PostMapping("/history/music")
    public ApiResult<BaseCursorQueryVO<HistoryMusicMessageVO>> getMusicMessage(@RequestBody MusicMessageCursorQueryDTO req){
        BaseCursorQueryVO<HistoryMusicMessageVO> result = messageService.getMusicMessage(req);
        return ApiResult.success(result);
    }


    @PostMapping("/pin")
    public ApiResult<Void> getMusicMessage(@RequestBody PinMessageDTO req) throws BusinessException {
        messageService.pinMessage(req);
        return ApiResult.success();
    }







}
