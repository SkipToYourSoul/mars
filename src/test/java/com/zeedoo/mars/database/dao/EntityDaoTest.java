package com.zeedoo.mars.database.dao;

import com.zeedoo.mars.database.DatabaseService;

public class EntityDaoTest {
	
	protected DatabaseService databaseService;
	
	public EntityDaoTest() throws Exception {
		databaseService = new DatabaseService();
		databaseService.init();
	}
}
