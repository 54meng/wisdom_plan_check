package com.wpc.dfish.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wpc.utils.thread.ThreadLocalFactory;

public class EncodingFiler implements Filter {

	private String encoding = null;
	
	
	public void destroy() {
		this.encoding = null;

	}

	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//response.setContentType("text/html; charset=gb2312"); 
		request.setCharacterEncoding(encoding);
		ThreadLocalFactory.setThreadLocalRequest((HttpServletRequest)request);
		ThreadLocalFactory.setThreadLocalResponse((HttpServletResponse)response);
		try {
			chain.doFilter(request, response);
		} finally{
			ThreadLocalFactory.clear();
		}
	}

	
	public void init(FilterConfig filterConfig) throws ServletException {
		this.encoding = filterConfig.getInitParameter("encoding");

	}

}
