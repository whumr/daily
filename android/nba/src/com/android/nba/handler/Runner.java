package com.android.nba.handler;

public interface Runner {
	
	//runner状态
	//未开始
	public static final int NULL = -1;
	//初始化
	public static final int INIT = 0;
	//运行中
	public static final int RUNNING = 1;
	//结束
	public static final int FINISHED = 2;

	/**
	 * 初始化
	 */
	public void init();
	
	/**
	 * 开始执行任务
	 */
	public void start();
	
	/**
	 * 重新开始
	 */
	public void restart();
	
	/**
	 * 销毁
	 */
	public void destroy();
	
	/**
	 * 获取当前状态
	 */
	public int getStatus();
}