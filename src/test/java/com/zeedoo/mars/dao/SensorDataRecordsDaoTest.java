package com.zeedoo.mars.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.mars.dao.SensorDataRecordsDao;
import com.zeedoo.mars.utils.DateUtils;

public class SensorDataRecordsDaoTest extends EntityDaoTest {
	
	private SensorDataRecordsDao sensorDataRecordsDao;
	
	private static final String TEST_SENSOR_ID = "fakeSensorId";

	public SensorDataRecordsDaoTest() throws Exception {
		super();
		sensorDataRecordsDao = new SensorDataRecordsDao();
		sensorDataRecordsDao.setCoreApiClient(coreApiClient);
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
		DateTime baseDateTime = DateUtils.nowDateUTC();
		for (int i = 0; i < 100; i++) {
			SensorDataRecord record = new SensorDataRecord();
			record.setSensorId("fakeSensorId");
			record.setTimestampLong(baseDateTime.plusMinutes(i).getMillis());
			record.setValue("" + new Random().nextInt(100));
			list.add(record);
		}
		sensorDataRecordsDao.insertDataRecords(list);
	}
	
	@Test
	public void testDelete() {	
	}
	
	@Test
	@Ignore
	public void testPopulateData() throws Exception {
		List<SensorDataRecord> list = new ArrayList<SensorDataRecord>();
		String sensor = "Sensor24";
		DateTime baseDateTime = DateUtils.nowDateUTC();
		for (int i = 0; i < 100; i++) {
			SensorDataRecord record = new SensorDataRecord();
			record.setSensorId(sensor);
			record.setTimestampLong(baseDateTime.plusMinutes(i).getMillis());
			//double randLatitude = 31.191859 + new Random().nextDouble();
			//double randLongitude = 121.457687 + new Random().nextDouble();
			//record.setValue("" + randLatitude + "," + randLongitude);
			//record.setValue(getRandomDouble(1,100));
			record.setValue(getRandomInteger(50,100));
			//record.setValue(i + getRandomInteger(0,2));
			list.add(record);
			//System.out.println(record.toString());
		}
		sensorDataRecordsDao.insertDataRecords(list);
	}
	
	public String getRandomInteger(int low, int high) {
		Random r = new Random();
		int num = low + (int)(Math.random() * ((high - low) + 1));
		return num + "";
	}
	
	public String getRandomDouble(double low, double high) {
		Random r = new Random();
		double num = low + (Math.random() * (high - low) + 1);
		return num + "";
	}

}
