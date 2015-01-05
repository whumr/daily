package com.android.nba;

import java.util.Date;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;

import com.android.nba.domain.Constants.System_Config;
import com.android.nba.domain.Constants.System_Config.System_DateFormat;
import com.android.nba.domain.Constants;
import com.android.nba.domain.MatchesMap;
import com.android.nba.handler.MatchListUpdateHandler;
import com.android.nba.handler.TabUpdateHandler;
import com.android.nba.listener.MatchTabListener;
import com.android.nba.runner.MatchResearchRunner;
import com.android.nba.util.MatchUtil;

/**
 * 主界面
 * @author lming
 */
public class NBAActivity extends TabActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);
        Date date = new Date();
        Constants.SEASON = "";
        //计算当前赛季
        MatchUtil.initSeason();
        //初始化MatchesMap
        initMatchesMap(date);
        //初始化tab页
        initTab(date);
    }
    
    /**
     * 初始化MatchesMap
     * @param date
     */
    private void initMatchesMap(Date date) {
    	//初始化当前日
    	MatchesMap.setCurrentDate(System_DateFormat.URL_DATEFORMAT.format(date));
    	MatchesMap.addNextMatch(System_DateFormat.URL_DATEFORMAT.format(date));
    }
    
    /**
     * 初始化tab 
     */
    private void initTab(Date date) {
    	TabHost tabHost = getTabHost();
    	String[][] tags = System_Config.TAB_TAGS;
    	int focusIndex = ((tags.length % 2) == 0 ? tags.length / 2 - 1 : tags.length / 2);
    	//新建tab
    	for(int i = 0; i < tags.length; i ++) {
    		//非当天比赛,禁用tab,等比赛加载完毕后,若有比赛,再启用点击
    		if(i != focusIndex) {
    			tabHost.addTab(tabHost.newTabSpec(tags[i][0]).setIndicator(tags[i][1])
    					.setContent(R.id.matchListView));
    			//禁用tab点击
    			tabHost.getTabWidget().getChildAt(i).setEnabled(false);
    		} else
    			tabHost.addTab(tabHost.newTabSpec(tags[i][0]).setIndicator(
    					System_DateFormat.DEFAULT_DISPLAY_DATEFORMAT.format(date))
    					.setContent(R.id.matchListView));
    	}
    	//启动获取当日比赛的runner
    	//启动更新tab日期的runner
    	startMatchReader(date);
    	//当天tab获取焦点
    	tabHost.setCurrentTab(focusIndex);
    	//初始化MatchesMap中保存的tab
    	MatchesMap.setCurrentTab(focusIndex);
    	//tab切换监听器
    	tabHost.setOnTabChangedListener(new MatchTabListener(this, tabHost));
    }
    
    /**
     * 启动数据读取runner
     * @param date
     */
    private void startMatchReader(Date date) {
    	//获取当天数据
    	MatchListUpdateHandler muhToday = new MatchListUpdateHandler(this, (ListView)findViewById(R.id.matchListView));
    	MatchResearchRunner msrToday = new MatchResearchRunner(muhToday, date);
    	msrToday.start();
    	//获取前一场数据
    	TabUpdateHandler tuBefore = new TabUpdateHandler(getTabHost().getTabWidget(), 0);
    	MatchResearchRunner msrBefore = new MatchResearchRunner(tuBefore, date, MatchResearchRunner.BEFORE);
    	msrBefore.start();
    	//获取后一场数据
    	TabUpdateHandler tuAfter = new TabUpdateHandler(getTabHost().getTabWidget(), 2);
    	MatchResearchRunner msrAfter = new MatchResearchRunner(tuAfter, date, MatchResearchRunner.AFTER);
    	msrAfter.start();
    }
}