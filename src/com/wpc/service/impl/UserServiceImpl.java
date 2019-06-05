package com.wpc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wpc.commons.sql.SearchContainer;
import com.wpc.commons.sql.SearchContainer.Op;
import com.wpc.dao.UserDao;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.User;
import com.wpc.service.UserService;
import com.wpc.utils.SecurityUtils;
import com.wpc.utils.string.StringUtils;


public class UserServiceImpl implements UserService {
	protected final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	
	public boolean deleteUser(User user) throws Exception {
		if (user != null) {
			return this.userDao.delete(user);
		}
		return false;
	}

	
	public boolean deleteUserById(String userId) throws Exception {
		if (Utils.notEmpty(userId)) {
			return this.userDao.deleteById(userId);
		}
		return false;
	}

	
	public boolean deleteUserByIds(String[] userids) throws Exception {
		if ((userids != null) && (userids.length > 0)) {
			for (int i = 0; i < userids.length; i++) {
				this.deleteUserById(userids[i]);
			}
		}
		return true;
	}

	
	public User getUserById(String userid) throws Exception {
		if (Utils.notEmpty(userid)) {
			return this.userDao.findById(userid);
		}
		return null;
	}
	
	public List<User> getUsersByIds(String... userIds) throws Exception {
		if (Utils.notEmpty(userIds)) {
			SearchContainer searchContainer = new SearchContainer();
			searchContainer.and("t.userId",Op.IN,userIds);
			List<User> users = findUsersByPage(null, searchContainer);
			return users;
		}
		return null;
	}

	
	
	public User saveUser(User user) throws Exception {
		if (user != null) {
			user = this.userDao.save(user);
		}
		return user;
	}

	
	public User updateUser(User user) throws Exception {
		if (user != null) {
			user = this.userDao.update(user);
		}
		return user;
	}


	@Override
	public User getUserByLoginName(String loginName) throws Exception {
		if (Utils.notEmpty(loginName)) {
			return this.userDao.findByLoginName(loginName);
		}
		return null;
	}


	@Override
	public boolean isLoginNameExist(String loginName, String userType) throws Exception {
		if (Utils.notEmpty(loginName)) {
			return this.userDao.isLoginNameExist(loginName, userType);
		}
		return false;
	}
	
	@Override
	public boolean isUserAvailable(String userId) throws Exception {
		if (Utils.notEmpty(userId)) {
			return this.userDao.isUserAvailable(userId);
		}
		return true;
	}


	@Override
	public List<User> findUsersByPage(Page page, SearchContainer searchContainer)
			throws Exception {
		return this.userDao.findUsersByPage(page,searchContainer);
	}


	@Override
	public String createToken(String userId) throws Exception {
		Map tokenInfo = new HashMap();
		tokenInfo.put("userId", userId);
//		tokenInfo.put("generTime", ""+(new Date().getTime()));
		tokenInfo.put("expiredTime", ""+(new Date().getTime()+1000*60*60*24*30));//暂设过期时间为30天
		String enBase64 = StringUtils.enBASE64(gson.toJson(tokenInfo));
		String md5 = StringUtils.md5(enBase64+"hello");
		String token = md5+"|"+enBase64;
		User user = getUserById(userId);
		if(user!=null){
			user.setAccesstoken(token);
			updateUser(user);
		}
		return token;
	}


	@Override
	public User getUserByToken(String token) throws Exception {
		User user = null;
		if(Utils.notEmpty(token)){
			String[] tokenInfo = token.split("\\|");
			if(tokenInfo!=null&&tokenInfo.length>1){
				String checkCode = tokenInfo[0];
				String enBase64 = tokenInfo[1];
				if(checkCode!=null&&enBase64!=null&&checkCode.equals(StringUtils.md5(enBase64+"hello"))){
					String json = StringUtils.deBASE64(enBase64);
					Map<String,String> tokenMap = (Map<String,String>)gson.fromJson(json, new TypeToken<Map<String,String>>(){}.getType());
					String userId = tokenMap.get("userId");
//					String generTime = tokenMap.get("generTime");
					String expiredTime = tokenMap.get("expiredTime");
					//判断是否过期，暂设置1个月
					//if(Long.parseLong(expiredTime)-(new Date().getTime())<0){
						User userById = getUserById(userId);
						if(userById!=null&&userById.getAccesstoken().equals(token)){
							user = userById;
						}
					//}
				}
			}
		}
		return user;
	}

	@Override
	public boolean checkUserPassword(User user, String password) {
		if(user!=null&&Utils.notEmpty(password)){
			String md5Pw = user.getPassword();
			try {
				return md5Pw.equals(SecurityUtils.encryptPassword(password));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}


	@Override
	public boolean deleteByIds(String[] ids, boolean isDel) throws Exception {
		// TODO Auto-generated method stub
		return userDao.deleteByIds(ids, isDel);
	}


	@Override
	public List<User> findUsersByIds(String[] idArray) throws Exception {
		// TODO Auto-generated method stub
		return userDao.findUsersByIds(idArray);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(SecurityUtils.encryptPassword(StringUtils.md5("12345")));
	}
}
