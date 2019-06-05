package com.wpc.dfish.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.PubCommonDAO;



public class PubCommonDAOImpl extends HibernateDaoSupport implements PubCommonDAO {
	private static final boolean DEBUG=true;
	
	@SuppressWarnings("unchecked")
	public List getQueryList(final String strSql, final Object... object) {
		long beginTimeMillis=0;
		if(DEBUG)beginTimeMillis=System.currentTimeMillis();
		final HibernateTemplate template = getHibernateTemplate();
		template.setCacheQueries(true);
		// return template.find(strSql,object);
		// 有一个timeStamp类型转化过程所以不能用find
		HibernateCallback action = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(strSql);
				if (object != null) {
					for (int i = 0; i < object.length; i++) {
						setArgument(query, i, object[i]);
					}
				}

				ArrayList arrayList = new ArrayList();
				arrayList = (ArrayList) query.list();

				return arrayList;
			}
		};

		List list= (List) template.execute(action);
		if(DEBUG){
			long endTimeMillis=System.currentTimeMillis();
			System.out.println("查询使用的HQL为:"+strSql+"\r\n查询时间为"+(endTimeMillis-beginTimeMillis)+"ms");
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public int deleteSQL(String strSql, Object... object) {
		List list = getQueryList(strSql, object);
		if (list != null) {
			getHibernateTemplate().deleteAll(list);
		}
		return list == null ? 0 : list.size();
	}

	public void delete(Object obj) {
		if (obj != null) {
			getHibernateTemplate().delete(obj);
		}
	}

	public void saveObject(final Object object) {
		if (object != null) {
			getHibernateTemplate().save(object);
		}
	}

	public void updateObject(final Object object) {
		if (object != null) {
			getHibernateTemplate().update(object);
//			getHibernateTemplate().merge(object);
		}
	}
	
	public void update(final String strSql) {
		if (strSql != null) {
			getHibernateTemplate().bulkUpdate(strSql);
		}
	}

	protected static final void setArgument(Query query, int index, Object o) {
		if (o instanceof java.lang.String) {
			query.setString(index, (String) o);
		} else if (o instanceof java.lang.Integer) {
			query.setInteger(index, ((Integer) o).intValue());
		} else if (o instanceof java.util.Date) {
			query.setTimestamp(index, (java.util.Date) o);
		} else if (o instanceof java.lang.Boolean) {
			query.setBoolean(index, ((Boolean) o).booleanValue());
		} else if (o instanceof java.lang.Byte) {
			query.setByte(index, ((Byte) o).byteValue());
		} else if (o instanceof java.lang.Long) {
			query.setLong(index, ((Long) o).longValue());
		} else if (o instanceof java.lang.Double) {
			query.setDouble(index, ((Double) o).doubleValue());
		} else if (o instanceof java.lang.Float) {
			query.setFloat(index, ((Float) o).floatValue());
		} else if (o instanceof java.lang.Number) {
			query.setBigDecimal(index, new java.math.BigDecimal(((Number) o)
					.doubleValue()));
		} else {
			query.setString(index, o.toString());
		}

	}

	public void evictObject(final Object object) {
		if (object != null) {
			getHibernateTemplate().evict(object);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List getQueryList(final String strSql, final Page page, final boolean autoGetRowCount, final Object... object) {
		final HibernateTemplate template = getHibernateTemplate();
		template.setCacheQueries(true);//在程序中必须手动启用查询缓存
		HibernateCallback action = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				// 建立查询
				Query query = session.createQuery(strSql);
				// 存有参数列表
				if (object != null) {

					// 取出参数列表
					for (int i = 0; i < object.length; i++) {
						setArgument(query, i, object[i]);
					}
				}
				if (autoGetRowCount) {
					// 取总行数
					String upperSQL = strSql.toUpperCase();
					if (upperSQL.indexOf("DISTINCT") < 0) { // 复杂的query不提供setRowCount的功能
						int i = 0;
						String strHql4cout = strSql;
						// strHql4cout count 语句必须取出from前面的语句和order by 后面的语句.
						if ((i = upperSQL.indexOf(" FROM ")) >= 0) {
							strHql4cout = strSql.substring(i);
							upperSQL = upperSQL.substring(i);
						}
						i = upperSQL.indexOf("ORDER ");
						outter: while (i > 0) {
							// 后面如果出现 0-N个空格+BY，则为ORDER BY语句。
							char[] ca = upperSQL.toCharArray();
							int point = i + 6;
							while (point < ca.length - 3) {
								if (ca[point] == 'B') {
									if (ca[point + 1] == 'Y'
											&& (ca[point + 2] == ' ' || ca[point + 2] == '(')) {
										strHql4cout = strHql4cout.substring(0,
												i);
										break outter;
									}
								} else if (ca[point] == ' ') {
									point++;
								} else {
									break;
								}
							}
							int k = upperSQL.substring(i + 6).indexOf("ORDER ");
							if (k < 0) {
								break;
							} else {
								i = i + 6 + k;
							}
						}

						// strHql4cout 为from开头的
						Query query1 = session.createQuery("select count(*) "
								+ strHql4cout);
						// 存有参数列表
						if (object != null) {

							// 取出参数列表
							for (int k = 0; k < object.length; k++) {
								setArgument(query1, k, object[k]);
							}
						}
						List arrayList = query1.list();
						Integer inte = ((Number) arrayList.get(0)).intValue();
						page.setRowCount(inte.intValue());
					} else { // 复杂的query不提供setRowCount的功能
						ArrayList arrayList = new ArrayList();
						arrayList = (ArrayList) query.list();
						page.setRowCount(arrayList.size());
					}
				}
				// 按页检索数据
				if (page.getCurrentPage() != 0) {
					int pageno = page.getCurrentPage();
					query.setFirstResult((pageno - 1) * (page.getPageSize())); // 从第2万条开始取出100条记录 
					query.setMaxResults(page.getPageSize());
				}
				ArrayList arrayList = new ArrayList();
				arrayList = (ArrayList) query.list();

				return arrayList;
			}
		};

		return (List) template.execute(action);
	}
	
	@SuppressWarnings("unchecked")
	public Object queryAsAnObject(String strSql, Object... object) {
		List list = this.getQueryList(strSql, object);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
