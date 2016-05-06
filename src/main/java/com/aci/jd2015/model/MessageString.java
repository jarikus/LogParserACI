package com.aci.jd2015.model;

public class MessageString {
	
	private String string;
	private MessageStringType type;
	
	public MessageString (MessageStringType type, String string) {
		this.type = type;
		this.string = string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MessageString)) {
			return false;
		}
		MessageString other = (MessageString) obj;
		if (string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!string.equals(other.string)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return string;
	}

	public String getString() {
		return string;
	}

	public MessageStringType getType() {
		return type;
	}
	
	
}
