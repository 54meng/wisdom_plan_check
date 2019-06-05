package com.wpc.dfish.framework;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.wpc.dfish.framework.InitApp;

public class InitableServlet extends
		org.springframework.web.servlet.DispatcherServlet {
	private static final long serialVersionUID = -8204866037171748505L;
    
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		InitApp.init(this.getServletContext());
	}

}