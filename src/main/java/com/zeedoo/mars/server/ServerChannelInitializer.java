package com.zeedoo.mars.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.zeedoo.mars.server.handler.InboundSunMessageHandler;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
@ManagedResource
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerChannelInitializer.class);
		
	private static final StringDecoder DECODER = new StringDecoder(CharsetUtil.UTF_8);
	
	@Autowired
	private InboundSunMessageHandler inboundSunMessageHandler;

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// DelimiterBasedFrameDecoder is needed if we are sending stream-based
		// messages. (It will strip delimiters)
		//pipeline.addLast("framer", new LineBasedFrameDecoder(8192));
		//pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.nulDelimiter()));
		//pipeline.addLast("decoder", DECODER);

		// and then business logic
		//pipeline.addLast("loggingHandler", new LoggingHandler(LogLevel.INFO));
		pipeline.addLast("messageDecoder", new MessageDecoder());
		pipeline.addLast("messageEncoder", new MessageEncoder());

		pipeline.addLast("inboundSunMesageHandler", inboundSunMessageHandler);
	}
}
