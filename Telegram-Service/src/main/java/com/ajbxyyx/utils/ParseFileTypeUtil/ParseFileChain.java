package com.ajbxyyx.utils.ParseFileTypeUtil;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.BaseFileVO;
import com.ajbxyyx.utils.ParseFileTypeUtil.Handler.FileHandler;
import com.ajbxyyx.utils.ParseFileTypeUtil.Handler.MusicHandler;
import com.ajbxyyx.utils.ParseFileTypeUtil.Handler.PhotoHandler;
import com.ajbxyyx.utils.ParseFileTypeUtil.Handler.VideoHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParseFileChain extends ParseFileTypeUtil{

    public List<ParseFileTypeUtil> handlers = new ArrayList<ParseFileTypeUtil>();

    @PostConstruct
    public void init(){
        handlers.add(new PhotoHandler());
        handlers.add(new MusicHandler());
        handlers.add(new VideoHandler());
        handlers.add(new FileHandler());

    }

    @Override
    public BaseFileVO handle(MultipartFile file) throws BusinessException {
        for (ParseFileTypeUtil handler : handlers) {
            try {
                return handler.handle(file);
            }catch (Exception e){

            }
        }
        throw new BusinessException(500,"no handler suit");
    }
}
