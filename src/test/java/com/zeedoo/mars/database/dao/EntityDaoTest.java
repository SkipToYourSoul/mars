package com.zeedoo.mars.database.dao;

import com.zeedoo.mars.api.CoreApiClient;

public class EntityDaoTest {
	
	private final String testApiKey = "dev";
	private final String testApiSecret = "6fdd1400-a709-11e2-9e96-0800200c9a66";
	private final String baseUrl = "http://localhost:8080";
	
	protected CoreApiClient coreApiClient;
	
	public EntityDaoTest() throws Exception {
		coreApiClient = new CoreApiClient(testApiKey, testApiSecret , baseUrl);
	}
}
