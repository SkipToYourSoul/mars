package com.zeedoo.mars.database.dao;

import org.junit.Assert;
import org.junit.Test;

import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.SensorStatus;

public class SensorStatusDaoTest extends EntityDaoTest {

	private SensorStatusDao sensorStatusDao;
	
	private static final String FAKE_SENSOR_ID = "fakeSensorId";
	
	public SensorStatusDaoTest() throws Exception {
		super();
		sensorStatusDao = new SensorStatusDao();
		sensorStatusDao.setDatabaseService(databaseService);
	}

	@Test
	public void testGet() throws Exception {
		SensorStatus status = sensorStatusDao.get(FAKE_SENSOR_ID);
		Assert.assertNotNull(status);
		Assert.assertEquals("127.0.0.1", status.getSunIpAddress());
		Assert.assertEquals((Integer)8080, status.getSunIpPort());
	}
	
	@Test
	public void testInsert() throws Exception {	
		SensorStatus status = getSampleSensorStatus();
		status.setSensorId("fakeSensorId2");
		int result = sensorStatusDao.insert(status);
		Assert.assertEquals(1, result);
		//clean up
		result = sensorStatusDao.delete("fakeSensorId2");
		Assert.assertEquals(1, result);
	}
	
	@Test
	public void testUpdate() throws Exception {
		SensorStatus status =  getSampleSensorStatus();
		status.setSensorStatus(DeviceStatus.OFFLINE);
		status.setSunIpAddress("205.10.100.10");
		
		sensorStatusDao.update(status);		
		status = sensorStatusDao.get(FAKE_SENSOR_ID);
		Assert.assertEquals("205.10.100.10", status.getSunIpAddress());
		Assert.assertEquals(DeviceStatus.OFFLINE, status.getSensorStatus());
		
		sensorStatusDao.update(getSampleSensorStatus());
		status = sensorStatusDao.get(FAKE_SENSOR_ID);
		Assert.assertEquals("127.0.0.1", status.getSunIpAddress());
		Assert.assertEquals(DeviceStatus.ONLINE, status.getSensorStatus());		
	}
	
	private SensorStatus getSampleSensorStatus() {
		SensorStatus status = new SensorStatus();
		status.setSensorId(FAKE_SENSOR_ID);
		status.setSensorStatus(DeviceStatus.ONLINE);
		status.setSunIpAddress("127.0.0.1");
		status.setSunIpPort(8080);
		status.setSunMacAddress("01-23-45-67-89-AB");
		return status;
	}
}
