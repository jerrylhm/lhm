package com.example.demo.netty;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelHandlerAdapter{

	//发生异常时补抓
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
//			ByteBuf data = (ByteBuf) msg;
//			if(data.isReadable()) {
//				System.out.println(data.toString(Charset.forName("utf-8")));
//			}
//			System.out.println(data.toString());
			ctx.write("杰哥回答说:吃屎吧你");
			ctx.flush();
		} catch (Exception e) {
			ReferenceCountUtil.release(msg);
		}
	}

	
}
