package com.wpc.service.impl;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Feedback;

@SuppressWarnings("unchecked")
public class FeedbackDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public Feedback save(Feedback entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getId())) {
				entity.setId(FrameworkHelper.getNewId("Feedback", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public boolean delete(Feedback entity) throws Exception {
		if(null == entity){
			return false;
		}
		int rs = this.deleteSQL("FROM Feedback t WHERE t.id = ?", entity.getId());
		return rs > 0 ? true : false;
	}
	
	public boolean deleteByIds(String[] ids) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			int rs = this.deleteSQL("FROM Feedback t WHERE t.id in("+idStr.substring(1)+")");
			return rs > 0 ? true : false;
		}
		return false;
	}
	
	public Feedback update(Feedback entity) throws Exception {
		if (entity != null) {
			super.updateObject(entity);
		}
		return entity;
	}
	
	public List<Feedback> findByIds(String[] idArray) throws Exception {
		if(Utils.isEmpty(idArray)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(String id : idArray){
			sb.append(",").append("'"+id+"'");
		}
		String hql = "FROM Feedback t WHERE t.id in("+sb.substring(1)+") " ;
		List<Feedback> list = getQueryList(hql);
	
		return list;
	}
	
	public Feedback findById(String id) throws Exception {
		if (Utils.notEmpty(id)) {
			List<Feedback> list = getQueryList("FROM Feedback t WHERE t.id = ?", id);
			if ((list != null) && (list.size() > 0)) {
				return (Feedback) list.get(0);
			}
		}
		return null;
	}

	public List<Feedback> findByPage(Page page, SearchContainer searchContainer) {
		if(searchContainer == null){
			searchContainer = new SearchContainer();
			searchContainer.orderByDesc("t.id");
		}
		StringBuilder hql = new StringBuilder("FROM Feedback t ");
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
