package com.zeedoo.mars.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.Sensor;
import com.zeedoo.commons.domain.UserCredentials;

public class SensorDaoTest extends EntityDaoTest {

	private SensorDao sensorDao;
	
	private static final String FAKE_SENSOR_ID = "fakeSensorId";
	
	public SensorDaoTest() throws Exception {
		super();
		sensorDao = new SensorDao();
		sensorDao.setCoreApiClient(coreApiClient);
	}
	
	@Test
	public void testGet() throws Exception {
		Sensor sensor = sensorDao.get(FAKE_SENSOR_ID);
		Assert.assertNotNull(sensor);
		Assert.assertEquals("127.0.0.1", sensor.getSunIpAddress());
		Assert.assertEquals((Integer)8080, sensor.getSunIpPort());
	}
	
	@Test
	public void testFindBy() throws Exception {
		List<Sensor> result = sensorDao.findSensors("ABCDEFGH", null);
		Assert.assertEquals(2, result.size());
		result = sensorDao.findSensors("ABCDEFGH", "test2@gmail.com");
		Assert.assertEquals(1, result.size());
		result = sensorDao.findSensors(null, "test2@gmail.com");
		Assert.assertEquals(1, result.size());
	}
	
	@Test
	public void testInsert() throws Exception {	
		Sensor sensor = getSampleSensor();
		sensor.setSensorId("fakeSensorId2");
		Sensor created = sensorDao.insert(sensor);
		Assert.assertNotNull(created);
		//clean up
		boolean result = sensorDao.delete("fakeSensorId2");
		Assert.assertTrue(result);
	}
	
	@Test
	@Ignore
	public void allocateSensors() {
		String macAddr = "3A-2B-1C-DD-";
		String sensorMacAddr = "54-2B-TM-SB-";
		for (int i = 1; i <=24; i = i + 3) {
			String fullAddr = String.format(macAddr + "%02X-%02X", i, i);
			//String id1 = "Sensor" + i;
			String id2 = "Sensor" + (i+1);
			String id3 = "Sensor" + (i+2);
			//Sensor sensor1 = sensorDao.get(id1);
			Sensor sensor2 = sensorDao.get(id2);
			Sensor sensor3 = sensorDao.get(id3);
			//sensor1.setSensorMacId(String.format(sensorMacAddr + "%02X-%02X", i, i));
			sensor2.setSensorMacId(String.format(sensorMacAddr + "%02X-%02X", i, i));
			sensor3.setSensorMacId(String.format(sensorMacAddr + "%02X-%02X", i, i));
			/*sensor1.setSunMacAddress(fullAddr);
			sensor2.setSunMacAddress(fullAddr);
			sensor3.setSunMacAddress(fullAddr);*/
			//sensorDao.update(sensor1);
			sensorDao.update(sensor2);
			sensorDao.update(sensor3);
		}
	}
	
	@Test
	public void testUpdate() throws Exception {
		Sensor sensor =  getSampleSensor();
		sensor.setSensorStatus(DeviceStatus.OFFLINE);
		sensor.setSunIpAddress("205.10.100.10");
		
		sensorDao.update(sensor);		
		sensor = sensorDao.get(FAKE_SENSOR_ID);
		Assert.assertEquals("205.10.100.10", sensor.getSunIpAddress());
		Assert.assertEquals(DeviceStatus.OFFLINE, sensor.getSensorStatus());
		
		sensorDao.update(getSampleSensor());
		sensor = sensorDao.get(FAKE_SENSOR_ID);
		Assert.assertEquals("127.0.0.1", sensor.getSunIpAddress());
		Assert.assertEquals(DeviceStatus.ONLINE, sensor.getSensorStatus());		
	}
	
	private Sensor getSampleSensor() {
		Sensor sensor = new Sensor();
		sensor.setScale(1);
		sensor.setSensorAccessHash("0");
		sensor.setSensorMacId("fakeMacId");
		sensor.setSensorTypeId("11");
		sensor.setSensorUsername("test@gmail.com");
		sensor.setSensorId(FAKE_SENSOR_ID);
		sensor.setSensorStatus(DeviceStatus.ONLINE);
		sensor.setSunIpAddress("127.0.0.1");
		sensor.setSunIpPort(8080);
		sensor.setSunMacAddress("01-23-45-67-89-AB");
		return sensor;
	}

}
