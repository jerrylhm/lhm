package com.example.demo.netty;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

public class MyGenericFutureListener implements GenericFutureListener<ChannelFuture> {

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		System.out.println("异步操作完成");
		
	}

}
