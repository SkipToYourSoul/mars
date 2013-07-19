package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import com.google.common.base.Optional;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageType;

public class ResponseTimedSensorDataSyncMessageHandler extends AbstractMessageHandler {

	@Override
	public MessageType getHandledType() {
		return MessageType.RESPONSE_TIMED_SENSOR_DATA_SYNC;
	}

	@Override
	protected Optional<Message> doHandleMessage(Message message,
			ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}
}
