package com.wpc.webapp.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.wpc.commons.sql.SearchContainer;
import com.wpc.commons.sql.SearchContainer.Op;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Category;
import com.wpc.persistence.User;
import com.wpc.service.ServiceLocator;
import com.wpc.utils.FileTypeUtils;
import com.wpc.utils.FileUtil;
import com.wpc.utils.SecurityUtils;
import com.wpc.utils.SystemEnvInfo;
import com.wpc.utils.string.StringUtils;
import com.wpc.webapp.controller.utils.DataTableUtils;
import com.wpc.webapp.controller.vo.CallResult;
import com.wpc.webapp.controller.vo.DTRequest;
import com.wpc.webapp.controller.vo.TableData;

/**
 * 分类管理控制类
 * 
 * @author whp
 *
 */
public class CateController extends MultiActionController {
	Gson gson = new Gson();
	SimpleDateFormat pathFormat = new SimpleDateFormat("yyyy"+File.separator+"MM"+File.separator+"dd");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	int index = 1;
	
	/**
	 * 加载数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView loadData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//获取DataTabel请求过来的数据
		DTRequest dtRequest = DataTableUtils.getDTRequest();
		Page page = dtRequest.getPage();
		//是否有批量操作？
		if(dtRequest.isHasGroupAction()){
			String action = dtRequest.getGroupAction();
			if(Utils.notEmpty(dtRequest.getSelectItem())){
				//批量删除操作
				if("delete".equals(action)){
					ServiceLocator.getCateService().deleteByIds(dtRequest.getSelectItem());
				}
			}
		}
		//过滤条件
		SearchContainer searchContainer = dtRequest.getSearchContainer();
		searchContainer.orderByDesc("t.cateId");
		
		List<Category> list = ServiceLocator.getCateService().findByPage(page, searchContainer);
		TableData tableData = new TableData();
		if(Utils.notEmpty(list)){
			for(Category entity : list){
				tableData.addDataItem(
						"<input type='checkbox' name='userId' value='"+entity.getCateId()+"'>",
						entity.getCateId(),
						entity.getCateName(),												
						"<a href='../../../cate.sp?act=addOrEdit&cateId="+entity.getCateId()+"' data-toggle='modal' data-target='#ajax'  class='btn btn-xs default'><i class='fa fa-search'></i>编辑</a>"
						);
			}
		}
		//设置tableData的总记录与过滤记录
		tableData.setRecordsFiltered(page.getRowCount());
		tableData.setRecordsTotal(page.getRowCount());
		//以JSON格式输出
		Utils.outPutJson(response,tableData);
		return null;
		
	}
	
	public ModelAndView addOrEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cateId = request.getParameter("cateId");
		Category entity = ServiceLocator.getCateService().findById(cateId);
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/cate_add_edit.jsp");
		modelAndView.addObject("category", entity==null?new Category():entity);	
		modelAndView.addObject("isNew", entity==null);
		return modelAndView;
	}
	
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cateId = request.getParameter("cateId");
		Category entity = ServiceLocator.getCateService().findById(cateId);
		boolean isNew = false;
		if(entity==null){
			isNew = true;
			entity = new Category();		
		}

		String cateName = Utils.getParameter(request, "cateName");
		
		CallResult callResult = new CallResult();
		
		entity.setCateName(cateName);
		
		//文件上传
		MultipartHttpServletRequest multipartRequest = null;
		String coverUrl = null;
		if(request instanceof MultipartHttpServletRequest){
			multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile[] files = FrameworkHelper.getFiles(multipartRequest, "files");
			String relativePath = File.separator+"image";
			String floderPath = SystemEnvInfo.getResourcePath() + relativePath;
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					try {
						String originalFileName = files[i].getOriginalFilename();
						String ext = FileTypeUtils.getExtension(originalFileName); //得到文件的后缀名和扩展名
						String uuid = UUID.randomUUID().toString().replaceAll("-", "");
						String realFileName = uuid+"."+ext;
						long size = files[i].getSize();
						size = size / (1024 * 1024);// 以兆为单位
						File orgFile = FileUtil.saveFile(files[i].getInputStream(), floderPath, realFileName);
						//生成指定的规格的图片
						/*String thumb80x80Name = floderPath+File.separator+"thumb.80x80."+ext;
						Thumbnails.of(orgFile).size(80, 80).toFile(thumb80x80Name);
						String thumb400x400Name = floderPath+File.separator+"thumb.400x400."+ext;
						Thumbnails.of(orgFile).size(400, 400).toFile(thumb400x400Name);
						String thumb800x800Name = floderPath+File.separator+"thumb.800x800."+ext;
						Thumbnails.of(orgFile).size(800, 800).toFile(thumb800x800Name);*/
						coverUrl = SystemEnvInfo.getResourceRelativePath()+relativePath+File.separator+realFileName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(Utils.notEmpty(coverUrl)){
			entity.setCoverUrl(coverUrl);
		}

		try {
			if(isNew){
				entity = ServiceLocator.getCateService().save(entity);
			}else{
				entity = ServiceLocator.getCateService().update(entity);
			}
			callResult.setRet(0);
			callResult.setMsg("保存成功！");
			callResult.setRetValue(entity.getCateId());
		} catch (Exception e) {
			e.printStackTrace();
			callResult.setRet(1);
			callResult.setMsg(e.getMessage());
		}
		
		//以JSON格式输出
		Utils.outPutJson(response,callResult);
		return null;
		
	}
	
}
