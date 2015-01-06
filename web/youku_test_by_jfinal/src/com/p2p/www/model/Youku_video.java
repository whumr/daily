package com.p2p.www.model;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;

/**
 * @author Administrator
 */
public class Youku_video extends BaseModel<Youku_video> {
	
	private static final long serialVersionUID = 3972301376002554655L;
	
	public static final String ID = "id", USER_ID = "user_id", Y_ID = "y_id", PLAYLIST_ID = "playlist_id", 
			TITLE = "title", CATEGORY = "category", LINK = "link", THUMBNAIL = "thumbnail", PUBLISHED = "published", 
			DURATION = "duration", VIEW_COUNT = "view_count", COMMENT_COUNT = "comment_count", UP_COUNT = "up_count", 
			DOWN_COUNT = "down_count", FAVORITE_COUNT = "favorite_count", PUBLIC_TYPE = "public_type", STATE = "state", 
			STREAMTYPES = "streamtypes", OPERATION_LIMIT = "operation_limit";

	public static final Youku_video dao = new Youku_video();

	public Youku_video() {
	}
	
	public Youku_video(JSONObject json) {
		super(json);
	}
	
	public int deleteByUserId(int id) {
		return Db.update("delete from youku_video where user_id = ?", id);
	}
}