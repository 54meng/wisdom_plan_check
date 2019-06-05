package com.wpc.dao.impl;

import java.util.Date;
import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.commons.sql.SearchContainer.Op;
import com.wpc.dao.UserDao;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.User;

@SuppressWarnings("unchecked")
public class UserDaoImpl extends PubCommonDAOImpl implements UserDao {
	private static final String tblName = "User";
	private static final String idName = "userid";
	private static final String initId = "00000001";
	
	
	public boolean delete(User user) throws Exception {
		if (user != null) {
			super.delete(user);
			return true;
		}
		return false;
	}
	
	
	public boolean deleteById(String userid) throws Exception {
		if (Utils.notEmpty(userid)) {
			int rs = this.deleteSQL("FROM " + tblName + " t WHERE t.userid = ?", userid);
			return rs > 0 ? true : false;
		}
		return false;
	}
	
	public boolean deleteByIds(String[] ids, boolean isDel) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			if(isDel){
				int rs = this.deleteSQL("FROM User t WHERE t.userid in("+idStr.substring(1)+")");
				return rs > 0 ? true : false;
			}else{
				this.update("UPDATE User t SET t.isDel='1' WHERE t.userid in("+idStr.substring(1)+")");
				return true;
			}
		}
		return false;
	}
	
	public User findById(String userid) throws Exception {
		if (Utils.notEmpty(userid)) {
			List<User> list = getQueryList("FROM " + tblName + " t WHERE t.userid = ?", userid);
			if ((list != null) && (list.size() > 0)) {
				return (User) list.get(0);
			}
		}
		return null;
	}
	
	public List findUsers() throws Exception {
		return this.findUsersByPage(null, true);
	}
	
	public List findUsersByPage(Page page, boolean ignoreSearchConditions) throws Exception {
		String hql = "FROM " + tblName + " t ORDER BY t.userid DESC " ;
		List list = null;
		if (page != null) {
			list = getQueryList(hql, page, true, null);
		} else {
			list = getQueryList(hql);
		}
		return list;
	}
	
	@Override
	public List<User> findUsersByIds(String[] idArray) throws Exception {
		if(Utils.isEmpty(idArray)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(String id : idArray){
			sb.append(",").append("'"+id+"'");
		}
		String hql = "FROM " + tblName + " t WHERE userid in("+sb.substring(1)+") " ;
		List<User> list = getQueryList(hql);
	
		return list;
	}
	
	public User save(User user) throws Exception {
		if (user != null) {
			if (Utils.isEmpty(user.getUserid())) {
				user.setUserid(FrameworkHelper.getNewId(tblName, idName, initId));
			}
			if(user.getCreate_time()==null){
				user.setCreate_time(new Date());
			}
			if(user.getCookieCode()==null){
				user.setCookieCode("cookie_code_"+user.getCreate_time().getTime());
			}
			super.saveObject(user);
		}
		return user;
	}
	
	public User saveOrUpdate(User user) throws Exception {
		return null;
	}
	
	public User update(User user) throws Exception {
		if (user != null) {
			//user.setCookieCode("cookie_code_"+(new Date().getTime()));
			super.updateObject(user);
		}
		return user;
	}

	@Override
	public User findByLoginName(String loginName) {
		if (Utils.notEmpty(loginName)) {
			List<User> list = getQueryList("FROM " + tblName + " t WHERE t.phone = ? or t.username = ?", loginName, loginName);
			if ((list != null) && (list.size() > 0)) {
				return (User) list.get(0);
			}
		}
		return null;
	}

	
	@Override
	public boolean isLoginNameExist(String loginName, String userType) {
		if (Utils.notEmpty(loginName)) {
			Integer count = (Integer)queryAsAnObject("Select count(*) FROM " + tblName + " t WHERE t.phone = ? and userType = ?", loginName, userType);
			if ((count != null) && (count.intValue() > 0)) {
				return true;
			}else{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isUserAvailable(String userId) {
		if (Utils.notEmpty(userId)) {
			String status = (String)queryAsAnObject("Select t.userStatus FROM " + tblName + " t WHERE t.userid = ?", userId);
			if ("1".equals(status)) {
				return true;
			}else{
				return false;
			}
		}
		return false;
	}


	@Override
	public List<User> findUsersByPage(Page page, SearchContainer searchContainer) {
		if(searchContainer == null){
			searchContainer = new SearchContainer();
		}
		
		StringBuilder hql = new StringBuilder("FROM " + tblName + " t " );
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
