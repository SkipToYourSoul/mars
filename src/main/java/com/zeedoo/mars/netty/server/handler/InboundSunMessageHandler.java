package com.zeedoo.mars.netty.server.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.error.MarsErrorHandler;
import com.zeedoo.mars.event.HandshakeEvent;
import com.zeedoo.mars.event.HandshakeState;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.handler.MessageHandler;
import com.zeedoo.mars.message.handler.MessageHandlerConfiguration;
import com.zeedoo.mars.service.SunManagementService;
import com.zeedoo.mars.task.TaskManager;

@Sharable
@Component
public class InboundSunMessageHandler extends SimpleChannelInboundHandler<Message> {
	
	@Autowired
	private MarsErrorHandler marsErrorHandler;
	
	@Autowired
	private MessageHandlerConfiguration messageHandlerConfiguration;
	
	@Autowired
	private SunManagementService sunManagementService;
	
	@Autowired
	private TaskManager taskManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(InboundSunMessageHandler.class);
	
	/** Connection establishment / interruption **/
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("Established Connection with Sun SocketAddress={}", ctx.channel().remoteAddress());
		InetSocketAddress socketAddress = getRemoteSocketAddress(ctx);
		final String sunIpAddress = socketAddress.getAddress().getHostAddress();
		final Integer sunPort = socketAddress.getPort();
		sunManagementService.onSunConnectionEstablished(sunIpAddress, sunPort);
		LOGGER.info("Initiating DataSync tasks for Sun RemoteAddress={}", ctx.channel().remoteAddress());
		initDataSyncTasks(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("Lost Connection with Sun SocketAddress={}", ctx.channel().remoteAddress());
		InetSocketAddress socketAddress = getRemoteSocketAddress(ctx);
		final String sunIpAddress = socketAddress.getAddress().getHostAddress();
		final Integer sunPort = socketAddress.getPort();
		sunManagementService.onSunConnectionInterrupted(sunIpAddress, sunPort);
	}

	/** Message processing **/
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
		Preconditions.checkNotNull("Message should not be null", message);
		LOGGER.info("Received Message={}",message.toString());
		InetSocketAddress socketAddress = getRemoteSocketAddress(ctx);
		final String sunIpAddress = socketAddress.getAddress().getHostAddress();
		final Integer sunPort = socketAddress.getPort();
		// We will do a DB call per message here since the load should be relatively light
		sunManagementService.onSunMessageReceived(message.getSourceId(), sunIpAddress, sunPort);
		doProcessMessage(message, ctx);
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
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
		marsErrorHandler.handleError(ctx, cause);
	}
	
	private InetSocketAddress getRemoteSocketAddress(ChannelHandlerContext ctx) {
		Preconditions.checkNotNull("ChannelHandlerContext should not be null", ctx);
		InetSocketAddress socketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		return socketAddress;
	}
	
	private void doProcessMessage(Message msg, ChannelHandlerContext ctx) throws Exception {
		// Get handler, and process the message
		MessageHandler handler = messageHandlerConfiguration.getMessageHandler(msg.getMessageType());
		handler.handleMessage(msg, ctx);
	}

	private void initDataSyncTasks(ChannelHandlerContext ctx) {
		ctx.channel().eventLoop()
				.schedule(taskManager.createNewSensorDataSyncTask(ctx), 10, TimeUnit.SECONDS);
	}
}
