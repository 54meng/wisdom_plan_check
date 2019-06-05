package com.wpc.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.PostMethod;

import com.rongji.dfish.engines.util.Utils;
import com.wpc.service.SmsService;
import com.wpc.utils.HttpClientUtil;
import com.wpc.utils.SecurityUtils;
import com.wpc.utils.string.StringUtils;


public class SmsServiceImpl implements SmsService {

	public static Map<String,String> tipMap = new HashMap<String, String>();
	static {
		tipMap.put(">0","成功");
		tipMap.put("-1","账号或者密码错误");
		tipMap.put("-2","缺少企业账号");
		tipMap.put("-3","缺少密码");
		tipMap.put("-4","缺少短信内容(彩信标题)");
		tipMap.put("-5","缺少目标号码");
		tipMap.put("-6","缺少产品类型");
		tipMap.put("-7","短信内容过长(小灵通最大56个字)，或彩信标题过长(最大30个字符)");
		tipMap.put("-8","短信内容(彩信标题)含有非法字符，第二行返回非法关键词");
		tipMap.put("-9","目标号码格式错误");
		tipMap.put("-10","超过规定发送时间，禁止提交发送");
		tipMap.put("-11","产品类型错误");
		tipMap.put("-12","余额不足");
		tipMap.put("-14","号码超过发送数量限制");
		tipMap.put("-15","短信内容(彩信标题)前面需加签名，例如【XXX公司】，签名必须放在最前面，如果内容的编码错误，出现乱码，识别不出签名【XXX公司】，也会返回-15");
		tipMap.put("-16","提交号码数量小于最小提交量限制");
		tipMap.put("-17","查询速度过快");
		tipMap.put("-18","tms文件格式错误");
		tipMap.put("-19","无查看回执权限");
		tipMap.put("-20","未开通接口");
		tipMap.put("-21","系统繁忙");
		tipMap.put("-22","短信内容(彩信标题)签名不正确");
		tipMap.put("-99","连接失败");
		tipMap.put("-100","系统内部错误");
	}
	
	
	private String serviceUrl;
	private String username;
	private String password;
	
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean sendMsg(String phoneNum, String content) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", getUsername());
//		params.put("password", getPassword());
		params.put("passwordMd5", SecurityUtils.md5(getPassword(),16));
		params.put("mobile", phoneNum);
		params.put("message", content);
		String post = HttpClientUtil.post(getServiceUrl()+"/sendsms.asp", params, "GBK");
		System.out.println("post:"+post);
		if(Utils.notEmpty(post)){
			String[] lines = post.split("\n");
			if(Utils.notEmpty(lines)&&lines.length>0){
				String code = lines[0];
				if(Long.parseLong(code)>0){
					return true;
				}
				String msg = null;
				if(lines.length>1){
					msg = lines[1]; 
				}
				if(Utils.isEmpty(msg)){
					msg = tipMap.get(code);
				}
				throw new Exception(msg);
			}
		}
		return false;
	}
	public static void main(String[] args) throws Exception {
		System.out.println(SecurityUtils.md5("pkeyunji"));
	}
	
}
