package com.zeedoo.mars.netty.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// Load Spring Context
		@SuppressWarnings("resource")
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"com/zeedoo/mars/spring/applicationContext.xml");
							
		// load server
		Server server = (Server) appContext.getBean(Server.class);
		server.run();
	}

	
}
