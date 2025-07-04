package com.ajbxyyx.entity.dto.message;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MessageDTO {


    private String text;

    private List<MultipartFile> photos;
    private List<MultipartFile> videos;
    private List<MultipartFile> files;
    private List<MultipartFile> voices;



    private Long replyId;
    private Long to;


}
