package com.zeedoo.mars.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.zeedoo.mars.netty.server.handler.InboundSunMessageHandler;
import com.zeedoo.mars.netty.server.handler.MessageDecoder;
import com.zeedoo.mars.netty.server.handler.MessageEncoder;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerChannelInitializer.class);
		
	// This integer represents the maximum size of a TCP packet we are allowing
	private static final int MAX_FRAME_SIZE = 16384;
	
	private static final StringDecoder DECODER = new StringDecoder(CharsetUtil.UTF_8);
	
	@Autowired
	private InboundSunMessageHandler inboundSunMessageHandler;
	
	@Autowired
	private MessageDecoder messageDecoder;
	
	@Autowired
	private MessageEncoder messageEncoder;

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// DelimiterBasedFrameDecoder is needed if we are sending stream-based
		// messages. (It will strip delimiters)
		// pipeline.addLast("framer", new LineBasedFrameDecoder(8192));
		// pipeline.addLast("loggingHandler", new LoggingHandler(LogLevel.INFO));
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(MAX_FRAME_SIZE, Delimiters.nulDelimiter()));
		pipeline.addLast("decoder", DECODER);

		// and then business logic
		pipeline.addLast("messageDecoder", messageDecoder);
		pipeline.addLast("messageEncoder", messageEncoder);

		pipeline.addLast("inboundSunMessageHandler", inboundSunMessageHandler);
	}
}
