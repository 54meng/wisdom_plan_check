package com.wpc.service.impl;

import java.util.List;

import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Config;

@SuppressWarnings("unchecked")
public class ConfigDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public Config save(Config entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getId())) {
				entity.setId(FrameworkHelper.getNewId("Config", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public Config update(Config entity) throws Exception {
		if (entity != null) {
			super.updateObject(entity);
		}
		return entity;
	}
	
	public Config findById(String id) throws Exception {
		if (Utils.notEmpty(id)) {
			List<Config> list = getQueryList("FROM Config t WHERE t.id = ?", id);
			if ((list != null) && (list.size() > 0)) {
				return (Config) list.get(0);
			}
		}
		return null;
	}
	
	public List<Config> findList() throws Exception {
		List<Config> list = getQueryList("FROM Config t");
		return list;
	}
}
