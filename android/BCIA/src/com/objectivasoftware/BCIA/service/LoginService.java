package com.objectivasoftware.BCIA.service;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.objectivasoftware.BCIA.R;
import com.objectivasoftware.BCIA.model.UserInfo;
import com.objectivasoftware.BCIA.response.LoginHandler;
import com.objectivasoftware.BCIA.util.ServiceCallUrl;
import com.objectivasoftware.BCIA.util.ServiceRequestUtil;
import com.objectivasoftware.BCIA.util.SharedPreferencesUtil;
import com.objectivasoftware.BCIA.util.XmlUtil;

public class LoginService {
	private Activity activity;
	public UserInfo userInfo;
	private String password;
	private String userName;

	private final int LOGIN_SUCCESS = 1;
	private final int LOGIN_FAILD = 2;
	private final int CONNECT_ERROR = 3;
	private final int SERVER_ERROR = 4;

	public LoginService(Activity activity) {
		this.activity = activity;
	}

	private Handler mHandler = new Handler();

	public void login() {

		EditText nameText = (EditText) activity
				.findViewById(R.id.user_id_namein);
		EditText passwordText = (EditText) activity
				.findViewById(R.id.user_id_passwordin);
		userName = nameText.getText().toString();
		password = passwordText.getText().toString();

		if ("".equals(userName)) {
			Toast.makeText(activity, R.string.user_name_is_empty, Toast.LENGTH_LONG).show();
		} else if ("".equals(password)) {
			Toast.makeText(activity, R.string.password_is_empty, Toast.LENGTH_LONG).show();
		} else if (!"".equals(userName) && !"".equals(password)) {
			Log.d("LoginActivity", "Login Begin");
			TextView loginInfo = (TextView) activity.findViewById(R.id.login_info);
			loginInfo.setVisibility(View.VISIBLE);
			logInAsync();
		}

	}

	private void logInAsync() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					//HttpResponse resp = ServiceRequestUtil.postRequest(ServiceCallUrl.LOGIN_URL, XmlUtil.getLoginXml(userName, password));
					userInfo = new UserInfo();
							//getLogin(resp.getEntity().getContent());
					
					if (userInfo.getException() != null) {
						Message msg = mHandler.obtainMessage(LOGIN_FAILD);
						mHandler.sendMessage(msg);
						
					} else {
						SharedPreferencesUtil.storeUserInfo(activity, userInfo);
						Message msg = mHandler.obtainMessage(LOGIN_SUCCESS);
						mHandler.sendMessage(msg);
					}

//				} catch (SAXException e) {
//					Message msg = mHandler.obtainMessage(SERVER_ERROR);
//					mHandler.sendMessage(msg);
//					e.printStackTrace();
				} catch (Exception e) {
					Message msg = mHandler.obtainMessage(CONNECT_ERROR);
					mHandler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public UserInfo getLogin(InputStream is)
			throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		LoginHandler handler = new LoginHandler();
		parser.parse(is, handler);
		is.close();
		return handler.getUserInfo();
	}

	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

}
