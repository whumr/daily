package com.android.nba.handler;

public abstract class AbstractRunner implements Runner {
	
	//状态
	protected int status = NULL;

	@Override
	public void init() {
		status = INIT;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		//初始化runner
		init();
		//启动runner
		run();
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	/**
	 * 具体启动操作
	 */
	public abstract void run(); 
}