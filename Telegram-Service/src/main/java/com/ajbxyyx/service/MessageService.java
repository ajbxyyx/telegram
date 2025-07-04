package com.ajbxyyx.service;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.dto.MsgHistoryCursorQueryDTO;
import com.ajbxyyx.entity.dto.message.*;
import com.ajbxyyx.entity.po.MessageReactionOld;
import com.ajbxyyx.entity.vo.*;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryFileMessageVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryLinkMessageVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryMediaMessageVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryMusicMessageVO;
import com.ajbxyyx.entity.vo.Message.PinnedMessage;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface MessageService {
    SendMessageVO dealMessage(String text,
                              List<MultipartFile> file,
                              Long replayId,
                              Long receive,
                              Long uid,
                              String uuid,
                              String sticker) throws BusinessException;

    Long getGroupUnreadCount(Long chatId, Long uid, Date lastReadTime);

    void deleteMessage(DeleteMessageDTO req) throws BusinessException;

    List<UrlParseVO> parseUrl(ParseUrlDTO req) throws BusinessException;


    List<MessageHistoryVO> getChatHistoryMessage(MsgHistoryCursorQueryDTO cursorQueryDTO);

    List<MessageHistoryVO> getGroupHistoryMessage(MsgHistoryCursorQueryDTO cursorQueryDTO);



    Long getChatUnreadCount(Long chatId, Long uid, Date lastReadTime);

    void typingMessage(TypingMessageDTO req) throws BusinessException;


    ReplyMessageVO getReplyMessageInfo(ReplyMessageDTO req);

    void reactMessage(ReactMessageDTO req) throws BusinessException;


    void addMessageReaction(Long k, List<MessageReactionOld> v);

    BaseCursorQueryVO<HistoryMediaMessageVO> getMediaMessage(MediaMessageCursorQueryDTO req);

    BaseCursorQueryVO<HistoryFileMessageVO> getFileMessage(FileMessageCursorQueryDTO req);

    BaseCursorQueryVO<HistoryLinkMessageVO> getLinkMessage(LinkMessageCursorQueryDTO req);

    BaseCursorQueryVO<HistoryMusicMessageVO> getMusicMessage(MusicMessageCursorQueryDTO req);

    void pinMessage(PinMessageDTO req) throws BusinessException;

    List<PinnedMessage> checkAndAddNewPinnedMessage(List<PinnedMessage> originalPinnedMessageIds, PinnedMessage newPinnedMessage);
}
