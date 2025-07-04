package com.ajbxyyx.utils.ParseFileTypeUtil;

import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.BaseFileVO;
import org.springframework.web.multipart.MultipartFile;

public abstract class ParseFileTypeUtil {


    public abstract BaseFileVO handle(MultipartFile file) throws Exception;

}
