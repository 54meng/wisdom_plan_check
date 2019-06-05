package com.wpc.persistence;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class Team {
	private String tid;
	private String tname;
	private String owner;
	@JSONField(serialize=false)  
	private String members;
	private String managers;
	private String icon;
	private Date create_time;
	//@JSONField(serialize=false)  
	private String wy_tid;
	@JSONField(serialize=false)  
	private String wy_owner;
	private int msg_ope = 1; // 1：关闭消息提醒，2：打开消息提醒，其他值无效
	private boolean isJoin = false;
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getWy_tid() {
		return wy_tid;
	}
	public void setWy_tid(String wy_tid) {
		this.wy_tid = wy_tid;
	}
	public int getMsg_ope() {
		return msg_ope;
	}
	public void setMsg_ope(int msg_ope) {
		this.msg_ope = msg_ope;
	}
	public String getWy_owner() {
		return wy_owner;
	}
	public void setWy_owner(String wy_owner) {
		this.wy_owner = wy_owner;
	}
	public boolean isJoin() {
		return isJoin;
	}
	public void setJoin(boolean isJoin) {
		this.isJoin = isJoin;
	}
	public String getManagers() {
		return managers;
	}
	public void setManagers(String managers) {
		this.managers = managers;
	}
	
}
