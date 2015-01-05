package com.android.nba.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import com.android.nba.domain.Constants.System_Config;
import com.android.nba.domain.ScoreRecord;

/**
 * 解析比分直播
 * @author lming
 *
 */
public class ScoreParser {
	
	/**
	 * 字符集
	 */
	public static String ENCODING = System_Config.GBK_ENCODING; 

	/**
	 * 解析比分直播记录
	 * @param mid
	 * @param date
	 * @return
	 * @throws IOException
	 */
	public static List<ScoreRecord> parseSocreRecord(int mid, String date) throws IOException {
		return parseSocreRecord(NetUtil.getScores(mid, date, ENCODING));
	}
	
	/**
	 * 解析比分直播记录,获取最新的记录
	 * @param mid
	 * @param date
	 * @param minTime
	 * @return
	 * @throws IOException
	 */
	public static List<ScoreRecord> parseSocreRecord(int mid, String date, long minTime) throws IOException {
		List<ScoreRecord> scoreList =  parseSocreRecord(mid, date);
		//比分不为空,过滤重复信息
		if(scoreList != null && !scoreList.isEmpty()) {
			for(Iterator<ScoreRecord> iterator = scoreList.iterator(); iterator.hasNext(); ) {
				if(iterator.next().getPostTime() <= minTime)
					iterator.remove();
				else
					break;
			}
		}
		return scoreList;
	}
	
	/**
	 * <data time="20110503132020" inverseMsg="" inverseTime="" score="【小牛 96 -  94 湖人】">
	 * <![CDATA[<TEXTFORMAT LEADING="2" type="head"><FONT FACE="宋体" SIZE="12" COLOR="#ff3300" 
	 * LETTERSPACING="0" KERNING="0">【第四节】</FONT></TEXTFORMAT><TEXTFORMAT LEADING="2" 
	 * type="content"><FONT FACE="宋体" SIZE="12" COLOR="#ff3300" LETTERSPACING="0" KERNING="0">
	 * 网易直播员: 感谢大家的收看 本次直播就到这里 下次直播再见</FONT></TEXTFORMAT>  (13:20)]]></data>
	 * 
	 * String reg = "<data time=\"(\\d{14})\" inverseMsg=\"\" inverseTime=\"\" score=\"(.+)\">" +
	 * "LETTERSPACING=\"0\" KERNING=\"0\">(.+)</FONT></TEXTFORMAT><TEXTFORMAT LEADING=\"2\" " +
	 * "type=\"content\"><FONT FACE=\"宋体\" SIZE=\"12\" COLOR=\"#[f0-9]{6}\" LETTERSPACING=\"0\" KERNING=\"0\">" +
	 * "(.+)</FONT></TEXTFORMAT>  \\((\\d{2}:\\d{2})\\)\\]\\]></data>";
	 * 
	 */
	/**
	 * 解析直播记录
	 * @param content
	 * @return
	 */
	public static List<ScoreRecord> parseSocreRecord(String content) {
		if(content == null || content.trim().equals(""))
			return null;
		List<ScoreRecord> scoreList = new ArrayList<ScoreRecord>();
		Matcher matcher = System_Config.SCOREPATTERN.matcher(content);
		while(matcher.find()) {
			ScoreRecord scoreRecord = new ScoreRecord();
			scoreRecord.setPostTime(Long.parseLong(matcher.group(1)));
			scoreRecord.setCurrentScore(matcher.group(2));
			scoreRecord.setRound(matcher.group(3));
			scoreRecord.setContent(matcher.group(4));
			scoreRecord.setTime(matcher.group(5));
			scoreList.add(scoreRecord);
		}
		return scoreList;
	}
}