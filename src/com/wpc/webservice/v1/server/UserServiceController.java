package com.wpc.webservice.v1.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.jam.mutable.MPackage;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.reflect.TypeToken;
import com.wpc.business.TravelMethod;
import com.wpc.commons.sql.SearchContainer;
import com.wpc.commons.sql.SearchContainer.Op;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Advert;
import com.wpc.persistence.AppVersion;
import com.wpc.persistence.Author;
import com.wpc.persistence.BaseObject;
import com.wpc.persistence.Category;
import com.wpc.persistence.Code;
import com.wpc.persistence.Comment;
import com.wpc.persistence.Config;
import com.wpc.persistence.Count;
import com.wpc.persistence.Feedback;
import com.wpc.persistence.Follow;
import com.wpc.persistence.History;
import com.wpc.persistence.Image;
import com.wpc.persistence.Images;
import com.wpc.persistence.Praise;
import com.wpc.persistence.Special;
import com.wpc.persistence.Tag;
import com.wpc.persistence.Team;
import com.wpc.persistence.User;
import com.wpc.persistence.Video;
import com.wpc.service.ServiceLocator;
import com.wpc.service.UserService;
import com.wpc.service.impl.CodeDBMethod;
import com.wpc.service.impl.CountDBMethod;
import com.wpc.service.impl.FollowDBMethod;
import com.wpc.utils.AliyunSmsUtils;
import com.wpc.utils.Constant;
import com.wpc.utils.FileTypeUtils;
import com.wpc.utils.FileUtil;
import com.wpc.utils.RandomUtils;
import com.wpc.utils.SecurityUtils;
import com.wpc.utils.SystemEnvInfo;
import com.wpc.utils.WYunApi;
import com.wpc.utils.string.StringUtils;
import com.wpc.webapp.controller.vo.WYunResp;
import com.wpc.webservice.v1.vo.CallResult;

/**
 * 用户api服务
 * 
 * @author imlzw
 *
 */
public class UserServiceController extends ServiceBaseController {
	//存储手机号码的验证码
	private static final Map<String,Object> phone2codeCache = new HashMap<String, Object>();
	//记录每天发送的短信总量
	private static final Map<String,Integer> mesPostCache = new HashMap<String, Integer>();
	//
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:ss:mm");
	SimpleDateFormat pathFormat = new SimpleDateFormat("yyyy"+File.separator+"MM"+File.separator+"dd");
	private static final Logger logger = Logger.getLogger(UserServiceController.class);
	
	/*public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uri = request.getRequestURI().substring(request.getContextPath().length());
		String action = uri.substring(uri.lastIndexOf("/")+1);
		if("getInfo".equals(action)){//显示用户信息
			//return getInfo(request, response);
		}else if("getCode".equals(action)){//获取手机验证码
			//return getCode(request, response);
		}else{
			CallResult callResult = new CallResult();
			callResult.setCode(ERROR);
			callResult.setMessage("api地址["+uri+"]不存在!");
			responseText(response, getEncryJson(callResult));
			return null;
		}
	}*/
	
	
	
	public static void main(String[] s) throws Exception{
		
		System.out.println(SecurityUtils.encryptPassword(StringUtils.md5("12345")));
		System.out.println(1);
	}
}
