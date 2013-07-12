package com.zeedoo.mars.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.netty.channel.ChannelHandlerContext;

import com.google.common.base.Optional;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageGateway;
import com.zeedoo.mars.message.MessageType;

public abstract class AbstractMessageHandler implements MessageHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageHandler.class);
	
	@Autowired
	private MessageGateway messageGateway;

	@Override
	public void handleMessage(Message message, ChannelHandlerContext ctx) {
		Optional<Message> replyMessage = doHandleMessage(message, ctx);
		if (replyMessage.isPresent()) {
			LOGGER.info("Reply message is present for Message source id: " + message.getSourceId());
			reply(replyMessage.get(), ctx);
		}
	}

	@Override
	public MessageType getHandledType() {
		throw new UnsupportedOperationException("Not supported");
	}
	
	/**
	 * Do the real processing of the message, and return a reply message if necessary
	 * @param message
	 * @param ctx
	 * @return Optional reply message
	 */
	protected abstract Optional<Message> doHandleMessage(Message message, ChannelHandlerContext ctx);
	
	/**
	 * Replys a message by sending it to the Netty outbound handler chain
	 * @param replyMessage
	 * @param ctx
	 */
	protected void reply(Message replyMessage, ChannelHandlerContext ctx) {
		messageGateway.sendMessage(replyMessage, ctx);
	}

}
