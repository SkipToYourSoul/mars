package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.Sensor;
import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.mars.dao.SensorDao;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageDeserializer;
import com.zeedoo.mars.message.MessageType;

@Component
public class SensorAliveStatusMessageHandler extends AbstractMessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SensorAliveStatusMessageHandler.class);

	@Autowired
	private SensorDao sensorDao;

	@Override
	protected Optional<Message> doHandleMessage(Message message, ChannelHandlerContext ctx) throws Exception {
		Preconditions.checkArgument(message.getPayload() != null, "Payload should not be null");
		List<SensorStatus> statusList = MessageDeserializer.deserializeSensorAliveStatusPayload(message.getPayloadAsRawJson());
		int affectedRecords = 0;
		for (SensorStatus status : statusList) {
			// check if sensor status already exists
			Sensor updatedSensor = updateOrCreateSensorStatus(status);
			if (updatedSensor != null) {
				affectedRecords++;
			} else {
				LOGGER.warn("Failed to update/create status for sensorId={}", status.getSensorId());
			}
		}
		LOGGER.debug("Inserted/updated {} sensor(s)", affectedRecords);
		return Optional.<Message> absent();
	}

	private Sensor updateOrCreateSensorStatus(SensorStatus currentStatus) {
		// check if sensor already exists
		Sensor existingSensor = sensorDao.get(currentStatus.getSensorId());
		Sensor updatedSensor = null;
		if (existingSensor != null) {
			existingSensor.setSunIpAddress(currentStatus.getSunIpAddress());
			existingSensor.setSunIpPort(currentStatus.getSunIpPort());
			existingSensor.setSunMacAddress(currentStatus.getSunMacAddress());
			updatedSensor = sensorDao.update(existingSensor);
		} else {
			Sensor sensor = createNewSensorFromSensorStatus(currentStatus);
			updatedSensor = sensorDao.insert(sensor);
		}
		return updatedSensor;
	}

	@Override
	public MessageType getHandledType() {
		return MessageType.SENSOR_ALIVE_STATUS;
	}
	
	public Sensor createNewSensorFromSensorStatus(SensorStatus sensorStatus) {
		Sensor sensor = new Sensor();
		sensor.setSensorStatus(sensorStatus.getSensorStatus());
		sensor.setSensorId(sensorStatus.getSensorId());
		sensor.setSunIpAddress(sensorStatus.getSunIpAddress());
		sensor.setSunIpPort(sensorStatus.getSunIpPort());
		sensor.setSunMacAddress(sensorStatus.getSunMacAddress());
		return sensor;
	}

}
