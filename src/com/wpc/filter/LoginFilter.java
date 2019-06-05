package com.wpc.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.framework.FrameworkConstants;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.User;
import com.wpc.service.ServiceLocator;
import com.wpc.service.UserService;
import com.wpc.utils.SystemEnvInfo;
import com.wpc.utils.thread.ThreadLocalFactory;

/**
 * 用于登录校验过滤
 * 
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2009-2011</p>
 * <p>Company: RJ-SOFT</p>
 * @author I-TASK TEAM
 * @version 1.0
 */
public class LoginFilter implements Filter {
	private FilterConfig filterConfig = null;
	private boolean checkLogin;
	private Set<String> exceptURIs;

	/**
	 * 默认构造函数
	 */
	public LoginFilter() {
		this.checkLogin = true;
	}

	/**
	 * 登录过滤处理 过滤的内容包括 1、是否登录 2、登录的人员是否拥有当前URL的访问权限。
	 * 
	 * @param request
	 *            ServletRequest
	 * @param response
	 *            ServletResponse
	 * @param chain
	 *            FilterChain
	 * @throws java.io.IOException
	 * @throws javax.servlet.ServletException
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		ThreadLocalFactory.setThreadLocalRequest(request);
		ThreadLocalFactory.setThreadLocalResponse(response);
		if (!this.checkLogin) {
			chain.doFilter(request, response);
			return;
		}

		String loginUser = (String) request.getSession().getAttribute(FrameworkConstants.LOGIN_USER_KEY);
		String uri = request.getRequestURI();
		if (loginUser == null) { // 这个人没有登录 那么只能访问固定的几个页面
			// 需要放过几个URL
			for (String url : exceptURIs) {
				if (request.getRequestURI() != null && request.getRequestURI().indexOf(url)>0) {
					chain.doFilter(request, response);
					return;
				}
			}
			User user = null;
			//从cookie中取得userId 与cookieCode
			UserService userService = ServiceLocator.getUserService();
			String userId = null;
			String cookieCode = null;
			boolean isCookieLoginSuccess = false;
			Cookie[] cookies = request.getCookies();
			String loginJsp = SystemEnvInfo.getBasePath()+"login.jsp?redirect=1";
			if(cookies!=null&&cookies.length>0){
				for(Cookie cookie : cookies){
					if("userId".equals(cookie.getName())){
						userId = cookie.getValue();
					}else if("cookieCode".equals(cookie.getName())){
						cookieCode = cookie.getValue();
					}
				}
				if(Utils.notEmpty(userId)&&Utils.notEmpty(cookieCode)){//
					try {
						User userInfo = userService.getUserById(userId);
						//为空或者，禁用用户 ，非服务端用户
						if(userInfo==null||!"1".equals(userInfo.getUserStatus())||!"1".equals(userInfo.getUserType())){
							request.getSession().setAttribute(FrameworkConstants.LOGIN_USER_KEY,null);
							response.sendRedirect(loginJsp);
							return;
						}
						user = userInfo;
					} catch (Exception e) {
						e.printStackTrace();
					}
					//cookie登录成功
					if(user!=null&&cookieCode.equals(user.getCookieCode())){
						isCookieLoginSuccess = true;
					}
				}
			}
			if(isCookieLoginSuccess){//cookie登录成功
				request.getSession().setAttribute(FrameworkConstants.LOGIN_USER_KEY,userId);
				chain.doFilter(request, response);
				return;
			}
			if(Utils.requestFromAjax(request)){
				FrameworkHelper.outPutXML(response, new JSCommand("reload", "window.location.replace(\""+loginJsp+"\");"));
				return;
//			}else if(MobileUtils.isMobile(request)){
//				response.sendRedirect("mlogin.sp");
			}else{
				response.sendRedirect(loginJsp);
				return;
			}
		}else{
//			UserService userService = ServiceLocator.getUserService();
//			//检查用户是否可用,是否会影响性能?
//			try {
//				if(!userService.isUserAvailable(loginUser)){
//					request.getSession().setAttribute(FrameworkConstants.LOGIN_USER_KEY,null);
//					response.sendRedirect("login.jsp");
//					return;
//				}else{
//					System.out.println("用户["+loginUser+"]可用!");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * 初始化配置
	 * 
	 * @param filterConfig
	 * @throws ServletException
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		String checkLogin = filterConfig.getInitParameter("checkCode");
		if (checkLogin == null) {
			this.checkLogin = true;
		} else if (checkLogin.equalsIgnoreCase("false")) {
			this.checkLogin = false;
		} else {
			this.checkLogin = true;
		}
		this.exceptURIs = new HashSet<String>();
		String uris = filterConfig.getInitParameter("exceptURI");
		if (uris != null) {
			for (String uri : uris.trim().replace("\n", "").split(",")) {
				this.exceptURIs.add(uri);
			}
		}
	}

	/**
	 * 销毁过滤器
	 */
	public void destroy() {
		this.filterConfig = null;

	}

	/**
	 * 设置配置
	 * 
	 * @param filterConfig
	 *            FilterConfig
	 */
	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	/**
	 * 设置是否需要登录
	 * 
	 * @param forceEncoding
	 */
	public void setCheckLogin(boolean checkLogin) {
		this.checkLogin = checkLogin;
	}

}
