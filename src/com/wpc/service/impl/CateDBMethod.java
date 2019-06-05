package com.wpc.service.impl;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Category;
import com.wpc.persistence.User;

@SuppressWarnings("unchecked")
public class CateDBMethod extends PubCommonDAOImpl{
	private static final String idName = "cateId";
	private static final String initId = "00000001";
	
	public Category save(Category entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getCateId())) {
				entity.setCateId(FrameworkHelper.getNewId("Category", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public boolean delete(Category entity) throws Exception {
		if(null == entity){
			return false;
		}
		int rs = this.deleteSQL("FROM Category t WHERE t.cateId = ?", entity.getCateId());
		return rs > 0 ? true : false;
	}
	
	public boolean deleteByIds(String[] ids) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			int rs = this.deleteSQL("FROM Category t WHERE t.cateId in("+idStr.substring(1)+")");
			return rs > 0 ? true : false;
		}
		return false;
	}
	
	public Category update(Category entity) throws Exception {
		if (entity != null) {
			super.updateObject(entity);
		}
		return entity;
	}
	
	public List<Category> findByIds(String[] idArray) throws Exception {
		if(Utils.isEmpty(idArray)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(String id : idArray){
			sb.append(",").append("'"+id+"'");
		}
		String hql = "FROM Category t WHERE t.cateId in("+sb.substring(1)+") " ;
		List<Category> list = getQueryList(hql);
	
		return list;
	}
	
	public Category findById(String id) throws Exception {
		if (Utils.notEmpty(id)) {
			List<Category> list = getQueryList("FROM Category t WHERE t.cateId = ?", id);
			if ((list != null) && (list.size() > 0)) {
				return (Category) list.get(0);
			}
		}
		return null;
	}

	public List<Category> findByPage(Page page, SearchContainer searchContainer) {
		if(searchContainer == null){
			searchContainer = new SearchContainer();
			searchContainer.orderByDesc("t.cateId");
		}
		StringBuilder hql = new StringBuilder("FROM Category t ");
		hql.append(searchContainer.toHql());
		List list = null;
		if (page != null) {
			if(Utils.notEmpty(searchContainer.getArgs())){
				list = getQueryList(hql.toString(), page, true, searchContainer.getArgs());
			}else{
				list = getQueryList(hql.toString(), page, true, null);
			}
		} else {
			if(Utils.notEmpty(searchContainer.getArgs())){
				list = getQueryList(hql.toString(),searchContainer.getArgs());
			}else{
				list = getQueryList(hql.toString());
			}
		}
		return list;
	}

}
