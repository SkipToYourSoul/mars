package com.zeedoo.mars.task;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Task implements Runnable {
	
	protected ChannelHandlerContext channelHandlerContext;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);
		
	@Override
	public void run() {
		if (!isTaskEnabled()) {
			LOGGER.info("{} is disabled. Skipping...", getTaskName());
			return;
		}
		// Check channel open status
		if (!channelHandlerContext.channel().isOpen()) {
			LOGGER.info("Channel(remoteAddress={}) is NOT open. Closing channel...", channelHandlerContext.channel().remoteAddress().toString());
			channelHandlerContext.channel().close();
			return;
		}
		LOGGER.info("Running Task={} for Sun RemoteAddress={}", getTaskName(), channelHandlerContext.channel().remoteAddress().toString());
		performTask();
	}
	
		
	public void setChannelHandlerContext(ChannelHandlerContext ctx) {
		this.channelHandlerContext = ctx;
	}
	
	protected abstract boolean isTaskEnabled();

	protected abstract void scheduleNextRun();
		
	protected abstract void performTask();
	
	public abstract String getTaskName();
}
