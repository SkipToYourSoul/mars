package com.zeedoo.mars.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeedoo.mars.event.HandshakeEvent;
import com.zeedoo.mars.event.HandshakeState;

public class HandshakeHandler extends ChannelInboundHandlerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HandshakeHandler.class);
		
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("Established Connection with Sun IPAddress={}, Channel Id={}", ctx
				.channel().remoteAddress(), ctx.channel().id());
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageList<Object> requests)
			throws Exception {
		MessageList<String> msgs = requests.cast();
		// Process each message
		for (int i = 0; i < msgs.size(); i++) {
			String msg = msgs.get(i);
			if (msg.equals("Handshake")) {
				ctx.fireUserEventTriggered(new HandshakeEvent(HandshakeState.SUCCESS));
				removeHandshakeHandler(ctx);
			} else {
				ctx.fireUserEventTriggered(new HandshakeEvent(HandshakeState.FAILURE));
			}
		}
        msgs.releaseAllAndRecycle();
	}
	
	// remove handshake handler from the pipeline
	private void removeHandshakeHandler(ChannelHandlerContext ctx) {
		ctx.pipeline().remove(HandshakeHandler.class);	
		LOGGER.info("Removed HandshakeHandler");
	}

}
