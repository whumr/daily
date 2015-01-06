package com.p2p.www.util;

import com.p2p.www.model.Youku_user;

public abstract class YoukuTask implements Runnable {
	protected static final int DEFAULT_LOAD_COUNT = 500;
	protected static final int DEFAULT_FRESH_COUNT = 10;
	
	protected Youku_user user;
	protected String user_id;
	protected String user_name;
	
	public YoukuTask(Youku_user user) {
		this.user = user;
		this.user_id = user.getStr(Youku_user.Y_ID);
		this.user_name = user.getStr(Youku_user.NAME);
	}
	
	protected String getUserId() {
		return user_id;
	}

	protected String getUserName() {
		return user_name;
	}
}