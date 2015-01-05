package com.mr.stockalarm.common;

import java.util.List;

import com.mr.stockalarm.AppManager;
import com.mr.stockalarm.domain.Alarm;
import com.mr.stockalarm.domain.Stock;
import com.mr.stockalarm.util.SqliteUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;

public class BaseFragmentActivity extends FragmentActivity {

protected AppManager appManager;
	
	protected List<Stock> stocks;
	protected List<Alarm> alarms;
	protected SqliteUtil sqliteUtil;
	protected SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appManager = AppManager.getAppManger();
		appManager.addActivity(this);
		
		sqliteUtil = appManager.getSqliteUtil();
		if (sqliteUtil == null) {
			sqliteUtil = new SqliteUtil(this);
			appManager.setSqliteUtil(sqliteUtil);
		}
		db = appManager.getDb();
		if (db == null) {
			db = sqliteUtil.getReadableDatabase();
			db = sqliteUtil.getWritableDatabase();
			appManager.setDb(db);
		}
		stocks = appManager.getStocks();
		if (stocks == null) {
			stocks = sqliteUtil.getStocks(db);
			appManager.setStocks(stocks);
		}
		alarms = appManager.getAlarms();
		if (alarms == null) {
			alarms = sqliteUtil.getAlarms(db);
			appManager.setAlarms(alarms);
		}
	}
	
	@Override
	protected void onDestroy() {
		appManager.removeActivity(this);
		super.onDestroy();
	}
	
	protected void hideInputKeyboard(IBinder binder) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(binder, 0);
	}
}
