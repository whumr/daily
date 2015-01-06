package com.p2p.www.model;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Administrator
 */
public class Youku_user extends BaseModel<Youku_user> {
	
	private static final long serialVersionUID = 762910613132611855L;
	
	public static final String ID = "id", Y_ID = "y_id", NAME = "name", LINK = "link", AVATAR = "avatar", 
			AVATAR_LARGE = "avatar_large", GENDER = "gender", DESCRIPTION = "description", REGIST_TIME = "regist_time", 
			VIDEOS_COUNT = "videos_count", PLAYLISTS_COUNT = "playlists_count", FAVORITES_COUNT = "favorites_count", 
			FOLLOWERS_COUNT = "followers_count", FOLLOWING_COUNT = "following_count", STATUSES_COUNT = "statuses_count", 
			SUBSCRIBE_COUNT = "subscribe_count", VV_COUNT = "vv_count", FOLLOWER = "follower", 
			FOLLOWING = "following", IS_SHARE = "is_share";
	
	public static final Youku_user dao = new Youku_user();

	public Youku_user() {
	}
	
	public Youku_user(JSONObject json) {
		super(json);
	}
	
	public Youku_user getUserByName(String user_name) {
		return findFirst("select * from youku_user where name = ?", user_name);
	}
}