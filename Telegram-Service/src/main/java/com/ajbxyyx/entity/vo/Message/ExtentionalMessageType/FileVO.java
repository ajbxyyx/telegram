package com.ajbxyyx.entity.vo.Message.ExtentionalMessageType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileVO {
    private String fileName;

    private String fileSize;
    private String fileUrl;
}
