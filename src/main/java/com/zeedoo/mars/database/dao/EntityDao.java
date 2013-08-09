package com.zeedoo.mars.database.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zeedoo.mars.api.CoreApiClient;

// Basic abstract DAO class for entity
@Component
public abstract class EntityDao {
	
	@Autowired
	protected CoreApiClient coreApiClient;
	
	public void setCoreApiClient(CoreApiClient coreApiClient) {
		this.coreApiClient = coreApiClient;
	}
}
