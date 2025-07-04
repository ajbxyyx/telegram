package com.ajbxyyx.service.CursorQueryHandler;

import cn.hutool.json.JSONUtil;
import com.ajbxyyx.dao.MessageDao;
import com.ajbxyyx.entity.dto.message.MediaMessageCursorQueryDTO;
import com.ajbxyyx.entity.po.Message;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.ajbxyyx.entity.vo.Message.History.HistoryMediaMessageVO;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.PhotoVO;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.VideoVO;
import com.ajbxyyx.utils.CursorQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MediaMessageCursorQueryHandler extends CursorQueryUtil<MediaMessageCursorQueryDTO, Message, HistoryMediaMessageVO> {

    @Resource
    private MessageDao messageDao;

    @Override
    public Page<Message> getPage(Page page, LambdaQueryWrapper<Message> lambdaQueryWrapper) {
        return messageDao.page(page,lambdaQueryWrapper);
    }

    @Override
    public BaseCursorQueryVO<HistoryMediaMessageVO> getCursorQueryRespData(BaseCursorQueryVO<HistoryMediaMessageVO> resp, List<Message> list) {

        List<HistoryMediaMessageVO> result = new ArrayList<>();
        for (Message message : list) {
            Long id = message.getId();
            Long sender = message.getSender();
            Long time = message.getDate().getTime();

            List<VideoVO> videos = JSONUtil.toList(message.getVideo(), VideoVO.class);
            List<PhotoVO> photos = JSONUtil.toList(message.getPhoto(), PhotoVO.class);

            videos.forEach(video->{
                String videoUrl = video.getVideoUrl();
                HistoryMediaMessageVO vo = new HistoryMediaMessageVO(id,sender,time,videoUrl,null);
                result.add(vo);
            });
            photos.forEach(photo->{
                String photoUrl = photo.getPhotoUrl();
                HistoryMediaMessageVO vo = new HistoryMediaMessageVO(id,sender,time,photoUrl,null);
                result.add(vo);
            });
        }
        resp.setResult(result);
        return resp;
    }

    @Override
    public Long getDataLastCursor(List<HistoryMediaMessageVO> data) {
        return data.size() == 0?null:data.get(data.size() - 1).getMsgId();
    }
}
