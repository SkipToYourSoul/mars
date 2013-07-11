package com.zeedoo.mars.task;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

public class LocationDataSyncTask extends DataSyncTask {
	
	private static final String TASK_NAME = "LocationDataSyncTask";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationDataSyncTask.class);
	
	public LocationDataSyncTask(ChannelHandlerContext ctx) {
		super(ctx);
	}

	@Override
	protected void performTask() {
		if (!ctx.channel().isOpen()) {
			LOGGER.info("Channel is NOT open. Skipping...");
		}
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
