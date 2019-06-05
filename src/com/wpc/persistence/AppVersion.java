package com.wpc.persistence;

public class AppVersion {
	private String id;
	private String androidVersion;
	private String iosVersion;
	private String androidUpdContent;
	private String iosUpdContent;
	private String androidDownloadUrl;
	private String iosDownloadUrl;
	private String apkUrl;
	private String ipaUrl;
	private int iosIsForce; //1：强制更新，0：不强制更新
	private int androidIsForce;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAndroidVersion() {
		return androidVersion;
	}
	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}
	public String getIosVersion() {
		return iosVersion;
	}
	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}
	
	public String getAndroidDownloadUrl() {
		return androidDownloadUrl;
	}
	public void setAndroidDownloadUrl(String androidDownloadUrl) {
		this.androidDownloadUrl = androidDownloadUrl;
	}
	public String getIosDownloadUrl() {
		return iosDownloadUrl;
	}
	public void setIosDownloadUrl(String iosDownloadUrl) {
		this.iosDownloadUrl = iosDownloadUrl;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public String getIpaUrl() {
		return ipaUrl;
	}
	public void setIpaUrl(String ipaUrl) {
		this.ipaUrl = ipaUrl;
	}
	public String getAndroidUpdContent() {
		return androidUpdContent;
	}
	public void setAndroidUpdContent(String androidUpdContent) {
		this.androidUpdContent = androidUpdContent;
	}
	public String getIosUpdContent() {
		return iosUpdContent;
	}
	public void setIosUpdContent(String iosUpdContent) {
		this.iosUpdContent = iosUpdContent;
	}
	public int getIosIsForce() {
		return iosIsForce;
	}
	public void setIosIsForce(int iosIsForce) {
		this.iosIsForce = iosIsForce;
	}
	public int getAndroidIsForce() {
		return androidIsForce;
	}
	public void setAndroidIsForce(int androidIsForce) {
		this.androidIsForce = androidIsForce;
	}
}
