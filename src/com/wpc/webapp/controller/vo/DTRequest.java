package com.wpc.webapp.controller.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.wpc.commons.sql.SearchContainer;
import com.wpc.commons.sql.SearchContainer.Op;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.util.Utils;

/**
 * DataTable 对应的请求对象
 * 可以从这个对象中获取相关请求参数，如Page,request.getParameter()等
 * @author imlzw
 *
 */
public class DTRequest {
	private Page page;//分页数据
	private boolean hasGroupAction=false;//是否为指操作
	private String groupAction;//指操作动作名
	private String[] selectItem;//组操作时，选择的数据
	private Map<String,String[]> paramsMap;//request.getParams
	private SearchContainer searchContainer;//过滤条件
	/**
	 * 默认构造函数
	 */
	public DTRequest(HttpServletRequest request){
		if(request!=null){
			paramsMap = (Map<String,String[]>)request.getParameterMap();
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			page = new Page();
			try {
				page.setPageSize(Integer.parseInt(length));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				page.setCurrentPage((int) ((Long.parseLong(start))/page.getPageSize()+1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			selectItem = request.getParameterValues("selectItem[]");
			String groupAction = request.getParameter("groupAction");
			if("true".equals(groupAction)){
				hasGroupAction = true;
				this.groupAction = request.getParameter("groupActionType");
			}
			searchContainer = new SearchContainer();
			if(paramsMap!=null){
				for(Iterator<String> itor = paramsMap.keySet().iterator();itor.hasNext();){
					String key = itor.next();
					System.out.println(key);
					if(key.startsWith("filter_")){
						String[] filterInfo = key.split("_");
						String field = filterInfo[1];
						String op = filterInfo[2];
						String[] values = getParameterValues(key);
						if(Utils.notEmpty(values)){
							if(values.length<=1){
								if(null != values[0] && Utils.notEmpty(values[0].trim())){
									searchContainer.isQuery = true;
									values[0] = values[0].trim();
									if("cardholderMobilePhone".equals(field)){
										searchContainer.setExtraSql("(t.cardholderMobilePhone like '%"+values[0]+"%' " +
												"or t.cardholderCompanyPhone like '%"+values[0]+"%' " +
												"or t.cardholderHomePhone like '%"+values[0]+"%' " +
												"or t.contactsMobilePhone like '%"+values[0]+"%' " +
												"or t.contactsHomePhone like '%"+values[0]+"%' " +
												"or t.contactsCompanyPhone like '%"+values[0]+"%' " +
												"or t.relativesMobilePhone like '%"+values[0]+"%' " +
												"or t.relativesHomePhone like '%"+values[0]+"%' " +
												"or t.relativesOfficePhone like '%"+values[0]+"%' )");
									}else if("createTime".equals(field)){
										values[0] += "GE".equals(op) ? " 00:00:00" : "23:59:59";
										searchContainer.and("t."+field, Op.valueOf(op), values[0]);
									}else if("userName".equals(field)){
										searchContainer.extraSqlMap.put("userName", " and t.userName like '%"+values[0]+"%'");
									}else{
										searchContainer.and("t."+field, Op.valueOf(op), values[0]);
									}
																												
								}
							}else{
								List<String> vals = new ArrayList<String>();
								for(String v : values){
									if(Utils.notEmpty(v)){
										vals.add(v);
									}
								}
								if(Utils.notEmpty(vals)){
									searchContainer.and("t."+field, Op.valueOf(op), vals);
								}
							}
						}
					}
					if(key.startsWith("efilter#")){
						String[] filterInfo = key.split("#");
						String field = filterInfo[1];
						String op = filterInfo[2];
						String[] values = getParameterValues(key);
						if(Utils.notEmpty(values)){
							if(values.length<=1){
								if(Utils.notEmpty(values[0])){
									searchContainer.and("t."+field, Op.valueOf(op), values[0]);
								}
							}else{
								List<String> vals = new ArrayList<String>();
								for(String v : values){
									if(Utils.notEmpty(v)){
										vals.add(v);
									}
								}
								if(Utils.notEmpty(vals)){
									searchContainer.and("t."+field, Op.valueOf(op), vals);
								}
							}
						}
					}
				}	
				try {
					String sortColums = paramsMap.get("order[0][column]")[0];
					String sortType = paramsMap.get("order[0][dir]")[0];
					if("6".equals(sortColums)){
						if("desc".equals(sortType)){
							searchContainer.orderByDesc("CONVERT(balance,DECIMAL)");
						}else{
							searchContainer.orderByASC("CONVERT(balance,DECIMAL)");
						}
					}
					else if("7".equals(sortColums)){
						if("desc".equals(sortType)){
							searchContainer.orderByDesc("CONVERT(principalRmb,DECIMAL)");
						}else{
							searchContainer.orderByASC("CONVERT(principalRmb,DECIMAL)");
						}
					}
					
				} catch (Exception e) {
					//e.printStackTrace();
				}
								
			}
		}
	}
	
	/*public DTRequest(HttpServletRequest request, StringBuilder timeQuery){
		if(request!=null){
			paramsMap = (Map<String,String[]>)request.getParameterMap();
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			page = new Page();
			try {
				page.setPageSize(Integer.parseInt(length));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				page.setCurrentPage((int) ((Long.parseLong(start))/page.getPageSize()+1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			selectItem = request.getParameterValues("selectItem[]");
			String groupAction = request.getParameter("groupAction");
			if("true".equals(groupAction)){
				hasGroupAction = true;
				this.groupAction = request.getParameter("groupActionType");
			}
			searchContainer = new SearchContainer();
			if(paramsMap!=null){
				for(Iterator<String> itor = paramsMap.keySet().iterator();itor.hasNext();){
					String key = itor.next();
					System.out.println(key);
					if(key.startsWith("filter_")){
						String[] filterInfo = key.split("_");
						String field = filterInfo[1];
						String op = filterInfo[2];
						String[] values = getParameterValues(key);
						if(Utils.notEmpty(values)){
							if(values.length<=1){
								if(Utils.notEmpty(values[0])){
									searchContainer.and("t."+field, Op.valueOf(op), values[0]);
								}
							}else{
								List<String> vals = new ArrayList<String>();
								for(String v : values){
									if(Utils.notEmpty(v)){
										vals.add(v);
									}
								}
								if(Utils.notEmpty(vals)){
									searchContainer.and("t."+field, Op.valueOf(op), vals);
								}
							}
						}
					}
					if(key.startsWith("efilter#")){
						String[] filterInfo = key.split("#");
						String field = filterInfo[1];
						String op = filterInfo[2];
						String[] values = getParameterValues(key);
						if(Utils.notEmpty(values)){
							if(values.length<=1){
								if(Utils.notEmpty(values[0])){
									searchContainer.and("t."+field, Op.valueOf(op), values[0]);
								}
							}else{
								List<String> vals = new ArrayList<String>();
								for(String v : values){
									if(Utils.notEmpty(v)){
										vals.add(v);
									}
								}
								if(Utils.notEmpty(vals)){
									searchContainer.and("t."+field, Op.valueOf(op), vals);
								}
							}
						}
					}
				}	
				try {
					String sortColums = paramsMap.get("order[0][column]")[0];
					String sortType = paramsMap.get("order[0][dir]")[0];
					if("7".equals(sortColums)){
						if("desc".equals(sortType)){
							searchContainer.orderByDesc("extensionCount");
						}else{
							searchContainer.orderByASC("extensionCount");
						}
					}
					
				} catch (Exception e) {
					//e.printStackTrace();
				}
				try {
					String beginTime = paramsMap.get("createBeginTime")[0];
					if(Utils.notEmpty(beginTime)){
						timeQuery.append(" and create_time >= '"+beginTime+" 00:00:00'");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}	
				try {
					String endTime = paramsMap.get("createEndTime")[0];
					if(Utils.notEmpty(endTime)){
						timeQuery.append(" and create_time <= '"+endTime+" 23:59:59'");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
	}*/
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public boolean isHasGroupAction() {
		return hasGroupAction;
	}
	public void setHasGroupAction(boolean hasGroupAction) {
		this.hasGroupAction = hasGroupAction;
	}
	public String getGroupAction() {
		return groupAction;
	}
	public void setGroupAction(String groupAction) {
		this.groupAction = groupAction;
	}
	public Map<String, String[]> getParamsMap() {
		if(paramsMap == null){
			paramsMap = new HashMap<String, String[]>();
		}
		return paramsMap;
	}
	public void setParamsMap(Map<String, String[]> params) {
		this.paramsMap = params;
	}
	public String[] getSelectItem() {
		return selectItem;
	}
	public void setSelectItem(String[] selectItem) {
		this.selectItem = selectItem;
	}
	/**
	 * 获取参数值
	 * @param paramName
	 * @return
	 */
	public String getParameter(String paramName){
		String[] parameterValues = getParameterValues(paramName);
		if(Utils.notEmpty(parameterValues)){
			return parameterValues[0];
		}
		return null;
	}
	public String[] getParameterValues(String paramName){
		Map<String, String[]> params = getParamsMap();
		return params.get(paramName);
	}
	public SearchContainer getSearchContainer() {
		return searchContainer;
	}

	public void setSearchContainer(SearchContainer searchContainer) {
		this.searchContainer = searchContainer;
	}
}
