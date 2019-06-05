package com.wpc.webapp.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Tag;
import com.wpc.service.ServiceLocator;
import com.wpc.utils.FileTypeUtils;
import com.wpc.utils.FileUtil;
import com.wpc.utils.SystemEnvInfo;
import com.wpc.webapp.controller.utils.DataTableUtils;
import com.wpc.webapp.controller.vo.CallResult;
import com.wpc.webapp.controller.vo.DTRequest;
import com.wpc.webapp.controller.vo.TableData;

/**
 * 标签管理控制类
 * 
 * @author whp
 *
 */
public class TagController extends MultiActionController {
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
					ServiceLocator.getTagService().deleteByIds(dtRequest.getSelectItem());
				}
			}
		}
		//过滤条件
		SearchContainer searchContainer = dtRequest.getSearchContainer();
		searchContainer.orderByDesc("t.id");
		
		List<Tag> list = ServiceLocator.getTagService().findByPage(page, searchContainer);
		TableData tableData = new TableData();
		if(Utils.notEmpty(list)){
			for(Tag entity : list){
				tableData.addDataItem(
						"<input type='checkbox' name='userId' value='"+entity.getId()+"'>",
						entity.getId(),
						entity.getName(),
						"<a href='../../../tag.sp?act=addOrEdit&id="+entity.getId()+"' data-toggle='modal' data-target='#ajax'  class='btn btn-xs default'><i class='fa fa-search'></i>编辑</a>"
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
		String id = request.getParameter("id");
		Tag entity = ServiceLocator.getTagService().findById(id);
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/tag_add_edit.jsp");
		modelAndView.addObject("tag", entity==null?new Tag():entity);	
		modelAndView.addObject("isNew", entity==null);
		return modelAndView;
	}
	
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		Tag entity = ServiceLocator.getTagService().findById(id);
		boolean isNew = false;
		if(entity==null){
			isNew = true;
			entity = new Tag();		
		}

		String name = Utils.getParameter(request, "name");
		
		CallResult callResult = new CallResult();
		
		entity.setName(name);
		

		try {
			if(isNew){
				entity = ServiceLocator.getTagService().save(entity);
			}else{
				entity = ServiceLocator.getTagService().update(entity);
			}
			callResult.setRet(0);
			callResult.setMsg("保存成功！");
			callResult.setRetValue(entity.getId());
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
