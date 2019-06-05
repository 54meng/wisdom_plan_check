package com.wpc.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.wpc.dfish.util.Utils;
import com.wpc.utils.FileTypeUtils;
import com.wpc.utils.SystemEnvInfo;

/**
 * 用户对象
 * 
 * @author whp
 *
 */
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String userid;//用户ID
	private String username;
	private String phone;
	private String nickname;
	private String avatar_url;
	private String remark_name;
	private String sex;
	private String sign;
	private String city;
	private String birthday;
	private String company;
	private String idcard;
	private String allowLogin;
	@JSONField(serialize=false)  
	private String password;
	@JSONField(serialize=false)
	private String userType;
	@JSONField(serialize=false)  
	private String userStatus;
	@JSONField(serialize=false)
	private String cookieCode;	
	@JSONField(serialize=false)
	private String accesstoken;	
	
	private String userRole; //1管理员，2分管经理，3采购人员，4辅助审批专员

	private Date create_time;
	private Date update_time;
	
	public static String STATUS_ENABLE="1";//可用
	public static String STATUS_FORBID="0";//禁用
	
	/**
	 * 获取用户状态名称
	 * @param userStatus
	 * @return
	 */
	public static String getUserStatusName(String userStatus){
		if(STATUS_ENABLE.equals(userStatus)){
			return "启用";
		}else if(STATUS_FORBID.equals(userStatus)){
			return "禁用";
		}else{
			return "----";
		}
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}


	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar_url() {
		/*if(Utils.notEmpty(avatar_url)){
			return SystemEnvInfo.getBasePath()+avatar_url.replace(File.separator, "/")+"?nocache="+(new Date().getTime());
		}*/
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public String getRemark_name() {
		return remark_name;
	}

	public void setRemark_name(String remark_name) {
		this.remark_name = remark_name;
	}

	public String getSex() {
		return Utils.isEmpty(sex) ? "0" : sex;
	}
	
	public String getAllowLoginText() {
		return Utils.notEmpty(allowLogin) ? ("1".equals(allowLogin) ? "是" : ("0".equals(allowLogin) ? "否" : "未知")) : "未知" ;
	}
	
	public String getSexText() {
		return Utils.notEmpty(sex) ? ("1".equals(sex) ? "男" : ("2".equals(sex) ? "女" : "未知")) : "未知" ;
	}
	
	public String getUserStatusText() {
		return Utils.notEmpty(userStatus) ? ("1".equals(userStatus) ? "启用" : ("0".equals(userStatus) ? "禁用" : "未知")) : "未知" ;
	}
	
	public String getUserRoleText() {
		if(Utils.notEmpty(userRole)){
			if("1".equals(userRole)){
				return "管理员";
			}else if("2".equals(userRole)){
				return "分管经理";
			}else if("3".equals(userRole)){
				return "采购人员";
			}else if("4".equals(userRole)){
				return "辅助审批专员";
			}
		}
		return "";
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getCookieCode() {
		return cookieCode;
	}

	public void setCookieCode(String cookieCode) {
		this.cookieCode = cookieCode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	
	public String getAllowLogin() {
		return allowLogin;
	}

	public void setAllowLogin(String allowLogin) {
		this.allowLogin = allowLogin;
	}
}