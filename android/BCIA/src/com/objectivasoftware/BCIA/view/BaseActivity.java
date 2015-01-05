package com.objectivasoftware.BCIA.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.objectivasoftware.BCIA.R;

public class BaseActivity extends Activity {
    /** Called when the activity is first created. */
	private static List<String> flightIdList = new ArrayList<String>();
	private static boolean isNeedLoadData;
	
    public static boolean isNeedLoadData() {
		return isNeedLoadData;
	}

	public static void setNeedLoadData(boolean isNeedLoadData) {
		BaseActivity.isNeedLoadData = isNeedLoadData;
	}

	public static List<String> getFlightIdList() {
		return flightIdList;
	}

    public static void addFlightIdList(String flightId) {
		BaseActivity.flightIdList.add(flightId);
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void toastMessage (String msg) {
    	Toast toast = Toast.makeText(this, "",  Toast.LENGTH_SHORT);
		LinearLayout toastView = (LinearLayout) toast.getView();
		toastView.setOrientation(LinearLayout.HORIZONTAL);
		TextView text = new TextView(getApplicationContext());
		text.setText(msg);
		text.setWidth(450);
		text.setMinWidth(250);
		text.setHeight(40);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		toastView.addView(text);
		toast.show();
    }
}