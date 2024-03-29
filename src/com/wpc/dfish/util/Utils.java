﻿package com.wpc.dfish.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.engines.xmltmpl.XMLDecorator;
import com.wpc.dfish.framework.FrameworkHelper;

/**
 * 长期积累的通用方法类。
 * @author ITASK-team
 *
 */
public class Utils {
	private static final Log logger=LogFactory.getLog(Utils.class);
	
	private static final String ENCODING="UTF-8";

	/**
	 * 校验一个字符串是否为空判断 改名为notEmpty LinLW // 2005-09-19 原名为validNull容易产生歧义
	 * 
	 * @param str
	 * @return
	 */
	public static boolean notEmpty(String str) {
		return str != null && str.length() > 0;
	}

	/**
	 * 判断一个字符串为空或空串或全部有空格组成
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断一个List是否非空 ZHL 2008-07-28
	 * 
	 * @param c
	 * @return
	 */
	public static boolean notEmpty(Collection<?> c) {
		return c != null && c.size() > 0;
	}
	
	/**
	 * 判断一个List是否为空 ZHL 2008-07-28
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(Collection<?> c) {
		return c == null || c.size() <= 0;
	}
	
	/**
	 *  判断一个对象数组是否为空/非空 ZHL 2009-10-19
	 * 
	 * @param os
	 * @return
	 */
	public static boolean notEmpty(Object[] os) {
		return os != null && os.length > 0;
	}
	
	public static boolean isEmpty(Object[] os) {
		return os == null || os.length <= 0;
	}
	
	/**
	 *  判断一个Map否非空 ZHL 2009-10-19
	 * 
	 * @param os
	 * @return
	 */
	public static boolean notEmpty(Map<?, ?> m) {
		return m != null && !m.isEmpty();
	}
	
	/**
	 *  判断一个Map否为空 ZHL 2009-10-19
	 * 
	 * @param os
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> m) {
		return m == null || m.isEmpty();
	}


	/**
	 * 提供XML转义字符
	 * 
	 * @param appendTo
	 *            StringBuffer
	 * @param src
	 *            String
	 */
	public static void escapeXMLword(StringBuilder appendTo, String src) {
		// return title.replaceAll("&", "&amp;")
		// .replaceAll("<", "&lt;")
		// .replaceAll(">", "&gt;")
		// .replaceAll("\"", "&quot;")
		// .replaceAll("'", "&#39;");
		// 不用上述写法是为了效率。
		if (src == null) {
			return;
		}
		char[] ca = src.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			switch (ca[i]) {
			case '&':
				appendTo.append("&amp;");
				break;
			case '<':
				appendTo.append("&lt;");
				break;
			case '>':
				appendTo.append("&gt;");
				break;
			case '\"':
				appendTo.append("&quot;");
				break;
			case '\'':
				appendTo.append("&#39;");
				break;
			case '\r':
				appendTo.append('\r');
				break;
			case '\n':
				appendTo.append('\n');
				break;
			default:
				if (ca[i] < 32 || (ca[i] > 127 && ca[i] < 256)) {
					// appendTo.append("&#").append( (int) ca[i]).append(";");
				} else {
					appendTo.append(ca[i]);
				}
			}
		}
	}

	/**
	 * 对XML字符进行转义
	 * 
	 * @param src
	 *            String
	 * @return String
	 */
	public static String escapeXMLword(String src) {
		StringBuilder sb = new StringBuilder();
		escapeXMLword(sb, src);
		return sb.toString();
	}


	/**
	 * 拷贝属性,只拷贝基础属性 String Date Number等
	 * 
	 * @param to
	 *            Object
	 * @param from
	 *            Object
	 */
	public static void copyPropertiesExact(Object to, Object from) {
		if (from == null || to == null) {
			return;
		}
		Method[] fromM = from.getClass().getMethods();
		for (int i = 0; i < fromM.length; i++) {
			String methodName = fromM[i].getName();
			Class<?> returnType = fromM[i].getReturnType();
			Class<?>[] parapType = fromM[i].getParameterTypes();
			if (methodName.startsWith("get") && CARE_TYPES.contains(returnType)
					&& (parapType == null || parapType.length == 0)) {
				String setterName = "set" + methodName.substring(3);
				try {
					Method setter = to.getClass().getMethod(setterName, new Class[] { returnType });
					if (setter != null) {
						setter.invoke(to, new Object[] { fromM[i].invoke(from, EMPTY_PARAM) });
					}
				} catch (Exception ex) {
				} // ignore
			}
		}
	}

	private static final Object[] EMPTY_PARAM = new Object[0];

	private static final Set<Class<?>> CARE_TYPES = new HashSet<Class<?>>();
	static {
		// 数字
		CARE_TYPES.add(short.class);
		CARE_TYPES.add(Short.class);
		CARE_TYPES.add(int.class);
		CARE_TYPES.add(Integer.class);
		CARE_TYPES.add(long.class);
		CARE_TYPES.add(Long.class);
		CARE_TYPES.add(float.class);
		CARE_TYPES.add(Float.class);
		CARE_TYPES.add(double.class);
		CARE_TYPES.add(Double.class);
		CARE_TYPES.add(BigInteger.class);
		CARE_TYPES.add(BigDecimal.class);
		CARE_TYPES.add(Number.class);
		// 字符
		CARE_TYPES.add(char.class);
		CARE_TYPES.add(String.class);
		// 时间
		CARE_TYPES.add(java.util.Date.class);
		CARE_TYPES.add(java.sql.Date.class);
		CARE_TYPES.add(java.sql.Time.class);
		CARE_TYPES.add(java.sql.Timestamp.class);
	}

	public static String getParameter(HttpServletRequest request, String key) {
		// 处理tomcat下的中文URL的问题 tomcat在GET方法下传递URL
		// encode的数据会出错。它并是不是按照request设置的字符集进行解码的。
//		if(!"GET".equals(request.getMethod()))return request.getParameter(key);
		String query = request.getQueryString();
		if(notEmpty(query)){
			String[] pairs = query.split("[&]");
			for (String string : pairs) {
				String[] pair = string.split("[=]");
				if (pair.length == 2 && key.equals(pair[0]))
					try {
						return java.net.URLDecoder.decode(pair[1], ENCODING);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
			}
		}
		return request.getParameter(key);
	}

	/**
	 * 从COOKIE中取得值
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		return null;
	}

	/**
	 * 设置 cookie
	 * 
	 * @param response
	 *            HttpServletResponse 句柄
	 * @param name
	 *            cookie的名称
	 * @param value
	 *            cookie的值
	 */
	public static void setCookieValue(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		if (isEmpty(value)) {
			cookie.setMaxAge(0);
		} else {
			cookie.setMaxAge(30 * 86400);
		}
		response.addCookie(cookie);
	}

	/**
	 * 设置 cookie
	 * 
	 * @param response
	 *            HttpServletResponse 句柄
	 * @param name
	 *            cookie的名称
	 * @param value
	 *            cookie的值
	 * @param maxAge
	 *            有效时间(秒) 0表示马上清除该cookie, -1表示关闭浏览器时，cookie删除
	 * @param domain
	 *            有效的访问域，默认只有同一个域才能访问。例如"www.163.com"
	 *            如果需要在子网也能访问，如要让mail.163.com也能访问到这个cookie那么这里应该设置为".163.com"
	 * @param Path
	 *            有效的路径
	 *            如网址"http://2008.163.com/08/0807/10/4IO4QADS00742437.html"
	 *            所对应得path为"/08/0807/10/";
	 */
	public static void setCookieValue(HttpServletResponse response, String name, String value,
			int maxAge, String domain, String path) {
		Cookie cookie = new Cookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		if (isEmpty(value)) {
			cookie.setMaxAge(0);
		} else {
			cookie.setMaxAge(maxAge);
		}
		response.addCookie(cookie);
	}
	
	/**
	 * 按提供的字符串分隔字符串(带分隔符转换).
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	public static String[] split(String str,String regex){
		String[] sa=null; 
		if(str!=null && notEmpty(regex)){
			sa=str.split(rex4Str(regex));
		}
		
		return sa;
	}
	
	/**
	 * 正则转义字符和特殊字符串处理
	 * 
	 * @param regex
	 * @return
	 */
	public static String rex4Str(String regex){
		String rexc="";
		
		if(".".equals(regex)){
			rexc="\\.";
		}else if("^".equals(regex)){
			rexc="\\^";
		}else if("$".equals(regex)){
			rexc="\\$";
		}else if("*".equals(regex)){
			rexc="\\*";
		}else if("+".equals(regex)){
			rexc="\\+";
		}else if("|".equals(regex)){
			rexc="\\|";
		}else{
			rexc=regex;
		}
		
		return rexc;
	}

	/**
	 * 把null的对象/字符串变成空字符串
	 * 
	 * @param o
	 * @return
	 */
	public static String null2EmptyString(Object o){
		return o==null ? "" : o.toString();
	}
	
	/**
	 * 格式化XML
	 * 
	 * @param doc
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String formatXML(Document doc,String encoding) throws IOException{
		OutputFormat outputFormat=OutputFormat.createPrettyPrint();
		outputFormat.setIndent(" ");
		outputFormat.setIndentSize(1);
		outputFormat.setExpandEmptyElements(false);
		outputFormat.setLineSeparator(System.getProperty("line.separator","\n"));
        //设置xml的输出编码
		outputFormat.setEncoding(encoding);
		//创建输出(目标)
		StringWriter stringWriter=new StringWriter();
		//创建输出流
		XMLWriter xmlWriter=new XMLWriter(stringWriter,outputFormat);
       //输出格式化的串到目标中，执行后。格式化后的串保存在stringWriter中。
		try{
			xmlWriter.write(doc);
		}catch(IOException e){
			throw e;
		}finally{
			if(xmlWriter!=null){
				xmlWriter.close();
			}
		}
		
		return stringWriter.toString();
	}
	
	/**
	 * 重定向printStackTree的输出到字符串
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public static String printStackTrace2String(Throwable t){
		String exceMsg=null;
		StringWriter sw=null;
		PrintWriter ps=null;
		try{
			sw=new StringWriter();
			ps=new PrintWriter(sw);
			t.printStackTrace(ps);
			exceMsg=sw.toString();
		}catch (Exception e) {
			logger.info("重定向printStackTreec错误.",e);
		}finally{
			if(ps!=null){
				ps.close();
			}
			if(sw!=null){
				try{
					sw.close();
				}catch(IOException e){
					//什么也不做
				}
			}
		}
		
		return exceMsg;
	}
	
	private static String POSTFIX_ZIP=".zip";
	
	/**
	 * 提取文件名,如果路径没有文件名及后缀,又不想把最后一层的目录名作为文件名,则自动产生一个唯一的ID作为文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameFromFilePath(String filePath,boolean isPath2Name){
		String zipFileName="";
		if(notEmpty(filePath)){
			String strTempPath=conversionSpecialCharacters(filePath);
			if(hasSubStrNotInitialOrLast(zipFileName,".")){
				zipFileName=filePath.substring(strTempPath.indexOf(File.separator));
			}else{
				if(isPath2Name){
					if(strTempPath.endsWith(File.separator)){
						String sub=strTempPath.substring(0,strTempPath.length()-1);
						zipFileName=sub.substring(sub.lastIndexOf(File.separator)+1,sub.length());
					}
				}else{
					zipFileName=System.currentTimeMillis()+POSTFIX_ZIP;
				}
			}
		}
		
		return zipFileName;
	}
	
	/**
	 * 转换目录分隔符
	 * 
	 * @param zipFileRootPath
	 * @return
	 */
	public static String conversionSpecialCharacters(String zipFileRootPath){
		String strPath="";
		if(notEmpty(zipFileRootPath)){
			strPath=zipFileRootPath.replaceAll("[/(\\\\)]","/");
			
			if(!strPath.endsWith(File.separator)){
				strPath=strPath+File.separator;
			}
		}
		
		return strPath;
	}
	
	/**
	 * 替换字符串一次
	 * 
	 * @param template
	 * @param placeholder
	 * @param replacement
	 * @return
	 */
	public static String replaceOnce(String template, String placeholder, String replacement) {
		String strDes=template;
		
		if(notEmpty(template)){
			int loc = template.indexOf( placeholder );
			if (loc>=0) {
				strDes=new StringBuffer(template.substring(0,loc)).append(replacement)
						.append(template.substring(loc+placeholder.length())).toString();
			}
		}

        return strDes;
	}
	
	/**
	 * 用于判断子字符串是否出现在首尾(有可能不包涵子字符串)
	 * 
	 * @param subStr
	 * 					待判断字符串
	 * @return
	 * 			返回boolean类型值,当返回“true”表示待判断字符串符合判断规则,否则false
	 */
	public static boolean isSubStrNotInitialOrLast(String str,String subStr){
		return str!=null && !str.startsWith(subStr) && !str.endsWith(subStr);
	}
	
	/**
	 * 用于判断是否包涵子字符串且该子字符串不出现在首尾
	 * 
	 * @param subStr
	 * 					待判断字符串
	 * @return
	 * 			返回boolean类型值,当返回“true”表示待判断字符串符合判断规则,否则false
	 */
	public static boolean hasSubStrNotInitialOrLast(String str,String subStr){
		return str!=null && str.indexOf(subStr)>0 && !str.endsWith(subStr);
	}	  /**
	   * 取得唯一的值
	   *
	   * @return String
	   */
	  public static String getUID() {
	    return getUID(8);
	  }
	  private static  Random random=new Random(87860988L);
	  /**
	   * 取得唯一的值
	   * @param byteLength 相当于多少个字节。由于是16进制数表示。结果是byteLength的2倍
	   * @return String
	   */
	  public static String getUID(int byteLength) {
		byte[] temp=new byte[byteLength];
	    random.nextBytes(temp);
	    return StringCryptor.byte2hex(temp);
	  }

	  /**
	   * 判断字符串是否仅有数字组成,null或""均是非数字
	   * @param str
	   * @return
	   */
	  public static boolean isNumber(String str) {
		  if(Utils.isEmpty(str)) {
			  return false;
		  }
		  Pattern p = Pattern.compile("\\d*");
		  Matcher m =  p.matcher(str);
		  return m.matches();
	  }

	  /**
	   * 拼接字符串,等同于+操作
	   * @param string
	   * @return
	   */
	public static String joinString(Object... obj) {
	    StringBuilder sb = new StringBuilder();
		if(obj != null) {
	    	for(Object o : obj) {
	    		sb.append(o);
	    	}
	    }
		return sb.toString();
	}
	/**
	 * 判断请求是否由DFish框架发出的
	 * @param request
	 * @return
	 */
	public static boolean requestFromAjax(HttpServletRequest request) {
		String h = request.getHeader("x-requested-with");
		return "ajax".equals(h);
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
	
	public static String formatDecimal(double decimal, String format){
        String result = "";
        try {
            DecimalFormat df = new DecimalFormat(format);
            result = df.format(decimal);
        }catch (Exception e){
            return "";
        }
        return result;
    }
}
