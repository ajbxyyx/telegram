package com.ajbxyyx.utils;


import cn.hutool.json.JSONUtil;
import com.ajbxyyx.entity.vo.IpParseVO;
import org.apache.ibatis.annotations.Property;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IpParseUtil {

    @Value("${telegram.ipparse.apikey}")
    private String apikey;


    public IpParseVO parseIp(String ip){
        String url = "https://api.ipdata.co/"+ip+"?api-key="+apikey;
        String json = HttpClientUtil.doGet(url, null);
        IpParseVO result = JSONUtil.toBean(json, IpParseVO.class);
        return result;
    }


}
