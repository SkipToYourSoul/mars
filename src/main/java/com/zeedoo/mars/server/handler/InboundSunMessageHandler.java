package com.zeedoo.mars.server.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.event.HandshakeEvent;
import com.zeedoo.mars.event.HandshakeState;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.handler.MessageHandler;
import com.zeedoo.mars.message.handler.MessageHandlerConfiguration;
import com.zeedoo.mars.service.SunManagementService;
import com.zeedoo.mars.task.LocationDataSyncTask;
import com.zeedoo.mars.task.SensorDataSyncTask;

@Sharable
@Component
public class InboundSunMessageHandler extends ChannelInboundHandlerAdapter {
	
	@Autowired
	private MessageHandlerConfiguration messageHandlerConfiguration;
	
	@Autowired
	private SunManagementService sunManagementService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InboundSunMessageHandler.class);
	
	/** Connection establishment / interruption **/
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("Established Connection with Sun SocketAddress={}, Channel Id={}", ctx.channel().remoteAddress(), ctx.channel().id());
		String sunIpAddress = getRemoteIpAddress(ctx);
		sunManagementService.onSunConnectionEstablished(sunIpAddress);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("Lost Connection with Sun IPAddress={}, ChannelId={}", ctx.channel().remoteAddress(), ctx.channel().id());
		String sunIpAddress = getRemoteIpAddress(ctx);
		sunManagementService.onSunConnectionInterrupted(sunIpAddress);
	}

	/** Message processing **/
	@Override
	public void messageReceived(ChannelHandlerContext ctx,
			MessageList<Object> requests) throws Exception {
		// cast to String
		MessageList<Message> messages = requests.cast();
		LOGGER.info("Processing MessageList of size={}", messages.size());
		processMessages(messages, ctx);
		// IMPORTAT - Netty needs this
		messages.releaseAllAndRecycle();
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
	
	/** Error handling **/
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		//FIXME: We need to suppress and log most exceptions, for rare unrecoverables, we should close connection
		//ctx.close();
	}
	
	private String getRemoteIpAddress(ChannelHandlerContext ctx) {
		Preconditions.checkNotNull("ChannelHandlerContext should not be null", ctx);
		InetSocketAddress socketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		return socketAddress.getAddress().getHostAddress();
	}
	
	private void processMessages(MessageList<Message> messages, ChannelHandlerContext ctx) throws Exception {
		String sunIpAddress = getRemoteIpAddress(ctx);
		for (int i = 0; i < messages.size(); i++) {
			Message message = messages.get(i);
			LOGGER.info("Received Message={}",message.toString());
			// We will do a DB call per message here since most of the time there should be only one message
			sunManagementService.onSunMessageReceived(message.getSourceId(), sunIpAddress);
			doProcessMessage(message, ctx);
		}
	}
	
	private void doProcessMessage(Message msg, ChannelHandlerContext ctx) {
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
