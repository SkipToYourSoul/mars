package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageType;

/**
 * Generic Interface for a Message Handler
 * @author nzhu
 *
 */
public interface MessageHandler {
	
    /**
     * The original message to handle
     * @param message
     */
	void handleMessage(Message message, ChannelHandlerContext ctx);
	
	/**
	 * Returns the type this handler handles
	 * @return
	 */
	MessageType getHandledType();
}
