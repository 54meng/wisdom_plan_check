package com.wpc.service.impl;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.CiticEnt;
import com.wpc.persistence.CollectionRecord;

@SuppressWarnings("unchecked")
public class CollectionRecordDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public CollectionRecord findById(String id) throws Exception {
		if (Utils.notEmpty(id)) {
			List<CollectionRecord> list = getQueryList("FROM CollectionRecord t WHERE t.id = ?", id);
			if ((list != null) && (list.size() > 0)) {
				return (CollectionRecord) list.get(0);
			}
		}
		return null;
	}
	
	public List<CollectionRecord> findByIds(String[] ids) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			List<CollectionRecord> list = getQueryList("FROM CollectionRecord t WHERE t.id in("+idStr.substring(1)+")");
			if ((list != null) && (list.size() > 0)) {
				return list;
			}
		}
		return null;
	}
	
	public CollectionRecord save(CollectionRecord entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getId())) {
				entity.setId(FrameworkHelper.getNewId("CollectionRecord", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}

	
	public CollectionRecord update(CollectionRecord entity) throws Exception {
		if (entity != null) {
			super.updateObject(entity);
		}
		return entity;
	}


	public List<CollectionRecord> findByPage(Page page, SearchContainer searchContainer) {
		if(searchContainer == null){
			searchContainer = new SearchContainer();
		}
		StringBuilder hql = new StringBuilder("FROM CollectionRecord t ");
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
