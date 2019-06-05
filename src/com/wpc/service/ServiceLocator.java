package com.wpc.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanFactory;

import com.wpc.dfish.framework.SystemData;
import com.wpc.service.impl.AdvertDBMethod;
import com.wpc.service.impl.AppVersionDBMethod;
import com.wpc.service.impl.AuthorDBMethod;
import com.wpc.service.impl.CateDBMethod;
import com.wpc.service.impl.CodeDBMethod;
import com.wpc.service.impl.CollectionDBMethod;
import com.wpc.service.impl.CollectionRecordDBMethod;
import com.wpc.service.impl.CommentDBMethod;
import com.wpc.service.impl.ConfigDBMethod;
import com.wpc.service.impl.CountDBMethod;
import com.wpc.service.impl.FeedbackDBMethod;
import com.wpc.service.impl.FollowDBMethod;
import com.wpc.service.impl.HistoryDBMethod;
import com.wpc.service.impl.PraiseDBMethod;
import com.wpc.service.impl.PurchaseNoticeDBMethod;
import com.wpc.service.impl.SpecialDBMethod;
import com.wpc.service.impl.TagDBMethod;
import com.wpc.service.impl.TeamDBMethod;
import com.wpc.service.impl.VideoDBMethod;

/**
 * 读取Spring Bean服务实例类
 * 
 * @author LZW
 * @version 1.0.0
 * @since 1.0.0 LZW 2012-11-24
 */
public class ServiceLocator {
	private static final Logger logger = Logger.getLogger(ServiceLocator.class);

	private static XmlBeanFactory factory = null;

	/**
	 * 获取指定的bean名称的服务类实例,使用XmlBeanFactory(引用资源)加载配置文件
	 * 
	 * @param beanName
	 *            bean名称
	 * @return
	 */
	public static Object getBean(String beanName) {
		Object bean = null;
		try {
			if (factory == null) {
				factory = SystemData.getInstance().getBeanFactory();
			}
			bean = factory.getBean(beanName);
		} catch (Exception ex) {
			logger.error("获取指定的Bean对象[" + beanName + "]的实例失败，请检查配置文件!", ex);
			ex.printStackTrace();
		}
		return bean;
	}
	/**
	 * 获取用户服务
	 * @return
	 */
	public static UserService getUserService(){
		UserService bean = (UserService)getBean("userService");
		return bean;
	}
	
	
	public static FollowDBMethod getFollowService(){
		FollowDBMethod bean = (FollowDBMethod)getBean("followService");
		return bean;
	}
	
	public static TeamDBMethod getTeamService(){
		TeamDBMethod bean = (TeamDBMethod)getBean("teamService");
		return bean;
	}
	
	public static PraiseDBMethod getPraiseService(){
		PraiseDBMethod bean = (PraiseDBMethod)getBean("praiseService");
		return bean;
	}
	
	public static CommentDBMethod getCommentService(){
		CommentDBMethod bean = (CommentDBMethod)getBean("commentService");
		return bean;
	}
	
	public static VideoDBMethod getVideoService(){
		VideoDBMethod bean = (VideoDBMethod)getBean("videoService");
		return bean;
	}
	
	public static CateDBMethod getCateService(){
		CateDBMethod bean = (CateDBMethod)getBean("cateService");
		return bean;
	}
	
	public static SpecialDBMethod getSpecialService(){
		SpecialDBMethod bean = (SpecialDBMethod)getBean("specialService");
		return bean;
	}
	
	public static AuthorDBMethod getAuthorService(){
		AuthorDBMethod bean = (AuthorDBMethod)getBean("authorService");
		return bean;
	}

	public static TagDBMethod getTagService(){
		TagDBMethod bean = (TagDBMethod)getBean("tagService");
		return bean;
	}
	
	public static AdvertDBMethod getAdvertService(){
		AdvertDBMethod bean = (AdvertDBMethod)getBean("advertService");
		return bean;
	}
	
	public static HistoryDBMethod getHistoryService(){
		HistoryDBMethod bean = (HistoryDBMethod)getBean("historyService");
		return bean;
	}
	
	public static CountDBMethod getCountService(){
		CountDBMethod bean = (CountDBMethod)getBean("countService");
		return bean;
	}
	
	public static CodeDBMethod getCodeService(){
		CodeDBMethod bean = (CodeDBMethod)getBean("codeService");
		return bean;
	}
	
	public static ConfigDBMethod getConfigService(){
		ConfigDBMethod bean = (ConfigDBMethod)getBean("configService");
		return bean;
	}
	
	public static FeedbackDBMethod getFeedbackService(){
		FeedbackDBMethod bean = (FeedbackDBMethod)getBean("feedbackService");
		return bean;
	}
	
	public static AppVersionDBMethod getAppVersionService(){
		AppVersionDBMethod bean = (AppVersionDBMethod)getBean("appVersionService");
		return bean;
	}
	
	public static PurchaseNoticeDBMethod getPurchaseNoticeService(){
		PurchaseNoticeDBMethod bean = (PurchaseNoticeDBMethod)getBean("purchaseNoticeService");
		return bean;
	}
}

