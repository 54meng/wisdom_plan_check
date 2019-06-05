package com.wpc.webapp.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.service.ServiceLocator;
import com.wpc.utils.ExcelUtils;
import com.wpc.utils.FileTypeUtils;
import com.wpc.utils.FileUtil;
import com.wpc.utils.SystemEnvInfo;
import com.wpc.webapp.controller.vo.CallResult;


public class ExcelImportController extends MultiActionController {
	
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/excel_import.jsp");
		return modelAndView;
	}
	
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String belongBank = Utils.getParameter(request, "belongBank");
		//文件上传
		MultipartHttpServletRequest multipartRequest = null;
		if(request instanceof MultipartHttpServletRequest){
			multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile[] files = FrameworkHelper.getFiles(multipartRequest, "files");
			String relativePath = "";
			String floderPath = SystemEnvInfo.getResourcePath() + relativePath;
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					try {
						String originalFileName = files[i].getOriginalFilename();
						String ext = FileTypeUtils.getExtension(originalFileName); //得到文件的后缀名和扩展名
						if(!"xls".equalsIgnoreCase(ext) && !"xlsx".equalsIgnoreCase(ext)){
							outPutResult(response, 1, "文件后缀不合法，必须为.xls或.xlsx");		
							return null;
						}
						File orgFile = FileUtil.saveFile(files[i].getInputStream(), floderPath, originalFileName);
						ExcelUtils.readExcel(orgFile.getAbsolutePath(), belongBank);
											
					} catch (Exception e) {
						e.printStackTrace();
						outPutResult(response, 0, e.getMessage());		
						return null;
					}
				}
			}
		}
		outPutResult(response, 0, "保存成功!");		
		return null;
	}
	
	private void outPutResult(HttpServletResponse response, int code, String msg){
		CallResult callResult = new CallResult();
		callResult.setRet(code);
		callResult.setMsg(msg);
		//以JSON格式输出
		Utils.outPutJson(response, callResult);
	}
}
