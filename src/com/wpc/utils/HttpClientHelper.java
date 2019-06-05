package com.wpc.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wpc.dfish.util.Utils;
import com.wpc.utils.string.StringUtils;

/**
 * 
 * @author cx
 * 
 */
public class HttpClientHelper {
	public static int i = 0;
	public static List<Object[]> list = new ArrayList<Object[]>();
	public static final String HTTP_GET ="GET";
	public static final String HTTP_POST ="POST";

	public static Map<String, Object[]> map = Collections.synchronizedMap(new HashMap<String, Object[]>());
	
	static HttpClient client = new DefaultHttpClient();
	static{
		PoolingClientConnectionManager manage = new PoolingClientConnectionManager();
		//设置每个路由的最大连接数,即对同一个路由地址同时最多支持几个请求处理
		manage.setDefaultMaxPerRoute(10);
		//设置连接池最大连接数，即最大同时支持多少个连接
		manage.setMaxTotal(50);
		client = new DefaultHttpClient(manage);
		HttpParams params = client.getParams();
		//设置连接超时(5s)
		HttpConnectionParams.setConnectionTimeout(params,1000*5);
		//设置从连接池获取连接超时时间 (10s)
		HttpClientParams.setConnectionManagerTimeout(params, 1000*10);
		//设置读取超时 (5min)
		HttpConnectionParams.setSoTimeout(params, 1000*60*5);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
	}

	 /**
	   * 只编码URL中的中文字符
	   * 
	   * @param URL 被编码的URL字符串
	   * @param charset 字符集
	   * @return 编码好的URL
	   * @throws UnsupportedEncodingException 不支持的字符集
	   */
	  public static String encodeURL(String URL, String charset){
		String zhPattern = "[\u4e00-\u9fa5]+";
		Pattern p = Pattern.compile(zhPattern);
		Matcher m = p.matcher(URL);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			try {
				m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		m.appendTail(b);
		return b.toString();
	}
	  
	public static String invoke(String url, Map<String, Object> params){
		return invoke(url, params, HTTP_POST);
	}
	  
	public static String invoke(String url, Map<String, Object> params, String mehod){
		HttpGet httpGet = null;
		HttpPost httpPost = null;
		if(url!=null){
			url = encodeURL(url, "UTF-8");
		}
		if (HttpClientHelper.HTTP_GET.equals(mehod)) {
			StringBuilder sbp = new StringBuilder();
			if(null != params){
				int i = 0;
				for(Map.Entry<String, Object> entry : params.entrySet()){
					if(i == 0){
						sbp.append("?").append(entry.getKey()).append("=").append(entry.getValue());
					}else{
						sbp.append("&").append(entry.getKey()).append("=").append(entry.getValue());
					}
					i++;
				}
			}
			url = url + sbp.toString();
			httpGet = new HttpGet(url);
			httpGet.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		} else {
			httpPost = new HttpPost(url);
			if (null != params) {
				Gson gson = new Gson();
				try {
					StringEntity entity = new StringEntity(gson.toJson(params), "UTF-8");//解决中文乱码问题    
	                entity.setContentEncoding("UTF-8");    
	                entity.setContentType("application/json");    
	                httpPost.setEntity(entity);   
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
		}

		String result = null;
		try {
			int status = HttpStatus.SC_NOT_FOUND;
			HttpResponse httpResponse = null;
			HttpEntity entity = null;
			if(httpGet!=null){
				httpResponse = client.execute(httpGet);
			}else if(httpPost!=null){
				httpResponse = client.execute(httpPost);
			}
			if(httpResponse!=null){
				StatusLine statusLine = httpResponse.getStatusLine();
				entity = httpResponse.getEntity();
				if(statusLine!=null){
					status = statusLine.getStatusCode();
				}
			}
			if(HttpStatus.SC_OK ==status  ){
				if(entity!=null){
					result = EntityUtils.toString(entity,Charset.forName("UTF-8"));
					EntityUtils.consumeQuietly(entity);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(httpGet!=null){
				httpGet.abort();
			}
			if(httpPost!=null){
				httpPost.abort();
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://api2.juheapi.com/zteict/order/flightsearch";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fromCity", "SZX");
		params.put("toCity", "CSX");
		params.put("flightDate", "2015-09-02");
		params.put("incShare", "Y");
		//params.put("airline", "CZ");
		params.put("key", "4339d417f3ca31ea9bb197dabe89ab27");
		String result = invoke(url, params, HTTP_GET);
		
		System.out.println(transformResult(result, "200"));
	}
	
	protected static String transformResult(String result, String sucStatus){		
		JSONObject out = new JSONObject();
		try {
			out.put("ResultCode", "error");
			out.put("ResultMessage", "空数据");
		} catch (Exception e) {}
		
		if(Utils.notEmpty(result)){
			try {
				JSONObject object = new JSONObject(result);
				Object ecode = object.get("error_code");
				Object reason = object.get("reason");
				Object rst = object.get("result");
									
				if(sucStatus.equals(ecode.toString())){
					out.put("ResultCode", "0");
				}else{
					out.put("ResultCode", "error");
				}
				out.put("ResultMessage", reason);
				JSONObject data = new JSONObject();
				data.put("list", rst);
				out.put("ResultData", data);
				System.out.println(out.toString());				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return out.toString();
	}
}
