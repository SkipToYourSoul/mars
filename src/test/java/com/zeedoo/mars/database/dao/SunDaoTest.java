package com.zeedoo.mars.database.dao;

import org.junit.Assert;
import org.junit.Test;

import com.zeedoo.mars.domain.DeviceStatus;
import com.zeedoo.mars.domain.Sun;

public class SunDaoTest extends EntityDaoTest {
	
	private static final String TEST_SUN_ID = "1";
	private SunDao sunDao;
	
	
	public SunDaoTest() throws Exception {
		super();
		sunDao = new SunDao();
		sunDao.setDatabaseService(databaseService);
	}
	
	@Test
	public void testGet() {
		Sun sun = sunDao.get(TEST_SUN_ID);
		Assert.assertNotNull(sun);
		Assert.assertEquals("1", sun.getId());
		Assert.assertEquals("1A2B3C", sun.getSunSsid());
		Assert.assertEquals(DeviceStatus.OFFLINE, sun.getDeviceStatus());
	}
	
	@Test
	public void testUpdate() {
		Sun sun = sunDao.get(TEST_SUN_ID);
		Assert.assertNotNull(sun);
		sun.setDeviceStatus(DeviceStatus.ONLINE);
		sunDao.update(sun);
		//check value after update
		Sun updated = sunDao.get(TEST_SUN_ID);
		Assert.assertNotNull(updated);
		Assert.assertEquals(DeviceStatus.ONLINE, updated.getDeviceStatus());
		//revert to original
		updated.setDeviceStatus(DeviceStatus.OFFLINE);
		sunDao.update(updated);
		updated = sunDao.get(TEST_SUN_ID);
		Assert.assertNotNull(updated);
		Assert.assertEquals(Boolean.FALSE, updated.getDeviceStatus());
	}

}
