package com.wpc.business;

import org.apache.commons.lang.StringUtils;

import com.wpc.service.ServiceLocator;


public class TravelMethod extends Method{
	
	static TravelMethod instance = null;
	/**
	 * 取得默认构造函数的实例
	 * 
	 * @return
	 */
	public static TravelMethod getInstance() {
		if (instance == null) {
			synchronized (TravelMethod.class) {
				if (instance == null) {
					instance = new TravelMethod();
				}
			}
		}
		return instance;
	}
	
}
