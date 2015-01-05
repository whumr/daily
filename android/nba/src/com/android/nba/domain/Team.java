package com.android.nba.domain;

import java.io.Serializable;

public class Team implements Serializable {

	private static final long serialVersionUID = 3806467052955037113L;
	
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
	
	private int id;
	private int scoreTotal;
	private int[] scores;
	private String scoresOt;
	private String cnName;
	private String fullcnName;
	private String areaSub;
	private String area;
	private int vote;
	private Player topPlayer;
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScoresOt() {
		return scoresOt;
	}
	public void setScoresOt(String scoresOt) {
		this.scoresOt = scoresOt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getScoreTotal() {
		return scoreTotal;
	}
	public void setScoreTotal(int scoreTotal) {
		this.scoreTotal = scoreTotal;
	}
	public int[] getScores() {
		return scores;
	}
	public void setScores(int[] scores) {
		this.scores = scores;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getFullcnName() {
		return fullcnName;
	}
	public void setFullcnName(String fullcnName) {
		this.fullcnName = fullcnName;
	}
	public String getAreaSub() {
		return areaSub;
	}
	public void setAreaSub(String areaSub) {
		this.areaSub = areaSub;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
	public Player getTopPlayer() {
		return topPlayer;
	}
	public void setTopPlayer(Player topPlayer) {
		this.topPlayer = topPlayer;
	}
}
