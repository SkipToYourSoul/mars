package com.zeedoo.mars.message.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageType;

@Service
public class MessageService {
	
	@Autowired
	private Map<MessageType, MessageHandler> messageTypeToHandlerMap;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
	
	public void onMessage(Message message) {
		Preconditions.checkNotNull(message, "Message should not be null");
		LOGGER.info("Received Message: " + message);
		MessageHandler handler = getMessageHandler(message);
		handler.handleMessage(message);
	}
	
	/**
	 * Returns the message handler for this message
	 * @param message
	 * @return
	 */
	public MessageHandler getMessageHandler(Message message) {
		MessageType type = message.getMessageType();
		Preconditions.checkNotNull(type, "Message Type should not be null");
		MessageHandler handler = messageTypeToHandlerMap.get(type);
		if (handler == null) {
			throw new IllegalArgumentException("Could not find a MessageHandler for type: " + type);
		}
		return handler;
	}

}
