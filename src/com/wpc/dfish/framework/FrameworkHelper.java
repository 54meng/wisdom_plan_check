/*
 * FrameworkHelper.java
 * 
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.
 * 
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package com.wpc.dfish.framework;

import static com.rongji.dfish.framework.FrameworkConstants.ENCODING;
import static com.rongji.dfish.framework.FrameworkConstants.ENDLINE;
import static com.wpc.dfish.util.Utils.escapeXMLword;
import static com.wpc.dfish.util.Utils.notEmpty;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.CommandContainer;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.PanelContainer;
import com.rongji.dfish.engines.xmltmpl.Skin;
import com.rongji.dfish.engines.xmltmpl.View;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.XMLDecorator;
import com.rongji.dfish.engines.xmltmpl.XMLObject;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.component.CalendarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.ImagePanel;
import com.rongji.dfish.engines.xmltmpl.component.PortalPanel;
import com.rongji.dfish.engines.xmltmpl.component.TreePanel;
import com.rongji.dfish.framework.FrameworkConstants;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.util.FileUtil;
import com.wpc.dfish.util.JsonUtils;
import com.wpc.dfish.util.SizeFixedCache;
import com.wpc.dfish.util.Utils;
import com.wpc.dfish.util.XMLTools;


/**
 *  框架帮助类
 * 
 * @author	I-Task Team
 * @version	1.0.0
 * @since	1.0.0	ZHL		2009-9-8
 */
public class FrameworkHelper{
	/** 数据库编号缓存 */
	private static Map<String, String> cachedIds=new HashMap<String, String>();

	
	/**
	 * 输出XML内容
	 * 默认控制这个内容不会被本地缓存。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param xml
	 *            String
	 */
	public static void outPutXML(HttpServletResponse response, XMLObject xo) {
		String xml = xo.asXML();
			HashSet<String>allIds=new HashSet<String>();
		if(xo instanceof View){//FIXME DEBUG 专用，会影响效率，如果正式系统需要去除
			View view=(View)xo;
			Panel root=view.getRootPanel();
			checkPanel(allIds,root);
		}else if(xo instanceof Command){
			checkCommand(allIds,(Command)xo);
		}
		//暂时解决dfish关于groupgrid的设置BUG与parser设置bug（存在两个ps，去掉默认的ps='grv'） - linzhiwei 2013-12-16
		if(notEmpty(xml)){
			xml = xml.replaceAll(" crd=\"0,0\" ", " crd=\"0,1\" ").replaceAll(" ps=\"grv\"", "");
		}
//		System.out.println(new XMLDecorator(xml,false,false));//调试
		outPutContent("<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>" + ENDLINE + xml,
				"text/xml", response);
	}
	private static void checkCommand(HashSet<String> allIds, Command p) {
		if(p instanceof CommandContainer){
			CommandContainer cc=(CommandContainer)p;
			for(Command c:cc.getCommands()){
				checkCommand(null,c);
			}
		}else if(p instanceof UpdateCommand){
			UpdateCommand uc=(UpdateCommand)p;
			allIds=new HashSet<String>();
			if(uc.getContent() instanceof View){
				View view=(View) uc.getContent();
				checkPanel(allIds,view.getRootPanel());
			}else if(UpdateCommand.TYPE_PANEL.equals(uc.getType())){
				checkPanel(allIds,(Panel)uc.getContent());
			}
		}
	}
	private static void checkPanel(HashSet<String> allIds, Panel p) {
		if(p==null)return;
		String id=p.getId();
		if(id!=null&&!id.equals("")){
			if(!allIds.add(id))throw new RuntimeException("一个view中不可以含有两个或以上相同的ID["+id+"]");
		}else if(p instanceof FormPanel || 
				p instanceof GridPanel ||
				p instanceof TreePanel ||
				p instanceof PortalPanel || 
				p instanceof ImagePanel ||
				p instanceof CalendarPanel){
			System.out.println(p);
			throw new RuntimeException("功能面板ID不可以为空");
		}
		if(p instanceof PanelContainer){
			PanelContainer pc=(PanelContainer)p;
			for(Panel sp: pc.getSubPanels()){
				checkPanel(allIds,sp);
			}
		}
		
	}
	/**
	 * 输出HTML内容 默认控制这个内容不会被本地缓存。
	 * 
	 * @param response
	 * @param html
	 */
	public static void outPutHTML(HttpServletResponse response, String html) {
		outPutContent(html, "text/html", response);
	}

	/**
	 * 输出文本内容 默认控制这个内容不会被本地缓存。
	 * 
	 * @param response
	 * @param text
	 */
	public static void outPutJson(HttpServletResponse response, String text) {
		outPutContent(text, "text/json", response);
	}
	/**
	 * 输出json内容
	 * @param response
	 * @param obj
	 */
	public static void outPutJson(HttpServletResponse response, Object obj) {
		if(obj == null) {
			obj = "";
		}
		outPutJson(response, JsonUtils.toJson(obj, false));
	}
	/**
	 * 输出文本内容
	 * 默认控制这个内容不会被本地缓存。
	 * @param response
	 * @param text
	 */
	public static void outPutTEXT(HttpServletResponse response, String text) {
		outPutContent(text, "text/plain", response);
	}
	
	private static void outPutContent(final String conent, String contentType,
			HttpServletResponse response) {
		BufferedOutputStream bos = null;
		try {
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Content-Type", contentType + "; charset=" + ENCODING);
			bos = new BufferedOutputStream(response.getOutputStream());
			// 调试用语句
			bos.write(conent.getBytes(ENCODING));
			//FIXME 以下2句DEBUG 专用，正式系统需要去除
			if(FrameworkHelper.isDebugOn()){
				Executors.newSingleThreadExecutor().execute(new Runnable(){
					public void run(){
						System.out.println(new XMLDecorator(conent, false, false));
					}
				});
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * 取得request中的Locale定义 首先是session中是否有定义 -- 用户选择的 其次看request本身的字符集 -- 浏览器的
	 * 最后看服务器的字符集 -- 当没有足够的信息的时候，使用服务器默认字符集
	 * 
	 * @param request
	 * @return
	 */
	public static Locale getLocale(HttpServletRequest request){
	
		Locale loc = (Locale) request.getSession().getAttribute(FrameworkConstants.USER_LOCALE_KEY);
		if (loc != null)
			return loc;
		loc = request.getLocale();
		if (loc != null)
			return loc;
		return getLocale();
	}

	/**
	 * 取得服务器默认字符集。 与Locale.getDefault()不同，它会是用配置的默认字符集。
	 * 
	 * @return
	 */
	public static Locale getLocale(){
		String value = SystemData.getInstance().getSystemConfig().getProperty(
				"framework.pub.sysdefaultlocale");
		if (value != null) {
			Locale[] locs = Locale.getAvailableLocales();
			for (Locale locale : locs) {
				if (value.equals(locale.toString()))
					return locale;
			}
		}
		return Locale.getDefault();
	}

	/**
	 * 对SPRING MVC WEB的支持 由于底层的问题。所以我们在前端如果有批量文件上传的时候， 如果名字为file1
	 * 实际上在web页面中由多个文件上传的控件。 他们的名字分别是file1-1 file-2这样子后面的序号有可能不连续。 因为前端本身可以取消上传。
	 * 这个方法。就是为了透明的取得file1名字的文件。并以数组的形式存在 类似于request.getParameterValues(String);
	 * 
	 * @param request
	 * @param attachFileName
	 * @return
	 */
	public static MultipartFile[] getFiles(MultipartHttpServletRequest request,String attachFileName){
		Iterator iter = request.getFileNames();
		final int nameLen = attachFileName.length() + 1;
		List<MultipartFile> list = new ArrayList<MultipartFile>();
		while (iter.hasNext()) {
			MultipartFile file = request.getFile((String) iter.next());
			if(file.getName().startsWith(attachFileName)){
				list.add(file);	
			}
		}
		Collections.sort(list, new Comparator<MultipartFile>() {
			public int compare(MultipartFile o1, MultipartFile o2) {
				String s1 = o1.getName().substring(nameLen);
				String s2 = o2.getName().substring(nameLen);
	
				return Integer.parseInt(s1, 32) - Integer.parseInt(s2, 32);
			}
		});
	
		return list.toArray(new MultipartFile[list.size()]);
	}



	/**
	 * 取得这个登录人员使用的皮肤。
	 * @param request
	 * @return
	 */
	public static Skin getSkin(HttpServletRequest request){
		Skin skin = (Skin) request.getSession().getAttribute("com.rongji.dfish.SKIN");
		return skin == null ? Skin.VISTA : skin;
	}
	/**
	 * 取得这个登录人员使用的皮肤的配色，具体配色代码含义要根据皮肤本身的定义。
	 * @param request
	 * @return
	 */
	public static int getSkinColor(HttpServletRequest request){
		Integer skinColor = (Integer) request.getSession().getAttribute("com.rongji.dfish.SKIN_COLOR");
		return skinColor == null ? 1 : skinColor.intValue();
	}





	/**
	 * 判断用户是否为机构管理员
	 * 
	 * 机构管理员可以管理自己所属机构及其所有子机构，和这些机构中的人员
	 * 
	 * @param loginUser
	 * @return
	 */
	public static boolean isOrganiseAdmin(String loginUser){
		// FIXME
		return true;
	}

	/**
	 * 是否调试模式
	 * 
	 * @author ZHL V1.0.0;
	 * @return
	 */
	public static boolean isDebugOn(){
		boolean isDebugOn = false;
		if ("1".equals(getSystemConfig("framework.pub.debugOn", "1"))) {
			isDebugOn = true;
		}
		return isDebugOn;
	}

	/**
	 * 取得默认的视图
	 * 相当于
	 * <pre>
	 * Locale loc = FrameworkHelper.getLocale(request);
	 * Skin skin = FrameworkHelper.getSkin(request);
	 * ViewFactory viewFactory = ViewFactory.getViewFactory(skin,loc);
	 * </pre>
	 * @param request
	 * @return
	 * @since 2009-05-04
	 */
	public static ViewFactory getViewFactory(HttpServletRequest request){
		return ViewFactory.getViewFactory(getSkin(request),getLocale(request),getSkinColor(request));
	}

	/**
	 * 取得系统配置，
	 * 
	 * @param key
	 *            关键字 如sysarg.attach.freetaskAttachPath等
	 * @param defaultValue
	 *            默认值。如果系统配置信息没有的时候，则显示默认值
	 * @return
	 */
	public static String getSystemConfig(String key,String defaultValue){
		String value = SystemData.getInstance().getSystemConfig().getProperty(key);// properties.getProperty(key);
		if (notEmpty(value))
			return value;
		return defaultValue;
	}

	/**
	 * 将系统配置保存至配置文件中（xml）
	 * 
	 * @param key
	 * @param value
	 */
	public static void setSystemConfig(String key,String value){
		SystemData.getInstance().getSystemConfig().setProperty(key, value);
	}

	/**
	 * 更新缓存 
	 *
	 */
	public static void resetSystemConfig(){
	    String realPath=SystemData.getInstance().getServletInfo().getServletRealPath();
	    
		String configPath =realPath+"WEB-INF/config/dfish-config.xml";
		SystemData.getInstance().setSystemConfig(new XMLTools(configPath, 256));
	}

	/**
	 * 取得人员个人配置中，每页显示多少条数据的配置值
	 * 
	 * @param userId
	 * @return
	 */
	public static int getPersonalPageSize(String userId){
		String pageSize = getPersonalConfig(userId, "person.rows_per_page");
		try {
			return Integer.parseInt(pageSize);
		} catch (Exception ex) {
			return 10;
		}
	}
	
	/**
	 * 创建一个Page实体,总行数和总页数会在数据库查询的时候填充.
	 * 
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Page createPersonalPage(String userId, String currentPage) {
		Page page = new Page();
		int curPage = 1;
		if (notEmpty(currentPage)) {
			try {
				curPage = Integer.parseInt(currentPage);
			} catch (Exception ex) {
				curPage = 1;
			}
		}
		page.setCurrentPage(curPage);
		page.setPageSize(getPersonalPageSize(userId));

		return page;
	}

	/**
	 * 根据个人用户的每页行数,计算当前高亮行
	 * 
	 * @param sum
	 * 			总行数
	 * @param userId
	 * @return
	 */
	public static int getCurrentHightLight(int sum,String userId){
		int current=0;
	
		int pageSize=getPersonalPageSize(userId);
		
		if(sum>0){//确保删除第一个时是在第一上
			current=(sum-1) % pageSize;
		}
		
		return current;
	}

	/**
	 * 根据个人用户的每页行数,计算当前页
	 * 
	 * @param sum
	 * @param userId
	 * @return
	 */
	public static int getCurrentPage(int sum,String userId){
		int current=0;
		
		int pageSize=getPersonalPageSize(userId);
		
		current=(sum-1)/pageSize+1;
		
		return current;
	}

	/**
	 * 公用获取个人配置参数配置方法 如果当前个人设置为空则自动取默认值
	 * 
	 * @param userId
	 *            String
	 * @param argStr
	 *            String
	 * @return String
	 */
	public static String getPersonalConfig(String userId,String argStr){
		String tempStr = userId + "." + argStr;
		synchronized (propertiesPersonMap) {
	
			String value = propertiesPersonMap.get(tempStr);
			if (value != null) {
				return value;
			}
		}
		try {
			XMLTools tool = new XMLTools(SystemData.getInstance().getServletInfo()
					.getServletRealPath()
					+ "WEB-INF/config/person" + java.io.File.separator + userId + ".xml", false);
			String result = tool.getProperty(argStr);
			if (result != null && !result.equals("")) {
				synchronized (propertiesPersonMap) {
					propertiesPersonMap.put(tempStr, result);
				}
				return result;
			}
		} catch (Exception ex) {
		}
		if (!"default".equals(userId)) { // 防止死循环
			return getPersonalConfig("default", argStr);
		}
		return null;
	}

	/**
	 * 公用设置个人配置参数配置方法 如果当前个人设置为空则自动新增
	 * 
	 * @param userId
	 *            String
	 * @param argStr
	 *            String
	 * @param value
	 *            String
	 * @return String
	 */
	public static void setPersonalConfig(String userId,String argStr,String value){
		String tempStr = userId + "." + argStr;
		synchronized (propertiesPersonMap) {
	
			if (propertiesPersonMap.containsKey(tempStr)) {
				if ((value == null && propertiesPersonMap.get(tempStr) == null)
						|| (value != null && value.equals(propertiesPersonMap.get(tempStr)))) {
					return;
				}
			} // 如果相同就不要存了.
			propertiesPersonMap.put(tempStr, value);
		}
		// 如果文件不存在,则新建文件.
		String fileFullName = SystemData.getInstance().getServletInfo().getServletRealPath()
				+ "WEB-INF/config/person" + java.io.File.separator + userId + ".xml";
		File f = new File(fileFullName);
		if (!f.exists()) {
			FileUtil.writeFile("<?xml version=\"1.0\" encoding=\"UTF-8\"?><person/>", fileFullName,
					ENCODING);
		}
		XMLTools tool = new XMLTools(fileFullName, false);
		tool.setProperty(argStr, value);
		return;
	}

	private static Map<String, String> propertiesPersonMap=new SizeFixedCache<String, String>(256);


	
	/**
	 * 获取IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request){
		String ip=request.getHeader("X-Forwarded-For");
		if(Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip=request.getHeader("Proxy-Client-IP");
		}
		if(Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip=request.getHeader("WL-Proxy-Client-IP");
		}
		if(Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip=request.getRemoteAddr();
		}
		
		return ip;   
	}  
	
	/**
	 * 向数据库中插入一条新数据时，该数据的主键不可重复，可通过此方法获取该编号。 ID是一个十进制数，位数是一定的。如果前面没有数字，会自动补0。
	 * 这适用于只需确保在一个表中ID唯一的的情况，不适合新建人员时人员编号的生成。
	 * 
	 * @param clzName
	 *            持久化对象名字
	 * @param idName
	 *            ID字段字
	 * @param initId
	 *            如果这个表里面并没有内容，那么将使用这个ID。否则用表里面最大的ID加+1。注意，长度与原先表最大的ID一致。
	 *            当initId>1时,表示该编号是从设定的初始值开始递增,小于初始值的编号是预留的.//ZHL
	 * @return
	 */
	public static String getNewId(String clzName, String idName, String initId) {
		// FIXME 这个是本地的取法，不适合集群使用
		clzName = clzName.intern();
		synchronized (clzName) {
			// 如果内存中已经存在则不需要从数据库中读取
			String storedId = cachedIds.get(clzName);
			if (storedId != null) {
				String newId = getNextId(storedId, initId, 10);// getNextId(storedId,
																// 10);//ZHL
				cachedIds.put(clzName, newId);
				return newId;
			}
			String dataBaseMaxId = getMaxIdFromDataBase(clzName, idName);
			if (dataBaseMaxId != null && !dataBaseMaxId.equals("")) {
				String newId = getNextId(dataBaseMaxId, initId, 10);// getNextId(dataBaseMaxId,
																	// 10);//ZHL
				cachedIds.put(clzName, newId);
				return newId;
			}
			cachedIds.put(clzName, initId);
			return initId;
		}
	}

	@SuppressWarnings("unchecked")
	private static String getMaxIdFromDataBase(String clzName, String idName) {
		PubCommonDAO dao = (PubCommonDAO) SystemData.getInstance().getBeanFactory().getBean("PubCommonDAO");
		List list = dao.getQueryList("select max(t." + idName + ") from " + clzName + " t");
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return (String) list.get(0);
		}
		return null;
	}

	/**
	 * 当前的数字加1后以定长的字符串数据. 增加对预留Id的处理,如果Id>=初始值就在Id的基础上+1;反之就在初始值的基础上+1;//ZHL
	 * 比如初始值是10(即系统需要预留1~9的Id),那么如果此时数据库或缓存中的Id=5,那调用本方法之后nextId=10,跳过10以前的Id;
	 * 
	 * @param id
	 * @param initId
	 * @param radix
	 * @return
	 */
	private static String getNextId(String id, String initId, int radix) {
		String nextId = initId;
		int _id = Integer.parseInt(id);
		int _initId = Integer.parseInt(initId);
		if (_id >= _initId) {
			nextId = getNextId(id, radix);
		} else {
			nextId = getNextId(initId, radix);
		}

		return nextId;
	}
	
	/**
	 * 当前的数字加1后以定长的字符串数据 比如说输入0022 得到0023 输入00999得到01000 输入999 得到000
	 * 
	 * @param id
	 * @param radix
	 *            多少进制，最常用的是2/8/10/16/32/36
	 * @return
	 */
	private static String getNextId(String id, int radix) {
		long l = Long.parseLong(id, radix);
		l++;
		String targetValue = Long.toString(l, radix);
		int idLen = id.length();
		int nowLen = targetValue.length();
		if (idLen == nowLen) {
			return targetValue;
		} else if (idLen < nowLen) {
			throw new RuntimeException("can not get next id for [" + id + "].");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = nowLen; i < idLen; i++) {
			sb.append('0');
		}
		sb.append(targetValue);
		return sb.toString();
	}
	/**
	 * <p>取得某个路径下的文本文档的内容</p>
	 * <p>文本可以是JS/HTML/CSS等，但一般不建议使用JSP的内容。
	 * 因为其动态内容不会被执行。</p>
	 * <p>path 为从webcontext开始的绝对路径<br/>
	 * 如一个应用访问地址是 http://www.foo.com/myapp/<br/>
	 * 他的一个动态页面地址是http://www.foo.com/myapp/service/testingServ.sp?act=index<br/>
	 * 里面需要一个HTML的内容。这个HTML位于http://www.foo.com/myapp/res/introduce/total.html<br/>
	 * 那么这个path应该是/res/introduce/total.html或前面无斜杠res/introduce/total.html<br/>
	 * 而不是../res/introduce/total.html<br/>
	 * 也不是/myapp/res/introduce/total.html<br/>
	 * </p>
	 * <p>这个方法会自动识别文本的字符集</p>
	 * <p>典型用法:</p>
	 * <pre>
	 * HtmlPanel htmlPanel =new HtmlPanel("f_intro",null);
	 * htmlPanel.setFilter(false);
	 * htmlPanel.setHtml(getFileText("/res/introduce/total.html"));
	 * </pre>
	 * @param path 路径
	 * @return 文本内容
	 * @since 2012/2/2
	 */
	public static String getFileText(String path){
		String rootPath=SystemData.getInstance().getServletInfo().getServletRealPath();
		if(path.startsWith("/")||path.startsWith("\\")){
			path=path.substring(1);
		}
		try{
			String text= getFileText(new File(rootPath+path));
			return text;
		}catch(IOException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 取得某个文件的文本。自动识别字符集
	 * swf等非文本文件，会出现乱码。
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileText(File file)throws IOException{
		InputStream is=null;
		try{
			is=new BufferedInputStream(new FileInputStream(file));
			return getText(is);
		}catch(IOException ex){
			throw ex;
		}finally{
			if(is!=null){
				is.close();
			}
		}
	}
	/**
	 * 从流中读取文件内容。自动识别字符集
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static String getText(InputStream is) throws IOException{
		StreamContent content=getContent(is);
//		System.out.println("使用字符集"+content.findCharset+"识别文件内容");
		return new String(content.baos.toByteArray(),content.findCharset);
	}
	private static StreamContent getContent(InputStream is) throws IOException {
		final StreamContent content = new StreamContent();
		nsDetector det = new nsDetector(nsDetector.CHINESE);
		det.Init(new nsICharsetDetectionObserver() {
			public void Notify(String s) {
				content.findCharset = s;
			}
		});
		byte buff[] = new byte[1024];
		boolean done = false;
		boolean isAscii = true;
		int j;
		while ((j = is.read(buff, 0, buff.length)) >= 0) {
			content.baos.write(buff, 0, j);
			if (isAscii)
				isAscii = det.isAscii(buff, j);
			if (!isAscii && !done)
				done = det.DoIt(buff, j, false);// 模糊匹配的话，可以第一次找到就跳出
		}
		det.DataEnd();
		if (isAscii) {
			// 纯ascii 用系统编码读取应该更快
			content.findCharset = System.getProperty("file.encoding");
		}
		if (content.findCharset == null) {
			String chars[] = det.getProbableCharsets();
			if (chars != null && chars.length > 1) {
				content.findCharset = chars[0];
			} else {
				content.findCharset = System.getProperty("file.encoding");
			}
		}
		if("GB2312".equalsIgnoreCase(content.findCharset)&&Charset.isSupported("GBK")){
			content.findCharset="GBK";//一般中文机器支持GBK
		}
		return content;
	}
	/**
	 * 获取客户端的真实IP地址
	 * <p>
	 * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了。
	 * 经过代理以后，由于在客户端和服务之间增加了中间层，因此服务器无法直接拿到客户端的IP，服务器端应用也无法直接通过转发请求的地址返回给客户端。
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		//如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，那么真正的用户端的真实IP则是取X-Forwarded-For中第一个非unknown的有效IP字符串。
		String ip = request.getHeader("X-Forwarded-For");
		if (Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}  

	private static class StreamContent{
		String findCharset;
		ByteArrayOutputStream baos =new ByteArrayOutputStream();
	}
}