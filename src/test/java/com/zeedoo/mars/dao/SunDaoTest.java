package com.zeedoo.mars.dao;

import java.util.List;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.Sun;

public class SunDaoTest extends EntityDaoTest {
	
	private SunDao sunDao;

	public SunDaoTest() throws Exception {
		super();
		sunDao = new SunDao();
		sunDao.setCoreApiClient(coreApiClient);
	}
	
	@Test
	@Ignore
	public void addSunsToTestDb() {
		String macAddr = "3A-2B-1C-DD-";
		for (int i = 0; i < 50; i++) {
			//Sun sun = new Sun();
			String fullAddr = String.format(macAddr + "%02X-%02X", i, i);
			Sun sun = sunDao.getSunByMacAddress(fullAddr);
			//sun.setSunId("Sun" + (i+1));
			//sun.setMacAddress(fullAddr);
			//sun.setStatus(DeviceStatus.ONLINE);
			//sun.setLocation("Shanghai");
			double randLatitude = 31.191859 - new Random().nextDouble() * 0.5d;
			double randLongitude = 121.457687 - new Random().nextDouble() * 0.5d;
			sun.setLatitude(randLatitude);
			sun.setLongitude(randLongitude);
			sunDao.update(sun);
			//sunDao.insert(sun);
			//System.out.println(sun.toString());
		}
	}

}
