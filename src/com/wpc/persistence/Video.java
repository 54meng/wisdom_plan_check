package com.wpc.persistence;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.wpc.dfish.util.Utils;

/**
 * 视频
 *
 */
public class Video {
	private String id; // 主键ID
	private String name; //名称
	private String intros; //简介
	private String coverUrl1; //封面地址
	private String coverUrl2; //封面地址
	private String videoUrl; //视频地址
	private String size; //视频大小
	private String creator; //创建者
	private String cateId; //分类ID
	private String cateName; //分类名称
	private double score=0.0; //评分
	private int playCount; //播放数
	private int praiseCount; //点赞数
	private String tag; //标签
	private List<Tag> tagList; //标签
	@JSONField(serialize=false)
	private String tagJson;
	private String specialId; //专题
	private String authorId; //作者
	private Date createTime;
	private int isPraise;
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
	public String getIntros() {
		return intros;
	}
	public void setIntros(String intros) {
		this.intros = intros;
	}
	
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCateId() {
		return cateId;
	}
	public void setCateId(String cateId) {
		this.cateId = cateId;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public String getSpecialId() {
		return specialId;
	}
	public void setSpecialId(String specialId) {
		this.specialId = specialId;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getTagJson() {
		return tagJson;
	}
	public void setTagJson(String tagJson) {
		this.tagJson = tagJson;
	}
	public List<Tag> getTagList() {
		return tagList;
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
	public int getIsPraise() {
		return isPraise;
	}
	public void setIsPraise(int isPraise) {
		this.isPraise = isPraise;
	}
	public String getCoverUrl1() {
		return coverUrl1;
	}
	public void setCoverUrl1(String coverUrl1) {
		this.coverUrl1 = coverUrl1;
	}
	public String getCoverUrl2() {
		return coverUrl2;
	}
	public void setCoverUrl2(String coverUrl2) {
		this.coverUrl2 = coverUrl2;
	}

}
