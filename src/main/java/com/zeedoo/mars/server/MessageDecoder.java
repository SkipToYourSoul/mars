package com.zeedoo.mars.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageDeserializer;

/**
 * Decodes bytestream into {@link com.zeedoo.mars.message.Message} object
 * @author nzhu
 *
 */
public class MessageDecoder extends ByteToMessageDecoder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// Convert to String first
		String rawJsonString = in.readBytes(in.readableBytes()).toString(CharsetUtil.UTF_8);	
		// De-serialize JSON to Message object
		Message message = MessageDeserializer.fromJSON(rawJsonString);
		validateMessage(message);
		out.add(message);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//TODO: Add exception handling
		// We probably do not want to close connection here in case a bad message
		// We DO want to validate the message so that next handler in the pipeline does not need to worry about it
	    LOGGER.error("Error deserializing bytes to Message", cause);
	}
	
	private void validateMessage(Message message) {
		// Validate message
		Preconditions.checkNotNull("Source should not be null", message.getSource());
		Preconditions.checkNotNull("SourceId should not be null", message.getSourceId());
		Preconditions.checkNotNull("Timestamp should not be null", message.getTimestamp());
		Preconditions.checkNotNull("MessageType should not be null", message.getMessageType());
	}
}
