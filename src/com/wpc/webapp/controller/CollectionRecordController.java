package com.wpc.webapp.controller;
/*package com.onlybelieve.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.onlybelieve.commons.sql.SearchContainer;
import com.onlybelieve.commons.sql.SearchContainer.Op;
import com.onlybelieve.dfish.dao.Page;
import com.onlybelieve.dfish.util.FileUtil;
import com.onlybelieve.dfish.util.Utils;
import com.onlybelieve.persistence.CiticEnt;
import com.onlybelieve.persistence.CollectionRecord;
import com.onlybelieve.persistence.User;
import com.onlybelieve.service.ServiceLocator;
import com.onlybelieve.utils.ChinaInitial;
import com.onlybelieve.utils.DateUtils;
import com.onlybelieve.utils.ExcelUtils;
import com.onlybelieve.webapp.controller.utils.DataTableUtils;
import com.onlybelieve.webapp.controller.vo.CallResult;
import com.onlybelieve.webapp.controller.vo.DTRequest;
import com.onlybelieve.webapp.controller.vo.TableData;

*//**
 * 催收记录管理控制类
 * 
 * @author imlzw
 *
 *//*
public class CollectionRecordController extends MultiActionController {
	Gson gson = new Gson();
	
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<User> userList = ServiceLocator.getUserService().findUsersByPage(null, null);	
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/record_manager.jsp");
		modelAndView.addObject("userList", Utils.isEmpty(userList) ? new ArrayList<User>() : userList);
		return modelAndView;
	}

	*//**
	 * 加载数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *//*
	public ModelAndView loadData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//获取DataTabel请求过来的数据
		DTRequest dtRequest = DataTableUtils.getDTRequest();
		SearchContainer searchContainer = dtRequest.getSearchContainer();
		searchContainer.orderByDesc("t.createTime");
		Page page = dtRequest.getPage();
		//是否有批量操作？
		if(dtRequest.isHasGroupAction()){
			String action = dtRequest.getGroupAction();
			if(Utils.notEmpty(dtRequest.getSelectItem())){
				//批量删除操作
				if("export".equals(action)){
					
					return null;
				}
			}
		}
		String userRole = (String)request.getSession().getAttribute("userRole");
		String userId = (String)request.getSession().getAttribute("userId");
		if(!"1".equals(userRole) && !"2".equals(userRole)){
			searchContainer.and("t.userId", Op.EQ, userId);
		}
		List<CollectionRecord> list = ServiceLocator.getCollectionRecordService().findByPage(page, searchContainer);
		int currentPage = page.getCurrentPage();
		int pageSize = page.getPageSize();
		int index = (currentPage-1) * pageSize +1;		
		TableData tableData = new TableData();
		if(Utils.notEmpty(list)){
			for(CollectionRecord entity : list){
				tableData.addDataItem(
						"<input type='checkbox' name='cId' value='"+entity.getId()+"'>",
						//index++,
						entity.getCollectionBatch(),
                        entity.getCollectionName(),
                        entity.getCardNum(),
                        entity.getContent(),
                        entity.getUserName(),
                        DateUtils.formatDate(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"),
                        "");
			}
		}
		//设置tableData的总记录与过滤记录
		tableData.setRecordsFiltered(page.getRowCount());
		tableData.setRecordsTotal(page.getRowCount());
		//以JSON格式输出
		Utils.outPutJson(response,tableData);
		return null;
		
	}
	
	public ModelAndView lookRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cId = Utils.getParameter(request, "cId");
		//获取DataTabel请求过来的数据
		DTRequest dtRequest = DataTableUtils.getDTRequest();
		SearchContainer searchContainer = dtRequest.getSearchContainer();
		searchContainer.orderByDesc("t.createTime");
		Page page = dtRequest.getPage();
		//是否有批量操作？
		if(dtRequest.isHasGroupAction()){
			String action = dtRequest.getGroupAction();
			if(Utils.notEmpty(dtRequest.getSelectItem())){
				//批量删除操作
				if("export".equals(action)){
					
					return null;
				}
			}
		}
		searchContainer.and("t.collectionId", Op.EQ, cId);
		List<CollectionRecord> list = ServiceLocator.getCollectionRecordService().findByPage(page, searchContainer);
		int currentPage = page.getCurrentPage();
		int pageSize = page.getPageSize();
		int index = (currentPage-1) * pageSize +1;		
		TableData tableData = new TableData();
		if(Utils.notEmpty(list)){
			for(CollectionRecord entity : list){
				tableData.addDataItem(
						"<input type='checkbox' name='cId' value='"+entity.getId()+"'>",
						index++,
                        entity.getContent(),
                        entity.getUserName(),
                        DateUtils.formatDate(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"),
                        "");
			}
		}
		//设置tableData的总记录与过滤记录
		tableData.setRecordsFiltered(page.getRowCount());
		tableData.setRecordsTotal(page.getRowCount());
		//以JSON格式输出
		Utils.outPutJson(response,tableData);
		return null;
		
	}
	
	public ModelAndView exportExcel(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String[] cIds = request.getParameterValues("cId");
		List<CollectionRecord> cList = ServiceLocator.getCollectionRecordService().findByIds(cIds);
		if(Utils.notEmpty(cList)){
			String[] rowName = new String[]{"序号", "持卡人姓名", "卡号", "账户号", "身份证号", "催收记录", "催收人", "催收时间"};
			List<Object[]> dataList = new ArrayList<Object[]>();
			int i = 1;
			for(CollectionRecord record : cList){
				dataList.add(new Object[]{i++, record.getCollectionName(),record.getCardNum(),record.getAccountNumber(),record.getCertificateNum(),
						record.getContent(), record.getUserName(), DateUtils.formatDate(record.getCreateTime(), "yyyy-MM-dd HH:mm:ss")});
			}
			ExcelUtils.exportExcel(response, "催收记录", rowName, dataList);
		}	
		return null;
		
	}
	
}
*/