package com.wpc.persistence;

import java.util.Date;

public class Follow {
	private String id;
	private String userIdFrom;
	private String userIdTo;
	private Date create_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserIdFrom() {
		return userIdFrom;
	}
	public void setUserIdFrom(String userIdFrom) {
		this.userIdFrom = userIdFrom;
	}
	public String getUserIdTo() {
		return userIdTo;
	}
	public void setUserIdTo(String userIdTo) {
		this.userIdTo = userIdTo;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
