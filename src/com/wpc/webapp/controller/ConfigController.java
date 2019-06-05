package com.wpc.webapp.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.wpc.commons.sql.SearchContainer;
import com.wpc.commons.sql.SearchContainer.Op;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Author;
import com.wpc.persistence.Category;
import com.wpc.persistence.Config;
import com.wpc.persistence.Special;
import com.wpc.persistence.Tag;
import com.wpc.persistence.Team;
import com.wpc.persistence.User;
import com.wpc.persistence.Video;
import com.wpc.service.ServiceLocator;
import com.wpc.utils.FileTypeUtils;
import com.wpc.utils.FileUtil;
import com.wpc.utils.FileUtils;
import com.wpc.utils.SecurityUtils;
import com.wpc.utils.SystemEnvInfo;
import com.wpc.utils.string.StringUtils;
import com.wpc.webapp.controller.utils.DataTableUtils;
import com.wpc.webapp.controller.vo.CallResult;
import com.wpc.webapp.controller.vo.DTRequest;
import com.wpc.webapp.controller.vo.TableData;

/**
 * 配置管理控制类
 * 
 * @author whp
 *
 */
public class ConfigController extends MultiActionController {
	Gson gson = new Gson();
	SimpleDateFormat pathFormat = new SimpleDateFormat("yyyy"+File.separator+"MM"+File.separator+"dd");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public ModelAndView addOrEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Config> list = ServiceLocator.getConfigService().findList();
		Config config = null;
		if(Utils.notEmpty(list)){
			config = list.get(0);
		}
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/config.jsp");
		modelAndView.addObject("config", config);		
		
		return modelAndView;
	}
	
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		Config config = ServiceLocator.getConfigService().findById(id);
		boolean isNew = false;
		if(config==null){
			isNew = true;
			config = new Config();		
		}

		String downloadUrl = Utils.getParameter(request, "downloadUrl");
		String shareText = Utils.getParameter(request, "shareText");
		String qqGroupNo = Utils.getParameter(request, "qqGroupNo");
		config.setDownloadUrl(downloadUrl);
		config.setShareText(shareText);
		config.setQqGroupNo(qqGroupNo);
		
		//文件上传
		MultipartHttpServletRequest multipartRequest = null;
		String qrCodeUrl = null;
		if(request instanceof MultipartHttpServletRequest){
			multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile[] image = FrameworkHelper.getFiles(multipartRequest, "image");
			String relativePath = File.separator + "image";
			String floderPath = SystemEnvInfo.getResourcePath() + relativePath;
			if (image != null && image.length > 0) {
				for (int i = 0; i < image.length; i++) {
					try {
						String originalFileName = image[i].getOriginalFilename();
						String ext = FileTypeUtils.getExtension(originalFileName); //得到文件的后缀名和扩展名
						String uuid = UUID.randomUUID().toString().replaceAll("-", "");
						String realFileName = uuid+"."+ext;
						long size = image[i].getSize();
						size = size / (1024 * 1024);// 以兆为单位
						File orgFile = FileUtil.saveFile(image[i].getInputStream(), floderPath, realFileName);						
						qrCodeUrl = SystemEnvInfo.getResourceRelativePath()+relativePath+File.separator+realFileName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		if(Utils.notEmpty(qrCodeUrl)){
			config.setQrCodeUrl(qrCodeUrl);
		}	
		
		try {
			if(isNew){
				ServiceLocator.getConfigService().save(config);
			}else{
				ServiceLocator.getConfigService().update(config);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		Utils.outPutJson(response, 1);
		return null;
		
	}
	
}
