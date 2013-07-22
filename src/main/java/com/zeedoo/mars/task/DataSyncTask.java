package com.zeedoo.mars.task;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DataSyncTask implements Runnable {
	
	//TODO: Make this JMX persistent
	protected boolean isEnabled = true;
	
	protected final ChannelHandlerContext ctx;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncTask.class);
	
	public DataSyncTask(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	/** Getters/Setters */
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	@Override
	public void run() {
		if (!isEnabled) {
			LOGGER.info("{} is disabled. Skipping...", getTaskName());
		}
		// Check channel open status
		if (!ctx.channel().isOpen()) {
			LOGGER.info("Channel(remoteAddress={}) is NOT open. Closing channel...", ctx.channel().remoteAddress());
			ctx.channel().close();
		}
		LOGGER.info("DataSyncTask = {} triggered as scheduled", getTaskName());
		performTask();
	}
	
	protected abstract void scheduleNextRun();
	
	protected abstract void performTask();
	
	public abstract String getTaskName();
}