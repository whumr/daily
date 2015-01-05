package com.android.nba.handler;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.android.nba.domain.ScoreRecord;
import com.android.nba.util.MatchUtil;

public class ScoresUpdateHandler extends Handler {
	
	private static final String LOG = "ScoresUpdateHandler";
	
	//比分textview
	private TextView scoreText;
	
	public ScoresUpdateHandler(TextView scoreText) {
		super();
		this.scoreText = scoreText;
	}

	/* (non-Javadoc)
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@SuppressWarnings("unchecked")
	public void handleMessage(Message msg) {
		if(msg.obj != null) {
			List<ScoreRecord> scores = (List<ScoreRecord>)msg.obj;
			if(!scores.isEmpty()) {
				for(ScoreRecord score : scores)
					scoreText.append(MatchUtil.getScoreStringForLive(score) + "\n");
			}
		}
		Log.d(LOG, "update score list succeed.");
	}

	public TextView getScoreText() {
		return scoreText;
	}

	public void setScoreText(TextView scoreText) {
		this.scoreText = scoreText;
	}
}