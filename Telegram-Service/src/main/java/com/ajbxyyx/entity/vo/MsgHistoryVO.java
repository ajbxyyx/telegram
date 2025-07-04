package com.ajbxyyx.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class MsgHistoryVO {

    Long id;//消息id

    String avatar;//發送者頭像url
    String name;//發送者名字
    Long uid;//發送者uid
    String color;//發送者昵稱顔色

    String message;//發送的文本消息
    List<String> photo;//發送的圖像url集
    List<String> video;//發送的影片集
    String voice;//發送的音頻
    String file;//發送的文件
    Date date;//發送的日期

}
