package com.android.nba.handler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TabWidget;

import com.android.nba.domain.Constants.System_Static.TEXTCONSTANTS;
import com.android.nba.domain.Match;
import com.android.nba.util.TabUtil;

/**
 * 对TabWidget label进行修改的handler
 * @author lming
 *
 */
public class TabUpdateHandler extends Handler {

	private static final String LOG = "TabUpdateHandler";
	
	private TabWidget tabWidget;
	//tab的索引
	private int index;
	//tab的tag
	private String tag;
	
	public TabUpdateHandler() {
		super();
	}
	
	public TabUpdateHandler(TabWidget tabWidget, int index) {
		super();
		this.tabWidget = tabWidget;
		this.index = index;
	}

	public TabUpdateHandler(TabWidget tabWidget, int index, String tag) {
		super();
		this.tabWidget = tabWidget;
		this.index = index;
		this.tag = tag;
	}
	
	/**
	 * 更新tab显示日期,更新matchMap
	 */
	@SuppressWarnings("unchecked")
	public void handleMessage(Message msg) {
		//获取比赛列表
		List<Match> matches = null;
		if(msg.obj == null || ((List<Match>)msg.obj).isEmpty())
			matches = new ArrayList<Match>();
		else
			matches = (List<Match>)msg.obj;
		//tab显示文字
		String result = null;
		if(matches != null && !matches.isEmpty()) {
			result = matches.get(0).getUrlDate();
		}
		try {
			//无比赛,禁用tab
			if(result == null) {
				result = TEXTCONSTANTS.NO_MATCHES;
				TabUtil.updateTabLabel(tabWidget, index, result);
				TabUtil.disableTab(tabWidget, index);
			} else {
				TabUtil.updateTabLabelExt(tabWidget, index, result);
				TabUtil.enableTab(tabWidget, index);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(LOG, e.getMessage());
		}
		Log.d(LOG, "update tab " + index + " lable to " + result);
	}
	
	/**
	 * 更新tab日期,更新match map
	 * @param msg
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	private String updateMartchMap(Message msg) {
//		List<Match> matches = (List<Match>)msg.obj;
//		if(matches == null || matches.isEmpty())
//			return TEXTCONSTANTS.NO_MATCHES;
//		//加入map,list
//		MatchesMap.addMatches(matches.get(0).getUrlDate(), matches);
//		matchDate = msg.arg2 + "";
//		if(msg.arg1 == MatchResearchRunner.BEFORE)
//			MatchesMap.addPreMatch(matchDate);
//		else if(msg.arg1 == MatchResearchRunner.AFTER)
//			MatchesMap.addNextMatch(matchDate);
//		return matchDate;
//	}
	
	public TabWidget getTabWidget() {
		return tabWidget;
	}
	public void setTabWidget(TabWidget tabWidget) {
		this.tabWidget = tabWidget;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}