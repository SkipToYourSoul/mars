package com.zeedoo.mars.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeedoo.mars.event.HandshakeEvent;
import com.zeedoo.mars.event.HandshakeState;
import com.zeedoo.mars.task.LocationDataSyncTask;
import com.zeedoo.mars.task.SensorDataSyncTask;

public class DataSyncHandler extends ChannelInboundHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataSyncHandler.class);

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("Lost Connection with Sun IPAddress={}, ChannelId={}", ctx
				.channel().remoteAddress(), ctx.channel().id());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx,
			MessageList<Object> requests) throws Exception {
		// cast to String
		MessageList<String> msgs = requests.cast();
		for (int i = 0; i < msgs.size(); i++) {
			String msg = msgs.get(i);
			LOGGER.info(msg);
		}
		msgs.releaseAllAndRecycle();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof HandshakeEvent) {
			HandshakeEvent handShakeEvent = (HandshakeEvent) evt;
			if (handShakeEvent.getState() == HandshakeState.SUCCESS) {
				LOGGER.info("Handshake successful! Initiating DataSync tasks...");
				initDataSyncTasks(ctx);
			} else {
				LOGGER.info("Handshake failure! Closing channel...");
				ctx.channel().close();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private void initDataSyncTasks(ChannelHandlerContext ctx) {
		ctx.channel().eventLoop()
				.schedule(new SensorDataSyncTask(ctx), 10, TimeUnit.SECONDS);
		ctx.channel().eventLoop()
				.schedule(new LocationDataSyncTask(ctx), 20, TimeUnit.SECONDS);
	}
}
