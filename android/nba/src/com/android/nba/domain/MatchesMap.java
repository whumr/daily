package com.android.nba.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MatchesMap extends HashMap<String, List<Match>> {

	private static final long serialVersionUID = -7506696733750743378L;

	//存放match的map
	//key:日期字符串;value:比赛列表
	private static final Map<String, List<Match>> matchesMap = new HashMap<String, List<Match>>();
	
	//存放score的map
	//key:mid;value:比分列表
	private static final Map<Integer, List<ScoreRecord>> scoreMap = new HashMap<Integer, List<ScoreRecord>>();
	
	//赛事链表
	//存放日期字符串
	private static final LinkedList<String> matchesLink = new LinkedList<String>();
	
	//当前tab页的日期
	private static String currentDate;
	
	//当前tab页的id
	private static int currentTab;
	
	//添加matches
	public static void addMatches(String date, List<Match> matches) {
		matchesMap.put(date, matches);
	}
	
	//取得matches
	public static List<Match> getMatches(String date) {
		return matchesMap.get(date);
	}
	
	//添加比分
	public static void addMatchScores(int mid, List<ScoreRecord> scores) {
		scoreMap.put(mid, scores);
	}
	
	//获取比分
	public static List<ScoreRecord> getMatchScores(int mid) {
		return scoreMap.get(mid);
	}
	
	//追加比分
	public static void appendMatchScores(int mid, List<ScoreRecord> scores) {
		List<ScoreRecord> list = scoreMap.get(mid);
		if(list == null)
			list = new ArrayList<ScoreRecord>();
		list.addAll(scores);
	}
	
	//添加后一场matches
	public synchronized static void addNextMatch(String date) {
		matchesLink.addLast(date);
	}
	
	//添加前一场matches
	public synchronized static void addPreMatch(String date) {
		matchesLink.addFirst(date);
	}
	
	//获取下一次matches
	public static List<Match> getNextMatch(String date) {
		if(matchesLink.indexOf(date) == -1)
			return null;
		int index = matchesLink.indexOf(date) + 1;
		if(index > matchesLink.size() - 1)
			return null;
		return matchesMap.get(matchesLink.get(index));
	}

	//获取上一次matches
	public static List<Match> getPreMatch(String date) {
		if(matchesLink.indexOf(date) == -1)
			return null;
		int index = matchesLink.indexOf(date) - 1;
		if(index < 0)
			return null;
		return matchesMap.get(matchesLink.get(index));
	}
	
	//获取下一个比赛日
	public static String getNextMatchDate() {
		if(getCurrentIndex() >= matchesLink.size() - 1)
			return null;
		return matchesLink.get(getCurrentIndex() + 1);
	}
	
	//获取下一个比赛列表
	public static List<Match> getNextMatches() {
		if(getCurrentIndex() >= matchesLink.size() - 1)
			return null;
		return matchesMap.get(getNextMatchDate());
	}
	
	//获取上一个比赛日
	public static String getPreMatchDate() {
		if(getCurrentIndex() <= 0)
			return null;
		return matchesLink.get(getCurrentIndex() - 1);
	}
	
	//获取上一个比赛列表
	public static List<Match> getPreMatches() {
		if(getCurrentIndex() <= 0)
			return null;
		return matchesMap.get(getPreMatchDate());
	}
	
	//销毁
	public static void destroy() {
		matchesMap.clear();
		matchesLink.clear();
		scoreMap.clear();
		currentDate = null;
	}

	//获取当前比赛日
	public static String getCurrentDate() {
		return currentDate;
	}

	//设置当前比赛日
	public static void setCurrentDate(String currentDate) {
		MatchesMap.currentDate = currentDate;
	}
	
	//获取当前索引号
	private static int getCurrentIndex() {
		return matchesLink.indexOf(currentDate);
	}

	//获取当前tab id
	public static int getCurrentTab() {
		return currentTab;
	}

	//设置当前tab id
	public static void setCurrentTab(int currentTab) {
		MatchesMap.currentTab = currentTab;
	}
}
