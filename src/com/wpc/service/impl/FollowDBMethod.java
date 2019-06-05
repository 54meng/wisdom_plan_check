package com.wpc.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Follow;
import com.wpc.persistence.User;

@SuppressWarnings("unchecked")
public class FollowDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public List<User> findList(String userid, String type, String keyword) throws Exception {
		if("1".equals(type)){ //关注列表
			String sql = "SELECT u FROM User u, Follow t where u.userid=t.userIdTo and t.userIdFrom=?";
			if(Utils.notEmpty(keyword)){
				sql += " and (u.nickname like '%"+keyword+"%' or u.userid like '%"+keyword+"%')";
			}
			
			List<User> list = getQueryList(sql, userid);
			return list;
		}else if("2".equals(type)){ //互相关注列表
			String sql = "SELECT u FROM User u, Follow t where u.userid=t.userIdTo and t.userIdFrom=?";
			if(Utils.notEmpty(keyword)){
				sql += " and (u.nickname like '%"+keyword+"%' or u.userid like '%"+keyword+"%')";
			}
			List<User> list = getQueryList(sql, userid);//我关注的
			List<String> idList = getQueryList("SELECT t.userIdFrom FROM Follow t where t.userIdTo=?", userid);//关注我的
			if(Utils.notEmpty(list) && Utils.notEmpty(idList)){
				List<User> newList = new ArrayList<User>();
				for(User user : list){
					if(idList.contains(user.getUserid())){
						newList.add(user);
					}				
				}
				return newList;
			}
			
		}else if("3".equals(type)){ //粉丝列表
			String sql = "SELECT u FROM User u, Follow t where u.userid=t.userIdFrom and t.userIdTo=?";
			if(Utils.notEmpty(keyword)){
				sql += " and (u.nickname like '%"+keyword+"%' or u.userid like '%"+keyword+"%')";
			}
			List<User> list = getQueryList(sql, userid);//关注我的
			List<String> idList = getQueryList("SELECT t.userIdTo FROM Follow t where t.userIdFrom=?", userid);//我关注的
			if(Utils.notEmpty(list)){
				if(Utils.notEmpty(idList)){
					List<User> newList = new ArrayList<User>();
					for(User user : list){
						if(!idList.contains(user.getUserid())){
							newList.add(user);
						}
					}
					return newList;
				}
			}else{
				return list;
			}
		}

		return null;
	}
	
	public List<String> getMeFollowIds(String userid){
		List<String> idList = getQueryList("SELECT t.userIdTo FROM Follow t where t.userIdFrom=?", userid);
		return idList;
	}
	
	public List<String> getFollowMeIds(String userid){
		List<String> idList = getQueryList("SELECT t.userIdFrom FROM Follow t where t.userIdTo=?", userid);
		return idList;
	}
	
	public boolean followTa(String mUserId, String tUserId){
		List list = getQueryList("FROM Follow t where t.userIdFrom = ? and t.userIdTo=?", mUserId, tUserId);
		return Utils.notEmpty(list);
	}
	
	public boolean tafollow(String mUserId, String tUserId){
		List list = getQueryList("FROM Follow t where t.userIdFrom = ? and t.userIdTo=?", tUserId, mUserId);
		return Utils.notEmpty(list);
	}
	
	public Follow save(Follow entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getId())) {
				entity.setId(FrameworkHelper.getNewId("Follow", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public boolean delete(Follow entity) throws Exception {
		if(null == entity){
			return false;
		}
		int rs = this.deleteSQL("FROM Follow t WHERE t.userIdFrom = ? and t.userIdTo = ?", entity.getUserIdFrom(), entity.getUserIdTo());
		return rs > 0 ? true : false;
	}
	
	public boolean isFollowed(Follow entity){
		List<Follow> list = getQueryList("FROM Follow t WHERE t.userIdFrom = ? and t.userIdTo = ?", entity.getUserIdFrom(), entity.getUserIdTo());
		return Utils.notEmpty(list);
	}


	public List<Follow> findByPage(Page page, SearchContainer searchContainer) {
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
