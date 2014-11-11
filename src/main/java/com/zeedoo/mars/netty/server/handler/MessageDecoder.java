package com.zeedoo.mars.netty.server.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.zeedoo.mars.error.MarsErrorHandler;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageDeserializer;
import com.zeedoo.mars.service.CacheServiceBean;

/**
 * Decodes String into {@link com.zeedoo.mars.message.Message} object
 * @author nzhu
 *
 */
@Sharable
@Component
public class MessageDecoder extends MessageToMessageDecoder<String> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);
	
	@Autowired
	private CacheServiceBean cacheService;
	
	@Autowired
	private MarsErrorHandler marsErrorHandler;

	@Override
	protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		// If an error occurs here, the payload will be logged by the deserializer and the exception will be re-thrown
		// so that the error handler can handle the error
	    Message message = MessageDeserializer.fromJSON(msg);
	    // Log the raw payload here
	    LOGGER.info("Received raw JSON={}", msg);
		validateMessage(message);
		Cache<String, Boolean> messageIdCache = cacheService.getMessageIdCache();
		if (messageIdCache.asMap().containsKey(message.getId())) {
			LOGGER.info("Received DUPLICATE Message with messageId={}, DISCARDing Message", message.getId());
			return;
		} else {
			out.add(message);
			messageIdCache.put(message.getId(), true);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	    marsErrorHandler.handleError(ctx, cause);
	}
	
	private void validateMessage(Message message) {
		// Validate message
		Preconditions.checkNotNull("Source should not be null", message.getSource());
		Preconditions.checkNotNull("SourceId should not be null", message.getSourceId());
		Preconditions.checkNotNull("Timestamp should not be null", message.getTimestamp());
		Preconditions.checkNotNull("MessageType should not be null", message.getMessageType());
	}
}
