package com.zeedoo.mars.database.dao;

import com.zeedoo.mars.api.CoreApiClient;
import com.zeedoo.mars.database.DatabaseService;

public class EntityDaoTest {
	
	protected DatabaseService databaseService;
	
	protected CoreApiClient coreApiClient;
	
	public EntityDaoTest() throws Exception {
		databaseService = new DatabaseService();
		databaseService.init();
		coreApiClient = new CoreApiClient("dev","6fdd1400-a709-11e2-9e96-0800200c9a66","http://localhost:8080");
	}
}
