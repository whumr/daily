package com.p2p.www.util;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.log.Logger;
import com.p2p.www.common.Common_Constants;
import com.p2p.www.common.Common_Constants.KEYS;
import com.p2p.www.model.Youku_playlist;
import com.p2p.www.model.Youku_user;

public class UserPlaylistRefreshTask extends YoukuTask {

	private static final Logger Log = Logger.getLogger(UserPlaylistRefreshTask.class);
	
	private int new_count;
	
	public UserPlaylistRefreshTask(Youku_user user, int new_count) {
		super(user);
		this.new_count = new_count;
	}

	@Override
	public void run() {
		int i = 0, page = 1;
		while (i < new_count) {
			try {
				String url = Common_Constants.URL.YOUKU_USER_PLAYLISTS_URL_BY_ID 
						+ user_id + "&page=" + page + "&count=" + (DEFAULT_FRESH_COUNT > new_count ? new_count : DEFAULT_FRESH_COUNT);
				String response = HttpUtil.get(url);
				JSONObject json = JSON.parseObject(response);
				if (json.containsKey(KEYS.ERROR)) {
					Log.error(json.getString(KEYS.ERROR) + "\t" + url);
					break;
				} else if (!json.containsKey(KEYS.PLAYLISTS)) {
					Log.error("no playlist found" + "\t" + url);
					break;
				} 
				//读取数据
				JSONArray playlists = json.getJSONArray(KEYS.PLAYLISTS);
				for (int j = 0; j < playlists.size(); j++) {
					Youku_playlist playlist = new Youku_playlist(playlists.getJSONObject(j));
					playlist.set(Youku_playlist.USER_ID, user.getInt(Youku_user.ID));
					playlist.save();
					i ++;
				}
				page ++;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}