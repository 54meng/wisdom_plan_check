package com.wpc.dfish.framework;

import java.sql.Connection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import com.rongji.dfish.base.info.EthNetInfo;
import com.wpc.dfish.util.DataBaseInfo;
import com.wpc.dfish.util.ServletInfo;
import com.wpc.dfish.util.SystemInfo;
import com.wpc.dfish.util.XMLTools;
import com.wpc.utils.UpdateRemainCountTimer;

public class InitApp {

	public void destroy() {
		System.out.println("====== application has been destroy ======");
	}

	private static boolean inited = false;

	public static void init(ServletContext contetxt) {
		if (inited)
			return;
		synchronized (InitApp.class) {
			if (inited)
				return;
			try {

				System.out.println("====== initing application ======");
				SystemData.getInstance().setServletInfo(
						new ServletInfo(contetxt));

				String configPath = contetxt
						.getRealPath("WEB-INF/config/travel-config.xml");
				SystemData.getInstance().setSystemConfig(
						new XMLTools(configPath, 256));

				String daoPath = contetxt
						.getRealPath("WEB-INF/applicationContext.xml");
				SystemData.getInstance().setBeanFactory(
						new XmlBeanFactory(new FileSystemResource(daoPath)));
				// 如果支持数据库链接的话。

				DataSource ds = (DataSource) SystemData.getInstance()
						.getBeanFactory().getBean("dataSource");
				if (ds != null) {
					try {
						Connection conn = ds.getConnection();
						SystemData.getInstance().setDataBaseInfo(
								new DataBaseInfo(conn));
						conn.close();
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}

				SystemInfo si = SystemData.getInstance().getSysinfo();
				System.out
						.println("os name       = " + si.getOperationSystem());
				System.out.println("file encoding = " + si.getFileEncoding());

				System.out.println("====  config infomation  ====");
				System.out.println("framework config file path: " + configPath);
				System.out.println("spring config file path: " + daoPath);

				System.out.println("====  servlet infomation  ====");
				System.out.println("version   = "
						+ SystemData.getInstance().getServletInfo()
								.getServletVersion());
				System.out.println("real path = "
						+ SystemData.getInstance().getServletInfo()
								.getServletRealPath());
				System.out.println("====  local mac(s)  ====");
				for (Iterator iter = EthNetInfo.getAllMacAddress().iterator(); iter
						.hasNext();) {
					String item = (String) iter.next();
					System.out.println(item);
				}

			} catch (Throwable t) {
				System.out.println("====== application init fail! ======");
				t.printStackTrace();
			}
			inited = true;
		}
	}

}
