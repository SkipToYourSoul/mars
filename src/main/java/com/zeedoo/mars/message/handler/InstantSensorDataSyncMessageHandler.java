package com.zeedoo.mars.message.handler;

import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageType;

@Component
public class InstantSensorDataSyncMessageHandler implements MessageHandler {

	@Override
	public Optional<Message> handleMessage(Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageType getHandledType() {
		return MessageType.INSTANT_SENSOR_DATA_SYNC;
	}

}
