package com.ajbxyyx.utils.ParseFileTypeUtil.Handler;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.enums.FileTypeEnum;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.BaseFileVO;
import com.ajbxyyx.utils.AudioUtil;
import com.ajbxyyx.utils.ParseFileTypeUtil.ParseFileTypeUtil;
import org.springframework.web.multipart.MultipartFile;

public class MusicHandler extends ParseFileTypeUtil {

    @Override
    public BaseFileVO handle(MultipartFile file) throws Exception {
        if(file.getOriginalFilename().endsWith(".mp3")){

            BaseFileVO vo = new BaseFileVO();
            vo.setType(FileTypeEnum.AUDIO);
            AudioUtil.getAudioDuration(file);
            vo.setDuration( AudioUtil.getAudioDuration(file) );
            vo.setName( file.getOriginalFilename() );
            return vo;

        }else{
            throw new BusinessException(200,"mismatch");
        }
    }
}
