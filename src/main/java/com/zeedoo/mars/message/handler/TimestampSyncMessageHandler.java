package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageBuilder;
import com.zeedoo.mars.message.MessageResponseCode;
import com.zeedoo.mars.message.MessageType;

@Component
public class TimestampSyncMessageHandler extends AbstractMessageHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimestampSyncMessageHandler.class);

	@Override
	public MessageType getHandledType() {
		return MessageType.TIMESTAMP_SYNC;
	}

	@Override
	protected Optional<Message> doHandleMessage(Message message,
			ChannelHandlerContext ctx) throws Exception {
		// Since this is just a timestamp sync, we just need to write the correct server timestamp back
		Message response = MessageBuilder.buildResponseMessage(MessageType.REPONSE_TIMESTAMP_SYNC, MessageResponseCode.OK.getCode(), null);
		LOGGER.info("Built Message={}", response);
		return Optional.fromNullable(response);
	}

}
