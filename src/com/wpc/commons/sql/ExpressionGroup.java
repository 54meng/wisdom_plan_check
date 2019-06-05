package com.wpc.commons.sql;

import java.util.ArrayList;
import java.util.List;
/**
 * 多个表达式集合
 * @author XFB
 *
 */
public class ExpressionGroup {
	
	/*
	 *是否为第一个表达式 
	 */
	private boolean isFirst = true;
	
	private StringBuilder sb;
	    
	private List<Object> l;
	
	public ExpressionGroup() {
		sb = new StringBuilder(100);
		//sb.append("1=1");
		l = new ArrayList<Object>(5);
	}

	public ExpressionGroup and(Expression exp) {
		if(isFirst) {
			isFirst = false;
			sb.append(exp.toSql());
		}else {
			sb.append(" and ").append(exp.toSql());
		}
		l.addAll(exp.getArgs());
		return this;
	}
	
	public ExpressionGroup and(ExpressionGroup eg) {
		if(isFirst) {
			isFirst = false;
			sb.append(eg.toSql());
		}else {
			sb.append(" and ").append(eg.toSql());
		}
		l.addAll(eg.getArgs());
		return this;
	}
	
	public ExpressionGroup or(Expression exp) {
		if(isFirst) {
			isFirst = false;
			sb.append(exp.toSql());
		}else {
			sb.append(" or ").append(exp.toSql().replace("1=1", "1>1"));
		}
		l.addAll(exp.getArgs());
		return this;
	}
	
    public ExpressionGroup or(ExpressionGroup eg) {
    	if(isFirst) {
    		isFirst = false;
    		sb.append(eg.toSql());
    	}else {
        	sb.append(" or ").append(eg.toSql());
    	}
		l.addAll(eg.getArgs());
		return this;
	}
	
	public List<Object> getArgs() {
		return l;
	}

	
	public String toSql() {
		StringBuilder sbtemp = new StringBuilder(sb);
		return sbtemp.insert(0, "(").append(")").toString();
	}
}
