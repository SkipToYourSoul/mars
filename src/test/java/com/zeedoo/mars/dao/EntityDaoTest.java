package com.zeedoo.mars.dao;

import com.zeedoo.commons.api.client.CoreApiClient;

public class EntityDaoTest {
	
	private final String testApiKey = "dev";
	private final String testApiSecret = "6fdd1400-a709-11e2-9e96-0800200c9a66";
	//TODO: Change back to HTTPS
	private final String baseUrl = "https://www.zeedoo.cn:8080";
	
	protected CoreApiClient coreApiClient;
	
	public EntityDaoTest() throws Exception {
		coreApiClient = new CoreApiClient(testApiKey, testApiSecret , baseUrl);
	}
}
