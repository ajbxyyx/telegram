package com.ajbxyyx.service.CursorQueryHandler;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.dao.MessageDao;
import com.ajbxyyx.dao.MessageReactionDao;
import com.ajbxyyx.entity.dto.MsgHistoryCursorQueryDTO;
import com.ajbxyyx.entity.dto.StickerDTO;
import com.ajbxyyx.entity.enums.ReactionEnum;
import com.ajbxyyx.entity.enums.StickerDuckEnums;
import com.ajbxyyx.entity.po.Message;
import com.ajbxyyx.entity.po.MessageReaction;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.*;
import com.ajbxyyx.entity.vo.MessageHistoryVO;
import com.ajbxyyx.entity.vo.MessageReactionVO;
import com.ajbxyyx.utils.CursorQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MsgHistoryCursorQueryHandler
        extends CursorQueryUtil<MsgHistoryCursorQueryDTO, Message, MessageHistoryVO> {

    @Resource
    private MessageDao messageDao;
    @Resource
    private MessageReactionDao messageReactionDao;

    @Override
    public Page getPage(Page page, LambdaQueryWrapper lambdaQueryWrapper) {
        return messageDao.page(page,lambdaQueryWrapper);
    }

    @Override
    public BaseCursorQueryVO<MessageHistoryVO> getCursorQueryRespData(BaseCursorQueryVO<MessageHistoryVO> resp, List<Message> list) {
        Map<Long, List<MessageReaction>> reactionMap = null;
        if(list.size() != 0){
            //query reaction
            List<Long> msgIds = list.stream().map(o -> o.getId()).collect(Collectors.toList());
            reactionMap = messageReactionDao.lambdaQuery()
                    .in(MessageReaction::getMessageId, msgIds)
                    .list().stream().collect(Collectors.groupingBy(MessageReaction::getMessageId));
        }


        List<MessageHistoryVO> result = new ArrayList<>();
        for (Message message : list) {
            MessageHistoryVO vo = new MessageHistoryVO();
            vo.setId(message.getId());
            vo.setSender(message.getSender());
            vo.setRead(message.getRead());
            vo.setContent(message.getText());
            vo.setTime(message.getDate().getTime());

            vo.setReply(message.getReply());

            vo.setFile(JSONUtil.toList(message.getFile(), FileVO.class));
            vo.setPhoto(JSONUtil.toList(message.getPhoto(), PhotoVO.class));
            vo.setVideo(JSONUtil.toList(message.getVideo(), VideoVO.class));
            vo.setVoice(JSONUtil.toList(message.getVoice(), VoiceVO.class));
            vo.setMusic(JSONUtil.toList(message.getMusic(), MusicVO.class));
            //表情處理
            if(message.getSticker() != null){
                StickerDTO sticker = JSONUtil.toBean(message.getSticker(), StickerDTO.class);
                if(sticker.getType() == 1){
                    String value = StickerDuckEnums.of(sticker.getTypeDetail()).getValue();
                    vo.setSticker(value);
                }
            }



            Map<Integer,MessageReactionVO> reactionVOS = new HashMap<>();
            //add reactionVOs
            if(reactionMap.get(message.getId()) != null){
                reactionMap.get(message.getId()).forEach(reaction->{
                    if(reactionVOS.containsKey(reaction.getReactType())){
                        reactionVOS.get(reaction.getReactType()).getUser().add(new MessageReactionVO.User(reaction.getUid(),reaction.getCreateTime().getTime()));
                    }else{
                        MessageReactionVO reactionVO = new MessageReactionVO();
                        reactionVO.setReactType(reaction.getReactType());
                        reactionVO.setSrc(ReactionEnum.of(reaction.getReactType()).getSrc());
                        reactionVO.setUser(List.of(new MessageReactionVO.User(reaction.getUid(),reaction.getCreateTime().getTime())));

                        reactionVOS.put(reaction.getReactType(),reactionVO);
                    }
                });
            }
            vo.setReaction(reactionVOS);

            result.add(vo);
        }
        resp.setResult(result);
        return resp;
    }

    @Override
    public Long getDataLastCursor(List<MessageHistoryVO> data) {
        return data.size() == 0?null:data.get(data.size() - 1).getId();
    }
}
