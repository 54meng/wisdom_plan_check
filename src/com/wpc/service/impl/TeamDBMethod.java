package com.wpc.service.impl;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.CiticEnt;
import com.wpc.persistence.Team;

@SuppressWarnings("unchecked")
public class TeamDBMethod extends PubCommonDAOImpl{
	private static final String idName = "tid";
	private static final String initId = "00000001";
	
	public Team save(Team entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getTid())) {
				entity.setTid(FrameworkHelper.getNewId("Team", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public boolean delete(Team entity) throws Exception {
		if(null == entity){
			return false;
		}
		int rs = this.deleteSQL("FROM Team t WHERE t.tid = ?", entity.getTid());
		return rs > 0 ? true : false;
	}
	
	public Team update(Team entity) throws Exception {
		if (entity != null) {
			super.updateObject(entity);
		}
		return entity;
	}
	
	public Team findById(String tid) throws Exception {
		if (Utils.notEmpty(tid)) {
			List<Team> list = getQueryList("FROM Team t WHERE t.tid = ?", tid);
			if ((list != null) && (list.size() > 0)) {
				return (Team) list.get(0);
			}
		}
		return null;
	}

	public List<Team> findByPage(Page page, SearchContainer searchContainer) {
		if(searchContainer == null){
			searchContainer = new SearchContainer();
		}
		StringBuilder hql = new StringBuilder("FROM Team t ");
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
