package com.wpc.dfish.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * DAO基类。
 * 
 * 提供hql分页查询，example分页查询，拷贝更新等功能。
 * 
 * @author 
 * 
 * @param <T>
 */
public interface PubCommonDAO {

	  /**
	   * 带Where子条件的查询语句
	   * @param strSql String
	   * @param object Object[]
	   * @return List
	   */
	  public List getQueryList(final String strSql, final Object... object);
	  /**
	   * 经常由于查询结果只有一行一列。
	   * 比如说Count或者根据ID查对象。
	   * 这个封装过的接口，可以省去的list操作，简化开发
	   * @param strSql String
	   * @param object Object[]
	   * @return Object
	   */
	  public Object queryAsAnObject(final String strSql, final Object... object);

	  /**
	   * 带分页 Where子条件的查询语句
	   * <p>典型的用法</p>
	   * <pre>
	   *   Page page=new Page();
	   *   page.setCurrentPage(curPage);
	   *   page.setPageSize(getPersonalPageSize(pubUser));
	   *   List list =  dao.getQueryList("SELECT t.id.typeId,t.id.codeId,t.codeName,t.usableFlag " +
	   *     "FROM PubCode t WHERE t.id.typeId = ? ORDER BY t.id.codeId asc",
	   *     page,true,typeId);
	   * </pre>
	   * <p>经调用后，如果需要知道一共有多少页，可以从page里面获取。当前页则直接通过list获取</p>
	   * 
	   * @param strSql String
	   * @param page Page 分页信息
	   * @param autoGetRowCount boolean 是否自动取得行数并提供翻页信息.
	   * 如果条件简单。则，SQL的form到order by之前的部分，将会自动作为count的条件，取得总的行数，从而得到页数
	   * 但是如果条件复杂，如，一个语句里面有in等多个
	   * @param object Object[]
	   * @return List
	   */
	  public List getQueryList(final String strSql,final Page page,boolean autoGetRowCount, final Object... object);
	  /**
	   * 根据语句及参数删除数据
	   * @param strSql String
	   * @param object Object[]
	   * @return int
	   */
	  public int deleteSQL(String strSql, Object... object);

	  /**
	   * 根据对象删除数据
	   * @param obj Object
	   */
	  public void delete(Object obj);

	  /**
	   * 保存对象-共享对享
	   * @param object
	   */
	  public void saveObject(Object object);

	  /**
	   * evictObject
	   * @param object
	   */
	  public void evictObject(Object object);

	  /**
	   * 直接更新对象
	   * @param object
	   */
	  public void updateObject(Object object);


	  public HibernateTemplate getHibernateTemplate();
}
