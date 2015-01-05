package com.android.nba.runner;

import java.util.TimerTask;

import android.util.Log;

/**
 * 
 * @author lming
 */
public class MatchScoreTimerTask extends TimerTask implements Cloneable {

	private static final String LOG = "MatchScoreTimerTask";
	
	//scorerunner
	private MatchScoreRunner matchScoreRunner;
	
	public MatchScoreTimerTask(MatchScoreRunner matchScoreRunner) {
		super();
		this.matchScoreRunner = matchScoreRunner;
	}

	@Override
	public void run() {
		matchScoreRunner.start();
	}

	/**
	 *  取得一个副本
	 * @return
	 */
	public MatchScoreTimerTask getCopy() {
		try {
			/**
			 * 经过试验,clone后对象的内部属性与被clone的对象属性相等
			 * 不需要额外的调用set方法去保持一致
			 */
			return (MatchScoreTimerTask)this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			Log.e(LOG, "getCopy error " + e.getMessage());
		}
		return null;
	}
}