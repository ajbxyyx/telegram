package com.ajbxyyx.config.Netty.Handler;


import com.ajbxyyx.config.Netty.Handler.Service.UnconnectedHandler;
import com.ajbxyyx.utils.Websocket.WebsocketUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@ChannelHandler.Sharable
@Slf4j
@Component
public class WebSocketSharkHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {



    @Autowired
    private UnconnectedHandler unconnectedHandler;


    /**
     * 連接成功
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("active");
    }
    /**
     * 連接斷開
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        unconnectedHandler.handler(ctx.channel());
    }



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }
}
