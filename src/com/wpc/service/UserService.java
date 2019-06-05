package com.wpc.service;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.persistence.User;

/**
 * 业务逻辑接口类
 * 
 * @author hqj
 *
 */
public abstract interface UserService {
	/**
	 * 增加用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public abstract User saveUser(User user) throws Exception;
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public abstract User updateUser(User user) throws Exception;
	
	/**
	 * 删除用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public abstract boolean deleteUser(User user) throws Exception;
	
	/**
	 * 根据ID删除用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public abstract boolean deleteUserById(String userId) throws Exception;
	
	/**
	 * 根据序列ID删除用户
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	public abstract boolean deleteUserByIds(String[] userIds) throws Exception;
	
	public boolean deleteByIds(String[] ids, boolean isDel) throws Exception;
	
	/**
	 * 根据用户ID查找用户对象
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public abstract User getUserById(String userId) throws Exception;
	
	/**
	 * 根据用户IDs查找用户列表对象
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public abstract List<User> getUsersByIds(String... userIds) throws Exception;
	
	/**
	 * 检查LoginName是否已经存在
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public abstract boolean isLoginNameExist(String loginName, String userType) throws Exception;
	
	/**
	 * 检查用户是否可用
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public abstract boolean isUserAvailable(String userId) throws Exception;
	
	/**
	 * 根据用户LoginName查找用户对象
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public abstract User getUserByLoginName(String loginName) throws Exception;
	

	/**
	 * 返回所有用户列表(支持带分页列表,支持过滤)
	 * @param page
	 * @param searchContainer
	 * @return
	 */
	public abstract List<User> findUsersByPage(Page page,SearchContainer searchContainer) throws Exception;

	/**
	 * 为用户生成token
	 * 
	 * @param userId
	 * @return token
	 */
	public abstract String createToken(String userId)throws Exception ;
	
	/**
	 * 根据token获取用户对象
	 * 
	 * @param userId
	 * @return token
	 */
	public abstract User getUserByToken(String token)throws Exception ;
	
	
	/**
	 * 检查用户密码是否正确
	 * @param user
	 * @param passwordMd5
	 * @return
	 */
	public abstract boolean checkUserPassword(User user,String password);
	
	List<User> findUsersByIds(String[] idArray) throws Exception;
}
