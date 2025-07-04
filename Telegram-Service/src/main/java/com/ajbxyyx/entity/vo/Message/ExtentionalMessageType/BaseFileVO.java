package com.ajbxyyx.entity.vo.Message.ExtentionalMessageType;

import com.ajbxyyx.entity.enums.FileTypeEnum;
import lombok.Data;

@Data
public class BaseFileVO {
    private FileTypeEnum type;
    private String name;
    private String size;
    private String url;
    private String duration;
    private String thumb;
    private Integer height;
    private Integer width;

}
