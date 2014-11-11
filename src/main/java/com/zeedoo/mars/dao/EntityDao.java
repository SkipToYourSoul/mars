package com.zeedoo.mars.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mysql.jdbc.StringUtils;
import com.zeedoo.commons.api.client.CoreApiClient;
import com.zeedoo.commons.domain.Link;
import com.zeedoo.commons.domain.Paginator;
import com.zeedoo.mars.message.MessageDeserializer;

// Basic abstract DAO class for entity
@Component
public abstract class EntityDao {
	
	@Autowired
	protected CoreApiClient coreApiClient;
	
	public void setCoreApiClient(CoreApiClient coreApiClient) {
		this.coreApiClient = coreApiClient;
	}
	
	// Utility methods	
	protected <T> List<T> resolveObjectsFromPaginator(Paginator paginator, Class<T> clazz) {
		Preconditions.checkState(paginator != null);
		List<T> objects = Lists.newArrayList();
		Paginator currentPaginator = paginator;
		List<? extends Object> currentResultSet = paginator.getResult();
		while (currentResultSet != null && currentResultSet.size() > 0) {
			for (Object o : currentResultSet) {
				objects.add(MessageDeserializer.getMapper().convertValue(o, clazz));
			}
			final String nextPageLink = currentPaginator.getNext();
			if (!StringUtils.isNullOrEmpty(nextPageLink)) {
				currentPaginator = coreApiClient.getWithFullUrl(nextPageLink, Paginator.class);
				currentResultSet = currentPaginator.getResult();
			} else {
				break;
			}
		}
		return objects;
	}
	
	protected <T> List<T> resolveLinks(List<Link> links, Class<T> clazz) {
		Preconditions.checkState(links != null);
		List<T> objects = Lists.newArrayList();
		for (Link link : links) {
			T obj = coreApiClient.getWithFullUrl(link.getHref(), clazz);
			objects.add(obj);
		}
		return objects;
	}
}
