package com.zeedoo.mars.database;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Singleton database service to talk to MySql database
 */

@Service
public class DatabaseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseService.class);
	private static final String MYBATIS_CONFIG_FILE = "com/zeedoo/mars/db/mybatis-config.xml";
	
	private static SqlSessionFactory sqlSessionFactory = null;	
	private static SqlSessionManager sqlSessionManager = null;
	
	@Value("${environment}")
	private String environment;
	
	@PostConstruct
	public synchronized void init() throws IOException {
	    InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONFIG_FILE);
	    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, environment);
	    sqlSessionManager = SqlSessionManager.newInstance(sqlSessionFactory);
	    LOGGER.debug("DatabaseService bean created");
	}
    
	public SqlSessionFactory getSessionFactory() {
	    return sqlSessionFactory;
	}
		
	public <T> T getMapper(Class<T> clazz) {
		return sqlSessionManager.getMapper(clazz);
	}
	
	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
}
