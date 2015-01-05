package com.android.nba.listener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.nba.R;
import com.android.nba.domain.Match;
import com.android.nba.domain.Constants.System_Config.System_Params;

/**
 * 比赛点击listener
 * @author lming
 */
public class MatchListListener implements OnItemClickListener {

	//ListView对象
	private ListView listView;
	
	//Activity对象
	private Activity activity;
	
	public MatchListListener(ListView listView, Activity activity) {
		this.listView = listView;
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		//取得当前点击的比赛,传给直播界面
		Match match = (Match)listView.getAdapter().getItem(position);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable(System_Params.MATCH_PARAM, match);
		try {
			intent.setClass(activity, Class.forName(activity.getString(R.string.liveActivity)));
			intent.putExtras(bundle);
			activity.startActivity(intent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}