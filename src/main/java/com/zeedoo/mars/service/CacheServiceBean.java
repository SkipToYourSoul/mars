package com.zeedoo.mars.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.management.ManagementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;

@Component
public class CacheServiceBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceBean.class);
	private static final String FILE_TRANSFER_TASK_CACHE_NAME = "FileTransferTaskCache";
	
	// This cache is here to handle the case that Mars receives multiple messages, we will discard all messages except the first one
	// This cache doesn't need to be persistent, so we will use the google in-memory implementation
	private static final com.google.common.cache.Cache<String, Boolean> MESSAGE_ID_CACHE = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build();	
	
    // Determines how many elements can exist in the heap
	private final int maxEntriesLocalHeap = 1000;
	
	private final Configuration cacheManagerConfig = new Configuration().diskStore(new DiskStoreConfiguration().path("ehcache/"));
			
	// Creates a singleton cache manager
	private final CacheManager cacheManager = CacheManager.create(cacheManagerConfig);
	
	@Autowired
	private MBeanServerFactoryBean mbeanServer;
	
	@PostConstruct
	public void initialize() {
		cacheManager.setName("CacheManager");
		// Creates caches
		Cache fileTransferTaskPool = new Cache(
				new CacheConfiguration(FILE_TRANSFER_TASK_CACHE_NAME, maxEntriesLocalHeap)
				.logging(true)
				.eternal(true)
				//.persistence(new PersistenceConfiguration().strategy(Strategy.NONE)));
				.persistence(new PersistenceConfiguration().strategy(Strategy.LOCALTEMPSWAP)));
		cacheManager.addCache(fileTransferTaskPool);
		// Register to JMX control
		ManagementService.registerMBeans(cacheManager, mbeanServer.getObject(), true, true, true, true);
		fileTransferTaskPool.put(new Element("a","b"));
	}

	public Cache getFileTransferTaskPool() {
		return cacheManager.getCache(FILE_TRANSFER_TASK_CACHE_NAME);
	}
	
	public com.google.common.cache.Cache<String, Boolean> getMessageIdCache() {
		return MESSAGE_ID_CACHE;
	}
}
