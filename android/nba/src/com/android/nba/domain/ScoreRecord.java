package com.android.nba.domain;

import java.io.Serializable;

public class ScoreRecord implements Serializable {

	private static final long serialVersionUID = -1814860562849044777L;
	
	/**
	 * <data time="20110503132020" inverseMsg="" inverseTime="" score="【小牛 96 -  94 湖人】">
	 * <![CDATA[<TEXTFORMAT LEADING="2" type="head"><FONT FACE="宋体" SIZE="12" COLOR="#ff3300" 
	 * LETTERSPACING="0" KERNING="0">【第四节】</FONT></TEXTFORMAT><TEXTFORMAT LEADING="2" 
	 * type="content"><FONT FACE="宋体" SIZE="12" COLOR="#ff3300" LETTERSPACING="0" KERNING="0">
	 * 网易直播员: 感谢大家的收看 本次直播就到这里 下次直播再见</FONT></TEXTFORMAT>  (13:20)]]></data>
	 * String reg = "<data time=\"(\\d{14})\" inverseMsg=\"\" inverseTime=\"\" score=\"(.+)\">" +
	 * "LETTERSPACING=\"0\" KERNING=\"0\">(.+)</FONT></TEXTFORMAT><TEXTFORMAT LEADING=\"2\" " +
	 * "type=\"content\"><FONT FACE=\"宋体\" SIZE=\"12\" COLOR=\"#[f0-9]{6}\" LETTERSPACING=\"0\" KERNING=\"0\">" +
	 * "(.+)</FONT></TEXTFORMAT>  \\((\\d{2}:\\d{2})\\)\\]\\]></data>";
	 * 
	 */
	
	private long postTime;
	private String currentScore;
	private String round;
	private String content;
	private String time;
	private String postTimeString;
	public String getPostTimeString() {
		if(postTimeString == null) {
			postTimeString = (postTime + "").substring(8);
			postTimeString = postTimeString.substring(0, 2) + ":" + postTimeString.substring(2, 4) + ":" + postTimeString.substring(4, 6);
		}
		return postTimeString;
	}
	public void setPostTimeString(String postTimeString) {
		this.postTimeString = postTimeString;
	}
	public long getPostTime() {
		return postTime;
	}
	public void setPostTime(long postTime) {
		this.postTime = postTime;
	}
	public String getCurrentScore() {
		return currentScore;
	}
	public void setCurrentScore(String currentScore) {
		this.currentScore = currentScore;
	}
	public String getRound() {
		return round;
	}
	public void setRound(String round) {
		this.round = round;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		return buffer.append(getPostTimeString())
		.append(" ").append(getCurrentScore())
		.append(" ").append(getRound())
		.append(" ").append(getContent()).toString();
	}
}