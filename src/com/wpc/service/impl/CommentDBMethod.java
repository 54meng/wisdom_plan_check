package com.wpc.service.impl;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Comment;

@SuppressWarnings("unchecked")
public class CommentDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public Comment save(Comment entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getId())) {
				entity.setId(FrameworkHelper.getNewId("Comment", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public boolean delete(Comment entity) throws Exception {
		if(null == entity){
			return false;
		}
		int rs = this.deleteSQL("FROM Comment t WHERE t.id = ?", entity.getId());
		return rs > 0 ? true : false;
	}

	public List<Comment> findByPage(Page page, SearchContainer searchContainer) {
		if(searchContainer == null){
			searchContainer = new SearchContainer();
		}
		StringBuilder hql = new StringBuilder("from Comment t ");
		hql.append(searchContainer.toHql());
		List list = null;
		if (page != null) {
			if(Utils.notEmpty(searchContainer.getArgs())){
				list = getQueryList(hql.toString(), page, true,searchContainer.getArgs());
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
