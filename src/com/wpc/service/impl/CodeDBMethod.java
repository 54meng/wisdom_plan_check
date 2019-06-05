package com.wpc.service.impl;

import java.util.List;

import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Code;

@SuppressWarnings("unchecked")
public class CodeDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public Code save(Code entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getId())) {
				entity.setId(FrameworkHelper.getNewId("Code", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public boolean delete(Code entity) throws Exception {
		if(null == entity){
			return false;
		}
		int rs = this.deleteSQL("FROM Code t WHERE t.id = ?", entity.getId());
		return rs > 0 ? true : false;
	}
	
	public boolean deleteByIds(String[] ids) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			int rs = this.deleteSQL("FROM Code t WHERE t.id in("+idStr.substring(1)+")");
			return rs > 0 ? true : false;
		}
		return false;
	}
	
	public Code update(Code entity) throws Exception {
		if (entity != null) {
			super.updateObject(entity);
		}
		return entity;
	}

	
	public Code findByUserId(String id) throws Exception {
		if (Utils.notEmpty(id)) {
			List<Code> list = getQueryList("FROM Code t WHERE t.userId = ?", id);
			if ((list != null) && (list.size() > 0)) {
				return (Code) list.get(0);
			}
		}
		return null;
	}
	
	public Code findByOpenId(String id) throws Exception {
		if (Utils.notEmpty(id)) {
			List<Code> list = getQueryList("FROM Code t WHERE t.openId = ?", id);
			if ((list != null) && (list.size() > 0)) {
				return (Code) list.get(0);
			}
		}
		return null;
	}
	
	public Code findByCode(String code) throws Exception {
		if (Utils.notEmpty(code)) {
			List<Code> list = getQueryList("FROM Code t WHERE t.code = ?", code);
			if ((list != null) && (list.size() > 0)) {
				return (Code) list.get(0);
			}
		}
		return null;
	}
}
