package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageGateway;

public abstract class AbstractMessageHandler implements MessageHandler {
		
	@Autowired
	private MessageGateway messageGateway;

	@Override
	public void handleMessage(Message message, ChannelHandlerContext ctx) throws Exception {
		Preconditions.checkArgument(message != null, "Message should not be null.");
		Preconditions.checkArgument(message.getSourceId() != null, "Message sourceId should not be null");
		// logger should reflect the real handler class that's handling the message
		final Logger currentHandlerLogger = LoggerFactory.getLogger(this.getClass());
		currentHandlerLogger.info("Handling Message={}", message);
		Optional<Message> replyMessage = doHandleMessage(message, ctx);
		if (replyMessage.isPresent()) {
			currentHandlerLogger.info("Reply message is present for messageId={}, replyMessageId={}", message.getId(), replyMessage.get().getId());
			reply(replyMessage.get(), ctx);
		}
		currentHandlerLogger.info("Successfully handled messageId={}", message.getId());
	}

	/**
	 * Do the real processing of the message, and return a reply message if necessary
	 * @param message
	 * @param ctx
	 * @return Optional reply message
	 */
	protected abstract Optional<Message> doHandleMessage(Message message, ChannelHandlerContext ctx) throws Exception;
	
	/**
	 * Replys a message by sending it to the Netty outbound handler chain
	 * @param replyMessage
	 * @param ctx
	 */
	protected void reply(Message replyMessage, ChannelHandlerContext ctx) {
		messageGateway.sendMessage(replyMessage, ctx);
	}

}
