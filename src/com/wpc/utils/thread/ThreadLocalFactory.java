package com.wpc.utils.thread;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rongji.dfish.framework.FrameworkConstants;
import com.wpc.dfish.util.Utils;

public class ThreadLocalFactory {

	//线程局部变量
	private static ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();
	
	//Request key 
	private static final String KEY_REQUEST="request-key";
	//Response key
	private static final String KEY_RESPONSE="response-key";
	
	
	//rjspider-user-key
	public static final String KEY_NO_LOGIN_USERID="no-login-userId-key";
	
	/**
	 * 获取本地线程变量的request
	 * @return
	 */
	public static HttpServletRequest getThreadLocalRequest(){ 
		return (HttpServletRequest)get(KEY_REQUEST); 
	}
	
	/**
	 * 设置本地线程变量的request
	 * @return
	 */
	public static void setThreadLocalRequest(HttpServletRequest request){ 
		put(KEY_REQUEST,request); 
	}
	
	/**
	 * 获取本地线程变量的response
	 * @return
	 */
	public static HttpServletResponse getThreadLocalResponse(){ 
		return (HttpServletResponse)get(KEY_RESPONSE); 
	}
	
	/**
	 * 设置本地线程变量的response
	 * @return
	 */
    public static void setThreadLocalResponse(HttpServletResponse response){ 
    	put(KEY_RESPONSE,response); 
    }
    
    /**
     * 获取本地线程局部变量
     * @param key
     * @return
     */
    public static Object get(String key){
    	Map map = (Map)threadLocal.get();
    	if(map==null){
    		return null;
    	}
    	return map.get(key);
    }
    
    /**
     * 添加本地线程局部变量
     * @param key
     * @return oldValue
     */
    public static Object put(String key,Object value){
		Map map = (Map)threadLocal.get();
		if(map==null){
			map = new HashMap();
		}
		Object put = map.put(key, value);
		threadLocal.set(map);
		return put;
    }
    
    /**
     * 清空线程变量
     */
    public static void clear(){
    	Object object = threadLocal.get();
    	object = null;
    	threadLocal.set(null); 
    }
    
    /**
     * 获取当前线程用户ID
     * @return
     */
    public static String getCurrentUserId()
    {
    	HttpServletRequest threadLocalRequest = getThreadLocalRequest();
    	String userId = null;
    	//无需登录时，设置的USERID
    	if(ThreadLocalFactory.get(KEY_NO_LOGIN_USERID)!=null){
    		userId = (String)ThreadLocalFactory.get(KEY_NO_LOGIN_USERID);
    	}
    	if(Utils.isEmpty(userId)&&threadLocalRequest!=null){
    		userId = (String)threadLocalRequest.getSession().getAttribute(FrameworkConstants.LOGIN_USER_KEY);
    	}
    	return userId;
    }
    
}
