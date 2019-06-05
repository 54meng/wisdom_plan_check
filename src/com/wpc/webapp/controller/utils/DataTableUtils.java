package com.wpc.webapp.controller.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wpc.utils.thread.ThreadLocalFactory;
import com.wpc.webapp.controller.vo.DTRequest;


/**
 * JQuery DataTable 工具类
 * 
 * @author imlzw
 *
 */
public class DataTableUtils {

	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	/**
	 * 获取从DataTable中获取的参数
	 * 
	 * @return
	 */
	public static DTRequest getDTRequest(){
		return new DTRequest(ThreadLocalFactory.getThreadLocalRequest());
	}
	
	/*public static DTRequest getDTRequest(StringBuilder timeQuery){
		return new DTRequest(ThreadLocalFactory.getThreadLocalRequest(), timeQuery);
	}*/
	
	/**
	 * 对象转json数据
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object obj) throws Exception{
		if(obj!=null){
			return gson.toJson(obj);
		}
		return null;
	}
}
