package com.wpc.webservice.v1.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rongji.dfish.engines.util.Utils;
import com.rongji.dfish.framework.SystemData;
import com.wpc.dfish.dao.Page;
import com.wpc.utils.string.StringUtils;
import com.wpc.utils.thread.ThreadLocalFactory;
import com.wpc.webservice.v1.vo.CallResult;


/**
 * 基础服务端控件类
 * @author whp
 *
 */
public abstract class ServiceBaseController extends MultiActionController{
	protected final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	protected final static int ERROR = -1;
	protected final static int SUCCESS = 200;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			/*Map<String, String> userAgent1Header = getUserAgent1Header();
			String xRequestWith = userAgent1Header.get("x-requested-with");*/
			if(signValidate()){
				return super.handleRequest(request, response);	
			}else{
				CallResult callResult = new CallResult();
				callResult.setMessage("服务器拒绝请求!");
				responseText(response, getEncryJson(callResult));
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			CallResult callResult = new CallResult();
			callResult.setMessage("服务器内部错误："+ex.getMessage());
			responseText(response, getEncryJson(callResult ));
			return null;
		}
	}
	
	/**
	 * 验证签名
	 * @return
	 */
	public static boolean signValidate(){
		HttpServletRequest request = ThreadLocalFactory.getThreadLocalRequest();
		String sign = request.getHeader("sign");
		String openId = request.getHeader("openId");
		String time = request.getHeader("time");
		
		return true;
	}
	
	/**
	 * 获取头部加密的User-Agent1参数
	 * @return
	 */
	public static Map<String,String> getUserAgent1Header(){
		Map<String,String> result = (Map<String,String>)ThreadLocalFactory.get("request_header");
		if(result==null){
			result = new HashMap<String, String>();
			ThreadLocalFactory.put("request_header", result);
			HttpServletRequest request = ThreadLocalFactory.getThreadLocalRequest();
			if(request!=null){
				String value = request.getHeader("User-Agent1");
				if(Utils.notEmpty(value)&&value.startsWith("0")){
					String deBase64Value = StringUtils.deBASE64(value.substring(1));
					if(Utils.notEmpty(deBase64Value)){
						String[] params = deBase64Value.split("&");
						if(Utils.notEmpty(params)){
							for(String param : params){
								if(Utils.notEmpty(param)){
									String[] paramInfo = param.split("=");
									if(paramInfo!=null&&paramInfo.length>1){
										result.put(paramInfo[0], paramInfo[1]);
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	public static Map<String,String> getHeaderParams(){
		Map<String,String> result = (Map<String,String>)ThreadLocalFactory.get("request_header");
		if(result==null){
			result = new HashMap<String, String>();
			ThreadLocalFactory.put("request_header", result);
			HttpServletRequest request = ThreadLocalFactory.getThreadLocalRequest();
			if(request!=null){
				String sign = request.getHeader("sign");
				String version = request.getHeader("version");
				String app_type = request.getHeader("app_type");
				String openId = request.getHeader("openId");
				String os = request.getHeader("os");
				String model = request.getHeader("model");
				String access_user_token = request.getHeader("accesstoken");
				String time = request.getHeader("time");
				result.put("sign", sign);
				result.put("version", version);
				result.put("app_type", app_type);
				result.put("openId", openId);
				result.put("os", os);
				result.put("model", model);
				result.put("access_user_token", access_user_token);
				result.put("time", time);
				
			}
		}
		return result;
	}
	
	/**
	 * 获取加密的params参数
	 * @return
	 */
	public static Map<String,String> getRequestParams(){
		Map<String,String> result = (Map<String,String>)ThreadLocalFactory.get("request_params");
		if(result==null){
			result = new HashMap<String, String>();
			ThreadLocalFactory.put("request_params", result);
			HttpServletRequest request = ThreadLocalFactory.getThreadLocalRequest();
			if(request!=null){
				String value = request.getParameter("params");
				if(Utils.notEmpty(value)){
					String deBase64Value = StringUtils.deBASE64(value);
					if(Utils.notEmpty(deBase64Value)){
						try {
							Map<String,String> params = gson.fromJson(deBase64Value, new TypeToken<Map<String,String>>(){}.getType());
							if(params!=null){
								result.putAll(params);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}												
					}
				}
			}
		}
		return result;
	}
	
	public static Map<String,List<List<String>>> getRequestParams2(){
		Map<String,List<List<String>>> result = new HashMap<String, List<List<String>>>();
		ThreadLocalFactory.put("request_params", result);
		HttpServletRequest request = ThreadLocalFactory.getThreadLocalRequest();
		if(request!=null){
			String value = request.getParameter("params");
			if(Utils.notEmpty(value)&&value.startsWith("0")){
				String deBase64Value = StringUtils.deBASE64(value.substring(1));
				if(Utils.notEmpty(deBase64Value)){
					try {
						result = gson.fromJson(deBase64Value, new TypeToken<Map<String,List<List<String>>>>(){}.getType());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				}
			}
		}
		return result;
	}
	
	public static Map<String,List<String>> getRequestParams3(){
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		ThreadLocalFactory.put("request_params", result);
		HttpServletRequest request = ThreadLocalFactory.getThreadLocalRequest();
		if(request!=null){
			String value = request.getParameter("params");
			if(Utils.notEmpty(value)&&value.startsWith("0")){
				String deBase64Value = StringUtils.deBASE64(value.substring(1));
				if(Utils.notEmpty(deBase64Value)){
					try {
						result = gson.fromJson(deBase64Value, new TypeToken<Map<String,List<String>>>(){}.getType());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取加密的返回数据。
	 * @param callResult
	 * @return
	 */
	public String getEncryJson(CallResult callResult){
		if(callResult==null){
			callResult = new CallResult();
			callResult.setMessage("返回值为空!");
		}
		//String json = gson.toJson(callResult);
		String json = JSON.toJSONString(callResult, SerializerFeature.DisableCircularReferenceDetect);
		if(true||"true".equals(SystemData.getInstance().getSystemConfig().getProperty("debugModel"))){
			System.out.println(json);
		}
		//return StringUtils.enBASE64(json);
		return json;
	}
	
	/**
	 * 封装了响应的格式
	 * 
	 * @param json 返回的对象
	 * @param response 响应
	 * @throws IOException 写异常
	 */
	protected void responseJson(HttpServletResponse response,String json) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().print(json);
	}
	
	/**
	 * 封装了响应的格式
	 * 
	 * @param text 返回的对象
	 * @param response 响应
	 * @throws IOException 写异常
	 */
	protected void responseText(HttpServletResponse response,String text) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print(text);
	}
	
	/**
	 * 获取分页对象
	 * 
	 * @return
	 */
	protected Page getPage(){
		Page page = new Page();
		Map<String, String> requestParams = getRequestParams();
		String pageIndex = requestParams.get("currentPage");
		String pageSize = requestParams.get("pageSize");
		try {
			page.setCurrentPage(Integer.parseInt(pageIndex));
		} catch (Exception e) {
			page.setCurrentPage(1);
		}
		try {
			page.setPageSize(Integer.parseInt(pageSize));
		} catch (Exception e) {
			page.setPageSize(10);
		}
		if(page.getCurrentPage()<=0){
			page.setCurrentPage(1);
		}
		/*if(page.getPageSize()<=0||page.getPageSize()>1000){
			page.setPageSize(100);
		}*/
		return page;
	}
	
	public String getWYErrorMsg(int code){
		Map<Integer, String> errorMap = new HashMap<Integer, String>();
		errorMap.put(200, "操作成功");
		errorMap.put(403, "非法操作或没有权限");
		errorMap.put(414, "参数错误");
		errorMap.put(416, "频率控制");
		errorMap.put(431, "HTTP重复请求");
		errorMap.put(500, "服务器内部错误");

		return errorMap.get(code);
	}
}
