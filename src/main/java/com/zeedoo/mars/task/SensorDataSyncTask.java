package com.zeedoo.mars.task;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensorDataSyncTask extends DataSyncTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataSyncTask.class);
	
	private static final String TASK_NAME = "SensorDataSyncTask";
	
	public SensorDataSyncTask(ChannelHandlerContext ctx) {
		super(ctx);
	}

	@Override
	protected void performTask() {
		try {
			//TODO: Write data to outbound buffer to notify Sun
			scheduleNextRun();
		} catch (Throwable t) {
			//Determine if the exception is recoverable or not
			//Recoverable - Schedule next task
			//Unrecoverable - Close channel
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
