package com.wpc.commons.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wpc.commons.sql.impl.SimpleExpression;
import com.wpc.dfish.util.Utils;



/**
 * 
 * <p>Title: 榕基RJ-CMSV7.X</p>
 * <p>Description: 构建简单条件查询,拼装hql语句 </p>
 * <p>Copyright: Copyright (c) 2000-2012</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author XFB
 * @version 
 * @since 
 *
 */
public class SearchContainer {
	
	public final static String DESC=" desc";
	
	public final static String ASC = " asc";
	
	StringBuffer sb = null;
	
	List<Object> l = null;
	
	private String extraSql;
	
	public Map<String, String> extraSqlMap = new HashMap<String, String>();
	
	public boolean isQuery = false;
	
	/*
	 *是否为第一个表达式 
	 */
	private boolean isFirst = true;
	
	Set<String> orderBy = null;
	
	public SearchContainer() {
		orderBy = new LinkedHashSet<String>();
		l = new ArrayList<Object>();
		sb = new StringBuffer(200);
//		sb.append("1=1");
		
	}
	
	public SearchContainer(String name,Op op,Object value) {
		orderBy = new LinkedHashSet<String>();
		l = new ArrayList<Object>(10);
		sb = new StringBuffer();
		Expression exp = new SimpleExpression(name,op,value);
		
		sb.append(exp.toSql());
	    l.addAll(exp.getArgs());
	}
	
	public SearchContainer and(String name,Op op,Object value) {
		if(value == null) {
			return this;
		}
		Expression exp = new SimpleExpression(name,op,value);
		if(isFirst){
			isFirst = false;
			sb.append(exp.toSql());
		}else{
			sb.append(" and ");
			sb.append(exp.toSql());
		}
	    l.addAll(exp.getArgs());
		return this;
	}
	
	public SearchContainer or(String name,Op op,Object value) {
		if(value == null) {
			return this;
		}
		Expression exp = new SimpleExpression(name,op,value);
		if(isFirst){
			isFirst = false;
			sb.append(exp.toSql());
		}else{
			sb.append(" or ");
			sb.append(exp.toSql());
		}
	    l.addAll(exp.getArgs());
		return this;
	}
	
	public SearchContainer and(ExpressionGroup eg) {
		if(isFirst){
			isFirst = false;
			sb.append(eg.toSql());
		}else{
			sb.append(" and ");
			sb.append(eg.toSql());
		}
	    l.addAll(eg.getArgs());
		return this;
	}
	
	public SearchContainer or(ExpressionGroup eg) {
		if(isFirst){
			isFirst = false;
			sb.append(eg.toSql());
		}else{
			sb.append(" or ");
			sb.append(eg.toSql());
		}
	    l.addAll(eg.getArgs());
		return this;
	}
	
	public SearchContainer orderByDesc(String name) {
		orderBy.add(name + DESC);
		return this;
	}
	
    public SearchContainer orderByASC(String name) {
    	orderBy.add(name + ASC);
		return this;
	}
	
	public enum Op {
		EQ("="),NOEQ("<>"),LT("<"),LE("<="),GT(">"),GE(">="),IN(" in "),LIKE(" like "),
		LLIKE(" like "),RLIKE(" like "),BETWEEN(" between ")
		,NULL(" is null"),NOTNULL("is not null"),NOTIN(" not in");
		private String value = null;
		private Op(String value) {
			this.value = value;
		}
		@Override
		public String toString() {		
			return this.value;
		}
		
	}

	
	public String toHql() {
		return this.toHql(true);
	}
	
	public String toHqlWitOutWhere() {
		return this.toHql(false);
	}
	
	private String toHql(boolean hasWhere) {
		StringBuilder sbtemp = new StringBuilder(sb);
		if(sbtemp.toString().trim().length()>0){
			if(hasWhere) {
				sbtemp.insert(0, " WHERE ");
			}
		}else{
			if(hasWhere) {
				sbtemp.insert(0, " WHERE 1=1 ");
			}
		}
		if(Utils.notEmpty(extraSql)){
			sbtemp.append(extraSql);
		}
		if(this.hasOrder()) {
			sbtemp.append(" ORDER BY ");
			for(String str : this.orderBy) {
				sbtemp.append(str).append(",");
			}
			return sbtemp.substring(0, sbtemp.length()-1);
		}
		return sbtemp.toString();
	}
	
	public Object[] getArgs(){
		return l.toArray();
	}
	
	public boolean hasOrder() {
		if(this.orderBy != null && this.orderBy.size() >0){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		SearchContainer sc = new SearchContainer();
		
//		//sc.and("t.name", Op.EQ, "name");
//		
//		ExpressionGroup e = new ExpressionGroup();
////		
//		Expression e1 = new SimpleExpression("e1",Op.LLIKE,"e1");
//		Expression e2 = new SimpleExpression("e2",Op.RLIKE,"e2");
//		e.and(e1);
//		e.or(e2);
//		
//		Expression e3 = new SimpleExpression("e3",Op.IN,new String[]{"e2","e33"});
//		
//		ExpressionGroup eg = new ExpressionGroup();
//		Expression e4 = new SimpleExpression("e4",Op.NULL,"e5");
//		Expression e5 = new SimpleExpression("e5",Op.RLIKE,"e4");
//		eg.and(e4);
//		eg.and(e5);
//		eg.and(e3);
//		sc.and(e).or(eg);
//		sc.orderByASC("tt");
//		sc.or("test", Op.EQ, "1");
//		sc.or("test1", Op.EQ, "11");
		sc.and("123", Op.EQ, "123");
		System.out.println(sc.toHql());
		
	}

	public String getExtraSql() {
		return extraSql;
	}

	public void setExtraSql(String extraSql) {
		this.extraSql = extraSql;
	}
}
