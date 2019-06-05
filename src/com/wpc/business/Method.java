package com.wpc.business;


public class Method {
	static Method instance = null;
	/**
	 * 取得默认构造函数的实例
	 * 
	 * @return
	 */
	public static Method getInstance() {
		if (instance == null) {
			synchronized (Method.class) {
				if (instance == null) {
					instance = new Method();
				}
			}
		}
		return instance;
	}
}
