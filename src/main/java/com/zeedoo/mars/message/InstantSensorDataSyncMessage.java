package com.zeedoo.mars.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.zeedoo.mars.message.json.JsonSensorDataRecord;

public class InstantSensorDataSyncMessage extends Message {
	
	private List<JsonSensorDataRecord> payload;
	
	public InstantSensorDataSyncMessage() {
		
	}
	
	public List<JsonSensorDataRecord> getPayload() {
		return payload;
	}

	public void setPayload(List<JsonSensorDataRecord> payload) {
		this.payload = payload;
	}
	
	public InstantSensorDataSyncMessage(List<JsonSensorDataRecord> payload) {
		this.payload = payload;
	}
}
