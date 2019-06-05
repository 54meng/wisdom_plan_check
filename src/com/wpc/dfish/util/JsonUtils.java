package com.wpc.dfish.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * JSON工具类
 * 
 * <p>Title: 榕基RJ-CMSV7.X</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2009-2011</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author LZW
 * @version 1.0.0
 * @since	1.0.0	LZW		2012-07-30
 */
public class JsonUtils {

	private static Gson gsonAnnotation =new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private static Gson gson = new Gson();
	
	/**
	 * 将对象转化成json格式的字符串
	 * @param src 要转化的对象
	 * @param useAnnotation true为使用注解
	 * @return
	 */
	public static String toJson(Object src, boolean useAnnotation){
		if(useAnnotation)
			return gsonAnnotation.toJson(src);
		return gson.toJson(src);
	}
	
	/**
	 * 将json格式的字符串转化成对象
	 * @param <T>
	 * @param json 要转化的json字符串
	 * @param classOfT 生成对象的class
	 * @param useAnnotation true为使用注解
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> classOfT, boolean useAnnotation){
		if(useAnnotation)
			return gsonAnnotation.fromJson(json, classOfT);
		return gson.fromJson(json, classOfT);
	}
	
	/**
	 * 将json格式的字符串转化成对象，使用typeOfT时请注意存在缓存如果有修改需重启服务
	 * @param <T>
	 * @param json 要转化的json字符串
	 * @param typeOfT 生成对象的类型  示例：new TypeToken&ltMap&ltString, String&gt&gt(){}.getType()
	 * @param useAnnotation true为使用注解
	 * @return
	 */
	public static <T> T fromJson(String json, Type typeOfT, boolean useAnnotation){
		if(useAnnotation)
			return gsonAnnotation.fromJson(json, typeOfT);
		return gson.fromJson(json, typeOfT);
	}
}
