package com.zeedoo.mars.message.handler;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;
import com.zeedoo.mars.message.MessageType;

@Configuration
public class MessageHandlerConfiguration {

	@Autowired
	private List<MessageHandler> handlers;

	private static final Map<MessageType, MessageHandler> MESSAGE_TYPE_TO_HANDLER_MAP = Maps.newHashMap();

	@PostConstruct
	private void init() {
		// Initialize map
		for (MessageHandler handler : handlers) {
			MESSAGE_TYPE_TO_HANDLER_MAP.put(handler.getHandledType(), handler);
		}
	}
	
	public MessageHandler getMessageHandler(MessageType type) {
		MessageHandler handler = MESSAGE_TYPE_TO_HANDLER_MAP.get(type);
		if (handler == null) {
			throw new IllegalArgumentException("Unsupported Message Type : " + type);
		}
		return handler;
	}
}
