package com.zeedoo.mars.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zeedoo.mars.task.SensorDataSyncTask;
import com.zeedoo.mars.task.TaskManager;

@Component
public class Server {
	
	@Value("${server.port}")
	private Integer port; //Default port
	
	@Autowired
	private ServerChannelInitializer serverChannelInitializer;
	
	public Server() {}
	//public Server(int port) {
	//	this.port = port;
	//}
	
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			 .channel(NioServerSocketChannel.class)
			 .option(ChannelOption.TCP_NODELAY, true)
			 .childHandler(serverChannelInitializer);
			b.bind(port).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
