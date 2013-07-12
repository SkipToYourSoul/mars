package com.zeedoo.mars.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;

@Component
public class MessageGatewayBean implements MessageGateway {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageGatewayBean.class);

	@Override
	public void sendMessage(Message message, ChannelHandlerContext ctx) {
		LOGGER.info("Sending Outbound Message={}", message);
		ctx.write(message);
	}

}
