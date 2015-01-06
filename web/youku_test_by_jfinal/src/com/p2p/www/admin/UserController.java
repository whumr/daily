package com.p2p.www.admin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.p2p.www.common.BaseController;
import com.p2p.www.common.Common_Constants;
import com.p2p.www.model.Youku_playlist;
import com.p2p.www.model.Youku_user;
import com.p2p.www.model.Youku_video;
import com.p2p.www.util.HttpUtil;
import com.p2p.www.util.UserPlaylistLoadTask;
import com.p2p.www.util.UserPlaylistRefreshTask;
import com.p2p.www.util.UserVedioLoadTask;
import com.p2p.www.util.UserVedioRefreshTask;

/**
 * 用户管理
 * @author Administrator
 *
 */
public class UserController extends BaseController {

	private static final Logger Log = Logger.getLogger(UserController.class);
	
	private static final String SELECT_SQL = "select id, y_id, name, link, avatar, description, regist_time, videos_count, playlists_count";
	private static final String FROM_SQL = "from youku_user";
	
	/**
	 * 用户管理首页
	 */
	public void index() {
		Integer page = getParaToInt();
		Page<Record> users = Db.paginate(page == null ? 1 : page, 
				Common_Constants.PAGE_SIZE, SELECT_SQL, FROM_SQL);
		setAttr("data", users);
	}
	
	/**
	 * 新增用户
	 */
	public void add() {
		String user_name = null;
		JSONObject result = new JSONObject();
		try {
			user_name = URLDecoder.decode(getPara("name"), "UTF-8");
			if (Youku_user.dao.getUserByName(user_name) != null)
				result.put("error", "user already exsits");
			else {
				Youku_user user = HttpUtil.getUserInfoByName(user_name);
				if (user != null)
					user.save();
				else {
					Log.error("user not found");
					result.put("error", "user not found");
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result.put("error", "user_name not correct");
		} catch (IOException e) {
			e.printStackTrace();
			result.put("error", "youku connect error");
		}
		renderJson(result.toJSONString());
	}

	/**
	 * 删除用户及其数据
	 */
	public void deleteUser() {
		JSONObject result = new JSONObject();
		int id = getParaToInt("id");
		Youku_user.dao.deleteById(id);
		Youku_video.dao.deleteByUserId(id);
		Youku_playlist.dao.deleteByUserId(id);
		renderJson(result.toJSONString());
	}
	
	/**
	 * 刷新用户数据
	 */
	public void refreshUser() {
		JSONObject result = new JSONObject();
		int id = getParaToInt("id");
		try {
			//取出用户信息，根据现存视频数，取优酷用户信息视频数，再决定page_count，取更新的视频
			Youku_user user_db = Youku_user.dao.findById(id);
			if (user_db != null) {
				Youku_user user_youku = HttpUtil.getUserInfoById(user_db.getStr(Youku_user.Y_ID));
				user_youku.set(Youku_user.ID, user_db.get(Youku_user.ID));
				//更新用户
				user_youku.update();
				//更新视频
				int new_vedio_count = user_youku.getInt(Youku_user.VIDEOS_COUNT) - user_db.getInt(Youku_user.VIDEOS_COUNT);
				if (new_vedio_count > 0)
					new Thread(new UserVedioRefreshTask(user_db, new_vedio_count)).start();
				//更新专辑
				int new_playlist_count = user_youku.getInt(Youku_user.PLAYLISTS_COUNT) - user_db.getInt(Youku_user.PLAYLISTS_COUNT);
				if (new_playlist_count > 0)
					new Thread(new UserPlaylistRefreshTask(user_db, new_playlist_count)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			result.put("error", "youku connect error");
		}
		renderJson(result.toJSONString());
	}

	/**
	 * 重新加载用户数据
	 */
	public void reloadUser() {
		JSONObject result = new JSONObject();
		int id = getParaToInt("id");
		Youku_user user_db = Youku_user.dao.findById(id);
		if (user_db != null) {
			//删除原有数据
			Youku_video.dao.deleteByUserId(id);
			Youku_playlist.dao.deleteByUserId(id);
			try {
				Youku_user user_youku = HttpUtil.getUserInfoById(user_db.getStr(Youku_user.Y_ID));
				user_youku.set(Youku_user.ID, user_db.get(Youku_user.ID));
				//更新用户
				user_youku.update();
				//加载视频
				new Thread(new UserVedioLoadTask(user_youku)).start();
				//加载专辑
				new Thread(new UserPlaylistLoadTask(user_youku)).start();
			} catch (IOException e) {
				e.printStackTrace();
				result.put("error", "youku connect error");
			}
		}
		renderJson(result.toJSONString());
	}
	
}