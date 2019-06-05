package com.wpc.webapp.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.AppVersion;
import com.wpc.service.ServiceLocator;
import com.wpc.utils.FileUtil;
import com.wpc.utils.SystemEnvInfo;


public class AppVersionController extends MultiActionController {
	Gson gson = new Gson();
	SimpleDateFormat pathFormat = new SimpleDateFormat("yyyy"+File.separator+"MM"+File.separator+"dd");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public ModelAndView addOrEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<AppVersion> list = ServiceLocator.getAppVersionService().findList();
		AppVersion appVersion = null;
		if(Utils.notEmpty(list)){
			appVersion = list.get(0);
			String apkUrl = appVersion.getApkUrl();
			String ipaUrl = appVersion.getIpaUrl();
			if(Utils.notEmpty(apkUrl)){
				appVersion.setApkUrl(apkUrl.substring(apkUrl.lastIndexOf("\\")+1));
			}
			if(Utils.notEmpty(ipaUrl)){
				appVersion.setIpaUrl(ipaUrl.substring(ipaUrl.lastIndexOf("\\")+1));
			}
		}
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/app_version_mgr.jsp");
		modelAndView.addObject("appVersion", appVersion);		
		
		return modelAndView;
	}
	
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		AppVersion entity = ServiceLocator.getAppVersionService().findById(id);
		boolean isNew = false;
		if(entity==null){
			isNew = true;
			entity = new AppVersion();		
		}

		String androidVersion = Utils.getParameter(request, "androidVersion");
		String iosVersion = Utils.getParameter(request, "iosVersion");
		String androidUpdContent = Utils.getParameter(request, "androidUpdContent");
		String iosUpdContent = Utils.getParameter(request, "iosUpdContent");
		String androidDownloadUrl = Utils.getParameter(request, "androidDownloadUrl");
		String iosDownloadUrl = Utils.getParameter(request, "iosDownloadUrl");
		String iosIsForce = Utils.getParameter(request, "iosIsForce");
		String androidIsForce = Utils.getParameter(request, "androidIsForce");
		entity.setAndroidVersion(androidVersion);
		entity.setIosVersion(iosVersion);
		entity.setAndroidUpdContent(androidUpdContent);
		entity.setIosUpdContent(iosUpdContent);
		entity.setAndroidDownloadUrl(androidDownloadUrl);
		entity.setIosDownloadUrl(iosDownloadUrl);
		entity.setIosIsForce(Integer.parseInt(iosIsForce));
		entity.setAndroidIsForce(Integer.parseInt(androidIsForce));
		
		//文件上传
		MultipartHttpServletRequest multipartRequest = null;
		String apkUrl = null;
		String ipaUrl = null;
		if(request instanceof MultipartHttpServletRequest){
			multipartRequest = (MultipartHttpServletRequest) request;
			String floderPath = SystemEnvInfo.getResourcePath();
			MultipartFile[] apkUrlFile = FrameworkHelper.getFiles(multipartRequest, "apkUrl");
			if (apkUrlFile != null && apkUrlFile.length > 0) {
				for (int i = 0; i < apkUrlFile.length; i++) {
					try {
						String originalFileName = apkUrlFile[i].getOriginalFilename();
						FileUtil.saveFile(apkUrlFile[i].getInputStream(), floderPath, originalFileName);						
						apkUrl = SystemEnvInfo.getResourceRelativePath()+File.separator+originalFileName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			MultipartFile[] ipaUrlFile = FrameworkHelper.getFiles(multipartRequest, "ipaUrl");
			if (ipaUrlFile != null && ipaUrlFile.length > 0) {
				for (int i = 0; i < ipaUrlFile.length; i++) {
					try {
						String originalFileName = ipaUrlFile[i].getOriginalFilename();
						FileUtil.saveFile(ipaUrlFile[i].getInputStream(), floderPath, originalFileName);						
						ipaUrl = SystemEnvInfo.getResourceRelativePath()+File.separator+originalFileName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		if(Utils.notEmpty(apkUrl)){
			entity.setApkUrl(apkUrl);
		}	
		if(Utils.notEmpty(ipaUrl)){
			entity.setIpaUrl(ipaUrl);
		}
		try {
			if(isNew){
				ServiceLocator.getAppVersionService().save(entity);
			}else{
				ServiceLocator.getAppVersionService().update(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		Utils.outPutJson(response, 1);
		return null;
		
	}
	
}
