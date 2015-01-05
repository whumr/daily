package com.objectivasoftware.BCIA.view;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.objectivasoftware.BCIA.R;
import com.objectivasoftware.BCIA.library.InfoRecordAdapter;
import com.objectivasoftware.BCIA.model.FlightDeicing;
import com.objectivasoftware.BCIA.model.FlightDeicingInfo;
import com.objectivasoftware.BCIA.model.SearchCondition;
import com.objectivasoftware.BCIA.model.UserInfo;
import com.objectivasoftware.BCIA.service.FlightDeicingInfoService;
import com.objectivasoftware.BCIA.util.SharedPreferencesUtil;

public class InfoRecordAcitvity extends BaseActivity {
	private ProgressDialog dialog;
	private static final int DIALOG_KEY = 0;
	private Button deleteButton;
	private Button confButton;
	private Button helpButton;
	private Button searchButton;
	private Button freshButton;
	
	private FlightDeicingInfoService flightDeicingInfoService;
	
	private final int GET_VIEW_SUCCESS = 5;
	private final int GET_VIEW_FAILD = 6;
	private final int LIST_NONE = 7;
	private final int SERVER_ERROR = 1;
	private final int CONNECT_ERROR = 2;
	
	
	List<FlightDeicingInfo> flightDeicingInfoList = null;
	private InfoRecordAdapter listAdapter = null;
	ListView mListView = null;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String warmMsg;
			switch (msg.what) {
			case GET_VIEW_SUCCESS:
				Log.d("InfoRecordAcitvity", "Get date form server");
				initializeListView();
				dialog.cancel();
				break;
			case GET_VIEW_FAILD:
				dialog.cancel();
				warmMsg = flightDeicingInfoService.flightdeicing.getException().getErrMessage();
				toastMessage(warmMsg);
				break;
			case LIST_NONE:
				dialog.cancel();
				warmMsg = "当前列表为空";
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
		FlightDeicing flightDeicing = new FlightDeicing();
		if (flightDeicingInfoService.flightdeicing != null) {
			flightDeicing = flightDeicingInfoService.flightdeicing;
		}
		
		flightDeicingInfoList = flightDeicing.getFlightDeicingInfos();
		
		TextView listNum = (TextView) findViewById(R.id.info_record_info_listnum);
		if (flightDeicingInfoList != null) {
			int num = flightDeicingInfoList.size();
			listNum.setText("信息条数： " + String.valueOf(num));
		}
		
		listAdapter = new InfoRecordAdapter(InfoRecordAcitvity.this, flightDeicingInfoList);
		mListView = (ListView) findViewById(R.id.info_record_body);
		mListView.setAdapter(listAdapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_record);
		showDialog(DIALOG_KEY);
 
		flightDeicingInfoService = new FlightDeicingInfoService();
		flightDeicingInfoService.setmHandler(mHandler);
		flightDeicingInfoService.setmList(getFlightIdList());
		flightDeicingInfoService.getView();
		
		initializeView();
	}
	
	@Override
	protected void onRestart() {
		
		flightDeicingInfoService = new FlightDeicingInfoService();
		flightDeicingInfoService.setmHandler(mHandler);
		flightDeicingInfoService.setmList(getFlightIdList());
		flightDeicingInfoService.getView();
		showDialog(DIALOG_KEY);
		super.onRestart();
	}
	
	
	/**
	 * Click listener for the buttons in the {@link LoginActivity}.
	 */
	private OnClickListener mListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.info_record_delete_button:
				if (listAdapter.flyIds.size() == 0) {
					String warmMsg = "删除选项为空";
					toastMessage(warmMsg);
				} else {
					showDialog(DIALOG_KEY);
					for (String key : listAdapter.flyIds.keySet()) {
						String flightId = listAdapter.flyIds.get(key);
						if (getFlightIdList().contains(flightId)) {
							getFlightIdList().remove(flightId);
							if (getFlightIdList().size() == 0) {
								getFlightIdList().add("1");
							}
							flightDeicingInfoService = new FlightDeicingInfoService();
							flightDeicingInfoService.setmHandler(mHandler);
							flightDeicingInfoService.setmList(getFlightIdList());
							flightDeicingInfoService.getView();
							showDialog(DIALOG_KEY);
						}
					}
					listAdapter.notifyDataSetChanged();
					mListView.invalidate();
				}

				break;
				
			case R.id.info_record_conf_button:
				
				Intent intent = new Intent();
				intent.setClass(InfoRecordAcitvity.this, FlightChooseActivity.class);
				startActivity(intent);
				
				break;
			case R.id.info_record_help_button:
				
				intent = new  Intent();
				intent.setClass(InfoRecordAcitvity.this, HelpActivity.class);
				startActivity(intent);
				
				break;
			case R.id.info_record_search_button:
				intent = new Intent();
				intent.setClass(InfoRecordAcitvity.this, FlightMonitorSearchBoxActivity.class);
				startActivity(intent);
				break;
			case R.id.info_record_fresh_button:
				
				showDialog(DIALOG_KEY);
				flightDeicingInfoService = new FlightDeicingInfoService();
				flightDeicingInfoService.setmHandler(mHandler);
				flightDeicingInfoService.setmList(getFlightIdList());
				flightDeicingInfoService.getView();
				break;
			case R.id.info_record_user_info_view:
				AlertDialog.Builder mBuilder = new AlertDialog.Builder(InfoRecordAcitvity.this);
				mBuilder.setTitle("注销");
				mBuilder.setMessage("是否注销当前用户");
				mBuilder.setNegativeButton(R.string.cancel, null);
				mBuilder.setPositiveButton(R.string.user_login, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface arg0, int arg1) {
						SharedPreferencesUtil.logoutUser(InfoRecordAcitvity.this);
						SearchCondition searchCondition = new SearchCondition();
						searchCondition.setData(null);
						SharedPreferencesUtil.storeSearchCondition(InfoRecordAcitvity.this, searchCondition);
						Intent intent = new Intent();
						intent.setClass(InfoRecordAcitvity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}
				});
					
				mBuilder.create().show();
				break;
			}
		}
	};
	
	private void initializeView() {
		deleteButton = (Button) findViewById(R.id.info_record_delete_button);
		confButton = (Button)findViewById(R.id.info_record_conf_button);
		helpButton = (Button)findViewById(R.id.info_record_help_button);
		searchButton = (Button)findViewById(R.id.info_record_search_button);
		freshButton = (Button)findViewById(R.id.info_record_fresh_button);	
		deleteButton.setOnClickListener(mListener);
		confButton.setOnClickListener(mListener);
		helpButton.setOnClickListener(mListener);
		searchButton.setOnClickListener(mListener);
		freshButton.setOnClickListener(mListener);
		
		UserInfo mUserInfo = SharedPreferencesUtil.fetchUserInfo(this);
		TextView nameText = (TextView) findViewById(R.id.info_record_user_info_view);
		
		nameText.setOnClickListener(mListener);
		nameText.setText(mUserInfo.getUserName());
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		dialog = new ProgressDialog(this);
		dialog.setMessage("数据加载中......");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				AlertDialog.Builder mBuilder = new AlertDialog.Builder(InfoRecordAcitvity.this);
				mBuilder.setTitle("退出");
				mBuilder.setMessage("是否退出当前应用");
				mBuilder.setNegativeButton(R.string.cancel, null);
				mBuilder.setPositiveButton(R.string.user_login, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface arg0, int arg1) {
						SharedPreferencesUtil.logoutUser(InfoRecordAcitvity.this);
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
