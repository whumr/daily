package com.objectivasoftware.BCIA.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.objectivasoftware.BCIA.R;
import com.objectivasoftware.BCIA.model.CheckBoxState;
import com.objectivasoftware.BCIA.model.Park;
import com.objectivasoftware.BCIA.model.Parks;
import com.objectivasoftware.BCIA.service.UpdateConfService;
import com.objectivasoftware.BCIA.util.SharedPreferencesUtil;

public class FlightChooseActivity extends BaseActivity{
	
	private CheckBox cBoxP1;
	private CheckBox cBoxP2;
	private CheckBox cBoxP3;
	private CheckBox cBoxP4;
	private CheckBox cBoxP5;
	private CheckBox cBoxP6;
	private CheckBox cBoxP7;
	private CheckBox cBoxP8;
	private CheckBox cBoxP9;
	private CheckBox cBoxP10;
	private CheckBox cBoxP11;
	private CheckBox cBoxP12;
	private CheckBox cBoxP13;
	private CheckBox cBoxP14;
	private CheckBox cBoxP15;
	private Button flgtChooseclButton;
	private Button flgtChoosecfButton;
	
	private UpdateConfService updateConfService;
	
	private final int UPDATE_PARK_SUCCESS = 1;
	private final int UPDATE_PARK_FAILD = 2;
	private final int CONNECT_ERROR = 3;
	private final int SERVER_ERROR = 4;
	
	private ProgressDialog dialog;
	private static final int DIALOG_KEY = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flight_choose);
		showDialog(DIALOG_KEY);
		initView();
		updateConfService = new UpdateConfService();
		updateConfService.setmHandler(mHandler);
		updateConfService.update();
		
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_PARK_SUCCESS:
				dialog.cancel();
				setCheckBoxVisiable(updateConfService.parks);
				break;
			case UPDATE_PARK_FAILD:
				dialog.cancel();
				String warmMsg = updateConfService.parks.getException().getErrMessage();
				toastMessage(warmMsg);
				break;
			case CONNECT_ERROR:
				dialog.cancel();
				warmMsg = "服务器连接失败，请检查网络后再试。";
				toastMessage(warmMsg);
				break;
			case SERVER_ERROR:
				dialog.cancel();
				warmMsg = "服务器异常，请稍后再试。";
				toastMessage(warmMsg);
				break;
			}
		}

		
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
		dialog = new ProgressDialog(this);
		dialog.setMessage("数据加载中......");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		return dialog;
	}
	
	private void initView(){
		cBoxP1 = (CheckBox)findViewById(R.id.flight_choose_p1);
		cBoxP2 = (CheckBox)findViewById(R.id.flight_choose_p2);
		cBoxP3 = (CheckBox)findViewById(R.id.flight_choose_p3);
		cBoxP4 = (CheckBox)findViewById(R.id.flight_choose_p4);
		cBoxP5 = (CheckBox)findViewById(R.id.flight_choose_p5);
		cBoxP6 = (CheckBox)findViewById(R.id.flight_choose_p6);
		cBoxP7 = (CheckBox)findViewById(R.id.flight_choose_p7);
		cBoxP8 = (CheckBox)findViewById(R.id.flight_choose_p8);
		cBoxP9 = (CheckBox)findViewById(R.id.flight_choose_p9);
		cBoxP10 = (CheckBox)findViewById(R.id.flight_choose_p10);
		cBoxP11 = (CheckBox)findViewById(R.id.flight_choose_p11);
		cBoxP12 = (CheckBox)findViewById(R.id.flight_choose_p12);
		cBoxP13 = (CheckBox)findViewById(R.id.flight_choose_p13);
		cBoxP14 = (CheckBox)findViewById(R.id.flight_choose_p14);
		cBoxP15 = (CheckBox)findViewById(R.id.flight_choose_p15);
		
		flgtChooseclButton = (Button)findViewById(R.id.flight_choose_clear_btn);
		flgtChoosecfButton = (Button)findViewById(R.id.flight_choose_sub_btn);
		flgtChooseclButton.setOnClickListener(mListener);
		flgtChoosecfButton.setOnClickListener(mListener);
	}
	
	/**
	 * Click listener for the buttons in the {@link LoginActivity}.
	 */
	private OnClickListener mListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.flight_choose_clear_btn:
				clearCheckBoxState();
				break;
			case R.id.flight_choose_sub_btn:
				CheckBoxState checkBoxState = getCheckBoxState();
				SharedPreferencesUtil.storeFlightParkSetting(FlightChooseActivity.this, checkBoxState);
				finish();
				break;
			default:
				break;
			}
		}
	};
	
	private CheckBoxState getCheckBoxState() {
		CheckBoxState checkBoxState = new CheckBoxState();
		if(cBoxP1.isChecked()) {
			checkBoxState.setCheckP1("P1");
		}
		if(cBoxP2.isChecked()) {
			checkBoxState.setCheckP2("P2");
		}
		if(cBoxP3.isChecked()) {
			checkBoxState.setCheckP3("P3");
		}
		if(cBoxP4.isChecked()) {
			checkBoxState.setCheckP4("P4");
		}
		if(cBoxP5.isChecked()) {
			checkBoxState.setCheckP5("P5");
		}
		if(cBoxP6.isChecked()) {
			checkBoxState.setCheckP6("P6");
		}
		if(cBoxP7.isChecked()) {
			checkBoxState.setCheckP7("P7");
		}
		if(cBoxP8.isChecked()) {
			checkBoxState.setCheckP8("P8");
		}
		if(cBoxP9.isChecked()) {
			checkBoxState.setCheckP9("P9");
		}
		if(cBoxP10.isChecked()) {
			checkBoxState.setCheckP10("P10");
		}
		if(cBoxP11.isChecked()) {
			checkBoxState.setCheckP11("P11");
		}
		if(cBoxP12.isChecked()) {
			checkBoxState.setCheckP12("P12");
		}
		if(cBoxP13.isChecked()) {
			checkBoxState.setCheckP13("P13");
		}
		if(cBoxP14.isChecked()) {
			checkBoxState.setCheckP14("P14");
		}
		if(cBoxP15.isChecked()) {
			checkBoxState.setCheckP15("P15");
		}
		return checkBoxState;
	}
	
	private void clearCheckBoxState() {
		cBoxP1.setChecked(false);
		cBoxP2.setChecked(false);
		cBoxP3.setChecked(false);
		cBoxP4.setChecked(false);
		cBoxP5.setChecked(false);
		cBoxP6.setChecked(false);
		cBoxP7.setChecked(false);
		cBoxP8.setChecked(false);
		cBoxP9.setChecked(false);
		cBoxP10.setChecked(false);
		cBoxP11.setChecked(false);
		cBoxP12.setChecked(false);
		cBoxP13.setChecked(false);
		cBoxP14.setChecked(false);
		cBoxP15.setChecked(false);
	}
	
	private void setCheckBoxVisiable(Parks parks) {
		
		List<Park> mList = new ArrayList<Park>();
		mList = parks.getParkList();
		for (Park v : mList) {
			if ("P1".equals(v.getParkValue())) {
				cBoxP1.setVisibility(View.VISIBLE);
			}
			if ("P2".equals(v.getParkValue())) {
				cBoxP2.setVisibility(View.VISIBLE);
			}
			if ("P3".equals(v.getParkValue())) {
				cBoxP3.setVisibility(View.VISIBLE);
			}
			if ("P4".equals(v.getParkValue())) {
				cBoxP4.setVisibility(View.VISIBLE);
			}
			if ("P5".equals(v.getParkValue())) {
				cBoxP5.setVisibility(View.VISIBLE);
			}
			if ("P6".equals(v.getParkValue())) {
				cBoxP6.setVisibility(View.VISIBLE);
			}
			if ("P7".equals(v.getParkValue())) {
				cBoxP7.setVisibility(View.VISIBLE);
			}
			if ("P8".equals(v.getParkValue())) {
				cBoxP8.setVisibility(View.VISIBLE);
			}
			if ("P9".equals(v.getParkValue())) {
				cBoxP9.setVisibility(View.VISIBLE);
			}
			if ("P10".equals(v.getParkValue())) {
				cBoxP10.setVisibility(View.VISIBLE);
			}
			if ("P11".equals(v.getParkValue())) {
				cBoxP11.setVisibility(View.VISIBLE);
			}
			if ("P12".equals(v.getParkValue())) {
				cBoxP12.setVisibility(View.VISIBLE);
			}
			if ("P13".equals(v.getParkValue())) {
				cBoxP13.setVisibility(View.VISIBLE);
			}
			if ("P14".equals(v.getParkValue())) {
				cBoxP14.setVisibility(View.VISIBLE);
			}
			if ("P15".equals(v.getParkValue())) {
				cBoxP15.setVisibility(View.VISIBLE);
			}
		}
	}
	
	

}
