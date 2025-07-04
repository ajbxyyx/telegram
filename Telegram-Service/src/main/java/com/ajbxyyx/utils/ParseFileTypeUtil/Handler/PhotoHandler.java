package com.ajbxyyx.utils.ParseFileTypeUtil.Handler;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.enums.FileTypeEnum;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.BaseFileVO;
import com.ajbxyyx.utils.ParseFileTypeUtil.ParseFileTypeUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PhotoHandler extends ParseFileTypeUtil {


    @Override
    public BaseFileVO handle(MultipartFile file) throws Exception {
        if(file.getOriginalFilename().endsWith(".png") ||
                file.getOriginalFilename().endsWith(".jpg") ||
                file.getOriginalFilename().endsWith(".jpeg")){

            BaseFileVO vo = new BaseFileVO();
            vo.setType(FileTypeEnum.IMAGE);
            vo.setName( file.getOriginalFilename() );
            int[] imageDimensions = getImageDimensions(file);
            vo.setWidth(imageDimensions[0]);
            vo.setHeight(imageDimensions[1]);
            //todo thumbnalid
            return vo;

        }else{
            throw new BusinessException(200,"mismatch");
        }
    }






    public static int[] getImageDimensions(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("Invalid image file");
            }
            return new int[]{image.getWidth(), image.getHeight()};
        }
    }
}
