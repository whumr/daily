package com.android.nba.handler;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.android.nba.adapter.MatchListAdapter;
import com.android.nba.domain.Match;
import com.android.nba.listener.MatchListListener;

/**
 * 更新比赛列表的handler
 * @author lming
 *
 */
public class MatchListUpdateHandler extends Handler {

	private static final String LOG = "MatchListUpdateHandler";
	
	//Activity对象
	private Activity activity;
	
	//ListView对象
	private ListView listView;
	
	public MatchListUpdateHandler(Activity activity, ListView listView) {
		super();
		this.activity = activity;
		this.listView = listView;
	}

	/**
	 * 更新tab显示日期,更新matchMap
	 */
	@SuppressWarnings({"unchecked" })
	public void handleMessage(Message msg) {
		List<Match> matches = null; 
		if(msg.obj == null || ((List<Match>)msg.obj).isEmpty())
			matches = new ArrayList<Match>();
		else
			matches = (List<Match>)msg.obj;
		//更新列表
		listView.setAdapter(new MatchListAdapter(matches, activity));
		//添加点击事件
		listView.setOnItemClickListener(new MatchListListener(listView, activity));
		
		Log.d(LOG, "update match list succeed.");
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}
}