package com.zeedoo.mars.task;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.zeedoo.mars.error.MarsErrorHandler;
import com.zeedoo.mars.message.MessageBuilder;
import com.zeedoo.mars.message.MessageGateway;
import com.zeedoo.mars.message.MessageType;

@Component
@Scope("prototype")
public class SensorDataSyncTask extends Task {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataSyncTask.class);
	
	private static final String TASK_NAME = "SensorDataSyncTask";
	
	@Autowired
	private MarsErrorHandler marsErrorHandler;
	
	@Autowired
	private MessageGateway messageGateway;
	
	@Autowired
	private TaskManager taskManager;
		
	@Override
	protected void performTask(){
		try {
			//Send message
			messageGateway.sendMessage(MessageBuilder.buildMessage(MessageType.GET_SENSOR_FILEDATA_SYNC_INFO, Optional.<JsonNode>absent()), 
					channelHandlerContext);
			//scheduleNextRun();
		} catch (Exception e) {
			marsErrorHandler.handleError(channelHandlerContext, e);
		}		
	}

	@Override
	public String getTaskName() {
		return TASK_NAME;
	}
	
	@Override
	protected void scheduleNextRun() {
		//TODO: The delay will become dynamic
		channelHandlerContext.channel().eventLoop().schedule(this, 60, TimeUnit.MINUTES);
	}

	@Override
	protected boolean isTaskEnabled() {
		return taskManager.isSensorDataSyncTaskEnabled();
	}
}
