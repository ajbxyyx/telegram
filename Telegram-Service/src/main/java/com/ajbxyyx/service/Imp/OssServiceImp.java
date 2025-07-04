package com.ajbxyyx.service.Imp;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.BaseFileVO;
import com.ajbxyyx.service.OssService;
import com.ajbxyyx.utils.AliOssUtil;
import com.ajbxyyx.utils.ParseFileTypeUtil.ParseFileChain;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OssServiceImp implements OssService {

    @Resource
    private AliOssUtil aliOssUtil;
    @Resource
    private ParseFileChain parseFileChain;


    public static String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }

        int unit = 1024;
        String[] units = {"KB", "MB", "GB", "TB", "PB", "EB"};
        double size = bytes;
        int index = -1;

        do {
            size /= unit;
            index++;
        } while (size >= unit && index < units.length - 1);

        return String.format("%.2f %s", size, units[index]);
    }
    @Override
    @Async
    public CompletableFuture<List<BaseFileVO>> uploadFile(List<MultipartFile> files) {
        if(files != null && files.size() != 0){
            List<BaseFileVO> file = new ArrayList<>();
            for (MultipartFile perFile : files) {
                try {

                    BaseFileVO vo = parseFileChain.handle(perFile);
                    //upload OSS
                    String originalFilename = perFile.getOriginalFilename();
                    originalFilename = UUID.randomUUID() + ":"+ originalFilename;
                    String url = aliOssUtil.upload(perFile.getBytes(), originalFilename);
                    //set url
                    vo.setUrl(url);
                    file.add(vo);
                }catch (Exception e){
                    return CompletableFuture.failedFuture(new BusinessException(500,"IOException"));
                }
            }
            return CompletableFuture.completedFuture(file);
        }else{
            return CompletableFuture.failedFuture(new BusinessException(500,"files empty!"));
        }
    }








}
