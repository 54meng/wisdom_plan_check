package com.wpc.service.impl;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Advert;
import com.wpc.persistence.History;

@SuppressWarnings("unchecked")
public class HistoryDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public History save(History entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getId())) {
				entity.setId(FrameworkHelper.getNewId("History", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public boolean delete(History entity) throws Exception {
		if(null == entity){
			return false;
		}
		int rs = this.deleteSQL("FROM History t WHERE t.id = ?", entity.getId());
		return rs > 0 ? true : false;
	}
	
	public boolean deleteByVideoIds(String[] ids, String userId) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			int rs = this.deleteSQL("FROM History t WHERE t.videoId in("+idStr.substring(1)+") and t.userId = ?", userId);
			return rs > 0 ? true : false;
		}
		return false;
	}
	
	public boolean isExist(History entity){
		List<History> list = getQueryList("FROM History t WHERE t.videoId = ? and t.userId = ?", entity.getVideoId(), entity.getUserId());
		return Utils.notEmpty(list);
	}
	
	public int getCount(String userId){
		List<History> list = getQueryList("FROM History t WHERE t.userId = ?", userId);
		return Utils.notEmpty(list) ? list.size() : 0;
	}

	public List<History> findByPage(Page page, SearchContainer searchContainer) {
		if(searchContainer == null){
			searchContainer = new SearchContainer();
		}
		StringBuilder hql = new StringBuilder("FROM History t ");
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
