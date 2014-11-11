package com.zeedoo.mars.netty.server.handler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zeedoo.mars.error.MarsErrorHandler;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageSerializer;

/**
 * Encodes a {@link com.zeedoo.mars.message.Message} to ByteBuf
 * @author nzhu
 *
 */
@Sharable
@Component
public class MessageEncoder extends MessageToByteEncoder<Message> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);
	
	@Autowired
	private MarsErrorHandler marsErrorHandler;
	
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
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	    marsErrorHandler.handleError(ctx, cause);
	}
}
