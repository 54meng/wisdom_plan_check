package com.wpc.utils;

import java.util.Map;
import java.util.TreeMap;

public class Constant {
	public static final int[] IMAGE_SMALL_SPEC = new int[]{80,80};
	public static final int[] IMAGE_LARGE_SPEC = new int[]{600,600};
	public static final int[] IMAGE_HD_SPEC = new int[]{300,300};
	
	public static Map<String, String> getTargetTypes(){
		Map<String, String> types = new TreeMap<String, String>();
		types.put("1", "景点");
		types.put("2", "美食");
		types.put("3", "购物地点");
		types.put("4", "攻略");
		types.put("5", "web地址");
		return types;
	}
	
	public static Map<String, String> getRaidersTags(){
		Map<String, String> tags = new TreeMap<String, String>();
		tags.put("1", "热门");
		tags.put("2", "精华");
		tags.put("3", "最新");
		return tags;
	}
	
	public static Map<String, String> getPagePositions(){
		Map<String, String> ms = new TreeMap<String, String>();
		ms.put("1", "头部");
		ms.put("2", "中间推荐");
		ms.put("3", "底部");
		ms.put("4", "攻略轮播");
		return ms;
	}
	
	public static Map<String, String> getOrderTypes(){
		Map<String, String> ms = new TreeMap<String, String>();
		ms.put("1", "导游");
		ms.put("2", "用车");
		return ms;
	}
	
	public static Map<String, String> getGenders(){
		Map<String, String> ms = new TreeMap<String, String>();
		ms.put("0", "男");
		ms.put("1", "女");
		return ms;
	}
}
