package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.mars.dao.SensorDataRecordsDao;
import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageDeserializer;
import com.zeedoo.mars.message.MessageType;

@Component
public class InstantSensorDataSyncMessageHandler extends AbstractMessageHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InstantSensorDataSyncMessageHandler.class);
	
	@Autowired
	private SensorDataRecordsDao sensorDataRecordsDao;

	@Override
	public MessageType getHandledType() {
		return MessageType.INSTANT_SENSOR_DATA_SYNC;
	}

	@Override
	protected Optional<Message> doHandleMessage(Message message, ChannelHandlerContext ctx) throws Exception {
		Preconditions.checkArgument(message.getPayload() != null, "Payload should NOT be null");
		List<SensorDataRecord> records = MessageDeserializer.deserializeSensorDataSyncPayload(message.getPayloadAsRawJson());
		LOGGER.info("Message payload successfully deserialized and converted into a list of SensorDataRecords of size={}", records.size());
		// Do the database update
		sensorDataRecordsDao.insertDataRecords(records);
		LOGGER.info("Inserted/updated {} sensor data records", records.size());
		// We do not need to reply to this message
		return Optional.<Message>absent();
	}
}
