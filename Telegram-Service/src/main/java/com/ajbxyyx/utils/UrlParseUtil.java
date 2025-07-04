package com.ajbxyyx.utils;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.entity.vo.Message.ExtentionalMessageType.LinkVO;
import com.ajbxyyx.entity.vo.UrlParseVO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.hutool.core.lang.Console.log;
@Component
public class UrlParseUtil {


    @Async
    public CompletableFuture<UrlParseVO> parseURL(String url){
        try {
            if(!url.startsWith("http://") && !url.startsWith("https://") ){
                url = "http://"+url;
            }
            Connection connect = Jsoup.connect(url);
            connect.timeout(1000);
            Document document = connect.get();

            String title = document.title();
            String description = document.select("head meta[name*=description]").attr("content");
            if(title != "" && description != ""){
                return CompletableFuture.completedFuture(new UrlParseVO(title,description));
            }else{
                throw new BusinessException(500,"title or description is null");
            }
        }catch (Exception e){
            return CompletableFuture.completedFuture(new UrlParseVO());
        }
    }
    public List<LinkVO> parseLinkVO(String textMsg){
        List<LinkVO> result = new ArrayList<>();
        String regex = "((http|https):\\/\\/)?(www\\.)?([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(textMsg);

        while (matcher.find()) {
            result.add(new LinkVO(matcher.group()));
        }
        return result;
    }






}
