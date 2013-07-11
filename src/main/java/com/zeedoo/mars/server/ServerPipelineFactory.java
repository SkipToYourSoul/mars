package com.zeedoo.mars.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
@Component
public class ServerPipelineFactory extends ChannelInitializer<SocketChannel> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerPipelineFactory.class);
	
	//TODO: Make this JMX persistent
	//TODO: Use real handshake once we're there
	@Value("${skipHandshake}")
	private boolean skipHandshake;
	
	private static final StringDecoder DECODER = new StringDecoder(CharsetUtil.UTF_8);
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// DelimiterBasedFrameDecoder is needed if we are sending stream-based
		// messages. (It will strip delimiters)
		//pipeline.addLast("framer", new LineBasedFrameDecoder(8192));
		//pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.nulDelimiter()));
		//pipeline.addLast("decoder", DECODER);

		// and then business logic
		pipeline.addLast("jsonDecoder", new JsonDecoder());
		
		if (skipHandshake) {
			LOGGER.info("Handshake on initial connection is DISABLED. Skipping adding handshake handler");
		} else{
			// Add handshake handler
			pipeline.addLast("handshakeHandler", new HandshakeHandler());
		}
		pipeline.addLast("dataSyncHandler", new DataSyncHandler());
	}
}
