package com.zeedoo.mars.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageDeserializer;
import com.zeedoo.mars.message.MessageType;

/**
 * Decodes String into {@link com.zeedoo.mars.message.Message} object
 * @author nzhu
 *
 */
public class MessageDecoder extends MessageToMessageDecoder<String> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		// De-serialize JSON to Message object
		// If an error occurs here, log the payload and re-throw Exception
		try {
		    Message message = MessageDeserializer.fromJSON(msg);
		    // Log the raw payload here if the message type is not file transfer
		    if (MessageType.RESPONSE_TIMED_SENSOR_DATA_FILE_TRANSFER != message.getMessageType()) {
		    	LOGGER.info("Received raw JSON={}", msg);
		    } else {
		    	LOGGER.info("MessageType={} is related to file transfer, skipping logging payload", message.getMessageType());
		    }
			validateMessage(message);
			out.add(message);
		} catch (Exception e) {
			LOGGER.error("Error trying to deserialize String={} into Message", msg, e);
			throw e;
		}
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
