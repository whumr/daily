package com.ming.server.common.domain;

import java.sql.Types;
import java.util.Date;

public class TableColumn {

	private String name;

	private int type;
	
	private Object value;

	private boolean primaryKey;

	public TableColumn(String name, Object value, int type) {
		this(name, value, type, false);
	}

	public TableColumn(String name, Object value) {
		this(name, value, false);
	}

	public TableColumn(String name, Object value, boolean primaryKey) {
		super();
		this.name = name;
		this.primaryKey = primaryKey;
		type = Types.VARCHAR;
		if(value instanceof Long)
			type = Types.BIGINT;
		else if(value instanceof Integer)
			type = Types.INTEGER;
		else if(value instanceof Date)
			type = Types.TIMESTAMP;
		else if(value instanceof Double)
			type = Types.DOUBLE;
	}

	public TableColumn(String name, Object value, int type, boolean primaryKey) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
		this.primaryKey = primaryKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
