package com.zeedoo.mars.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

/**
 * Simple POJO that represents a cross-device Message
 * @author nzhu
 *
 */
public class Message {
	
	/**
	 * Indicates the unique identifier of this message (32 digit UUID with dashes)
	 */
	private String id;

	/**
	 * Indicates the source (sender) of this message
	 */
	private String source;
	
	/**
	 * Identifier of the source
	 */
	private String sourceId;
	
	/**
	 * Timestamp of when this message was originated
	 */
	private Long timestamp;

	/**
	 * Type of the message
	 */
	protected MessageType messageType;
	
	/**
	 * Payload of the message (if applicable)
	 */
	private Object payload;
	
	/**
	 * Response code (if applicable)
	 */
	private String responseCode;
	
	/**
	 * Error Message (if applicable)
	 */
	private String errorMessage;
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
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
	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
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
	
	/** 
	 * Use this method to get the raw JSON payload for deserialization 
	 * Won't be used during serialization
	 **/
	@JsonIgnore
	public String getPayloadAsRawJson() {
		return payload == null ? null : payload.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, source, sourceId, messageType, payload, responseCode, errorMessage);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		return Objects.equal(this.id, other.id) && Objects.equal(this.source, other.source) && Objects.equal(this.sourceId, other.sourceId) && Objects.equal(this.timestamp, other.timestamp)
				&& Objects.equal(this.messageType, other.messageType) && Objects.equal(this.payload, other.payload) && Objects.equal(this.responseCode, other.responseCode)
				&& Objects.equal(this.errorMessage, other.errorMessage);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(Message.class).add("id", id).add("source", source).add("sourceId", sourceId).add("timestamp", timestamp)
				.add("messageType", messageType).add("payload", payload).add("responseCode", responseCode).add("errorMessage", errorMessage).toString();
	}
}
