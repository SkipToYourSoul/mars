package com.zeedoo.mars.service;

import org.springframework.stereotype.Service;

import com.zeedoo.mars.domain.Sun;

/**
 * Service that maintains the states of SUN systems
 *
 */
@Service
public interface SunManagementService {
	
	/**
	 * Registers a Sun device to in-memory cache and database
	 * @param sun
	 * @return
	 */
	boolean registerSun(Sun sun);
	
	/**
	 * Updates the state of a Sun device in cache as well as database
	 * @param sunId
	 * @param currentStatus
	 * @return
	 */
	boolean updateSunDeviceStatus(String sunId, Sun currentSun);
	
	/**
	 * Gets the Sun device from the in-memory cache.
	 * If not found in cache, retrieve it from the database
	 * @param sunId
	 * @return
	 */
	Sun getSun(String sunId);
}
