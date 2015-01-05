package com.android.nba.domain;

import java.io.Serializable;

public class Player implements Serializable {
	
	private static final long serialVersionUID = -1104784047754136470L;
	
	/**
	 * "homeTopPlayerTech":{
	 * 		"type":"季后赛","sid":2010,"teamId":5,"status":0,"mid":13668,"playerId":216,"nameFullCn":"扎克·兰多夫","nameCn":"兰多夫",
	 * 		"nameEn":null,"nameFullEn":null,"logo":null,"point":29,"steal":0,"reboundTotal":13,"assist":0,"foul":3,
	 * 		"substitute":"前锋","block":2,"shot":22,"shotHit":12,"shotPercent":54.55,"point3":1,"point3Hit":0,"point3Percent":0.00,
	 * 		"freeThrow":6,"freeThrowHit":5,"freeThrowPercent":83.33,"reboundOff":1,"reboundDef":0,"turnover":2,
	 * 		"postTime":"2011-05-14 11:40:04,000","times":"39:09","blockAgainst":0,"effect":null,"teamCn":"灰熊","teamFullCn":"孟菲斯灰熊"},
	 */
	
	private int playerId;
	private String sid;
	private int teamId;
	private String teamcn;
	private String teamcnfull;
	private String posttime;
	private String times;
	private int status;
	private int mid;
	private String type;
	private String namecn;
	private String namecnFull;
	private String substitute;
	/**
	 * 数据统计
	 */
	private int point;
	private int steal;
	private int reboundTotal;
	private int assist;
	private int foul;
	private int block;
	private int shot;
	private int shotHit;
	private double shotPercent;
	private int point3;
	private int point3Hit;
	private double point3Percent;
	private int freeThrow;
	private int freeThrowHit;
	private double freeThrowPercent;
	private int reboundOff;
	private int reboundDef;
	private int turnover;
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public String getTeamcn() {
		return teamcn;
	}
	public void setTeamcn(String teamcn) {
		this.teamcn = teamcn;
	}
	public String getTeamcnfull() {
		return teamcnfull;
	}
	public void setTeamcnfull(String teamcnfull) {
		this.teamcnfull = teamcnfull;
	}
	public String getPosttime() {
		return posttime;
	}
	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNamecn() {
		return namecn;
	}
	public void setNamecn(String namecn) {
		this.namecn = namecn;
	}
	public String getNamecnFull() {
		return namecnFull;
	}
	public void setNamecnFull(String namecnFull) {
		this.namecnFull = namecnFull;
	}
	public String getSubstitute() {
		return substitute;
	}
	public void setSubstitute(String substitute) {
		this.substitute = substitute;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getSteal() {
		return steal;
	}
	public void setSteal(int steal) {
		this.steal = steal;
	}
	public int getReboundTotal() {
		return reboundTotal;
	}
	public void setReboundTotal(int reboundTotal) {
		this.reboundTotal = reboundTotal;
	}
	public int getAssist() {
		return assist;
	}
	public void setAssist(int assist) {
		this.assist = assist;
	}
	public int getFoul() {
		return foul;
	}
	public void setFoul(int foul) {
		this.foul = foul;
	}
	public int getBlock() {
		return block;
	}
	public void setBlock(int block) {
		this.block = block;
	}
	public int getShot() {
		return shot;
	}
	public void setShot(int shot) {
		this.shot = shot;
	}
	public int getShotHit() {
		return shotHit;
	}
	public void setShotHit(int shotHit) {
		this.shotHit = shotHit;
	}
	public int getPoint3() {
		return point3;
	}
	public void setPoint3(int point3) {
		this.point3 = point3;
	}
	public int getPoint3Hit() {
		return point3Hit;
	}
	public void setPoint3Hit(int point3Hit) {
		this.point3Hit = point3Hit;
	}
	public int getFreeThrow() {
		return freeThrow;
	}
	public void setFreeThrow(int freeThrow) {
		this.freeThrow = freeThrow;
	}
	public int getFreeThrowHit() {
		return freeThrowHit;
	}
	public void setFreeThrowHit(int freeThrowHit) {
		this.freeThrowHit = freeThrowHit;
	}
	public int getReboundOff() {
		return reboundOff;
	}
	public void setReboundOff(int reboundOff) {
		this.reboundOff = reboundOff;
	}
	public int getReboundDef() {
		return reboundDef;
	}
	public void setReboundDef(int reboundDef) {
		this.reboundDef = reboundDef;
	}
	public int getTurnover() {
		return turnover;
	}
	public void setTurnover(int turnover) {
		this.turnover = turnover;
	}
	public double getShotPercent() {
		return shotPercent;
	}
	public void setShotPercent(double shotPercent) {
		this.shotPercent = shotPercent;
	}
	public double getPoint3Percent() {
		return point3Percent;
	}
	public void setPoint3Percent(double point3Percent) {
		this.point3Percent = point3Percent;
	}
	public double getFreeThrowPercent() {
		return freeThrowPercent;
	}
	public void setFreeThrowPercent(double freeThrowPercent) {
		this.freeThrowPercent = freeThrowPercent;
	}
}
