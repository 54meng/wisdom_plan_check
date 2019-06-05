package com.wpc.commons.sql;

import java.util.List;

public interface Sql {
	
	public String toSql();
	
	public List<Object> getArgs();

}
