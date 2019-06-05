package com.wpc.webservice.v1.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

/**
 * 短信服务api
 * 
 * @author imlzw
 *
 */
public class SmsServiceController extends ServiceBaseController {
	private static final Logger logger = Logger.getLogger(SmsServiceController.class);
	
	/**
	 * 短信推送回调接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private ModelAndView callback(HttpServletRequest request,HttpServletResponse response) throws Exception{
		responseText(response, "0");
		return null;
	}
}
