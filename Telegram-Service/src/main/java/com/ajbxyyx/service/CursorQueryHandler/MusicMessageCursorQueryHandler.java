package com.ajbxyyx.service.CursorQueryHandler;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.dao.MessageDao;
import com.ajbxyyx.entity.dto.message.MusicMessageCursorQueryDTO;
import com.ajbxyyx.entity.po.Message;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryMusicMessageVO;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.MusicVO;
import com.ajbxyyx.utils.CursorQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MusicMessageCursorQueryHandler extends CursorQueryUtil<MusicMessageCursorQueryDTO, Message, HistoryMusicMessageVO> {

    @Resource
    private MessageDao messageDao;

    @Override
    public Page<Message> getPage(Page page, LambdaQueryWrapper<Message> lambdaQueryWrapper) {
        return messageDao.page(page,lambdaQueryWrapper);
    }

    @Override
    public BaseCursorQueryVO<HistoryMusicMessageVO> getCursorQueryRespData(BaseCursorQueryVO<HistoryMusicMessageVO> resp, List<Message> messages) {
        List<HistoryMusicMessageVO> result = new ArrayList<>();
        for (Message message : messages) {

            Long id = message.getId();
            Long sender = message.getSender();
            Long time = message.getDate().getTime();

            List<MusicVO> musics = JSONUtil.toList(message.getMusic(), MusicVO.class);
            for (MusicVO music : musics) {
                HistoryMusicMessageVO vo = new HistoryMusicMessageVO();
                vo.setMsgId(id);
                vo.setSender(sender);
                vo.setTime(time);
                vo.setMusicName(music.getMusicName());
                vo.setMusicUrl(music.getMusicUrl());
                vo.setMusicDuration(music.getMusicDuration());
                result.add(vo);
            }
        }
        resp.setResult(result);
        return resp;
    }


    @Override
    public Long getDataLastCursor(List<HistoryMusicMessageVO> data) {
        return data.size() == 0?null:data.get(data.size() - 1).getMsgId();
    }
}
