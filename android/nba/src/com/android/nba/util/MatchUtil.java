package com.android.nba.util;

import java.util.Calendar;

import com.android.nba.domain.Constants;
import com.android.nba.domain.Constants.System_Static;
import com.android.nba.domain.Constants.System_Static.TEXTCONSTANTS;
import com.android.nba.domain.Match;
import com.android.nba.domain.Player;
import com.android.nba.domain.ScoreRecord;
import com.android.nba.domain.Team;

/**
 * Match工具类,包含组织显示内容等方法
 * @author lming
 *
 */
public class MatchUtil {

	/**
	 * 计算赛季
	 * @return
	 */
	public static void initSeason() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int month = calendar.get(Calendar.MONTH);
		if(month >= 10)
			Constants.SEASON = String.valueOf(calendar.get(Calendar.YEAR));
		else
			Constants.SEASON = String.valueOf(calendar.get(Calendar.YEAR) - 1);
		
		System_Static.MATCHURL = System_Static.MATCHURL.replaceFirst("#", Constants.SEASON);
		System_Static.BOXSCOREURL = System_Static.BOXSCOREURL.replaceFirst("#", Constants.SEASON);
	}
	
	/**
	 * 获取用于显示的比赛概要信息
	 * @param match
	 * @return
	 */
	public static String getMatchString(Match match) {
		return null;
	}
	
	/**
	 * 获取状态文字
	 * @param match
	 * @return
	 */
	public static String getMatchScoreText(Match match) {
		return getTeamScoreText(match.getHomeTeam()) + " : " + getTeamScoreText(match.getAwayTeam());
	}
	
	/**
	 * 获取team的比分文字
	 * 格式如"(12 35 15 25 [9 15 12])"
	 * @param team
	 * @return
	 */
	public static String getTeamScoreText(Team team) {
		//常规比分
		int[] commonScores = team.getScores();
		StringBuilder buffer = new StringBuilder();
		buffer.append(team.getScoreTotal()).append(" (");
		for(int i = 0; i < commonScores.length; i ++)
			buffer.append(commonScores[i]).append(" ");
		//加时赛
		if(!team.getScoresOt().equals(System_Static.NO_OTSCORES))
			//加时赛比分
			buffer.append(team.getScoresOt().replaceAll(",", " "));
		else 
			buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(")");
		return buffer.toString();
	}
	
	/**
	 * 获取team的比分文字
	 * 格式如"12 35 15 25 [9 15 12]"
	 * @param team
	 * @return
	 */
	public static String getTeamScoreDetail(Team team) {
		//常规比分
		int[] commonScores = team.getScores();
		StringBuilder buffer = new StringBuilder();
		for(int i = 0; i < commonScores.length; i ++)
			buffer.append(commonScores[i]).append(" ");
		//加时赛
		if(!team.getScoresOt().equals(System_Static.NO_OTSCORES))
			//加时赛比分
			buffer.append(team.getScoresOt().replaceAll(",", " "));
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}
	
	/**
	 * 获取比分的直播显示字符串
	 * 格式如"10:11 "
	 * @param score
	 * @return
	 */
	public static String getScoreStringForLive(ScoreRecord score) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(score.getPostTimeString())
		.append(" ").append(score.getContent());
		return buffer.toString();
	}
	
	/**
	 * 获取topplayer数据
	 * @param player
	 * @return
	 */
	public static String getPlayerPerformance(Player player) {
		if(player == null)
			return TEXTCONSTANTS.NO_TOPPLAYER;
		StringBuilder buffer = new StringBuilder();
		buffer.append(player.getNamecn()).append(" (");
		buffer.append(player.getPoint()).append("分")
		.append(" ").append(player.getReboundTotal()).append("篮板")
		.append(" ").append(player.getAssist()).append("助攻").append(")");
		return buffer.toString();
	}
}