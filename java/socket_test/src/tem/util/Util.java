package tem.util;

import org.json.JSONException;
import org.json.JSONObject;

import tem.domain.User;

public class Util {

	public static User parseUser(String userInfo) throws JSONException {
		User user = new User();
		JSONObject json = new JSONObject(userInfo);
		user.setName(json.getString("name"));
		user.setPass(json.getString("pass"));
		if(json.get("age") != null)
			user.setAge(json.getInt("age"));
		if(json.get("gender") != null)
			user.setGender(json.getInt("gender"));
		if(json.get("nickname") != null)
			user.setNickname(json.getString("nickname"));
		return user;
	}
}