package com.ming.server.util;

import com.ming.server.common.domain.BaseEntity;
import com.ming.server.common.domain.TableColumn;

public class SQLUtil {

	private static final String INSERT_SQL = "insert into {1} ({2}) values ({3})";
	
	private static final char FIELD_SEPRATOR = ',';

	private static final char PREPARE_SEPRATOR = '?';
	
	/**
	 * create insert sql
	 * @param entity
	 * @return
	 */
	public static String getInsertSql(BaseEntity entity) {
		String result = INSERT_SQL.replaceFirst("\\{1\\}", entity.getTableName());
		StringBuilder valuesBuffer = new StringBuilder();
		StringBuilder paramsBuffer = new StringBuilder();
		for(TableColumn column : entity.getColumns()) {
			if(!column.isPrimaryKey()) {
				paramsBuffer.append(column.getName()).append(FIELD_SEPRATOR);
				valuesBuffer.append(PREPARE_SEPRATOR).append(FIELD_SEPRATOR);
			}
		}
		if(paramsBuffer.length() > 1) {
			paramsBuffer.deleteCharAt(paramsBuffer.length() - 1);
			valuesBuffer.deleteCharAt(valuesBuffer.length() - 1);
		}
		result = result.replaceFirst("\\{2\\}", paramsBuffer.toString())
			.replaceFirst("\\{3\\}", valuesBuffer.toString());
		return result;
	}
}
