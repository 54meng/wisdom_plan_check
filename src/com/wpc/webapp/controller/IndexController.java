package com.wpc.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.wpc.dfish.framework.FrameworkHelper;

public class IndexController extends MultiActionController {

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(new Gson().toJson(request.getParameterMap()));
		String line = "[\"<inpu type='hidden' class='form-filter' name='page' value='123'><input type='checkbox' name='id2' value='111'>\",1,\"12/09/2013\",\"Jhon Doe\",\"Jhon Doe\",\"450.60$\",6,\"<span class='label label-sm label-info'>Closed</span>\",\"<a href='javascript:;' class='btn btn-xs default'><i class='fa fa-search'></i> View</a>\"]";
		for(int i=0;i<10;i++){
			line += ","+"[\"<input type='checkbox' name='id' value='1'>\",1,\"12/09/2013\",\"Jhon Doe\",\"Jhon Doe\",\"450.60$\",6,\"<span class='label label-sm label-info'>Closed</span>\",\"<a href='user_add_edit.jsp?param="+i+"' data-toggle='modal' data-target='#ajax'  class='btn btn-xs default'><i class='fa fa-search'></i> View</a>\"]";
		}
		Thread.sleep(1000);
		String data = "{\"data\":["+line+"],\"draw\":"+request.getParameter("draw")+",\"recordsTotal\":178,\"recordsFiltered\":178}";
		FrameworkHelper.outPutTEXT(response,data);
		return null;
		
	}
	
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(new Gson().toJson(request.getParameterMap()));
		Thread.sleep(1000);
		FrameworkHelper.outPutTEXT(response,"{\"ret\":\"0\",\"message\":\"11111\",\"value\":\"333333\"}");
		return null;
		
	}
}
