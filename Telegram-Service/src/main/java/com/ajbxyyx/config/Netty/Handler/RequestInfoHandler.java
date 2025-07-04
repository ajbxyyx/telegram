package com.ajbxyyx.config.Netty.Handler;


import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.config.Netty.Handler.Service.ConnectedHandler;
import com.ajbxyyx.utils.JwtUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * 解析websocket连接的token处理器
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class RequestInfoHandler extends ChannelInboundHandlerAdapter {



    @Autowired
    private ConnectedHandler connectedHandler;


    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {



        if (msg != null && msg instanceof HttpRequest) {

            //转化为http请求
            HttpRequest request = (HttpRequest) msg;
            //拿到请求地址
            String uri = request.uri();
            //获取type参数
            try {

                String token = dealUri(uri);
                if(token == null){
                    throw new BusinessException(200,"unlegal websocket request!");
                }
                //解析token
                Long uid = jwtUtil.getUidOrNull(token);
                if(uid == null){
                    throw new BusinessException(200,"auth fail");
                }
                connectedHandler.handler(ctx.channel(),uid);
            }catch (Exception e){
                ctx.close();
                throw e;
            }
            //接着建立请求
            request.setUri("/");
            super.channelRead(ctx, msg);
        }else {
            ctx.close();
        }
    }








    private String dealUri(String uri) throws BusinessException {

        if(uri==null){//uri为空 不处理
            throw new BusinessException(500,"uri错误");
        }
        String[] uriSplit = uri.split("=");
        if(uriSplit.length <= 1 || uriSplit.length >= 3){//分段结果长度不对 不处理
            throw new BusinessException(500,"uri分段长度错误");
        }
        return uriSplit[1];
    }
}
