package com.zeedoo.mars.database.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zeedoo.mars.database.DatabaseService;
import com.zeedoo.mars.database.mapper.Mapper;

// basic abstract DAO class for entity
@Component
public abstract class EntityDao<M extends Mapper> {
	
	@Autowired
	protected DatabaseService databaseService;
	
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	protected abstract M getMapper();

}
