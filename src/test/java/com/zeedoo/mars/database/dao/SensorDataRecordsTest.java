package com.zeedoo.mars.database.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.zeedoo.mars.domain.SensorDataRecord;
import com.zeedoo.mars.utils.DateUtils;

public class SensorDataRecordsTest extends EntityDaoTest {
	
	private SensorDataRecordsDao sensorDataRecordsDao;
	
	private static final String TEST_SENSOR_ID = "fakeSensorId";

	public SensorDataRecordsTest() throws Exception {
		super();
		sensorDataRecordsDao = new SensorDataRecordsDao();
		sensorDataRecordsDao.setDatabaseService(databaseService);
	}

	@Test
	public void testGet() throws Exception {
		List<SensorDataRecord> records = sensorDataRecordsDao.get(TEST_SENSOR_ID, null, DateUtils.nowDateUTC());		
		Assert.assertTrue(records.size() > 1);
		for (SensorDataRecord record : records) {
			Assert.assertEquals(TEST_SENSOR_ID, record.getSensorId());
		}
	}
	
	@Test
	public void testInsert() throws Exception {
		List<SensorDataRecord> list = new ArrayList<SensorDataRecord>();
		for (int i = 0; i < 20; i++) {
			SensorDataRecord record = new SensorDataRecord();
			record.setSensorId("fakeSensorId");
			record.setTimestamp(DateUtils.nowDateUTC());
			record.setValue(String.valueOf(i));
			list.add(record);
			// sleep for a short while
			Thread.sleep(100);
		}
		sensorDataRecordsDao.insertDataRecords(list);
	}
	
	@Test
	public void testDelete() {
		
	}

}
