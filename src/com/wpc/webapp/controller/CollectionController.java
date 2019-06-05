package com.wpc.webapp.controller;
/*package com.onlybelieve.webapp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.onlybelieve.commons.sql.SearchContainer;
import com.onlybelieve.commons.sql.SearchContainer.Op;
import com.onlybelieve.dfish.dao.Page;
import com.onlybelieve.dfish.util.Utils;
import com.onlybelieve.persistence.CiticEnt;
import com.onlybelieve.persistence.CollectionRecord;
import com.onlybelieve.persistence.User;
import com.onlybelieve.service.ServiceLocator;
import com.onlybelieve.utils.ChinaInitial;
import com.onlybelieve.webapp.controller.utils.DataTableUtils;
import com.onlybelieve.webapp.controller.vo.CallResult;
import com.onlybelieve.webapp.controller.vo.DTRequest;
import com.onlybelieve.webapp.controller.vo.TableData;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

*//**
 * 催收记录管理控制类
 * 
 * @author imlzw
 *
 *//*
public class CollectionController extends MultiActionController {
	Gson gson = new Gson();
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cType = request.getParameter("cType");
		List<User> userList = ServiceLocator.getUserService().findUsersByPage(null, null);	
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/collection_manager.jsp?cType="+cType);
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
		String cType = request.getParameter("cType");
		//获取DataTabel请求过来的数据
		DTRequest dtRequest = DataTableUtils.getDTRequest();		
		//是否有批量操作？
		if(dtRequest.isHasGroupAction()){
			String action = dtRequest.getGroupAction();
			if(Utils.notEmpty(dtRequest.getSelectItem())){
				//批量删除操作
				if("delete".equals(action)){
					ServiceLocator.getCollectionService().deleteByIds(dtRequest.getSelectItem(), "5".equals(cType));
				}else if("recover".equals(action)){
					ServiceLocator.getCollectionService().recoverByIds(dtRequest.getSelectItem());
				}
			}
		}
		SearchContainer searchContainer = dtRequest.getSearchContainer();
		boolean isQuery = searchContainer.isQuery;
		Page page = dtRequest.getPage();
		String userRole = (String)request.getSession().getAttribute("userRole");
		String userId = (String)request.getSession().getAttribute("userId");
		if(!"1".equals(userRole) && !"2".equals(userRole) && !isQuery){
			searchContainer.and("t.operUserId", Op.EQ, userId);
		}
		List<CiticEnt> list = ServiceLocator.getCollectionService().findCiticsByPage(page, searchContainer, cType);
		int currentPage = page.getCurrentPage();
		int pageSize = page.getPageSize();
		int index = (currentPage-1) * pageSize +1;		
		TableData tableData = new TableData();
		if(Utils.notEmpty(list)){
			for(CiticEnt citic : list){
				tableData.addDataItem(
						"<input type='checkbox' name='id' value='"+citic.getId()+"'>",
                        //index++,
                        citic.getEntrustBatch(),
                        citic.getCardholderName(),
                        citic.getCardNum(),
                        citic.getCertificateNum(),
                        citic.getCardholderMobilePhone(),
                        citic.getBalance(),
                        citic.getPrincipalRmb(),
                        "1".equals(citic.getBelongBank()) ? "中信银行" : "浦发银行",
                        citic.getOperUserName(),
                        "<a href='../../../collection.sp?act=recordDetail&cId="+citic.getId()+"' data-toggle='modal' data-target='#ajax'  class='btn btn-xs default'>查看</a>" +
						(!userId.equals(citic.getOperUserId()) && "1".equals(userRole) && "2".equals(userRole) ? " " : "<a href='../../../collection.sp?act=addRecord&cId="+citic.getId()+"&cName="+citic.getCardholderName()+"&cBatch="+citic.getEntrustBatch()+"' data-toggle='modal' data-target='#ajax'  class='btn btn-xs default'>添加催记</a>")+
						"<a href='look_record.jsp?cId="+citic.getId()+"' data-toggle='modal' data-target='#ajax'  class='btn btn-xs default'>查看催记</a>"
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
	
	public ModelAndView assignedTask(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cIds = request.getParameter("cIds");
		List<User> userList = ServiceLocator.getUserService().findUsersByPage(null, null);
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/assigned_task.jsp");
		modelAndView.addObject("userList", userList);
		modelAndView.addObject("cIds", cIds);
		return modelAndView;
	}
	
	public ModelAndView saveAssignedTask(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cIds = request.getParameter("cIds");
		String userId = request.getParameter("userId");
		CallResult callResult = new CallResult();
		try {
			ServiceLocator.getCollectionService().updateOperUser(cIds.split(","), userId);
			callResult.setRet(0);
			callResult.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			callResult.setRet(1);
			callResult.setMsg(e.getMessage());
		}
		//以JSON格式输出
		Utils.outPutJson(response,callResult);
		return null;
	}
	
	public ModelAndView updateStatusBatch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cIds = request.getParameter("cIds");
		List<User> userList = ServiceLocator.getUserService().findUsersByPage(null, null);
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/update_status_batch.jsp");
		modelAndView.addObject("userList", userList);
		modelAndView.addObject("cIds", cIds);
		return modelAndView;
	}
	
	public ModelAndView saveUpdateStatusBatch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cIds = request.getParameter("cIds");
		String status = request.getParameter("status");
		CallResult callResult = new CallResult();
		try {
			ServiceLocator.getCollectionService().updateStatus(cIds.split(","), status);
			callResult.setRet(0);
			callResult.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			callResult.setRet(1);
			callResult.setMsg(e.getMessage());
		}
		//以JSON格式输出
		Utils.outPutJson(response,callResult);
		return null;
	}
	
	public ModelAndView addRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cId = request.getParameter("cId");
		CiticEnt citicEnt = ServiceLocator.getCollectionService().findById(cId);
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/record_add_edit.jsp");
		modelAndView.addObject("citicEnt", citicEnt);
		return modelAndView;
	}
	
	public ModelAndView recordDetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cId = request.getParameter("cId");
		CiticEnt citic = ServiceLocator.getCollectionService().findById(cId);
		citic.setBelongBank("1".equals(citic.getBelongBank()) ? "中信银行" : "浦发银行");
		ModelAndView modelAndView = new ModelAndView("admin/template/admin/record_detail.jsp");
		modelAndView.addObject("citic", citic);
		return modelAndView;
	}
	
	public ModelAndView saveRecord(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cId = request.getParameter("cId");
		String content = request.getParameter("content");
		CiticEnt citicEnt = ServiceLocator.getCollectionService().findById(cId);
		CallResult callResult = new CallResult();
		CollectionRecord record = new CollectionRecord();
		record.setCollectionId(cId);
		record.setCollectionName(citicEnt.getCardholderName());
		record.setCollectionBatch(citicEnt.getEntrustBatch());
		record.setCardNum(citicEnt.getCardNum());
		record.setAccountNumber(citicEnt.getAccountNumber());
		record.setCertificateNum(citicEnt.getCertificateNum());
		record.setContent(content);
		record.setCreateTime(new Date());
		record.setUserId((String)request.getSession().getAttribute("userId"));
		record.setUserName((String)request.getSession().getAttribute("userName"));
		try {
			ServiceLocator.getCollectionRecordService().save(record);
			callResult.setRet(0);
			callResult.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			callResult.setRet(1);
			callResult.setMsg(e.getMessage());
		}
		//以JSON格式输出
		Utils.outPutJson(response,callResult);
		return null;
		
	}
	
	public ModelAndView editCollection(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cId = request.getParameter("cId");
		String balance = request.getParameter("balance");
		String principalRmb = request.getParameter("principalRmb");
		String cardholderMobilePhone = request.getParameter("cardholderMobilePhone");
		String cardholderCompanyPhone = request.getParameter("cardholderCompanyPhone");
		String cardholderHomePhone = request.getParameter("cardholderHomePhone");
		String contactsMobilePhone = request.getParameter("contactsMobilePhone");
		String contactsHomePhone = request.getParameter("contactsHomePhone");
		String contactsCompanyPhone = request.getParameter("contactsCompanyPhone");
		String relativesMobilePhone = request.getParameter("relativesMobilePhone");
		String relativesHomePhone = request.getParameter("relativesHomePhone");
		String relativesOfficePhone = request.getParameter("relativesOfficePhone");
		String cardholderCompanyAddress = request.getParameter("cardholderCompanyAddress");
		String cardholderHomeAddress = request.getParameter("cardholderHomeAddress");
		String cardholderPostalAddress = request.getParameter("cardholderPostalAddress");
		String contactsHomeAddress = request.getParameter("contactsHomeAddress");
		String contactsCompanyAddress = request.getParameter("contactsCompanyAddress");
		String remarks = request.getParameter("remarks");
		String operStatus = request.getParameter("operStatus");
		CallResult callResult = new CallResult();
		
		try {
			CiticEnt citicEnt = ServiceLocator.getCollectionService().findById(cId);
			citicEnt.setBalance(balance);
			citicEnt.setPrincipalRmb(principalRmb);
			citicEnt.setCardholderMobilePhone(cardholderMobilePhone);
			citicEnt.setCardholderCompanyPhone(cardholderCompanyPhone);
			citicEnt.setCardholderHomePhone(cardholderHomePhone);
			citicEnt.setContactsMobilePhone(contactsMobilePhone);
			citicEnt.setContactsHomePhone(contactsHomePhone);
			citicEnt.setContactsCompanyPhone(contactsCompanyPhone);
			citicEnt.setRelativesMobilePhone(relativesMobilePhone);
			citicEnt.setRelativesHomePhone(relativesHomePhone);
			citicEnt.setRelativesOfficePhone(relativesOfficePhone);
			citicEnt.setCardholderCompanyAddress(cardholderCompanyAddress);
			citicEnt.setCardholderHomeAddress(cardholderHomeAddress);
			citicEnt.setCardholderPostalAddress(cardholderPostalAddress);
			citicEnt.setContactsHomeAddress(contactsHomeAddress);
			citicEnt.setContactsCompanyAddress(contactsCompanyAddress);
			citicEnt.setRemarks(remarks);
			citicEnt.setOperStatus(operStatus);
			ServiceLocator.getCollectionService().update(citicEnt);
			callResult.setRet(0);
			callResult.setMsg("保存成功！");
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
*/