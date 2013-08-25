package com.zeedoo.mars.server;

import java.io.FileOutputStream;

import com.sun.jersey.core.util.Base64;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestHandler extends SimpleChannelInboundHandler<String> {
	
	    @Override
	    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
	    	byte[] decodedBytes = Base64.decode(msg);
			FileOutputStream output = new FileOutputStream("wtf.jpg");
			output.write(decodedBytes, 0, decodedBytes.length);
			output.close();
	    }
}
