package com.wpc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.commons.sql.SearchContainer.Op;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.CiticEnt;
import com.wpc.utils.DateUtils;

@SuppressWarnings("unchecked")
public class CollectionDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public CiticEnt findById(String citicId) throws Exception {
		if (Utils.notEmpty(citicId)) {
			List<CiticEnt> list = getQueryList("FROM CiticEnt t WHERE t.id = ?", citicId);
			if ((list != null) && (list.size() > 0)) {
				return (CiticEnt) list.get(0);
			}
		}
		return null;
	}
	
	public CiticEnt save(CiticEnt citic) throws Exception {
		if (citic != null) {
			if (Utils.isEmpty(citic.getId())) {
				citic.setId(FrameworkHelper.getNewId("CiticEnt", idName, initId));
			}
			
			super.saveObject(citic);
		}
		return citic;
	}

	
	public CiticEnt update(CiticEnt citic) throws Exception {
		if (citic != null) {
			super.updateObject(citic);
		}
		return citic;
	}
	
	public void updateOperUser(String[] cIds, String userId){
		if (Utils.notEmpty(cIds)) {
			String idStr = "";
			for(String id: cIds){
				idStr += ","+"'"+id+"'";
			}	
			this.update("UPDATE CiticEnt t SET t.operUserId='"+userId+"', t.operStatus='1' WHERE t.id in("+idStr.substring(1)+")");
		}
	}
	
	public void updateStatus(String[] cIds, String status){
		if (Utils.notEmpty(cIds)) {
			String idStr = "";
			for(String id: cIds){
				idStr += ","+"'"+id+"'";
			}	
			this.update("UPDATE CiticEnt t SET t.operStatus='"+status+"' WHERE t.id in("+idStr.substring(1)+")");
		}
	}

	public boolean deleteByIds(String[] ids, boolean isDel) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			if(isDel){
				int rs = this.deleteSQL("FROM CiticEnt t WHERE t.id in("+idStr.substring(1)+")");
				return rs > 0 ? true : false;
			}else{
				String now = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
				this.update("UPDATE CiticEnt t SET t.isDel='1', t.delDate='"+now+"' WHERE t.id in("+idStr.substring(1)+")");
				return true;
			}
		}
		return false;
	}
	
	public boolean recoverByIds(String[] ids) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			this.update("UPDATE CiticEnt t SET t.isDel=null WHERE t.id in("+idStr.substring(1)+")");
			return true;
		}
		return false;
	}

	public List<CiticEnt> findCiticsByPage(Page page, SearchContainer searchContainer, String cType) {
		if(searchContainer == null){
			searchContainer = new SearchContainer();
		}
		if("5".equals(cType)){
			searchContainer.orderByDesc("t.delDate");
		}
		searchContainer.orderByDesc("t.id");		
		searchContainer.and("t.isDel", "5".equals(cType) ? Op.EQ : Op.NULL , "1");
		StringBuilder hql = new StringBuilder("FROM CiticEnt t WHERE 1=1 " );
		if("1".equals(cType)){
			hql.append(" and (t.operStatus='0' or t.operUserId is null)");
		}else if("2".equals(cType)){
			hql.append(" and t.operStatus='1'");
		}else if("3".equals(cType)){
			hql.append(" and t.operStatus='2'");
		}
		String extraSql = searchContainer.getExtraSql();
		Map<String, String> extraSqlMap = searchContainer.extraSqlMap;
		if(Utils.notEmpty(extraSql)){
			hql.append(" and "+extraSql);
		}
		hql.append(" and ");
		hql.append(searchContainer.toHqlWitOutWhere());
		List<CiticEnt> list = null;
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
		if(Utils.notEmpty(list)){
			String userIds = "";
			for(CiticEnt citicEnt : list){
				if(Utils.notEmpty(citicEnt.getOperUserId())){
					userIds += ","+"'"+citicEnt.getOperUserId()+"'";
				}
			}
			String userNameQuery = extraSqlMap.get("userName");
			if(Utils.notEmpty(userIds)){			
				userNameQuery = Utils.isEmpty(userNameQuery) ? "" : userNameQuery;
				List<Object[]> userList = getQueryList("select t.userId, t.userName from User t where t.userId in("+userIds.substring(1)+")"+userNameQuery);
				if(Utils.notEmpty(userNameQuery)){
					List<CiticEnt> newList = new ArrayList<CiticEnt>();
					if(Utils.notEmpty(userList)){
						for(Object[] users : userList){
							for(CiticEnt citicEnt : list){
								if(users[0].toString().equals(citicEnt.getOperUserId())){
									citicEnt.setOperUserName(users[1].toString());
									newList.add(citicEnt);
								}
							}
						}
					}
					return newList;
				}
				if(Utils.notEmpty(userList)){
					for(Object[] users : userList){
						for(CiticEnt citicEnt : list){
							if(users[0].toString().equals(citicEnt.getOperUserId())){
								citicEnt.setOperUserName(users[1].toString());
							}
						}
					}
				}
			}else{
				if(Utils.notEmpty(userNameQuery)){
					return null;
				}
			}
		}
		return list;
	}

}
