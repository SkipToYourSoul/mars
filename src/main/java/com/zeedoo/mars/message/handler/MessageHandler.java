package com.zeedoo.mars.message.handler;

import com.google.common.base.Optional;
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
     * @return a Response message if necessary
     */
	Optional<Message> handleMessage(Message message);
	
	/**
	 * Returns the type this handler handles
	 * @return
	 */
	MessageType getHandledType();
}
