package com.zeedoo.mars.task;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageBuilder;
import com.zeedoo.mars.message.MessageGateway;
import com.zeedoo.mars.message.MessageGatewayBean;
import com.zeedoo.mars.message.MessageType;

public class SensorDataSyncTask extends DataSyncTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataSyncTask.class);
	
	private static final String TASK_NAME = "SensorDataSyncTask";
	
	// Task is not a Spring bean so we have to manually create an instance hre
	private final MessageGateway messageGateway = new MessageGatewayBean();
	
	public SensorDataSyncTask(ChannelHandlerContext ctx) {
		super(ctx);
	}

	@Override
	protected void performTask(){
		try {
			//Send an outbound message to notify Sun
			Message message = MessageBuilder.buildMessage(MessageType.TIMED_SENSOR_DATA_SYNC, Optional.<JsonNode>absent(), null, null);
			LOGGER.info("Built Message={}", message.toString());
			messageGateway.sendMessage(message, ctx);
			scheduleNextRun();
			//throw new IllegalStateException("test exception");
		} catch (Exception e) {
			//Determine if the exception is recoverable or not
			//Recoverable - Schedule next task
			//Unrecoverable - Close channel
			LOGGER.error("Exception thrown in scheduled task", e);
			// We won't throw exception here since this is inside a Runnable so nobody would catch it anyway
			//throw e;
		}		
	}

	@Override
	public String getTaskName() {
		return TASK_NAME;
	}

	@Override
	protected void scheduleNextRun() {
		//TODO: The delay will become dynamic
		ctx.channel().eventLoop().schedule(this, 10, TimeUnit.SECONDS);
	}


}
