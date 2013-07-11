package com.zeedoo.mars.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

@Sharable
public class JsonDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			MessageList<Object> out) throws Exception {
		out.add(in.readBytes(in.readableBytes()).toString(CharsetUtil.UTF_8));	
	}
}
