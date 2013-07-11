package com.zeedoo.mars.message.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.zeedoo.mars.message.MessageType;

@Component
public class MessageHandlerConfiguration {
	
	@Autowired
	private List<MessageHandler> handlers;
	
	@Bean
	public Map<MessageType, MessageHandler> messageTypeToHandlerMap() {
		final Map<MessageType, MessageHandler> m = Maps.newHashMap();
		// Initialize map
		for (MessageHandler handler : handlers) {
			m.put(handler.getHandledType(), handler);
		}
		return m;
	}
}
