package com.ajbxyyx.utils.ParseFileTypeUtil.Handler;

import com.ajbxyyx.entity.enums.FileTypeEnum;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.BaseFileVO;
import com.ajbxyyx.utils.ParseFileTypeUtil.ParseFileTypeUtil;
import org.springframework.web.multipart.MultipartFile;

public class FileHandler extends ParseFileTypeUtil {

    @Override
    public BaseFileVO handle(MultipartFile file) throws Exception {
        BaseFileVO vo = new BaseFileVO();
        vo.setType(FileTypeEnum.FILE);
        vo.setSize(String.valueOf(file.getSize()));
        vo.setName( file.getOriginalFilename() );
        return vo;
    }
}
