package com.zeedoo.mars.dao;

import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.net.InetAddresses;
import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.SunStatus;
import com.zeedoo.mars.dao.SunStatusDao;

public class SunStatusDaoTest extends EntityDaoTest {
	
	private SunStatusDao sunStatusDao;

	public SunStatusDaoTest() throws Exception {
		super();
		sunStatusDao = new SunStatusDao();
		sunStatusDao.setCoreApiClient(coreApiClient);
	}
	
	@Test
	public void testUpdate() {
		String fakeId = UUID.randomUUID().toString();
		SunStatus newStatus = new SunStatus("198.84.173.33", 2222, null, DeviceStatus.OFFLINE);
		sunStatusDao.update(newStatus);
		newStatus = sunStatusDao.getStatusBySocketAddress("198.84.173.33", 2222);
		Assert.assertEquals(null, newStatus.getSunMacAddress());
		Assert.assertEquals(DeviceStatus.OFFLINE, newStatus.getSunStatus());
		
		newStatus.setSunMacAddress(fakeId);
		newStatus.setSunStatus(DeviceStatus.ONLINE);
		sunStatusDao.update(newStatus);
		Assert.assertEquals(fakeId, newStatus.getSunMacAddress());
		Assert.assertEquals(DeviceStatus.ONLINE, newStatus.getSunStatus());
	}

	@Test
	public void testInsertAndGet() {
		String fakeId = UUID.randomUUID().toString();
		String fakeIpAddress = InetAddresses.fromInteger(new Random().nextInt()).getHostAddress();
		Integer fakePort = new Random().nextInt(1000);
		SunStatus status = new SunStatus(fakeIpAddress, fakePort, fakeId, DeviceStatus.ONLINE);
		SunStatus newStatus = sunStatusDao.insert(status);
		Assert.assertNotNull(newStatus);
		// GET the object we just created
		status = sunStatusDao.getStatusBySocketAddress(fakeIpAddress, fakePort);
		Assert.assertNotNull(status);
		Assert.assertEquals(fakeId, status.getSunMacAddress());
		// Try getting by sunId
		status = sunStatusDao.getStatusBySunMacAddress(fakeId);
		Assert.assertNotNull(status);
	}
}
