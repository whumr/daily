package com.android.nba.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.nba.R;
import com.android.nba.domain.Constants.System_Config.System_DateFormat;
import com.android.nba.domain.Constants.System_Static.Match_Constants.Match_Status;
import com.android.nba.domain.Match;
import com.android.nba.util.MatchUtil;

public class MatchListAdapter extends BaseAdapter {

	//数据
	private List<Match> matchList;
	
	//context
	private Context context;
	
	//LayoutInflater
	private LayoutInflater mInflater;
	
	public MatchListAdapter(List<Match> matchList, Context context) {
		super();
		this.matchList = matchList;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MatchListViewHolder matchListViewHolder;
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.match_list, null);
			matchListViewHolder = new MatchListViewHolder();
			matchListViewHolder.statusText = (TextView)convertView.findViewById(R.id.statusText);
			matchListViewHolder.homeTeamText = (TextView)convertView.findViewById(R.id.homeTeamText);
			matchListViewHolder.homeScoreText = (TextView)convertView.findViewById(R.id.homeScoreText);
			matchListViewHolder.homePlayerText = (TextView)convertView.findViewById(R.id.homePlayerText);
			matchListViewHolder.awayTeamText = (TextView)convertView.findViewById(R.id.awayTeamText);
			matchListViewHolder.awayScoreText = (TextView)convertView.findViewById(R.id.awayScoreText);
			matchListViewHolder.awayPlayerText = (TextView)convertView.findViewById(R.id.awayPlayerText);
			convertView.setTag(matchListViewHolder);
		} else {
			matchListViewHolder = (MatchListViewHolder)convertView.getTag();
		}
		Match match = matchList.get(position);
		matchListViewHolder.statusText.setText(Match_Status.getStatusString(match.getStatus()) + "\n" 
				+ System_DateFormat.MATCH_TIME_DATEFORMAT.format(match.getDateTime()));
		matchListViewHolder.homeTeamText.setText(match.getHomeTeam().getFullcnName());
		matchListViewHolder.homeScoreText.setText(MatchUtil.getTeamScoreText(match.getHomeTeam()));
		matchListViewHolder.homePlayerText.setText(MatchUtil.getPlayerPerformance(match.getHomeTeam().getTopPlayer()));
		matchListViewHolder.awayTeamText.setText(match.getAwayTeam().getFullcnName());
		matchListViewHolder.awayScoreText.setText(MatchUtil.getTeamScoreText(match.getAwayTeam()));
		matchListViewHolder.awayPlayerText.setText(MatchUtil.getPlayerPerformance(match.getAwayTeam().getTopPlayer()));
		return convertView;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return matchList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return matchList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public List<Match> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<Match> matchList) {
		this.matchList = matchList;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}

class MatchListViewHolder {
	TextView statusText;
	TextView homeTeamText;
	TextView homeScoreText;
	TextView homePlayerText;
	TextView awayTeamText;
	TextView awayScoreText;
	TextView awayPlayerText;
}