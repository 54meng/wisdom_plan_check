package com.wpc.utils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.User;
import com.wpc.webapp.controller.vo.WYunResp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WYunApi {
	private static String appKey = "4d68ad9e539990bb8263e26c557e2c63";
	private static String appSecret = "0559c4c0aab6";
	private static String wy_server_url = "https://api.netease.im/nimserver/";
	
	public static String doPost(String url, Map<String, String> params){
	       DefaultHttpClient httpClient = new DefaultHttpClient();
	       
	       HttpPost httpPost = new HttpPost(url);
	      
	       String nonce =  "abcd12345";
	       String curTime = String.valueOf((new Date()).getTime() / 1000L);
	       String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

	       // 设置请求的header
	       httpPost.addHeader("AppKey", appKey);
	       httpPost.addHeader("Nonce", nonce);
	       httpPost.addHeader("CurTime", curTime);
	       httpPost.addHeader("CheckSum", checkSum);
	       httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

	       // 设置请求的参数
	       List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	       if(Utils.notEmpty(params)){
	    	   for (Map.Entry<String, String> entry : params.entrySet()) { 
	    		   nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	    		 }
	       }	       
	       try {
	    	   httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

		       // 执行请求
		       HttpResponse response = httpClient.execute(httpPost);
		    
		       return EntityUtils.toString(response.getEntity(), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
	       return null;
	   }

	/**
	 * 获取网易云通信token
	 * @param accid
	 * @return
	 */
	public static String getToken(String accid){
		String url = wy_server_url + "user/create.action";
		Map<String, String> params = new HashMap<String, String>();
		params.put("accid", accid);
		String result = doPost(url, params);
		if(Utils.notEmpty(result)){
			Gson gson = new Gson();
			WYunResp resp = gson.fromJson(result, WYunResp.class);
			if(null != resp){				
				return resp.getInfo().getToken();
			}
		}
		return null;
	}
	
	/**
	 * 更新用户信息
	 * @param accid
	 * @return
	 */
	public static int updateUinfo(Map<String, String> params){
		String url = wy_server_url + "user/updateUinfo.action";
		String result = doPost(url, params);
		if(Utils.notEmpty(result)){
			Gson gson = new Gson();
			WYunResp resp = gson.fromJson(result, WYunResp.class);
			if(null != resp){				
				return resp.getCode();
			}
		}
		return 500;
	}
	
	/**
	 * 创建群
	 * @param params
	 * @return
	 */
	public static WYunResp createTeam(Map<String, String> params){
		String url = wy_server_url + "team/create.action";
		String result = doPost(url, params);
		if(Utils.notEmpty(result)){
			Gson gson = new Gson();
			WYunResp resp = gson.fromJson(result, WYunResp.class);
			return resp;
		}
		return null;
	}
	
	/**
	 * 更新群信息
	 * @param params
	 * @return
	 */
	public static WYunResp updateTeam(Map<String, String> params){
		String url = wy_server_url + "team/update.action";
		String result = doPost(url, params);
		if(Utils.notEmpty(result)){
			Gson gson = new Gson();
			WYunResp resp = gson.fromJson(result, WYunResp.class);
			return resp;
		}
		return null;
	}
	
	/**
	 * 解散群
	 * @param params
	 * @return
	 */
	public static int removeTeam(Map<String, String> params){
		String url = wy_server_url + "team/remove.action";
		String result = doPost(url, params);
		if(Utils.notEmpty(result)){
			Gson gson = new Gson();
			WYunResp resp = gson.fromJson(result, WYunResp.class);
			if(null != resp){				
				return resp.getCode();
			}
		}
		return 500;
	}
	
	/**
	 * 添加成员
	 * @param params
	 * @return
	 */
	public static int addMembers(Map<String, String> params){
		String url = wy_server_url + "team/add.action";
		String result = doPost(url, params);
		if(Utils.notEmpty(result)){
			Gson gson = new Gson();
			WYunResp resp = gson.fromJson(result, WYunResp.class);
			if(null != resp){				
				return resp.getCode();
			}
		}
		return 500;
	}
	
	/**
	 * 移除成员
	 * @param params
	 * @return
	 */
	public static int removeMembers(Map<String, String> params){
		String url = wy_server_url + "team/kick.action";
		String result = doPost(url, params);
		if(Utils.notEmpty(result)){
			Gson gson = new Gson();
			WYunResp resp = gson.fromJson(result, WYunResp.class);
			if(null != resp){				
				return resp.getCode();
			}
		}
		return 500;
	}
	
	/**
	 * 添加管理员
	 * @param params
	 * @return
	 */
	public static int addManagers(Map<String, String> params){
		String url = wy_server_url + "team/addManager.action";
		String result = doPost(url, params);
		if(Utils.notEmpty(result)){
			Gson gson = new Gson();
			WYunResp resp = gson.fromJson(result, WYunResp.class);
			if(null != resp){				
				return resp.getCode();
			}
		}
		return 500;
	}
	
	/**
	 * 移除管理员
	 * @param params
	 * @return
	 */
	public static int removeManagers(Map<String, String> params){
		String url = wy_server_url + "team/removeManager.action";
		String result = doPost(url, params);
		if(Utils.notEmpty(result)){
			Gson gson = new Gson();
			WYunResp resp = gson.fromJson(result, WYunResp.class);
			if(null != resp){				
				return resp.getCode();
			}
		}
		return 500;
	}
	
	public static void main(String[] args) {
		String url = wy_server_url + "team/addManager.action";
		Map<String, String> params = new HashMap<String, String>();
		JSONArray jm = new JSONArray();
		jm.add("00000004df2cabe5");
		
		params.put("tid", "1544031476");
		params.put("owner", "00000004df2cabe5");
		String result = doPost(url, params);
		System.out.print(result);
	}
}
