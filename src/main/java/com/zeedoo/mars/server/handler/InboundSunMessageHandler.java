package com.zeedoo.mars.server.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zeedoo.mars.event.HandshakeEvent;
import com.zeedoo.mars.event.HandshakeState;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.handler.MessageHandler;
import com.zeedoo.mars.message.handler.MessageHandlerConfiguration;
import com.zeedoo.mars.task.LocationDataSyncTask;
import com.zeedoo.mars.task.SensorDataSyncTask;

@Sharable
@Component
public class InboundSunMessageHandler extends ChannelInboundHandlerAdapter {
	
	@Autowired
	private MessageHandlerConfiguration messageHandlerConfiguration;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InboundSunMessageHandler.class);

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("Lost Connection with Sun IPAddress={}, ChannelId={}", ctx
				.channel().remoteAddress(), ctx.channel().id());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx,
			MessageList<Object> requests) throws Exception {
		// cast to String
		MessageList<Message> msgs = requests.cast();
		for (int i = 0; i < msgs.size(); i++) {
			Message msg = msgs.get(i);
			LOGGER.info(msg.toString());
			processMessage(msg, ctx);
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
	
	private void processMessage(Message msg, ChannelHandlerContext ctx) {
		// Get handler, and process the message
		MessageHandler handler = messageHandlerConfiguration.getMessageHandler(msg.getMessageType());
		handler.handleMessage(msg, ctx);
	}

	private void initDataSyncTasks(ChannelHandlerContext ctx) {
		ctx.channel().eventLoop()
				.schedule(new SensorDataSyncTask(ctx), 10, TimeUnit.SECONDS);
		ctx.channel().eventLoop()
				.schedule(new LocationDataSyncTask(ctx), 20, TimeUnit.SECONDS);
	}
}
