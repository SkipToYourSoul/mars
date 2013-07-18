package com.zeedoo.mars.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.event.HandshakeEvent;
import com.zeedoo.mars.event.HandshakeState;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageType;

@Component
public class HandshakeHandler extends SimpleChannelInboundHandler<Message> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HandshakeHandler.class);
		
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("Established Connection with Sun IPAddress={}", ctx.channel().remoteAddress());
	}
	
	/** Message processing **/
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
		Preconditions.checkNotNull("Message should not be null", message);
		LOGGER.info("Received Message={}",message.toString());
		//FIXME: Implement real handshake protocol
		if (message.getMessageType() == MessageType.HANDSHAKE) {
			ctx.fireUserEventTriggered(new HandshakeEvent(HandshakeState.SUCCESS));
			removeHandshakeHandler(ctx);
		} else {
			ctx.fireUserEventTriggered(new HandshakeEvent(HandshakeState.FAILURE));
		}
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
		
	// remove handshake handler from the pipeline
	private void removeHandshakeHandler(ChannelHandlerContext ctx) {
		ctx.pipeline().remove(HandshakeHandler.class);	
		LOGGER.info("Removed HandshakeHandler");
	}
}
