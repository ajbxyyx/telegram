package com.ajbxyyx.service.CursorQueryHandler;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.dao.MessageDao;
import com.ajbxyyx.entity.dto.message.FileMessageCursorQueryDTO;
import com.ajbxyyx.entity.po.Message;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.FileVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryFileMessageVO;
import com.ajbxyyx.utils.CursorQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileMessageCursorQueryHandler extends CursorQueryUtil<FileMessageCursorQueryDTO, Message, HistoryFileMessageVO> {

    @Resource
    private MessageDao messageDao;

    @Override
    public Page<Message> getPage(Page page, LambdaQueryWrapper<Message> lambdaQueryWrapper) {
        return messageDao.page(page,lambdaQueryWrapper);
    }

    @Override
    public BaseCursorQueryVO<HistoryFileMessageVO> getCursorQueryRespData(BaseCursorQueryVO<HistoryFileMessageVO> resp, List<Message> list) {

        List<HistoryFileMessageVO> result = new ArrayList<>();
        for (Message message : list) {
            Long id = message.getId();
            Long sender = message.getSender();
            Long time = message.getDate().getTime();

            List<FileVO> files = JSONUtil.toList(message.getFile(), FileVO.class);

            files.forEach(file->{
                String fileUrl = file.getFileUrl();
                String fileName = file.getFileName();
                String fileSize = file.getFileSize();
                HistoryFileMessageVO vo = new HistoryFileMessageVO(id,sender,time,fileUrl,fileName,fileSize,null);
                result.add(vo);
            });
        }
        resp.setResult(result);
        return resp;
    }

    @Override
    public Long getDataLastCursor(List<HistoryFileMessageVO> data) {
        return data.size() == 0?null:data.get(data.size() - 1).getMsgId();
    }
}
