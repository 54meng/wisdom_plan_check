package com.wpc.persistence;

import java.io.File;

import com.wpc.utils.SystemEnvInfo;

/**
 * 单个image的json对象
 * 
 * @author imlzw
 *
 */
public class Image {
	private String orgUrl;//原始图
	private String smallUrl;//小图
	private String largeUrl;//大图
	private String hdUrl;//高清图
	private boolean isConver;//是否为卦图
	private String imageDesc;//图片描述
	public String getOrgUrl() {
		return orgUrl;
	}
	public void setOrgUrl(String orgUrl) {
		this.orgUrl = orgUrl;
	}
	public String getSmallUrl() {
		return smallUrl;
	}
	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}
	public String getLargeUrl() {
		return largeUrl;
	}
	public void setLargeUrl(String largeUrl) {
		this.largeUrl = largeUrl;
	}
	public String getHdUrl() {
		return hdUrl;
	}
	public void setHdUrl(String hdUrl) {
		this.hdUrl = hdUrl;
	}
	public boolean isConver() {
		return isConver;
	}
	public void setConver(boolean isConver) {
		this.isConver = isConver;
	}
	public String getImageDesc() {
		return imageDesc;
	}
	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}
	public Image getFullUrlImage(){
		Image fullUrlImage = new Image();
		fullUrlImage.setOrgUrl(SystemEnvInfo.getBasePath()+this.orgUrl.replace(File.separator, "/"));
		fullUrlImage.setSmallUrl(SystemEnvInfo.getBasePath()+this.smallUrl.replace(File.separator, "/"));
		fullUrlImage.setLargeUrl(SystemEnvInfo.getBasePath()+this.largeUrl.replace(File.separator, "/"));
		fullUrlImage.setHdUrl(SystemEnvInfo.getBasePath()+this.hdUrl.replace(File.separator, "/"));
		return fullUrlImage;
	} 
}
