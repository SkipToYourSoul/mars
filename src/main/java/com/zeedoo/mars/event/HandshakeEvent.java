package com.zeedoo.mars.event;

import com.google.common.base.Objects;

public final class HandshakeEvent {
	
	private HandshakeState state;
	
	public HandshakeEvent(HandshakeState state) {
		this.state = state;
	}

	public HandshakeState getState() {
		return state;
	}

	public void setState(HandshakeState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(HandshakeEvent.class).add("state", state).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HandshakeEvent other = (HandshakeEvent) obj;
		return Objects.equal(state, other.state);
	}	
}
