package com.zeedoo.mars.message;

import java.util.List;

import com.zeedoo.mars.message.json.JsonSensorDataRecord;

public class ResponseTimedSensorDataSyncMessage extends Message {
	
private List<JsonSensorDataRecord> payload;
	
	public ResponseTimedSensorDataSyncMessage() {
		
	}
	
	public List<JsonSensorDataRecord> getPayload() {
		return payload;
	}

	public void setPayload(List<JsonSensorDataRecord> payload) {
		this.payload = payload;
	}
	
	public ResponseTimedSensorDataSyncMessage(List<JsonSensorDataRecord> payload) {
		this.payload = payload;
	}

}
