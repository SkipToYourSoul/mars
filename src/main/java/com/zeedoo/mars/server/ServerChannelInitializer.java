package com.zeedoo.mars.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.zeedoo.mars.server.handler.InboundSunMessageHandler;
import com.zeedoo.mars.server.handler.HandshakeHandler;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
@ManagedResource
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerChannelInitializer.class);
		
	private static final StringDecoder DECODER = new StringDecoder(CharsetUtil.UTF_8);
	
	//TODO: Make this JMX persistent
	//TODO: Use real handshake once we're there
	private boolean skipHandshake = true;
	
	@Autowired
	private InboundSunMessageHandler inboundSunMessageHandler;
	
	@Autowired
	private HandshakeHandler handshakeHandler;
	
	@ManagedAttribute(description = "Whether the initial handshake is skipped upon the connection establishment with a Sun device")
	public boolean isSkipHandshake() {
		return skipHandshake;
	}

	@ManagedAttribute(description = "Sets whether the initial handshake is skipped upon the connection establishment with a Sun device")
	public void setSkipHandshake(boolean skipHandshake) {
		this.skipHandshake = skipHandshake;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// DelimiterBasedFrameDecoder is needed if we are sending stream-based
		// messages. (It will strip delimiters)
		//pipeline.addLast("framer", new LineBasedFrameDecoder(8192));
		//pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.nulDelimiter()));
		//pipeline.addLast("decoder", DECODER);

		// and then business logic
		pipeline.addLast("messageDecoder", new MessageDecoder());
		
		if (skipHandshake) {
			LOGGER.info("Handshake on initial connection is DISABLED. Skipping adding handshake handler");
		} else{
			// Add handshake handler
			pipeline.addLast("handshakeHandler", handshakeHandler);
		}
		pipeline.addLast("inboundSunMesageHandler", inboundSunMessageHandler);
	}
}
