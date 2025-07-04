package com.ajbxyyx.entity.vo.Websocket;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketBaseVO {

    private Integer type;
    private Long chatId;//optional
    private String data;

    public static WebsocketBaseVO push(Integer type , String json){
        WebsocketBaseVO resp = new WebsocketBaseVO();
        resp.setType(type);
        resp.setData(json);
        return  resp;
    }
}
