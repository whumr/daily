package com.android.nba.listener;

import java.text.ParseException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.android.nba.R;
import com.android.nba.domain.Match;
import com.android.nba.domain.MatchesMap;
import com.android.nba.domain.Constants.System_Config;
import com.android.nba.domain.Constants.System_Config.System_DateFormat;
import com.android.nba.domain.Constants.System_Static.TEXTCONSTANTS;
import com.android.nba.handler.MatchListUpdateHandler;
import com.android.nba.handler.TabUpdateHandler;
import com.android.nba.runner.MatchResearchRunner;
import com.android.nba.util.TabUtil;

/**
 * tablistener
 * @author lming
 */
public class MatchTabListener implements OnTabChangeListener {
	
	private static final String LOG = "MatchTabListener";

	private Context context;
	private TabHost tabHost;
	
	public MatchTabListener(Context context, TabHost tabHost) {
		super();
		this.context = context;
		this.tabHost = tabHost;
	}

	/**
	 * 切换tab,显示当前tab的比赛列表.
	 * 轮转tab显示,使切换后的tab居中
	 */
	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		//获取列表
		ListView listView = (ListView)(((Activity)context).findViewById(R.id.matchListView));
		//更新列表
		//切换tab
		try {
			updateMatchList(listView);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(LOG, "tabChage error " + e.getMessage());
		}
	}
	
	/**
	 * 更新列表
	 * @param listView
	 * @throws ParseException 
	 */
	private void updateMatchList(ListView listView) throws ParseException {
		//当前tab索引
		int index = tabHost.getCurrentTab();
		//更新列表的handler
		MatchListUpdateHandler matchListUpdateHandler = new MatchListUpdateHandler((Activity)context, listView);
		//message
		Message msg = new Message();
		//先清空列表
		matchListUpdateHandler.sendMessage(msg);
		//比赛list
		List<Match> matchList = null;
		//切换后日期
		//tab1
		String date1 = null;
		//tab2
		String date2 = null;
		//tab3
		String date3 = null;
		//获取比赛list
		switch (index) {
			//前一场
			case 0 :
				//前一场到达tab2
				matchList = MatchesMap.getPreMatches();
				date2 = MatchesMap.getPreMatchDate();
				//当前页到tab3
				date3 = MatchesMap.getCurrentDate();
				//tab1获取pre predate
				MatchesMap.setCurrentDate(date2);
				date1 = MatchesMap.getPreMatchDate();
				//修改label
				if(date1 == null) {
					date1 = System_Config.TAB_TAGS[0][1];
					TabUtil.updateTabLabel(tabHost.getTabWidget(), 0, date1);
					//禁用tab1
					TabUtil.disableTab(tabHost.getTabWidget(), 0);
					//启动matchRearchRunner
					TabUpdateHandler tuBefore = new TabUpdateHandler(getTabHost().getTabWidget(), 0);
			    	MatchResearchRunner msrBefore = new MatchResearchRunner(tuBefore, 
			    			System_DateFormat.URL_DATEFORMAT.parse(date2), 
			    			MatchResearchRunner.BEFORE);
			    	msrBefore.start();
				} else {
					//如果无比赛,禁用tab1
					if(date1.equals(TEXTCONSTANTS.NO_MATCHES)) {
						TabUtil.updateTabLabel(tabHost.getTabWidget(), 0, date1);
						TabUtil.disableTab(tabHost.getTabWidget(), 0);
					} else
						TabUtil.updateTabLabelExt(tabHost.getTabWidget(), 0, date1);
				}
				TabUtil.updateTabLabelExt(tabHost.getTabWidget(), 1, date2);
				TabUtil.updateTabLabelExt(tabHost.getTabWidget(), 2, date3);
				//启用tab3
				if(date3 != null)
					TabUtil.enableTab(tabHost.getTabWidget(), 2);
				break;
			//当前日,理论上不会点击到此次
			case 1:
				return;
			//后一场
			case 2:
				//当前页到达tab1
				matchList = MatchesMap.getNextMatches();
				date1 = MatchesMap.getCurrentDate();
				//下一页到tab2
				date2 = MatchesMap.getNextMatchDate();
				//tab1获取next next date
				MatchesMap.setCurrentDate(date2);
				date3 = MatchesMap.getNextMatchDate();
				//修改label
				if(date3 == null) {
					date3 = System_Config.TAB_TAGS[2][1];
					TabUtil.updateTabLabel(tabHost.getTabWidget(), 2, date3);
					//禁用tab3
					TabUtil.disableTab(tabHost.getTabWidget(), 2);
					//启动matchRearchRunner
					TabUpdateHandler tuBefore = new TabUpdateHandler(getTabHost().getTabWidget(), 2);
			    	MatchResearchRunner msrBefore = new MatchResearchRunner(tuBefore, 
			    			System_DateFormat.URL_DATEFORMAT.parse(date2), 
			    			MatchResearchRunner.AFTER);
			    	msrBefore.start();
				} else {
					//如果无比赛,禁用tab1
					if(date3.equals(TEXTCONSTANTS.NO_MATCHES)) {
						TabUtil.updateTabLabel(tabHost.getTabWidget(), 2, date3);
						TabUtil.disableTab(tabHost.getTabWidget(), 2);
					} else
						TabUtil.updateTabLabelExt(tabHost.getTabWidget(), 2, date3);
				}
				TabUtil.updateTabLabelExt(tabHost.getTabWidget(), 0, date1);
				TabUtil.updateTabLabelExt(tabHost.getTabWidget(), 1, date2);
				//启用tab3
				if(date1 != null)
					TabUtil.enableTab(tabHost.getTabWidget(), 0);
				break;
		}
		tabHost.setCurrentTab(1);
		//list为空,新起runner去取比赛列表
		//注意结果返回前先禁用tab
		if(matchList == null)
			;
		
		//更新列表的handler
		MatchListUpdateHandler matchListUpdateHandler2 = new MatchListUpdateHandler((Activity)context, listView);
		//message
		Message msg2 = new Message();
		//处理message
		msg2.obj = matchList;
		//更新match列表
		matchListUpdateHandler2.sendMessage(msg2);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public TabHost getTabHost() {
		return tabHost;
	}

	public void setTabHost(TabHost tabHost) {
		this.tabHost = tabHost;
	}
}