package com.wpc.persistence;

public class CiticEnt {
	private String id;
	//信用卡信息
	private String entrustBatch;//委托批次
	private String entrustCode;//委托代码
	private String applyNum;//申请号	
	private String accountNumber;//帐户号	
	private String cardNum;//卡号
	private String cardType;//卡类
	private String primarySecondCard;//主副卡
	private String primaryCardholderCode;//主卡持卡人代码
	private String balance;//余额
	private String openAccountDate;//开户日期	
	private String billDay;//帐单日
	private String principalRmb;//本金
	private String creditLimit;//信用额
	private String belongBank;//所属银行
	private String lastPayDate;//最后缴款日期
	private String lastPayMoney;//最后缴款金额
	private String lastRetailDate;//最后零售日期
	private String lastBillDate;//最后发单日期
	private String lastCashDate;//最后提现日期
	private String overdueStateDesc;//逾期状态
	private String remarks;//备注
	//持卡人信息
	private String cardholderName;//持卡人姓名
	private String cardholderMobilePhone;//持卡人手机
	private String sex;//性别
	private String email;//邮箱
	private String certificateNum;//证件号码
	private String cardholderCompanyName;//持卡人单位名称
	private String cardholderCompanyPhone;//持卡人单位电话
	private String cardholderCompanyAddress;//持卡人单位地址
	private String cardholderJob;//持卡人职务
	private String cardholderHomeAddress;//持卡人家庭地址
	private String cardholderHomePhone;//持卡人家庭电话
	private String cardholderPostalAddress;//持卡人通讯地址
	private String cardholderZipCode;//持卡人邮编
	
	private String province;//省份
	private String city;//城市
	private String cityChange;//城市调整
	
	private String policeAddress;//公安户籍地址
	private String servicePlace;//服务住所
	private String jurisdictionPolice;//管辖派出所
	//联系人信息
	private String contactsName;//联系人姓名
	private String contactsMobilePhone;//联系人手机
	private String contactsHomePhone;//联系人家庭电话
	private String contactsHomeAddress;//联系人家庭地址
	private String contactsCompanyName;//联系人单位名称
	private String contactsCompanyPhone;//联系人单位电话
	private String contactsCompanyAddress;//联系人单位地址	
	
	//亲属信息
	private String relativesName;//亲属姓名
	private String relativesMobilePhone;//亲属手机	
	private String relationship;//亲属关系
	private String relativesHomePhone;//亲属家电	
	private String relativesOfficePhone;//亲属办公电话
	
	private String operUserId;
	private String operUserName;
	private String belongAdmin;//所属管理员
	private String isDel;//1删除
	private String delDate;
	private String operStatus;//1进行中，2已完结
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEntrustBatch() {
		return entrustBatch;
	}
	public void setEntrustBatch(String entrustBatch) {
		this.entrustBatch = entrustBatch;
	}

	public String getEntrustCode() {
		return entrustCode;
	}
	public void setEntrustCode(String entrustCode) {
		this.entrustCode = entrustCode;
	}
	public String getApplyNum() {
		return applyNum;
	}
	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCardholderName() {
		return cardholderName;
	}
	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}
	public String getLastBillDate() {
		return lastBillDate;
	}
	public void setLastBillDate(String lastBillDate) {
		this.lastBillDate = lastBillDate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getPrimarySecondCard() {
		return primarySecondCard;
	}
	public void setPrimarySecondCard(String primarySecondCard) {
		this.primarySecondCard = primarySecondCard;
	}
	public String getOverdueStateDesc() {
		return overdueStateDesc;
	}
	public void setOverdueStateDesc(String overdueStateDesc) {
		this.overdueStateDesc = overdueStateDesc;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityChange() {
		return cityChange;
	}
	public void setCityChange(String cityChange) {
		this.cityChange = cityChange;
	}
	public String getOpenAccountDate() {
		return openAccountDate;
	}
	public void setOpenAccountDate(String openAccountDate) {
		this.openAccountDate = openAccountDate;
	}
	public String getCertificateNum() {
		return certificateNum;
	}
	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
	public String getCardholderCompanyName() {
		return cardholderCompanyName;
	}
	public void setCardholderCompanyName(String cardholderCompanyName) {
		this.cardholderCompanyName = cardholderCompanyName;
	}
	public String getCardholderCompanyAddress() {
		return cardholderCompanyAddress;
	}
	public void setCardholderCompanyAddress(String cardholderCompanyAddress) {
		this.cardholderCompanyAddress = cardholderCompanyAddress;
	}
	public String getCardholderJob() {
		return cardholderJob;
	}
	public void setCardholderJob(String cardholderJob) {
		this.cardholderJob = cardholderJob;
	}
	public String getCardholderHomeAddress() {
		return cardholderHomeAddress;
	}
	public void setCardholderHomeAddress(String cardholderHomeAddress) {
		this.cardholderHomeAddress = cardholderHomeAddress;
	}
	public String getCardholderCompanyPhone() {
		return cardholderCompanyPhone;
	}
	public void setCardholderCompanyPhone(String cardholderCompanyPhone) {
		this.cardholderCompanyPhone = cardholderCompanyPhone;
	}
	public String getCardholderHomePhone() {
		return cardholderHomePhone;
	}
	public void setCardholderHomePhone(String cardholderHomePhone) {
		this.cardholderHomePhone = cardholderHomePhone;
	}
	public String getCardholderMobilePhone() {
		return cardholderMobilePhone;
	}
	public void setCardholderMobilePhone(String cardholderMobilePhone) {
		this.cardholderMobilePhone = cardholderMobilePhone;
	}
	public String getContactsName() {
		return contactsName;
	}
	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	public String getContactsCompanyName() {
		return contactsCompanyName;
	}
	public void setContactsCompanyName(String contactsCompanyName) {
		this.contactsCompanyName = contactsCompanyName;
	}
	public String getContactsCompanyAddress() {
		return contactsCompanyAddress;
	}
	public void setContactsCompanyAddress(String contactsCompanyAddress) {
		this.contactsCompanyAddress = contactsCompanyAddress;
	}
	public String getContactsHomeAddress() {
		return contactsHomeAddress;
	}
	public void setContactsHomeAddress(String contactsHomeAddress) {
		this.contactsHomeAddress = contactsHomeAddress;
	}
	public String getContactsCompanyPhone() {
		return contactsCompanyPhone;
	}
	public void setContactsCompanyPhone(String contactsCompanyPhone) {
		this.contactsCompanyPhone = contactsCompanyPhone;
	}
	public String getContactsHomePhone() {
		return contactsHomePhone;
	}
	public void setContactsHomePhone(String contactsHomePhone) {
		this.contactsHomePhone = contactsHomePhone;
	}
	public String getContactsMobilePhone() {
		return contactsMobilePhone;
	}
	public void setContactsMobilePhone(String contactsMobilePhone) {
		this.contactsMobilePhone = contactsMobilePhone;
	}
	public String getRelativesName() {
		return relativesName;
	}
	public void setRelativesName(String relativesName) {
		this.relativesName = relativesName;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getRelativesOfficePhone() {
		return relativesOfficePhone;
	}
	public void setRelativesOfficePhone(String relativesOfficePhone) {
		this.relativesOfficePhone = relativesOfficePhone;
	}
	public String getRelativesHomePhone() {
		return relativesHomePhone;
	}
	public void setRelativesHomePhone(String relativesHomePhone) {
		this.relativesHomePhone = relativesHomePhone;
	}
	public String getRelativesMobilePhone() {
		return relativesMobilePhone;
	}
	public void setRelativesMobilePhone(String relativesMobilePhone) {
		this.relativesMobilePhone = relativesMobilePhone;
	}
	public String getCardholderPostalAddress() {
		return cardholderPostalAddress;
	}
	public void setCardholderPostalAddress(String cardholderPostalAddress) {
		this.cardholderPostalAddress = cardholderPostalAddress;
	}
	public String getCardholderZipCode() {
		return cardholderZipCode;
	}
	public void setCardholderZipCode(String cardholderZipCode) {
		this.cardholderZipCode = cardholderZipCode;
	}

	public String getLastPayDate() {
		return lastPayDate;
	}
	public void setLastPayDate(String lastPayDate) {
		this.lastPayDate = lastPayDate;
	}
	public String getLastPayMoney() {
		return lastPayMoney;
	}
	public void setLastPayMoney(String lastPayMoney) {
		this.lastPayMoney = lastPayMoney;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getLastRetailDate() {
		return lastRetailDate;
	}
	public void setLastRetailDate(String lastRetailDate) {
		this.lastRetailDate = lastRetailDate;
	}
	public String getLastCashDate() {
		return lastCashDate;
	}
	public void setLastCashDate(String lastCashDate) {
		this.lastCashDate = lastCashDate;
	}
	public String getPrimaryCardholderCode() {
		return primaryCardholderCode;
	}
	public void setPrimaryCardholderCode(String primaryCardholderCode) {
		this.primaryCardholderCode = primaryCardholderCode;
	}
	public String getBillDay() {
		return billDay;
	}
	public void setBillDay(String billDay) {
		this.billDay = billDay;
	}
	public String getPrincipalRmb() {
		return principalRmb;
	}
	public void setPrincipalRmb(String principalRmb) {
		this.principalRmb = principalRmb;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getPoliceAddress() {
		return policeAddress;
	}
	public void setPoliceAddress(String policeAddress) {
		this.policeAddress = policeAddress;
	}
	public String getServicePlace() {
		return servicePlace;
	}
	public void setServicePlace(String servicePlace) {
		this.servicePlace = servicePlace;
	}
	public String getJurisdictionPolice() {
		return jurisdictionPolice;
	}
	public void setJurisdictionPolice(String jurisdictionPolice) {
		this.jurisdictionPolice = jurisdictionPolice;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBelongBank() {
		return belongBank;
	}
	public void setBelongBank(String belongBank) {
		this.belongBank = belongBank;
	}
	public String getBelongAdmin() {
		return belongAdmin;
	}
	public void setBelongAdmin(String belongAdmin) {
		this.belongAdmin = belongAdmin;
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	public String getOperUserId() {
		return operUserId;
	}
	public void setOperUserId(String operUserId) {
		this.operUserId = operUserId;
	}
	public String getOperUserName() {
		return operUserName;
	}
	public void setOperUserName(String operUserName) {
		this.operUserName = operUserName;
	}
	public String getOperStatus() {
		return operStatus;
	}
	public void setOperStatus(String operStatus) {
		this.operStatus = operStatus;
	}
	public String getDelDate() {
		return delDate;
	}
	public void setDelDate(String delDate) {
		this.delDate = delDate;
	}
}
