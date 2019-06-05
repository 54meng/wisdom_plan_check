package com.wpc.webapp.controller;

import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.wpc.dfish.framework.FrameworkHelper.outPutXML;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.AlertCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.LoadingCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.command.jsdoc.JSCmdLib;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.framework.FrameworkConstants;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.StringUtil;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.User;
import com.wpc.service.ServiceLocator;
import com.wpc.service.UserService;
import com.wpc.utils.string.StringUtils;

/**
 * 系统登录校验判断控制类
 * 支持单点登录，如从rss登录、portal登录、其它系统登录
 * 
 * <p>Title: Ordering 1.0</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright (c) 2012-2015</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author  LZW
 * @version 1.0
 * @since	1.0.0	LZW		2012-11-24
 */
public class LoginController extends MultiActionController {
	private static StringCryptor NONE_CRYPTOR = CryptFactory.getStringCryptor(CryptFactory.NONE, CryptFactory.UTF8,
			CryptFactory.BASE64);
	private static StringCryptor SHA512 = CryptFactory.getStringCryptor(CryptFactory.SHA512, CryptFactory.GBK,
			CryptFactory.HEX_STRING);
	private static Hashtable htUserNameToSessions = new Hashtable();
	private static Hashtable htIdToSession = new Hashtable();
	private HttpServletRequest m_oRequest;
	private ServletContext servletContext;
	private HttpSession httpSession;
	private static final String IP_LOCAL = "127.0.0.1";
	private static final String USER_SYSTEM = "SYSTEM";
	private static final String USER_DEFAULT = "GUEST";

	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_UNKOWN_LOGINNAME = 1;
	public static final int STATUS_PASSWORD_FAIL = 2;
	public static final int STATUS_FORBIDDEN = 3;
	public static final int STATUS_CHECK_CODE_ERROR = 4;
	public static final int STATUS_UNKOWN_ERROR = 5;
	public static final int STATUS_ACCESS_ERROR = 6;
	public static final int STATUS_NO_ALLOW_LOGIN = 7;
	public static final String[] STATUS_NAMES = { "登录成功!", "帐号不存在或密码错误!", "帐号不存在或密码错误!", "帐号已被禁用!", "校验码错误或已过期!","未知错误,登录失败!","访问权限不足!","帐号不允许登录!" };
	public static final String[] STATUS_LOGS = { "【${username}(${loginname})】登录成功!", "【未知(${loginname})】因帐号不存在,登录失败。",
			"【未知(${loginname})】因密码错误,登录失败。", "【未知(${loginname})】因帐号已被禁用,登录失败。", "【未知(${loginname})】因校验码错误或已过期,登录失败。", "未知错误,登录失败!","访问权限不足!" };


	/**
	 * 处理登录动作
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String loginName = Utils.getParameter(request, "loginName");// 登录帐号
		String passwd = Utils.getParameter(request, "passwd"); // 登录密码
		String checkCode = Utils.getParameter(request, "checkCode");// 校验码
		String lang = request.getParameter("lang"); // 语言
		String loginType = request.getParameter("loginType"); // 从哪里单点登录过来的
		/*StringBuffer path = new StringBuffer(request.getContextPath());
		if (loginType == null)
			loginType = "";
		// 校验用户登录信息
		if ((loginName == null) || (loginName.trim().equals(""))) {
			outPutXML(response, new AlertCommand("alert", "请填入用户名!", null, DialogPosition.middle, 0));
			return null;
		}
		if ((passwd == null) || (passwd.trim().equals(""))) {
			outPutXML(response, new AlertCommand("alert", "请填入密码!", null, DialogPosition.middle, 0));
			return null;
		}
		// 校验Licence证书是否有效
		int validateLicense = 0; // LoginMethods.validateLicense();
		if (validateLicense == 2 || validateLicense == 3) {
			String content = validateLicense == 2 ? "您的证书信息在本机是无效的!" : "您的证书已过期,您需要购买另一个使用许可才能继续使用!";
			outPutXML(response, new AlertCommand("alert", content, null, DialogPosition.middle, 0));
			return null;
		} else {*/
			if(Utils.isEmpty(loginName)){
				ModelAndView modelAndView = new ModelAndView("login.jsp?redirect=1");
				modelAndView.addObject("msg", "账号不能为空");
				modelAndView.addObject("loginName", loginName);
				modelAndView.addObject("passwd", passwd);
				return modelAndView;
			}
			if(Utils.isEmpty(passwd)){
				ModelAndView modelAndView = new ModelAndView("login.jsp?redirect=1");
				modelAndView.addObject("msg", "密码不能为空");
				modelAndView.addObject("loginName", loginName);
				modelAndView.addObject("passwd", passwd);
				return modelAndView;
			}
			User user = null;
			int status = STATUS_UNKOWN_ERROR;
			//String isCheckCode = FrameworkHelper.getSystemConfig("login.isCheckCode", "false");
			String isCheckCode = "false";
			if (isCheckCode.equals("true")&&(checkCode == null || !checkCode.equalsIgnoreCase((String) request.getSession().getAttribute(FrameworkConstants.CHECK_CODE_KEY)))) {
				status = STATUS_CHECK_CODE_ERROR;// 校验码错误
			} else {
				user = ServiceLocator.getUserService().getUserByLoginName(loginName);
				if (user == null) {
					status = STATUS_UNKOWN_LOGINNAME;// 帐号不存在
				} else {
					if("0".equals(user.getUserStatus())){
						status = STATUS_FORBIDDEN; // 用户已被禁用
					}else if("0".equals(user.getAllowLogin())){
						status = STATUS_NO_ALLOW_LOGIN;
					}else if (ServiceLocator.getUserService().checkUserPassword(user, StringUtils.md5(passwd))) {
						if("1".equals(user.getUserType())){//只可服务端用户登录
							status = STATUS_SUCCESS;
						}else{
							status = STATUS_ACCESS_ERROR;
						}
					} else {
						status = STATUS_PASSWORD_FAIL;// 密码错误
					}
				}
			}
			
			if (status == STATUS_SUCCESS) {
				this.httpSession = request.getSession();
				this.httpSession.setAttribute(LOGIN_USER_KEY, user.getUserid());
				String cookieTime = request.getParameter("cookieTime");//单位秒
				if(Utils.notEmpty(cookieTime)){//登录成功
					int parseInt = Integer.parseInt(cookieTime);
					Cookie cookieUserId = new Cookie("userId",user.getUserid());
					cookieUserId.setMaxAge(parseInt);
					Cookie cookieCCode = new Cookie("cookieCode",user.getCookieCode());
					cookieCCode.setMaxAge(parseInt);
					response.addCookie(cookieUserId);
					response.addCookie(cookieCCode);
				}
				// setMaxInactiveInterval和session-config的优先级：
				// 1、setMaxInactiveInterval的优先级高，如果setMaxInactiveInterval没有设置，则默认是session-config中设置的时间。
				// 2、setMaxInactiveInterval设置的是当前会话的失效时间，不是整个web服务的。
				// 3、setMaxInactiveInterval的参数是秒，session-config当中配置的session-timeout是分钟。
				this.httpSession.setMaxInactiveInterval(18000); // 设置了session非活动失效时间，单位为秒

				
//					String loginIp = FrameworkHelper.getClientIpAddr(request); // 当前用户登录IP
				// 设置上下文当前用户登录IP地址
				//ContextHelper.setArg(ContextHelper.CONTEXT_INDEX_IP, loginIp);
				// 清除上下文
				//ContextHelper.clear();
				// 初始化上下文
				//ContextHelper.initContext(user);
				try {
					// 用户成功登录后，记录当前用户的最后登录时间、登录IP地址
//						Date loginTime = new Date();
//						loginTime.setTime(loginTime.getTime());
//						user.setLoginTime(loginTime);
//						user.setLoginIp(loginIp);
//						securityService.updateUser(user);

					// 记录用户登录系统日志
//						AppLogger logger = new LazyInitLogger();
//						String logType = String.valueOf(LogType.ID_INFO);
//						String logContent = getLogContent(status, request.getLocalAddr(), user.getUserName(), loginName);
//						CmsLog log = new CmsLog(null, logType, OperateTypes.OPER_USER_LOGIN,
//								ObjectTypes.ID_OBJ_TYPE_USER, user.getUserId(), user.getLoginName(), user.getUserId(),
//								user.getUpperId(), loginIp, null, ListConstants.RESULT_SUCCEED, logContent);
//						logger.log(AppLogger.Level.info, log, null, LoginController.class, AppLogger.APPENDER_DATABASE,
//								logType, request);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// 登录验证成功后，页面跳转到后台首页。
				this.httpSession.setAttribute("userName", Utils.isEmpty(user.getNickname())?user.getUsername():user.getNickname());
				this.httpSession.setAttribute("userId", user.getUserid());
				ModelAndView modelAndView = new ModelAndView("redirect:admin/template/admin/index.jsp");
				return modelAndView;
			} else {
				String msg = STATUS_NAMES[status];
				ModelAndView modelAndView = new ModelAndView("login.jsp?redirect=1");
				modelAndView.addObject("msg", msg);
				return modelAndView;
			}
		//}
	}


	/**
	 * 退出本系统
	 * 
	 * @param request 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*String auto = Utils.getParameter(request, "auto");
		if(Utils.notEmpty(auto)) { // 由系统前端js自动退出
			Long l = (Long)request.getSession().getAttribute("keepSession");
			if(l != null && System.currentTimeMillis() - l < 30L*60*1000) {
				outPutXML(response,JSCommand.EMPTY);
				return null;
			}
		}
		
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
//		PubUser user = getLoginUser(userId);
//		if (user != null) {
//			AppLogger logger = new LazyInitLogger();
//			String logType = String.valueOf(LogType.ID_INFO);
//			String logContent = null;
//			if("1".equals(auto)){
//				logContent = "用户[" + user.getUserName() + "]长时间未操作自动退出系统";
//			}else{
//				logContent = "用户[" + user.getUserName() + "]退出系统";
//			}
//			String loginIp = FrameworkHelper.getClientIpAddr(request); // 当前用户登录IP
//			CmsLog log = new CmsLog(null, logType, OperateTypes.OPER_USER_LOGOUT, ObjectTypes.ID_OBJ_TYPE_USER, user
//					.getUserId(), user.getLoginName(), user.getUserId(), user.getUpperId(), loginIp, null,
//					ListConstants.RESULT_SUCCEED, logContent);
//			logger.log(AppLogger.Level.info, log, null, LoginController.class, AppLogger.APPENDER_DATABASE, logType,
//					request);
//		}
		this.httpSession = request.getSession();
		if (this.httpSession != null) {
			this.httpSession.removeAttribute(LOGIN_USER_KEY);
			this.httpSession.invalidate();
			//清空Cookie操作 
			Cookie[] cookies=request.getCookies(); 
			try {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = new Cookie(cookies[i].getName(), null);
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		if(Utils.requestFromAjax(request)){
			FrameworkHelper.outPutXML(response, new JSCommand("reload", "window.location.replace(\"login.jsp\");"));
		}else{
			response.sendRedirect("login.jsp");
		}*/
		request.getSession().invalidate();
		response.sendRedirect("login.jsp");
		return null;
	}
	
	/**
	 * 保持session连接
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView keepSession(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("keepSession", System.currentTimeMillis());
		outPutXML(response,JSCommand.EMPTY);
		return null;
	}
	
	/**
	 * 取得当前登录的用户信息
	 * 
	 * @param userId
	 *            当前登录的用户ID
	 * @return
	 */
//	private PubUser getLoginUser(String userId) {
//		if (userId == null)
//			return null;
//		try {
//			return ServiceLocator.getSecurityService().getUserById(userId);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return null;
//	}

	/**
	 * 取得登陆日志内容信息
	 * 
	 * @param status
	 * @param ip
	 * @param userName
	 * @param loginName
	 * @return
	 */
	public static String getLogContent(int status, String ip, String userName, String loginName) {
		String log = STATUS_LOGS[status];
		log = log.replace("${loginname}", loginName);
		if (userName != null)
			log = log.replace("${username}", userName);
		return log;
	}

	/**
	 * 校验码图形(点击刷新)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView validImg(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		String ck = Utils.getParameter(request, "ck");
		if(Utils.isEmpty(ck)){
			ck = FrameworkConstants.CHECK_CODE_KEY;
		}
		int width = 80, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(getRandColor(200, 255));
		g.fillRect(0, 0, width, height);
		randomLine(g, width, height);
		String code = StringUtil.getRadomString(4, true, true, true, false);
		drawChars(g, code);
		g.dispose();
		request.getSession().setAttribute(ck, code);// 把校验码的内容留在session内。
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "PNG", os);
		os.close();
		return null;
	}
	

	/**
	 * 给定范围的随机定义颜色
	 * 
	 * @param fc
	 *            int
	 * @param bc
	 *            int
	 * @return Color
	 */
	static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 产生干扰线条
	 * 
	 * @param g
	 *            Graphics
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @return Graphics
	 */
	static void randomLine(Graphics g, int width, int height) {
		Random random = new Random();
		int x, y, x1, y1;
		for (int i = 0; i < 160; i++) {
			x = random.nextInt(width);
			y = random.nextInt(height);
			x1 = random.nextInt(20) + x;
			y1 = random.nextInt(10) + y;
			g.setColor(getRandColor(150, 200));
			g.drawLine(x, y, x1, y1);
		}
	}

	/**
	 * 绘制文字
	 * 
	 * @param g
	 *            Graphics
	 * @return Object[]
	 */
	static void drawChars(Graphics g, String code) {
		Random random = new Random();
		double rotate = 0.0;
		double lastRotate = 0.0;
		char font;
		for (int i = 0; i < 4; i++) {
			if (g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D) g;
				rotate = (random.nextInt(60) - 30) * Math.PI / 180;
				g2d.rotate(rotate - lastRotate);
				lastRotate = rotate;
				// LinLW 2010-11-18 抗锯齿
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
			}
			font = code.charAt(i);
			g.setFont(new Font("", Font.BOLD, 18));
			g.setColor(getRandColor(0, 150));
			double x = 16 * Math.sin(rotate) + (8 + 18 * i) * Math.cos(rotate);
			double y = 16 * Math.cos(rotate) - (8 + 18 * i) * Math.sin(rotate);
			g.drawString(font + "", (int) x, (int) y);

		}
	}
}