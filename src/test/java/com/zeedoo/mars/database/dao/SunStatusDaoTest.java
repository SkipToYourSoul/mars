package com.zeedoo.mars.database.dao;

import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.net.InetAddresses;
import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.SunStatus;

public class SunStatusDaoTest extends EntityDaoTest {
	
	private SunStatusDao sunStatusDao;

	public SunStatusDaoTest() throws Exception {
		super();
		sunStatusDao = new SunStatusDao();
		sunStatusDao.setDatabaseService(databaseService);
		sunStatusDao.setCoreApiClient(coreApiClient);
	}
	
	@Test
	public void testUpdate() {
		String fakeId = UUID.randomUUID().toString();
		SunStatus newStatus = new SunStatus("198.84.173.33", null, DeviceStatus.OFFLINE);
		sunStatusDao.update(newStatus);
		newStatus = sunStatusDao.getStatusByIpAddress("198.84.173.33");
		Assert.assertEquals(null, newStatus.getSunId());
		Assert.assertEquals(DeviceStatus.OFFLINE, newStatus.getSunStatus());
		
		newStatus.setSunId(fakeId);
		newStatus.setSunStatus(DeviceStatus.ONLINE);
		sunStatusDao.update(newStatus);
		Assert.assertEquals(fakeId, newStatus.getSunId());
		Assert.assertEquals(DeviceStatus.ONLINE, newStatus.getSunStatus());
	}

	@Test
	public void testInsertAndGet() {
		String fakeId = UUID.randomUUID().toString();
		String fakeIpAddress = InetAddresses.fromInteger(new Random().nextInt()).getHostAddress();
		SunStatus status = new SunStatus(fakeIpAddress, fakeId, DeviceStatus.ONLINE);
		SunStatus newStatus = sunStatusDao.insert(status);
		Assert.assertNotNull(newStatus);
		// GET the object we just created
		status = sunStatusDao.getStatusByIpAddress(fakeIpAddress);
		Assert.assertNotNull(status);
		Assert.assertEquals(fakeId, status.getSunId());
	}
}
