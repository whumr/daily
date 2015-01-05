package com.android.nba.runner;

import java.io.IOException;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.nba.domain.MatchesMap;
import com.android.nba.domain.ScoreRecord;
import com.android.nba.handler.AbstractRunner;
import com.android.nba.util.ScoreParser;

/**
 * 获取比分的runner
 * @author lming
 */
public class MatchScoreRunner extends AbstractRunner {

	private static final String LOG = "MatchScoreRunner";
	
	//match id
	private int mid;
	
	//match date
	private String date;

	//最新一条的时间
	private long minTime = 0;
	
	//处理响应的handler
	private Handler handler;
	
	public MatchScoreRunner(int mid, String date, Handler handler) {
		super();
		this.mid = mid;
		this.date = date;
		this.handler = handler;
	}

	public MatchScoreRunner(int mid, String date, long minTime, Handler handler) {
		super();
		this.mid = mid;
		this.date = date;
		this.minTime = minTime;
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				Message msg = new Message();
				List<ScoreRecord> scores = getScores();
				//取得比分
				msg.obj = scores;
				//更新信息
				update(scores);
				//发送消息
				handler.sendMessage(msg);
				//状态切换为FINISHED
				status = FINISHED;
			}
		}.start();
		//状态切换为running
		status = RUNNING;
	}
	
	/**
	 * 获取比分列表
	 * @return
	 */
	private List<ScoreRecord> getScores() {
		List<ScoreRecord> scores = null;
		try {
			scores = ScoreParser.parseSocreRecord(mid, date, minTime);
			Log.d(LOG, "getScores mid " + mid + " date " + date);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(LOG, "getScores error " + e.getMessage());
		}
		return scores;
	}
	
	/**
	 * 更新最新时间,更新scoremap
	 */
	private void update(List<ScoreRecord> scores) {
		if(scores != null && !scores.isEmpty()) {
			//最新时间为最后一条记录的时间
			minTime = scores.get(scores.size() - 1).getPostTime();
			//更新scoreMap
			MatchesMap.appendMatchScores(mid, scores);
		}
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public long getMinTime() {
		return minTime;
	}

	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}
}