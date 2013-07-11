package com.zeedoo.mars.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Simple POJO that represents a cross-device Message
 * @author nzhu
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "messageType", visible = true)
@JsonSubTypes({  
    @Type(value = InstantSensorDataSyncMessage.class, name = "insant_sensor_data_sync"),  
    @Type(value = ResponseTimedSensorDataSyncMessage.class, name = "response_timed_sensor_data_sync") })
public class Message {

	protected String source;
	
	protected String sourceId;
	
	protected Long timestamp;

	protected MessageType messageType;
	
	protected Object payload;
	
	protected String errorMessage;
		
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public Object getPayload() {
		return payload;
	}

	/** Jackson deserializes this field to a JsonNode **/
	public void setPayload(JsonNode payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "Message [source=" + source + ", sourceId=" + sourceId
				+ ", timestamp=" + timestamp + ", messageType=" + messageType
				+ ", payload=" + payload + ", errorMessage=" + errorMessage
				+ "]";
	}
}
