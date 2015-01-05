package com.ming.server.domain.ext;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.ming.server.domain.User;


public class UserEx extends User {

	private static final long serialVersionUID = 2928748225749848015L;
	
	private static Logger logger = Logger.getLogger(UserEx.class);

	private String userIds;
	
	public static UserEx getUserByJson(String jsonString) {
		try {
			JSONObject json = new JSONObject(jsonString);
			UserEx user = new UserEx();
			if(json.getString(NAME) != null)
				user.setName(json.getString(NAME));
			if(json.getString(PASS) != null)
				user.setPass(json.getString(PASS));
			if(json.getString(AGE) != null)
				user.setAge(json.getInt(AGE));
			if(json.getString(GENDER) != null)
				user.setGender(json.getInt(GENDER));
			if(json.getString(NICKNAME) != null)
				user.setNickname(json.getString(NICKNAME));
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("error read user from json:" + jsonString, e);
			return null;
		}
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
}
