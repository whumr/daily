package com.android.nba.domain;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Constants {

	//赛季
	public static String SEASON;
	
	/**
	 * 系统参数
	 * @author lming
	 *
	 */
	public static final class System_Config {
		
		//主队前缀
		public static final String HOMEPREFIX = "home";
		
		//客队前缀
		public static final String AWAYPREFIX = "away";
		
		//缓存数组长度
		public static final int BYTEBUFFER_SIZE = 10240;

		//读取数据的字符集
		public static final String DEFAULT_ENCODING = "UTF_8";

		//读取比分的字符集
		public static final String GBK_ENCODING = "GBK";
		
		//搜索最近比赛时最大间隔
		public static final int SEARCH_MAX_GAP = 5;
		
		//搜索最近比赛时最大误差,误差大于该值,认为无效
		public static final long SEARCH_MAX_MISTAKE = 5 * 24 * 3600 * 1000;
		
		//读取分数的正则表达式
		public static final String SCOREREG = 
			"<data time=\"(\\d{14})\" inverseMsg=\"\" inverseTime=\"\" score=\"(.+)\">" +
			"<!\\[CDATA\\[<TEXTFORMAT LEADING=\"2\" type=\"head\"><FONT FACE=\"宋体\" SIZE=\"12\" COLOR=\"#[f0-9]{6}\" " +
			"LETTERSPACING=\"0\" KERNING=\"0\">(.+)</FONT></TEXTFORMAT><TEXTFORMAT LEADING=\"2\" " +
			"type=\"content\"><FONT FACE=\"宋体\" SIZE=\"12\" COLOR=\"#[f0-9]{6}\" LETTERSPACING=\"0\" KERNING=\"0\">" +
			"网易直播员: (.+)</FONT></TEXTFORMAT>  \\((\\d{2}:\\d{2})\\)\\]\\]></data>";
		
		//读取分数的正则表达式
		public static final Pattern SCOREPATTERN = Pattern.compile(SCOREREG);
		
		//tab的tag
		public static final String[][] TAB_TAGS = {
			{"preMatchDay", "上一比赛日(加载中)"}, 
			{"currentMatchDay", ""}, 
			{"nextMatchDay", "下一比赛日(加载中)"}
		};
		
		//更新比分的默认间隔时间
		public static final long DEFAULT_SCORE_UPDATE_GAP = 10 * 1000;
		
		//更新比分的默认延迟时间
		public static final long DEFAULT_SCORE_UPDATE_DELAY = 10 * 1000;
		
		/**
		 * 日期格式
		 * @author lming
		 *
		 */
		public static final class System_DateFormat {
			
			//数据源链接url中的日期格式
			public static final SimpleDateFormat URL_DATEFORMAT = new SimpleDateFormat("yyyyMMdd");
			
			//通用日期格式
			public static final SimpleDateFormat COMMON_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
			
			//默认显示日期格式
			public static final SimpleDateFormat DEFAULT_DISPLAY_DATEFORMAT = new SimpleDateFormat("MM月dd日");

			//取得数据中的date字段日期格式
			public static final SimpleDateFormat DATA_DATE_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
			
			//比赛开始时间的日期格式
			public static final SimpleDateFormat MATCH_TIME_DATEFORMAT = new SimpleDateFormat("HH:mm");
			
			//比赛开始时间的日期格式
			public static final SimpleDateFormat MATCH_FULLTIME_DATEFORMAT = new SimpleDateFormat("MM月dd日 HH:mm");
		}
		
		/**
		 * 传变量时使用的key值
		 * @author lming
		 *
		 */
		public static final class System_Params {
			
			//match
			public static final String MATCH_PARAM = "match";
		}
	}
	
	/**
	 * 系统常量数据
	 * @author lming
	 *
	 */
	public static final class System_Static {
		
		//数据源链接url
		public static String MATCHURL = "http://nba.sports.163.com/#/match/daily/#.html";
		
		//数据统计url
		public static String BOXSCOREURL = "http://nba.sports.163.com/#/match/boxscore/#.html";
		
		//直播数据源链接url
		public static final String SCOREURL = "http://live.news.163.com/chatlog/NBA#1_#2_update.xml";
		
		//无加时赛
		public static final String NO_OTSCORES = "[]";
		
		/**
		 * 文本常量
		 * @author lming
		 */
		public static final class TEXTCONSTANTS {
			//无比赛显示
			public static final String NO_MATCHES = "无比赛";
			
			//无最佳球员提示
			public static final String NO_TOPPLAYER = "最佳球员";
			
			//开赛
			public static final String MATCH_BEGIN = "开赛";
		}
		
		/**
		 * match相关常量
		 * @author lming
		 */
		public static final class Match_Constants {
			
			/**
			 * match状态
			 * if (data.status == 0 || data.status == 3) {
			 * 		status = '未开赛 '+data.date.split(" ")[1].substring(0,5);
			 * } else if (data.status == 1) {
			 * 		'本节结束'
			 * } else if (data.status == 2) {
			 * 		status = '已结束';
			 * }
			 * @author lming
			 */
			public static final class Match_Status {
				//未开赛
				public static final int NOT_BEGIN = 0;
				//单节结束
				public static final int ROUND_END = 1;
				//比赛结束
				public static final int MATCH_END = 2;
				//未开赛/无直播
				public static final int NOT_BROAD = 3;
				
				//未开赛
				public static final String NOT_BEGIN_STRING = "未开赛";
				//单节结束
				public static final String ROUND_END_STRING = "本节结束";
				//比赛结束
				public static final String MATCH_END_STRING = "已结束";
				//未开赛/无直播
				public static final String NOT_BROAD_STRING = "未开赛/无直播";
				
				/**
				 * @param status
				 * @return
				 */
				public static final String getStatusString(int status) {
					switch (status) {
						case ROUND_END:
							return ROUND_END_STRING;
						case MATCH_END:
							return MATCH_END_STRING;
						case NOT_BEGIN:
						case NOT_BROAD:
						default:
							return NOT_BEGIN_STRING;
					}
				}
			}
			
			/**
			 * if (data.live == 1) {
			 * 		link += '<a href="/2010/match/stat/'+ mid +'.html">直播/统计</a> ';
			 * }else{
			 * 		link += '<a href="/2010/match/stat/'+ mid +'.html">统计</a> ';
			 * }
			 * @author lming
			 */
			public static final class Match_Live {
				//直播
				public static final int MATCH_LIVE = 1;
				//不直播
			}
		}
	}
}