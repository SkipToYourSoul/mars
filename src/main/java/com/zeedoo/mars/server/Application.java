package com.zeedoo.mars.server;

import java.util.ArrayList;

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
					
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i= 0; i < 5;i++) {
			list.add("test");
		}
		for (int i=0; i < 5;i++) {
			if (list.get(i) instanceof String) {
				System.out.println("yes");
			}
		}
		
		// load server
		Server server = (Server) appContext.getBean(Server.class);
		server.run();
	}

}
