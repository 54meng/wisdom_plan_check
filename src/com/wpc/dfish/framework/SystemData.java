package com.wpc.dfish.framework;

import java.io.OutputStreamWriter;

import org.apache.log4j.ConsoleAppender;

import org.apache.log4j.PatternLayout;
import org.springframework.beans.factory.xml.XmlBeanFactory;

import com.wpc.dfish.util.DataBaseInfo;
import com.wpc.dfish.util.ServletInfo;
import com.wpc.dfish.util.SystemInfo;
import com.wpc.dfish.util.XMLTools;


/**
 * 系统信息 这些信息将在启动的时候加载，供其他功能调用
 * 
 * @author I-TASK TEAM
 * 
 */
public class SystemData {
	private XmlBeanFactory factory;

	private SystemInfo sysinfo = new SystemInfo();

	/**
	 * 设置spring的bean工厂
	 * 
	 * @param factory
	 */
	public void setBeanFactory(XmlBeanFactory factory) {
		this.factory = factory;
	}

	/**
	 * 取得spring的bean工厂
	 * 比如取得某个Bean 可以使用
	 * <pre>
	 * XmlBeanFactory bf = SystemData.getInstance().getBeanFactory();
	 * MyClass bean = (MyClass) bf.getBean("idDefinedInSpringConfig");
	 * </pre>
	 * @return
	 */
	public XmlBeanFactory getBeanFactory() {
		return factory;
	}
	/**
	 * 取得一个日志记录器
	 * @param clz
	 * @return
	 * @deprecated 将使用自定义的Logger
	 */
	@Deprecated
	public static org.apache.log4j.Logger getLogger(Class clz) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(clz);
		if (!logger.getAllAppenders().hasMoreElements()) {
			System.out
					.println("====== no log4j appender.try to config one. ====== ");
			ConsoleAppender capp = new ConsoleAppender();
			PatternLayout layout = new PatternLayout();
			layout
					.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss,SSS} [%c]-[%p] %m%n");
			capp.setTarget(ConsoleAppender.SYSTEM_OUT);
			capp.setName("default");
			capp.setWriter(new OutputStreamWriter(System.out));
			capp.setLayout(layout);
			
			logger.addAppender(capp);
		}
		return logger;
	}

	private XMLTools systemConfig;

	private SystemData() {}

	private static SystemData instance;
	/**
	 * 取得实例
	 * @return
	 */
	public static SystemData getInstance() {
		if (instance == null)
			synchronized (SystemData.class) {
				if (instance == null)
					instance = new SystemData();
			}
		return instance;
	}

	/**
	 * 取得系统配置
	 * @return
	 */
	public XMLTools getSystemConfig() {
		return systemConfig;
	}
	/**
	 * 设置系统配置，这个一般不由应用系统设置，而是由平台调用
	 * @param systemConfig
	 */
	public void setSystemConfig(XMLTools systemConfig) {
		this.systemConfig = systemConfig;
	}

	private DataBaseInfo dataBaseInfo = null;
	/**
	 * 获取数据库信息
	 * @return
	 */
	public DataBaseInfo getDataBaseInfo() {
		return dataBaseInfo;
	}
	/**
	 * 设置数据库信息，这个一般不由应用系统设置，而是由平台调用
	 * @param dataBaseInfo
	 */
	public void setDataBaseInfo(DataBaseInfo dataBaseInfo) {
		this.dataBaseInfo = dataBaseInfo;
	}

	private ServletInfo servletInfo;
	/**
	 * 设置Servlet信息，这个一般不由应用系统设置，而是由平台调用
	 * @param info
	 */
	public void setServletInfo(ServletInfo info) {
		servletInfo = info;
	}

	/**
	 * 取得Servlet信息
	 * @param info
	 */
	public ServletInfo getServletInfo() {
		return servletInfo;
	}

	/**
	 * 取得系统信息
	 * @param info
	 */
	public SystemInfo getSysinfo() {
		return sysinfo;
	}
}
