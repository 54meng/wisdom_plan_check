package com.wpc.persistence;

/**
 * 分类
 *
 */
public class Category {
	private String cateId;
	private String cateName;
	private String coverUrl;
	public String getCateId() {
		return cateId;
	}
	public void setCateId(String cateId) {
		this.cateId = cateId;
	}
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public String getCoverUrl() {		
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

}
