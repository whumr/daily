package com.p2p.www.jobs;

import java.io.IOException;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.log.Logger;
import com.p2p.www.model.Youku_user;
import com.p2p.www.util.HttpUtil;
import com.p2p.www.util.UserPlaylistRefreshTask;
import com.p2p.www.util.UserVedioRefreshTask;

public class VediosRefreshJob implements Job {

	private static final Logger Log = Logger.getLogger(VediosRefreshJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<Youku_user> list = Youku_user.dao.find("select * from youku_user");
		if (list != null && !list.isEmpty()) {
			for (Youku_user user_db : list) {
				try {
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
					Log.info("refresh user " + user_db.getStr(Youku_user.NAME));
				} catch (IOException e) {
					e.printStackTrace();
					Log.error("refresh user " + user_db.getStr(Youku_user.NAME) + e.getMessage());
				}
			}
		}
	}
}