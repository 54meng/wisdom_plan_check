package com.wpc.dao;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.PubCommonDAO;
import com.wpc.persistence.User;
/**
 * 用户管理的数据库访问接口类
 * @author hqj
 *
 */
public interface UserDao extends PubCommonDAO {
	/**
	 * 新增
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User save(User user) throws Exception;
	
	/**
	 * 更新
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User update(User user) throws Exception;

	/**
	 * 新增或更新
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User saveOrUpdate(User user) throws Exception;
	
	/**
	 * 删除
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean delete(User user) throws Exception;
	
	/**
	 * 根据ID删除对象
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public boolean deleteById(String userid) throws Exception;
	
	public boolean deleteByIds(String[] ids, boolean isDel) throws Exception;
	
	/**
	 * 根据ID查找对象
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public User findById(String userid) throws Exception;
	
	/**
	 * 根据登录用户名获取用户对象
	 * @param loginName
	 * @return User
	 */
	public User findByLoginName(String loginName);
	
	/**
	 * 检查用户名是否存在
	 * @param loginName
	 * @return boolean
	 */
	public boolean isLoginNameExist(String loginName, String userType);

	/**
	 * 检查用户是否可用
	 * 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public abstract boolean isUserAvailable(String userId) throws Exception;

	/**
	 * 返回所有用户列表(支持带分页列表,支持过滤)
	 * @param page
	 * @param searchContainer
	 * @return
	 */
	public List<User> findUsersByPage(Page page, SearchContainer searchContainer);
	
	List<User> findUsersByIds(String[] idArray) throws Exception;

}
