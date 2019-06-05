package com.wpc.persistence;

/**
 * 主角
 *
 */
public class Author {
	private String id;
	private String name;
	private String icon;
	private String intros;
	private int videoCount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIntros() {
		return intros;
	}
	public void setIntros(String intros) {
		this.intros = intros;
	}
	public int getVideoCount() {
		return videoCount;
	}
	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}
}
