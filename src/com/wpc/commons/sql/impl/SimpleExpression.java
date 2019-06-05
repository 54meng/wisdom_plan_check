package com.wpc.commons.sql.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wpc.commons.sql.Expression;
import com.wpc.commons.sql.SearchContainer.Op;
/**
 * 
 * <p>Title: 榕基RJ-CMSV7.X</p>
 * <p>Description: 简单条件表达式</p>
 * <p>Copyright: Copyright (c) 2000-2012</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author 
 * @version 
 * @since  
 *
 */
public class SimpleExpression implements Expression {
	private StringBuilder sb;

	private List<Object> l;

	public SimpleExpression(String name, Op op, Object value) {
		if(value == null) {
			sb = new StringBuilder(4);
			sb.append("1=1");
			l = Collections.emptyList();
			return;
		}
		sb = new StringBuilder(30);
		sb.append(name).append(" "+op+" ");
		l = new ArrayList<Object>(2);
		switch (op) {
		case LIKE:
			sb.append(" ? ");
			l.add("%" + value + "%");
			break;
		case NOTIN:
			break;
		case IN: //FIXME 用 ? 代替拼接避免sql注入 
			sb.append("(");
			if (value.getClass().isArray()) {
				Class type = value.getClass().getComponentType();
				if (type.isPrimitive() || isNumberClass(type)) {
					Object[] arr = (Object[]) value;
					for (Object o : arr) {
						sb.append(o).append(",");
					}
					sb = sb.delete(sb.length() - 1, sb.length());
				} else {
					Object[] arr = (Object[]) value;
					for (Object o : arr) {
						sb.append("'").append(o).append("',");
					}
					sb = sb.delete(sb.length() - 1, sb.length());
				}
			} else if (value.getClass().isAssignableFrom(List.class)) {

			}
			sb.append(")");
			break;
		case BETWEEN:
			sb.append(" ? and ? ");
			l.add(value);
			l.add(value);
			break;
		case NULL:
			break;
		case LLIKE:
			sb.append(" ? ");
			l.add("%" + value);
			break;
		case RLIKE:
			sb.append(" ? ");
			l.add(value + "%");
			break;
		case NOTNULL:
			break;
		default:
			sb.append(" ? ");
			l.add(value);
		}
	}


	public List<Object> getArgs() {
		return l;
	}

	public String toSql() {
		return sb.toString();
	}

	private boolean isNumberClass(Object obj) {
		return obj.getClass().isAssignableFrom(Number.class);
	}
}
