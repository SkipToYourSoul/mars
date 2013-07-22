package com.zeedoo.mars.server;

import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class MessageEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out)
			throws Exception {
		String json = MessageSerializer.toJSON(msg);
		ByteBuf encoded = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        try {
            out.writeBytes(encoded);
        } finally {
            encoded.release();
        }
	}

}
