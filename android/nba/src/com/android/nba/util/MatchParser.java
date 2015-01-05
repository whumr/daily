package com.android.nba.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.nba.domain.Constants.System_Config;
import com.android.nba.domain.Match;
import com.android.nba.domain.Player;
import com.android.nba.domain.Team;

public class MatchParser {
	
	private static final String LOG = "MatchParser";

	/**
	 * [{
	 * "location":"","type":"季后赛","date":"2011-05-14 09:00:00,000","time":"结束","sid":2010,
	 * "status":2,"live":1,"homeId":5,"awayId":8,"mid":13668,
	 * "homeScoreTotal":95,"homeScore1":23,"homeScore2":21,"homeScore3":28,"homeScore4":23,"homeScoreOt":"",
	 * "awayScoreTotal":83,"awayScore1":21,"awayScore2":33,"awayScore3":14,"awayScore4":15,"awayScoreOt":"",
	 * "linkNews":"topicid=000538BN;pointstart=60;pointend=90;titlelength=22;listnum=30;startday=2011-05-11;endday=2011-05-15;",
	 * "linkPhoto":"http://sports.163.com/photoview/0USG0005/67502.html",
	 * "linkPreview":"http://sports.163.com/11/0513/11/73UBTRDT00051CA1.html",
	 * "linkReport":"http://sports.163.com/11/0514/11/740TTFU600051CA1.html",
	 * "linkStar":"http://sports.163.com/11/0514/11/740UE9FH00051CA1.html\r\nhttp://sports.163.com/11/0514/11/740U8C5A00051CA1.html\r\nhttp://sports.163.com/11/0514/11/740ULKQ900051CA1.html\r\nhttp://sports.163.com/11/0514/12/740V5UF900051CA1.html\r\nhttp://sports.163.com/11/0514/12/74105OPE00051CA1.html",
	 * "round":4,"homeCn":"灰熊","awayCn":"雷霆","awayAreaSub":"西北","homeAreaSub":"西南",
	 * "homeTopPlayerId":216,"awayTopPlayerId":305,
	 * "homeArea":"西部","awayArea":"西部","homeFullCn":"孟菲斯灰熊","awayFullCn":"俄克拉荷马雷霆","awayVote":522,"homeVote":805,
	 * "homeEn":null,"homeFullEn":null,"homeLogo":null,"awayEn":null,"awayFullEn":null,"awayLogo":null,
	 * "homeTopPlayerTech":{
	 * 		"type":"季后赛","sid":2010,"teamId":5,"status":0,"mid":13668,"playerId":216,"nameFullCn":"扎克·兰多夫","nameCn":"兰多夫",
	 * 		"nameEn":null,"nameFullEn":null,"logo":null,"point":29,"steal":0,"reboundTotal":13,"assist":0,"foul":3,
	 * 		"substitute":"前锋","block":2,"shot":22,"shotHit":12,"shotPercent":54.55,"point3":1,"point3Hit":0,"point3Percent":0.00,
	 * 		"freeThrow":6,"freeThrowHit":5,"freeThrowPercent":83.33,"reboundOff":1,"reboundDef":0,"turnover":2,
	 * 		"postTime":"2011-05-14 11:40:04,000","times":"39:09","blockAgainst":0,"effect":null,"teamCn":"灰熊","teamFullCn":"孟菲斯灰熊"},
	 * "awayTopPlayerTech":{
	 * 		"type":"季后赛","sid":2010,"teamId":8,"status":0,"mid":13668,"playerId":305,"nameFullCn":"拉塞尔·维斯布鲁克","nameCn":"维斯布鲁克",
	 * 		"nameEn":null,"nameFullEn":null,"logo":null,"point":25,"steal":2,"reboundTotal":3,"assist":4,"foul":1,"substitute":"后卫","block":0,
	 * 		"shot":20,"shotHit":10,"shotPercent":50.00,"point3":4,"point3Hit":1,"point3Percent":25.00,"freeThrow":5,"freeThrowHit":4,
	 * 		"freeThrowPercent":80.00,"reboundOff":0,"reboundDef":0,"turnover":5,"postTime":"2011-05-14 11:40:04,000","times":"36:26",
	 * 		"blockAgainst":0,"effect":null,"teamCn":"雷霆","teamFullCn":"俄克拉荷马雷霆"},
	 * "postTime":"2011-05-14 13:52:02,696","homeScoreOts":[],"awayScoreOts":[],"homeTopPlayerMarkAvg":7.9528303,"awayTopPlayerMarkAvg":8.594936
	 * }]
	 */
	
	/**
	 * 解析直播信息
	 * @param content
	 * @return
	 */
	public static List<Match> parseMatch(String content, String date) {
		if(content == null || content.trim().equals(""))
			return null;
		List<String> contentList = splitMatch(content);
		if(!contentList.isEmpty()) {
			List<Match> titleList = new ArrayList<Match>();
			for (String contents : contentList) {
				Match match = parseMatch0(contents, date);
				if(match != null)
					titleList.add(match);
			}
			return titleList;
		}
		return null;
	}
	
	/**
	 * 解析直播信息
	 * @param content
	 * @return
	 * @throws IOException 
	 */
	public static List<Match> parseMatchByDate(String date) throws IOException {
		return parseMatch(NetUtil.getMatchInfo(date), date);
	}
	
	/**
	 * 解析topplayer信息
	 * @param JSONObject
	 * @return
	 */
	public static Player parseTopPlayer(JSONObject JSONObject) {
		return null;
	}
	
	/**
	 * 切分直播表
	 * @param content
	 * @return
	 */
	private static List<String> splitMatch(String content) {
		List<String> list = new ArrayList<String>();
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < content.length(); i++) {
			if(content.charAt(i) == '{')
				stack.push(i);
			else if(content.charAt(i) == '}') {
				Integer index = stack.pop();
				if(stack.isEmpty())
					list.add(content.substring(index, i + 1));
			}
		}
		return list;
	}
	
	/**
	 * 解析比赛信息
	 * @param content
	 * @return
	 */
	private static Match parseMatch0(String content, String date) {
		try {
//			"location":"","type":"季后赛","date":"2011-05-14 09:00:00,000","time":"结束","sid":2010,
//			 * "status":2,"live":1,"homeId":5,"awayId":8,"mid":13668,
//			 * "homeScoreTotal":95,"homeScore1":23,"homeScore2":21,"homeScore3":28,"homeScore4":23,"homeScoreOt":"",
//			 * "awayScoreTotal":83,"awayScore1":21,"awayScore2":33,"awayScore3":14,"awayScore4":15,"awayScoreOt":"",
//			 * "linkNews":"topicid=000538BN;pointstart=60;pointend=90;titlelength=22;listnum=30;startday=2011-05-11;endday=2011-05-15;",
//			 * "linkPhoto":"http://sports.163.com/photoview/0USG0005/67502.html",
//			 * "linkPreview":"http://sports.163.com/11/0513/11/73UBTRDT00051CA1.html",
//			 * "linkReport":"http://sports.163.com/11/0514/11/740TTFU600051CA1.html",
//			 * "linkStar":"http://sports.163.com/11/0514/11/740UE9FH00051CA1.html\r\nhttp://sports.163.com/11/0514/11/740U8C5A00051CA1.html\r\nhttp://sports.163.com/11/0514/11/740ULKQ900051CA1.html\r\nhttp://sports.163.com/11/0514/12/740V5UF900051CA1.html\r\nhttp://sports.163.com/11/0514/12/74105OPE00051CA1.html",
//			 * "round":4,"homeCn":"灰熊","awayCn":"雷霆","awayAreaSub":"西北","homeAreaSub":"西南",
//			 * "homeTopPlayerId":216,"awayTopPlayerId":305,
			JSONObject json = new JSONObject(content);
			Match match = new Match();
			//比赛基本信息
			match.setLocation(json.getString("location"));
			match.setDate(json.getString("date"));
			match.setLive(json.getInt("live"));
			match.setMid(json.getInt("mid"));
			match.setRound(json.getInt("round"));
			match.setSid(json.getString("sid"));
			match.setStatus(json.getInt("status"));
			match.setType(json.getString("type"));
			match.setPostTime(json.getString("postTime"));
			//球队信息
			match.setHomeTeam(parseTeam(json, System_Config.HOMEPREFIX));
			match.setAwayTeam(parseTeam(json, System_Config.AWAYPREFIX));
			return match;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(LOG, "parseMatch error " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * 解析球队信息
	 * @param json
	 * @param prefix	标记是主队还是客队
	 * @return
	 */
	private static Team parseTeam(JSONObject json, String prefix) {
//		"location":"","type":"季后赛","date":"2011-05-10 09:30:00,000","time":"结束","sid":2010,"status":2,"live":1,
//		"homeId":5,"awayId":8,"mid":13666,"homeScoreTotal":123,"homeScore1":28,"homeScore2":25,"homeScore3":19,"homeScore4":24,
//		"homeScoreOt":"13,10,4","awayScoreTotal":133,"awayScore1":16,"awayScore2":33,"awayScore3":22,"awayScore4":25,
//		"awayScoreOt":"13,10,14","linkNews":"topicid=000538BN;pointstart=60;pointend=90;titlelength=22;listnum=30;startday=2011-05-07;endday=2011-05-11;",
//		"linkPhoto":"http://sports.163.com/photoview/0USG0005/67332.html#p=73MINN3K0USG0005",
//		"linkPreview":"http://sports.163.com/11/0509/13/73K8CT6T00051CA1.html",
//		"linkReport":"http://sports.163.com/11/0510/13/73MR4E9I00051CA1.html",
//		"linkStar":"http://sports.163.com/11/0510/13/73MRJNQ100051CA1.html\r\nhttp://sports.163.com/11/0510/14/73MT7I8500051CA1.html\r\nhttp://sports.163.com/11/0510/13/73MRPLTP00051CA1.html\r\nhttp://sports.163.com/11/0510/14/73MS9L6B00051CA1.html\r\nhttp://sports.163.com/11/0510/15/73MVNP6700051CA1.html\r\nhttp://sports.163.com/11/0510/14/73MU17TL00051CA1.html",
//		"round":0,"homeCn":"灰熊","awayCn":"雷霆","awayAreaSub":"西北","homeAreaSub":"西南","homeTopPlayerId":210,"awayTopPlayerId":305,
//		"homeArea":"西部","awayArea":"西部","homeFullCn":"孟菲斯灰熊","awayFullCn":"俄克拉荷马雷霆","awayVote":854,
//		"homeVote":1112,"homeEn":null,"homeFullEn":null,"homeLogo":null,"awayEn":null,"awayFullEn":null,"awayLogo":null,
		Team team = new Team();
		try {
			team.setType(prefix);
			team.setArea(json.getString(prefix + "Area"));
			team.setAreaSub(json.getString(prefix + "AreaSub"));
			team.setCnName(json.getString(prefix + "Cn"));
			team.setFullcnName(json.getString(prefix + "FullCn"));
			team.setId(json.getInt(prefix + "Id"));
			int[] scores = {
					json.getInt(prefix + "Score1"),
					json.getInt(prefix + "Score2"),
					json.getInt(prefix + "Score3"),
					json.getInt(prefix + "Score4")
			};
			team.setScores(scores);
			team.setScoresOt(json.getString(prefix + "ScoreOt"));
			team.setScoreTotal(json.getInt(prefix + "ScoreTotal"));
//			team.setVote(json.getInt(prefix + "Vote"));
			try {
				team.setTopPlayer(parsePlayer(new JSONObject(json.getString(prefix + "TopPlayerTech"))));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i(LOG, "no topPlayer...");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(LOG, "parseTeam error " + e.getMessage());
		}
		return team;
	}
	
	/**
	 * 解析球队最佳球员信息
	 * 
	 * "homeTopPlayerTech":
	 * 		{"type":"季后赛","sid":2010,"teamId":5,"status":0,"mid":13666,"playerId":210,"nameFullCn":"马克·加索尔","nameCn":"小加索尔",
	 * 		"nameEn":null,"nameFullEn":null,"logo":null,"point":26,"steal":2,"reboundTotal":21,"assist":3,"foul":3,"substitute":"中锋",
	 * 		"block":1,"shot":20,"shotHit":11,"shotPercent":55.00,"point3":2,"point3Hit":0,"point3Percent":0.00,"freeThrow":5,
	 * 		"freeThrowHit":4,"freeThrowPercent":80.00,"reboundOff":10,"reboundDef":0,"turnover":6,"postTime":"2011-05-10 13:40:04,000",
	 * 		"times":"56:53","blockAgainst":0,"effect":null,"teamCn":"灰熊","teamFullCn":"孟菲斯灰熊"
	 * 		}
	 * 
	 * @param json
	 * @return
	 */
	private static Player parsePlayer(JSONObject json) {
		Player player = new Player();
		try {
			player.setAssist(json.getInt("assist"));
			player.setBlock(json.getInt("block"));
			player.setFoul(json.getInt("foul"));
			player.setFreeThrow(json.getInt("freeThrow"));
			player.setFreeThrowHit(json.getInt("freeThrowHit"));
			player.setFreeThrowPercent(json.getDouble("freeThrowPercent"));
			player.setMid(json.getInt("mid"));
			player.setNamecn(json.getString("nameCn"));
			player.setNamecnFull(json.getString("nameFullCn"));
			player.setPlayerId(json.getInt("playerId"));
			player.setPoint(json.getInt("point"));
			player.setPoint3(json.getInt("point3"));
			player.setPoint3Hit(json.getInt("point3Hit"));
			player.setPoint3Percent(json.getDouble("point3Percent"));
			player.setPosttime(json.getString("postTime"));
			player.setReboundDef(json.getInt("reboundDef"));
			player.setReboundOff(json.getInt("reboundOff"));
			player.setReboundTotal(json.getInt("reboundTotal"));
			player.setShot(json.getInt("shot"));
			player.setShotHit(json.getInt("shotHit"));
			player.setShotPercent(json.getDouble("shotPercent"));
			player.setSid(json.getString("sid"));
			player.setStatus(json.getInt("status"));
			player.setSteal(json.getInt("steal"));
			player.setSubstitute(json.getString("substitute"));
			player.setTeamcn(json.getString("teamCn"));
			player.setTeamcnfull(json.getString("teamFullCn"));
			player.setTeamId(json.getInt("teamId"));
			player.setTimes(json.getString("times"));
			player.setTurnover(json.getInt("turnover"));
			player.setType(json.getString("type"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(LOG, "parsePlayer error " + e.getMessage());
		}
		return player;
	}
}