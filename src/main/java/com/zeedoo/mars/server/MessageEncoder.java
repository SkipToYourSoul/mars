package com.zeedoo.mars.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * Encodes a {@link com.zeedoo.mars.message.Message} to ByteBuf
 * @author nzhu
 *
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out)
			throws Exception {
		String json = MessageSerializer.toJSON(msg);
		LOGGER.info("Converted Message to JSON String={}", json);
		ByteBuf encoded = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        try {
            out.writeBytes(encoded);
        } finally {
            encoded.release();
        }
	}

}
