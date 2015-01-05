package com.objectivasoftware.BCIA.view;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.objectivasoftware.BCIA.R;
import com.objectivasoftware.BCIA.service.LoginService;
import com.objectivasoftware.BCIA.util.DeviceInfo;
import com.objectivasoftware.BCIA.util.ServiceCallUrl;
import com.objectivasoftware.BCIA.util.SharedPreferencesUtil;

public class LoginActivity extends BaseActivity {
	private Button enterButton = null;
	private Button clearButon = null;
	private EditText userNameIn = null;
	private EditText userPasswordIn = null;
	private LoginService loginService;
	private final int LOGIN_SUCCESS = 1;
	private final int LOGIN_FAILD = 2;
	private final int CONNECT_ERROR = 3;
	private final int SERVER_ERROR = 4;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		loginService = new LoginService(this);
		setDeviceInfo();
		initView();
		
		if (SharedPreferencesUtil.isUserLogin(this)) {
			openFlightMonitorSearchActivity();
		}
	}
	
	private String intToIp(int i) {      
		return (i & 0xFF ) + "." +      
				((i >> 8 ) & 0xFF) + "." +      
				((i >> 16 ) & 0xFF) + "." +      
				( i >> 24 & 0xFF) ;
	}  

	private void setDeviceInfo() {
		WifiManager wifiManager = (WifiManager) getSystemService(LoginActivity.WIFI_SERVICE);
		//判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) { 
			wifiManager.setWifiEnabled(true);   
		} 
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
		DeviceInfo.setDeviceMac(wifiInfo.getMacAddress());
		int ipAddress = wifiInfo.getIpAddress();  
		DeviceInfo.setDeviceIP(intToIp(ipAddress));
		DeviceInfo.setDeviceVersion("Android " + android.os.Build.VERSION.RELEASE);
		String android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		DeviceInfo.setDeviceUdid(android_id);
	}
	
	private void initView() {
		enterButton = (Button)findViewById(R.id.user_confirm);
		clearButon = (Button)findViewById(R.id.user_clean);
		userNameIn = (EditText) findViewById(R.id.user_id_namein);
		userPasswordIn = (EditText) findViewById(R.id.user_id_passwordin);
		enterButton.setOnClickListener(mListener);
		clearButon.setOnClickListener(mListener);
		
		TextView textView = (TextView) findViewById(R.id.verson_name);
		textView.setText("当前版本： V" + ServiceCallUrl.VERSON_NAME);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			TextView loginInfo = (TextView) findViewById(R.id.login_info);
			String warmMsg;
			switch (msg.what) {
			case LOGIN_SUCCESS:
				loginInfo.setVisibility(View.INVISIBLE);
				openFlightMonitorSearchActivity();
				break;
			case LOGIN_FAILD:
				loginInfo.setVisibility(View.INVISIBLE);
				warmMsg = loginService.userInfo.getException().getErrMessage();
				toastMessage(warmMsg);
				break;
			case CONNECT_ERROR:
				loginInfo.setVisibility(View.INVISIBLE);
				warmMsg = "服务器连接失败，请检查网络后再试。";
				toastMessage(warmMsg);
				break;
			case SERVER_ERROR:
				loginInfo.setVisibility(View.INVISIBLE);
				warmMsg = "服务器异常，请稍后再试。";
				toastMessage(warmMsg);
				break;
			}
		};
	};

	/**
	 * Click listener for the buttons in the {@link LoginActivity}.
	 */
	private OnClickListener mListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.user_confirm:
				loginService.setHandler(mHandler);
				loginService.login();
				break;
			case R.id.user_clean:
				userNameIn.setText(null);
				userPasswordIn.setText(null);
				break;
			default:
				break;
			}
		}
	};
	
	private void openFlightMonitorSearchActivity () {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, FlightMonitorSearchActivity.class);
		startActivity(intent);
		finish();
	}
}