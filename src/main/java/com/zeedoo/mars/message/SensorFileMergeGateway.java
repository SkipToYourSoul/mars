package com.zeedoo.mars.message;

import com.zeedoo.mars.event.SensorFileMergeEvent;

public interface SensorFileMergeGateway {
	
	public void sendSensorFileMergeEvent(SensorFileMergeEvent sensorDataFile);

}
