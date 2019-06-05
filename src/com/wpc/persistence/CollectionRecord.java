package com.wpc.persistence;

import java.util.Date;

import com.wpc.dfish.util.Utils;

public class CollectionRecord {
	private String id;
	private String collectionId;
	private String collectionName;
	private String collectionBatch;
	private String cardNum;//卡号
	private String accountNumber;//帐户号	
	private String certificateNum;//证件号码
	private String userId;
	private String userName;
	private String content;
	private Date createTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCollectionName() {
		return Utils.isEmpty(collectionName) ? " " : collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public String getCollectionBatch() {
		return collectionBatch;
	}
	public void setCollectionBatch(String collectionBatch) {
		this.collectionBatch = collectionBatch;
	}
	public String getCardNum() {
		return Utils.isEmpty(cardNum) ? " " : cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getAccountNumber() {
		return Utils.isEmpty(accountNumber) ? " " : accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCertificateNum() {
		return Utils.isEmpty(certificateNum) ? " " : certificateNum;
	}
	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
}
