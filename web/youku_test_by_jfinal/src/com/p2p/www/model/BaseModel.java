package com.p2p.www.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableInfo;
import com.jfinal.plugin.activerecord.TableInfoMapping;

@SuppressWarnings("rawtypes")
public class BaseModel<M extends Model> extends Model<M> {

	private static final long serialVersionUID = -3575339467630984678L;
	
	protected TableInfo tableInfo;

	protected BaseModel() {
		tableInfo = TableInfoMapping.me().getTableInfo(this.getClass());
	}

	protected BaseModel(JSONObject json) {
		String id = json.getString("id");
		json.put("y_id", id);
		json.remove("id");
		tableInfo = TableInfoMapping.me().getTableInfo(this.getClass());
		for (String key : json.keySet()) {
			Object o = json.get(key);
			Class<?> colType = tableInfo.getColType(key);
			if (colType == Integer.class || colType == int.class)
				set(key, Math.abs(json.getInteger(key)));
			else if (colType == Long.class || colType == long.class)
				set(key, Math.abs(json.getLong(key)));
			else if (o instanceof Boolean)
				set(key, json.getBooleanValue(key) ? "1" : "0");
			else if (o instanceof String)
				set(key, json.getString(key));
			else if (o instanceof JSON)
				set(key, o.toString());
			else
				set(key, o);
			
		}
	}
	
	@Override
	public Integer getInt(String attr) {
		return Integer.parseInt(get(attr).toString());
	}
}
