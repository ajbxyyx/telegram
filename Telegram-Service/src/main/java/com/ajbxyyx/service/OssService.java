package com.ajbxyyx.service;

import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.BaseFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OssService {
    CompletableFuture<List<BaseFileVO>> uploadFile(List<MultipartFile> files);

}
