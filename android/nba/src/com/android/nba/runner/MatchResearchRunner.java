package com.android.nba.runner;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.nba.domain.Constants.System_Config;
import com.android.nba.domain.Constants.System_Config.System_DateFormat;
import com.android.nba.domain.Constants.System_Static.TEXTCONSTANTS;
import com.android.nba.domain.Match;
import com.android.nba.domain.MatchesMap;
import com.android.nba.handler.AbstractRunner;
import com.android.nba.util.MatchParser;

/**
 * 通用的比赛查询runner,返回统一格式的message
 * obj: 比赛列表;
 * arg1: 查询类型;
 * arg2: 基准日期
 * msg.obj = getMatch();
 * msg.arg1 = type;
 * msg.arg2 = Integer.parseInt(Constants.URL_DATEFORMAT.format(baseDate));
 * @author lming
 *
 */
public class MatchResearchRunner extends AbstractRunner {
	
	private static final String LOG = "MatchResearchRunner";
	//往前查找比赛
	public static final int BEFORE = -1;
	//查找当天比赛
	public static final int TODAY = 0;
	//往后查找比赛
	public static final int AFTER = 1;
	
	//处理响应的handler
	private Handler handler;
	//基准日期
	private Date baseDate;
	//查找类型
	private int type = TODAY;

	/**
	 * 默认查找当天的比赛信息
	 * @param handler
	 * @param baseDate
	 */
	public MatchResearchRunner(Handler handler, Date baseDate) {
		super();
		this.handler = handler;
		this.baseDate = baseDate;
	}

	public MatchResearchRunner(Handler handler, Date baseDate, int type) {
		super();
		this.handler = handler;
		this.baseDate = baseDate;
		this.type = type;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				//取得比赛
				msg.obj = getMatch();
				//取得无比赛的日子,加入matchmap,避免每次都重新去查询比赛
				if(msg.obj == null && type != TODAY) {
					if(type == BEFORE)
						MatchesMap.addPreMatch(TEXTCONSTANTS.NO_MATCHES);
					if(type == AFTER)
						MatchesMap.addNextMatch(TEXTCONSTANTS.NO_MATCHES);
				}
				msg.arg1 = type;
				msg.arg2 = Integer.parseInt(System_DateFormat.URL_DATEFORMAT.format(baseDate));
				handler.sendMessage(msg);
				status = FINISHED;
			}
		}.start();
		//状态切换为running
		status = RUNNING;
	}
	
	/**
	 * 更新matchMap
	 */
	private void updateMatchMap(String date, List<Match> matches) {
		//加入map,list
		if(matches != null) {
			MatchesMap.addMatches(date, matches);
			if(type == BEFORE)
				MatchesMap.addPreMatch(date);
			else if(type == AFTER)
				MatchesMap.addNextMatch(date);
		}
	}
	
	/**
	 * 查询比赛信息
	 * @return
	 */
	private List<Match> getMatch() {
		if(type == TODAY) {
			String urlDate = System_DateFormat.URL_DATEFORMAT.format(baseDate);
			try {
				List<Match> matches = MatchParser.parseMatchByDate(urlDate);
				//当日比赛可以为空,即当日无比赛
				if(matches != null) {
					urlDate = filterMatch(matches, urlDate, type);
					updateMatchMap(urlDate, matches);
				} else
					MatchesMap.addMatches(urlDate, new ArrayList<Match>());
				Log.d(LOG, "type " + type + " get matches by " + urlDate);
				return matches;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(LOG, e.getMessage());
				return null;
			}
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(baseDate);
			for(int i = 0; i < System_Config.SEARCH_MAX_GAP; i ++) {
				try {
					calendar.add(Calendar.DAY_OF_MONTH, type);
					String urlDate = System_DateFormat.URL_DATEFORMAT.format(calendar.getTime());
					List<Match> matches = MatchParser.parseMatchByDate(urlDate);
					//非当日比赛不能为空
					if(matches != null && !matches.isEmpty()) {
						//过滤比赛
						urlDate = filterMatch(matches, urlDate, type);
						if(matches != null && !matches.isEmpty()) {
							updateMatchMap(urlDate, matches);
							Log.d(LOG, "type " + type + " get matches by " + urlDate + " baseDate = " + 
									System_DateFormat.URL_DATEFORMAT.format(baseDate));
							return matches;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(LOG, e.getMessage());
					return null;
				}
			}
			return null;
		}
	}
	
	/**
	 * 过滤matchlist
	 * 1、当天的比赛,若日期不相等,设定当天无比赛
	 * 2、查询之前的比赛,若比赛日期早于查询日期,则最近的之前比赛则为取得的比赛
	 * 3、查询之后的比赛,暂未发现规律,逐日查询
	 * @param matchList
	 * @param urlDate
	 * @param type
	 */
	private String filterMatch(List<Match> matchList, String urlDate, int type) {
		switch(type) {
			case TODAY :
				//剔除非当日的比赛
				//是当日比赛的set urlDate
				for(Iterator<Match> it = matchList.iterator(); it.hasNext();) {
					Match match = it.next();
					if(!System_DateFormat.URL_DATEFORMAT.format(match.getDateTime()).equals(urlDate)) {
						it.remove();
					} else
						match.setUrlDate(urlDate);
				}
				return urlDate;
			case BEFORE:
				//取第一场match的日期
				Match match = matchList.get(0);
				//最近日期
				String matchDate = System_DateFormat.URL_DATEFORMAT.format(match.getDateTime());
				//不晚于urldate,为最近的之前比赛
				try {
					//增加判断条件,两个时间差距大于N天,无效,避免错误数据的影响
					Date searchDate = System_DateFormat.URL_DATEFORMAT.parse(matchDate);
					Date baseDate = System_DateFormat.URL_DATEFORMAT.parse(urlDate);
					if(!searchDate.after(baseDate) && baseDate.getTime() - searchDate.getTime() < System_Config.SEARCH_MAX_MISTAKE) {
						for(Match match1 : matchList) {
							match1.setUrlDate(matchDate);
						}
					} else
						matchList = null;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return matchDate;
			case AFTER:
				//取第一场match的日期
				Match match2 = matchList.get(0);
				//最近日期
				String matchDate2 = System_DateFormat.URL_DATEFORMAT.format(match2.getDateTime());
				//不早于urldate,为最近的之后比赛
				try {
					//增加判断条件,两个时间差距大于N天,无效,避免错误数据的影响
					Date searchDate = System_DateFormat.URL_DATEFORMAT.parse(matchDate2);
					Date baseDate = System_DateFormat.URL_DATEFORMAT.parse(urlDate);
					if(!searchDate.before(baseDate) && searchDate.getTime() - baseDate.getTime() < System_Config.SEARCH_MAX_MISTAKE) {
						for(Match match1 : matchList) {
							match1.setUrlDate(matchDate2);
						}
					} else
						matchList = null;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return matchDate2;
		}
		return null;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Date getBaseDate() {
		return baseDate;
	}

	public void setBaseDate(Date baseDate) {
		this.baseDate = baseDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}