package com.objectivasoftware.BCIA.view;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.objectivasoftware.BCIA.R;
import com.objectivasoftware.BCIA.library.FlightMonitorSearchAdapter;
import com.objectivasoftware.BCIA.model.FlightInfo;
import com.objectivasoftware.BCIA.model.FlightMonitor;
import com.objectivasoftware.BCIA.model.SearchCondition;
import com.objectivasoftware.BCIA.model.UserInfo;
import com.objectivasoftware.BCIA.service.FlightMonitorService;
import com.objectivasoftware.BCIA.util.SharedPreferencesUtil;

public class FlightMonitorSearchActivity extends BaseActivity {
	private FlightMonitorSearchAdapter listAdapter = null;
	private FlightMonitorService flightMonitorService;
	private ProgressDialog dialog;
	
	private final int SERVER_ERROR = 1;
	private final int CONNECT_ERROR = 2;
	private final int SEARCH_SUCCESS = 3;
	private final int SEARCH_FAILD = 4;
	
	private static final int DIALOG_KEY = 0;
	
	private List<FlightInfo> itemList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_flight);
		itemList = new ArrayList<FlightInfo>();
		listAdapter = new FlightMonitorSearchAdapter(FlightMonitorSearchActivity.this, itemList);
		initializeView();
	}
	
	@Override
	protected void onResume() {
		if (isNeedLoadData()) {
			SearchCondition searchCondition = SharedPreferencesUtil.fetchSearchCondition(this);
			flightMonitorService = new FlightMonitorService();
			flightMonitorService.setmHandler(mHandler);
			if (searchCondition != null) {
				flightMonitorService.setSearchCondition(searchCondition);
			}
			flightMonitorService.getView();
			showDialog(DIALOG_KEY);
			setNeedLoadData(false);
		}
		super.onResume();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		dialog = new ProgressDialog(this);
		dialog.setMessage("数据加载中......");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}

	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String warmMsg;
			switch (msg.what) {
			case SEARCH_SUCCESS:
				Log.d("FlightMonitorSearchActivity", "Get date form server");
				initializeListView();
				SearchCondition searchCondition = new SearchCondition();
				SharedPreferencesUtil.storeSearchCondition(FlightMonitorSearchActivity.this, searchCondition);
				dialog.cancel();
				break;
			case SEARCH_FAILD:
				dialog.cancel();
				warmMsg = flightMonitorService.flightMonitor.getException().getErrMessage();
				toastMessage(warmMsg);
				break;
			case SERVER_ERROR:
				dialog.cancel();
				warmMsg ="服务器异常，请稍后重试。";
				toastMessage(warmMsg);
				break;
			case CONNECT_ERROR:
				dialog.cancel();
				warmMsg = "服务器连接失败，请检查网络后再试。";
				toastMessage(warmMsg);
				break;
			}
		}
	};
	
	private void initializeListView() {
		FlightMonitor flightMonitor = flightMonitorService.flightMonitor;
		
		TextView listNum = (TextView) findViewById(R.id.user_info_listnum);
		
		if (flightMonitor != null) {
			itemList = flightMonitor.getFlightInfos();
			int num = itemList.size();
			listNum.setText("信息条数： " + String.valueOf(num));
		}
		listAdapter = new FlightMonitorSearchAdapter(FlightMonitorSearchActivity.this, itemList);
		ListView mListView = (ListView) findViewById(R.id.serch_flight_body);
		mListView.setAdapter(listAdapter);
	};
	
	private void initializeView() {
		ImageView searchButton = (ImageView) findViewById(R.id.search_button);
		searchButton.setOnClickListener(mListener);
		
		ImageView confButton = (ImageView) findViewById(R.id.conf_button);
		confButton.setOnClickListener(mListener);

		ImageView addButton = (ImageView) findViewById(R.id.add_button);
		addButton.setOnClickListener(mListener);
		
		ImageView helpButton = (ImageView) findViewById(R.id.help_button);
		helpButton.setOnClickListener(mListener);
		
		UserInfo mUserInfo = SharedPreferencesUtil.fetchUserInfo(this);
		TextView userText = (TextView) findViewById(R.id.user_info_view);
		userText.setOnClickListener(mListener);
		userText.setText(mUserInfo.getUserName());
	}
	
	/**
	 * Click listener for the buttons in the {@link LoginActivity}.
	 */
	private OnClickListener mListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.search_button:

				intent = new Intent();
				intent.setClass(FlightMonitorSearchActivity.this, FlightMonitorSearchBoxActivity.class);
				startActivity(intent);

				break;
			case R.id.conf_button:

				intent = new Intent();
				intent.setClass(FlightMonitorSearchActivity.this, FlightChooseActivity.class);
				startActivity(intent);

				break;
			case R.id.user_info_view:
				AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlightMonitorSearchActivity.this);
				mBuilder.setTitle("注销");
				mBuilder.setMessage("是否注销当前用户");
				mBuilder.setNegativeButton(R.string.cancel, null);
				mBuilder.setPositiveButton(R.string.user_login, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface arg0, int arg1) {
						SharedPreferencesUtil.logoutUser(FlightMonitorSearchActivity.this);
						SearchCondition searchCondition = new SearchCondition();
						searchCondition.setData(null);
						SharedPreferencesUtil.storeSearchCondition(FlightMonitorSearchActivity.this, searchCondition);
						Intent intent = new Intent();
						intent.setClass(FlightMonitorSearchActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}
				});
					
				mBuilder.create().show();
				break;
			case R.id.add_button:

				
				if ((listAdapter.flyIds != null 
					&& listAdapter.flyIds.size() != 0 ) || getFlightIdList().size() != 0) {
					for (String key : listAdapter.flyIds.keySet()) {
						String flightId = listAdapter.flyIds.get(key);
						addFlightIdList(flightId);
					}
						intent = new Intent();
						intent.setClass(FlightMonitorSearchActivity.this, InfoRecordAcitvity.class);
						startActivity(intent);
						finish();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(FlightMonitorSearchActivity.this);
					builder.setTitle("友情提示");
					builder.setMessage("您还未选择需要除冰航班");
					builder.setNegativeButton(R.string.cancel, null);
					builder.create().show();
				}
				
				break;
				
			case R.id.help_button:
				intent = new  Intent();
				intent.setClass(FlightMonitorSearchActivity.this, HelpActivity.class);
				startActivity(intent);
				
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				AlertDialog.Builder mBuilder = new AlertDialog.Builder(FlightMonitorSearchActivity.this);
				mBuilder.setTitle("退出");
				mBuilder.setMessage("是否退出当前应用");
				mBuilder.setNegativeButton(R.string.cancel, null);
				mBuilder.setPositiveButton(R.string.user_login, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface arg0, int arg1) {
						SharedPreferencesUtil.logoutUser(FlightMonitorSearchActivity.this);
						SearchCondition searchCondition = new SearchCondition();
						searchCondition.setData(null);
						finish();
					}
				});
					
				mBuilder.create().show();
				return true;
			} 
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
