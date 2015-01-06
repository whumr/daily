package com.p2p.www.model;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;

/**
 * @author Administrator
 */
public class Youku_playlist extends BaseModel<Youku_playlist> {
	
	private static final long serialVersionUID = 2498188842508887805L;
	
	public static final String ID = "id", USER_ID = "user_id", Y_ID = "y_id", NAME = "name", 
			LINK = "link", PALY_LINK = "paly_link", THUMBNAIL = "thumbnail", PUBLISHED = "published", 
			Y_USER = "y_user", DURATION = "duration", VIEW_COUNT = "view_count", VIDEO_COUNT = "video_count";
	
	public static final Youku_playlist dao = new Youku_playlist();

	public Youku_playlist() {
	}
	
	public Youku_playlist(JSONObject json) {
		super(json);
	}
	
	public int deleteByUserId(int id) {
		return Db.update("delete from youku_playlist where user_id = ?", id);
	}
}