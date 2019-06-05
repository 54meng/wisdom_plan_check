package com.wpc.webservice.v1.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.rongji.dfish.base.Utils;
import com.wpc.persistence.User;
import com.wpc.service.ServiceLocator;
import com.wpc.webservice.v1.vo.CallResult;

public class DataServiceController extends MultiActionController {

	/**
	 * 读取用户资料
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView showUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return null;
		
	}
	
	/**
	 * 创建用户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView createAccount(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return null;
	}
	
	/**
	 * 登录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView loginAccount(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
	
	/**
	 * 注销
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView logoutAccount(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
	
	/**
	 * 设置密码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView setPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
	
	/**
	 * 设置简介
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView setDescription(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
	
	/**
	 * 设置头像
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView setAvatar(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
	
	/**
	 * 找回密码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView forgetPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
	
	/**
	 * 获取攻略收藏
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView tacticsCollections(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
	
	/**
	 * 获取目的地收藏
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView destCollections(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
}
