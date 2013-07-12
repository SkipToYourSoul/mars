package com.zeedoo.mars.message;

import io.netty.channel.ChannelHandlerContext;

/**
 * Simple Message gateway to send outbound messages
 * @author nzhu
 *
 */
public interface MessageGateway {
	
	/**
	 * Sends an outbound message
	 * @param message
	 * @param ctx
	 */
	void sendMessage(Message message, ChannelHandlerContext ctx);

}
