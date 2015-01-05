package com.ming.server.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -8070483502623650913L;

	private List<TableColumn> columnList;
	
	private int filedCount;
	
	public String getTableName() {
		return null;
	}

	public String[] getPrimaryKey() {
		return null;
	}
	
	public void addProperty(String field, Object value) {
		if(columnList == null)
			columnList = new ArrayList<TableColumn>();
		columnList.add(new TableColumn(field, value));
		filedCount ++;
	}

	public void addProperty(String field, Object value, int type) {
		if(columnList == null)
			columnList = new ArrayList<TableColumn>();
		columnList.add(new TableColumn(field, value, type));
		filedCount ++;
	}

	public void addProperty(String field, Object value, int type, boolean primaryKey) {
		if(columnList == null)
			columnList = new ArrayList<TableColumn>();
		columnList.add(new TableColumn(field, value, type, primaryKey));
		if(!primaryKey)
			filedCount ++;
	}
	
	public List<TableColumn> getColumns() {
		return columnList;
	}

	public int getFiledCount() {
		return filedCount;
	}
}