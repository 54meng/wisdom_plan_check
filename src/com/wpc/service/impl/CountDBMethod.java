package com.wpc.service.impl;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Count;
import com.wpc.persistence.User;

@SuppressWarnings("unchecked")
public class CountDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public Count save(Count entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getId())) {
				entity.setId(FrameworkHelper.getNewId("Count", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public boolean delete(Count entity) throws Exception {
		if(null == entity){
			return false;
		}
		int rs = this.deleteSQL("FROM Count t WHERE t.id = ?", entity.getId());
		return rs > 0 ? true : false;
	}
	
	public boolean deleteByIds(String[] ids) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			int rs = this.deleteSQL("FROM Count t WHERE t.id in("+idStr.substring(1)+")");
			return rs > 0 ? true : false;
		}
		return false;
	}
	
	public Count update(Count entity) throws Exception {
		if (entity != null) {
			super.updateObject(entity);
		}
		return entity;
	}
	
	public void updateURemainCount(String userId, String now) throws Exception {		
		this.update("UPDATE Count t SET t.remainCount = t.watchCount, t.now='"+now+"' WHERE t.userId = '"+userId+"'");
	}
	
	public void updateORemainCount(String openId, String now) throws Exception {		
		this.update("UPDATE Count t SET t.remainCount = t.watchCount, t.now='"+now+"' WHERE t.openId = '"+openId+"'");
	}
	
	public void updateRemainCount(String id, int remainCount, int type) throws Exception {		
		this.update("UPDATE Count t SET t.remainCount = "+remainCount+" WHERE " + (type==1 ? "t.userId = '"+id+"'" : "t.openId = '"+id+"'"));
	}
	
	public Count getCountByUidNow(String userId, String now){
		if (Utils.notEmpty(userId) && Utils.notEmpty(now)) {
			List<Count> list = getQueryList("FROM Count t WHERE t.userId = ? and t.now = ?", userId, now);
			if ((list != null) && (list.size() > 0)) {
				return (Count) list.get(0);
			}
		}
		return null;
	}
	
	public Count getCountByOidNow(String openId, String now){
		if (Utils.notEmpty(openId) && Utils.notEmpty(now)) {
			List<Count> list = getQueryList("FROM Count t WHERE t.openId = ? and t.now = ?", openId, now);
			if ((list != null) && (list.size() > 0)) {
				return (Count) list.get(0);
			}
		}
		return null;
	}
	
	public Count findByUserId(String id) throws Exception {
		if (Utils.notEmpty(id)) {
			List<Count> list = getQueryList("FROM Count t WHERE t.userId = ?", id);
			if ((list != null) && (list.size() > 0)) {
				return (Count) list.get(0);
			}
		}
		return null;
	}
	
	public Count findByOpenId(String id) throws Exception {
		if (Utils.notEmpty(id)) {
			List<Count> list = getQueryList("FROM Count t WHERE t.openId = ?", id);
			if ((list != null) && (list.size() > 0)) {
				return (Count) list.get(0);
			}
		}
		return null;
	}
}
