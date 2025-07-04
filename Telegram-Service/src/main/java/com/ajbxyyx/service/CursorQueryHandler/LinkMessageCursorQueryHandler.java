package com.ajbxyyx.service.CursorQueryHandler;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.dao.MessageDao;
import com.ajbxyyx.entity.dto.message.LinkMessageCursorQueryDTO;
import com.ajbxyyx.entity.po.Message;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryLinkMessageVO;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.LinkVO;
import com.ajbxyyx.utils.CursorQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LinkMessageCursorQueryHandler extends CursorQueryUtil<LinkMessageCursorQueryDTO, Message, HistoryLinkMessageVO> {

    @Resource
    private MessageDao messageDao;

    @Override
    public Page<Message> getPage(Page page, LambdaQueryWrapper<Message> lambdaQueryWrapper) {
        return messageDao.page(page,lambdaQueryWrapper);
    }

    @Override
    public BaseCursorQueryVO<HistoryLinkMessageVO> getCursorQueryRespData(BaseCursorQueryVO<HistoryLinkMessageVO> resp, List<Message> messages) {
        List<HistoryLinkMessageVO> result = new ArrayList<>();
        for (Message message : messages) {

            Long id = message.getId();
            Long sender = message.getSender();
            Long time = message.getDate().getTime();

            List<LinkVO> links = JSONUtil.toList(message.getLink(), LinkVO.class);
            for (LinkVO link : links) {
                HistoryLinkMessageVO vo = new HistoryLinkMessageVO();
                vo.setMsgId(id);
                vo.setSender(sender);
                vo.setTime(time);
                vo.setLink(link.getLink());
                vo.setText(message.getText());
                result.add(vo);
            }
        }
        resp.setResult(result);
        return resp;
    }


    @Override
    public Long getDataLastCursor(List<HistoryLinkMessageVO> data) {
        return data.size() == 0?null:data.get(data.size() - 1).getMsgId();
    }
}
