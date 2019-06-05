package com.wpc.service;

import com.wpc.webapp.controller.vo.CallResult;


/**
 * 短信服务
 * 
 * @author imlzw
 *
 */
public abstract interface SmsService {

	/**
	 * 发送短信
	 * 
	 * @param phoneNum
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public boolean sendMsg(String phoneNum,String content) throws Exception;
	
}
