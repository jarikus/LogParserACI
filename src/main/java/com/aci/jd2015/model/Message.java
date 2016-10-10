package com.aci.jd2015.model;

import java.util.Date;

public class Message implements Comparable<Message>{
// кусок кода какой-то
	//еще кусок какого-то кода
	private final Date datetime;
	private final String message;

	public Message(Date datetime, String message) {
		this.datetime = datetime;
		this.message = message;
	}

	public Date getDatetime() {
		return datetime;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public int compareTo(Message message) {
		if (datetime.before(message.getDatetime())) {
			return -1;
		} else if (datetime.after(message.getDatetime())) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datetime == null) ? 0 : datetime.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		if (!(obj instanceof Message)) {
			return false;
		}
		Message other = (Message) obj;
		if (datetime == null) {
			if (other.datetime != null) {
				return false;
			}
		} else if (!datetime.equals(other.datetime)) {
			return false;
		}
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		return true;
	}

}
