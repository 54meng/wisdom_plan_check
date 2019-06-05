package com.wpc.utils;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.wpc.utils.thread.ThreadLocalFactory;

/**
 * 系统环境信息
 * 
 * @author imlzw
 *
 */
public class SystemEnvInfo {
	private static String webAbsolutePath = null;
	
	/**
	 * 获取系统web工程的webroot绝对路径（物理路径）
	 * 
	 * @return path
	 */
	public static String getWebAbsolutePath(){
		if(webAbsolutePath==null){
			webAbsolutePath = ThreadLocalFactory.getThreadLocalRequest().getSession().getServletContext().getRealPath("");
		}
		return webAbsolutePath;
	}
	
	/***
	 * 获取资源上传目录(相对)
	 * 
	 * @return
	 */
	public static String getResourceRelativePath(){
		return "res"+File.separator+"file";
	}
	/***
	 * 获取资源上传目录(绝对)
	 * 
	 * @return
	 */
	public static String getResourcePath(){
		String prePath = getWebAbsolutePath();
		return prePath+File.separator+getResourceRelativePath();
	}
	
	/**
	 * 获取资源上传临时目录
	 * 
	 * @return
	 */
	public static String getResourceTempPath(){
		String prePath = getWebAbsolutePath();
		return prePath+File.separator+"res"+File.separator+"tmp";
	}
	
	/**
	 * 获取应用的地址
	 * 
	 * @return
	 */
	public static String getBasePath(){
		HttpServletRequest request = ThreadLocalFactory.getThreadLocalRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		return basePath;
	}
	
}
