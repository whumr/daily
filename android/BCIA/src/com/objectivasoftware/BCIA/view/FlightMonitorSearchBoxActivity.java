package com.objectivasoftware.BCIA.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.objectivasoftware.BCIA.R;
import com.objectivasoftware.BCIA.model.SearchCondition;
import com.objectivasoftware.BCIA.util.DataUtil;
import com.objectivasoftware.BCIA.util.SharedPreferencesUtil;

public class FlightMonitorSearchBoxActivity extends BaseActivity {
	private Button searchConditionClrBtn;
	private Button searchConditionSubBtn;
	private EditText searchConditionDate;
	private EditText searchConditionPlaneNum;
	private EditText searchConditionCompany;
	private EditText searchConditionPlane;
	private EditText searchConditionSchNum;
	
	private int startLength;
	private int afterLength;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_view);
		initView();
	}
	
	private void initView(){
		searchConditionClrBtn = (Button)findViewById(R.id.search_condition_clear_btn);
		searchConditionSubBtn = (Button)findViewById(R.id.search_condition_sub_btn);
		searchConditionDate = (EditText)findViewById(R.id.search_condition_date_text);
		searchConditionPlaneNum = (EditText)findViewById(R.id.search_condition_plane_num_text);
		searchConditionPlane = (EditText)findViewById(R.id.search_condition_plane_text);
		searchConditionSchNum = (EditText)findViewById(R.id.search_condition_schedule_num_text);
		searchConditionCompany = (EditText)findViewById(R.id.search_condition_company_text);
		
		searchConditionClrBtn.setOnClickListener(mListener);
		searchConditionSubBtn.setOnClickListener(mListener);
		searchConditionDate.setText(DataUtil.getTime());
		
		searchConditionDate.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String ss = s.toString();
				if (ss.length() == 4 && !isDelState()) {
					String yy = ss + ":";
					searchConditionDate.setText(ss + "-");
					searchConditionDate.setSelection(yy.length());
				}
				
				if (ss.length() == 7 && !isDelState()) {
					String yy = ss + ":";
					searchConditionDate.setText(ss + "-");
					searchConditionDate.setSelection(yy.length());
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if (2 == s.length()) {
					startLength = 2;
				} else {
					startLength = start;
				}
			}
			public void afterTextChanged(Editable s) {
				afterLength = s.length();

			}
			private boolean isDelState (){
				return afterLength > startLength;
			}
		});
		
	}
	
	/**
	 * Click listener for the buttons in the {@link LoginActivity}.
	 */
	private OnClickListener mListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.search_condition_clear_btn:
				searchConditionDate.setText(null);
				searchConditionPlane.setText(null);
				searchConditionPlaneNum.setText(null);
				searchConditionSchNum.setText(null);
				searchConditionCompany.setText(null);
				break;
			case R.id.search_condition_sub_btn:
				
				setNeedLoadData(true);
				
				SharedPreferencesUtil.storeSearchCondition(FlightMonitorSearchBoxActivity.this, getSearchCondition());
				Intent intent = new Intent();
				intent.setClass(FlightMonitorSearchBoxActivity.this, FlightMonitorSearchActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		}
	};
	
	private SearchCondition getSearchCondition () {
		SearchCondition searchCondition = new SearchCondition();
		searchCondition.setData(searchConditionDate.getText().toString());
		searchCondition.setPlane(searchConditionPlane.getText().toString());
		searchCondition.setFightNum(searchConditionPlaneNum.getText().toString());
		searchCondition.setSchNum(searchConditionSchNum.getText().toString());
		searchCondition.setCompany(searchConditionCompany.getText().toString());
		return searchCondition;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				setNeedLoadData(false);
				
				finish();
			} 
		}
		return super.onKeyDown(keyCode, event);
	}
}
