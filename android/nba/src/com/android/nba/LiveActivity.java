package com.android.nba;

import java.util.Timer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nba.domain.Constants.System_Config;
import com.android.nba.domain.Constants.System_Config.System_DateFormat;
import com.android.nba.domain.Constants.System_Config.System_Params;
import com.android.nba.domain.Constants.System_Static.Match_Constants.Match_Status;
import com.android.nba.domain.Constants.System_Static.TEXTCONSTANTS;
import com.android.nba.domain.Match;
import com.android.nba.handler.ScoresUpdateHandler;
import com.android.nba.runner.MatchScoreRunner;
import com.android.nba.runner.MatchScoreTimerTask;
import com.android.nba.util.MatchUtil;

/**
 * 直播界面
 * @author lming
 */
public class LiveActivity extends Activity {

	//比赛状态文本
	private TextView matchStatusText;
	//主队文本
	private TextView homeText;
	//主队总分
	private TextView homeScoreText;
	//主队图片
	private ImageView homeImage;
	//比分概述
	private TextView scoreDetailText;
	//客队文本
	private TextView awayText;
	//客队总分
	private TextView awayScoreText;
	//客队图片
	private ImageView awayImage;
	//比分直播文本
	private TextView scoreText;
	
	//定期获取比分的timer
	private Timer timer;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_ui);
        //接收match数据,更新界面
        loadMatchInfo();
    }
    
    /**
     * 处理接收的数据 
     */
    private void loadMatchInfo() {
    	//取得match
    	Bundle bundle = getIntent().getExtras();
    	Match match = (Match)bundle.getSerializable(System_Params.MATCH_PARAM);
    	//设置标题
    	setMatchTitle(match);
    }
    
    /**
     * 设置标题,填充界面元素
     * @param match
     */
    private void setMatchTitle(Match match) {
    	//比赛状态文本
    	matchStatusText = (TextView)findViewById(R.id.live_matchStatusText);
    	//主队文本
    	homeText = (TextView)findViewById(R.id.live_homeTeamText);
    	//主队总分
    	homeScoreText = (TextView)findViewById(R.id.live_homeScoreText);
    	//主队图片
    	homeImage = (ImageView)findViewById(R.id.live_homeTeamImage);
    	//比分概述
    	scoreDetailText = (TextView)findViewById(R.id.live_scoreDetailText);
    	//客队文本
    	awayText = (TextView)findViewById(R.id.live_awayTeamText);
    	//客队总分
    	awayScoreText = (TextView)findViewById(R.id.live_awayScoreText);
    	//客队图片
    	awayImage = (ImageView)findViewById(R.id.live_awayTeamImage);
    	//比分直播文本
    	scoreText = (TextView)findViewById(R.id.live_scoreText);
    	
    	//---------------------设值 分割线-------------------------
    	
    	//状态
    	matchStatusText.setText(Match_Status.getStatusString(match.getStatus()) + "\t" 
				+ System_DateFormat.MATCH_FULLTIME_DATEFORMAT.format(match.getDateTime()) + TEXTCONSTANTS.MATCH_BEGIN);
    	//主队
    	//文字
    	homeText.setText(match.getHomeTeam().getCnName());
    	//传int型会去查找res
    	homeScoreText.setText(match.getHomeTeam().getScoreTotal() + "");
    	try {
    		//图片
			int homeImgResId = R.drawable.class.getField("team" + match.getHomeTeam().getId()).getInt(null);
			homeImage.setImageDrawable(getResources().getDrawable(homeImgResId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//比分
		scoreDetailText.setText(MatchUtil.getTeamScoreDetail(match.getHomeTeam()) + "\n"
				+ MatchUtil.getTeamScoreDetail(match.getAwayTeam()));
		//客队
		//文字
		awayText.setText(match.getAwayTeam().getCnName());
		//传int型会去查找res
		awayScoreText.setText(match.getAwayTeam().getScoreTotal() + "");
		try {
			//图片
			int awayImgResId = R.drawable.class.getField("team" + match.getAwayTeam().getId()).getInt(null);
			awayImage.setImageDrawable(getResources().getDrawable(awayImgResId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//启动更新比分任务
		startScoreRunner(scoreText, match.getMid(), System_DateFormat.URL_DATEFORMAT.format(match.getDateTime()));
    }
    
    /**
     * 启动更新比分任务
     */
    private void startScoreRunner(TextView scoreText, int mid, String date) {
    	//更新比分handler
    	ScoresUpdateHandler scoresUpdateHandler = new ScoresUpdateHandler(scoreText);
    	//获取比分runner
    	final MatchScoreRunner matchScoreRunner = new MatchScoreRunner(mid, date, scoresUpdateHandler);
    	//timerTask
    	MatchScoreTimerTask timerTask = new MatchScoreTimerTask(matchScoreRunner);
    	//启动定时任务
    	if(timer == null)
    		timer = new Timer();
    	//定期更新比分
    	timer.schedule(timerTask, 
    			System_Config.DEFAULT_SCORE_UPDATE_DELAY, 
    			System_Config.DEFAULT_SCORE_UPDATE_GAP);
    }
    
    @Override
    public void finish() {
    	super.finish();
    	timer.cancel();
    }
}