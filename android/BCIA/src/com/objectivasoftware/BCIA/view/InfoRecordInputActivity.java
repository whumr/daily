package com.objectivasoftware.BCIA.view;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.objectivasoftware.BCIA.R;
import com.objectivasoftware.BCIA.model.CheckBoxState;
import com.objectivasoftware.BCIA.model.DeicingStnd;
import com.objectivasoftware.BCIA.model.FlightDeicingInput;
import com.objectivasoftware.BCIA.model.FlightDeicingInputPitem;
import com.objectivasoftware.BCIA.model.ServerTime;
import com.objectivasoftware.BCIA.service.FlightDeicingInputService;
import com.objectivasoftware.BCIA.service.ParkService;
import com.objectivasoftware.BCIA.service.ServerTimeService;
import com.objectivasoftware.BCIA.service.UpdateService;
import com.objectivasoftware.BCIA.util.DataUtil;
import com.objectivasoftware.BCIA.util.SharedPreferencesUtil;

public class InfoRecordInputActivity extends BaseActivity implements OnTimeChangedListener {
	private Button buttonArrive;
	private Button buttonleadDic;
	private Button buttonArriveDic;
	private Button buttonStDicing;
	private Button buttonEndDicing;
	private Button buttonStDrive;
	private Button buttonOutDrive;
	private EditText arriveText;
	private EditText leadDicingText;
	private EditText arriveDicingText;
	private EditText startDicingText;
	private EditText endDicingText;
	private EditText startDriveText;
	private EditText outDriveText;
	private int option = 0;
	private Button flightOne;
	private Button flightTwo;
	private Button flightThree;
	
	private EditText currentEditText;
	
	private int startLength;
	private int afterLength;
	
	public EditText getCurrentEditText() {
		return currentEditText;
	}

	public void setCurrentEditText(EditText currentEditText) {
		this.currentEditText = currentEditText;
	}

	private String CurrentFlight = "";
	
	private Button clearButton;
	private Button confirmButton;
	
	private DeicingStnd mDeicingStnd;
	private String lastUpdate;
	
	private Spinner mSpinner;
	
	private FlightDeicingInputService flightDeicingInputService;
	private final int GET_DATA_SUCCESS = 9;
	private final int GET_DATA_FAILD = 10;
	private final int TIME_UPDATA_SUCCESS = 11;
	private final int TIME_UPDATA_FAILD = 12;
	private final int UPDATA_SUCCESS = 18;
	private final int UPDATA_FAILD = 17;
	private final int SERVER_ERROR = 1;
	private final int CONNECT_ERROR = 2;
	
	private ProgressDialog dialog;
	private static final int DIALOG_KEY = 0;
	
	private List<String> pstnds;
	
	private String currentStnd;
	private ServerTimeService serverTimeService;
	private UpdateService updateService;
	
	private FlightDeicingInput dateflightDeicingInput;
	
	private String flightId;
	private String flightName;
	
	private ParkService parkService;
	
	private final int GET_STND_SUCCESS = 15;
	private final int GET_STND_FAILD = 16;
	
	private TimePicker timePicker;
	
	private String y1;
	private String y2;
	private String y3;
	private String y4;
	private String y5;
	private String y6;
	private String y7;
	
	private String stand;
	
	private FlightDeicingInput flightDeicingInput;
	
	private boolean isVisable = true;
	
	
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		String hour = String.valueOf(hourOfDay);
		String mm = String.valueOf(minute);
		if (("" + hourOfDay).length() == 1) {
			hour = "0" + hourOfDay;
		}
		
		if (("" + minute).length() == 1) {
			mm = "0" + minute;
		}
		
		String time = hour + ":" + mm;
		switch (option) {
		case R.id.info_record_input_arrive_site_time_edit_time:
			arriveText.setText(time);
			y1 = DataUtil.getCurrentDate();
			break;
		case R.id.info_record_input_lead_dicing_edit_time:
			leadDicingText.setText(time);
			y2 = DataUtil.getCurrentDate();
			break;
		case R.id.info_record_input_arrive_dicing_edit_time:
			arriveDicingText.setText(time);
			y3 = DataUtil.getCurrentDate();
			break;
		case R.id.info_record_input_start_dicing_edit_time:
			startDicingText.setText(time);
			y4 = DataUtil.getCurrentDate();
			break;
		case R.id.info_record_input_end_dicing_edit_time:
			endDicingText.setText(time);
			y5 = DataUtil.getCurrentDate();
			break;
		case R.id.info_record_input_start_drive_edit_time:
			startDriveText.setText(time);
			y6 = DataUtil.getCurrentDate();
			break;
		case R.id.info_record_input_out_drive_edit_time:
			outDriveText.setText(time);
			y7 = DataUtil.getCurrentDate();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_record_input);
		
		showDialog(DIALOG_KEY);
		
		flightDeicingInputService = new FlightDeicingInputService();
		Intent intent = this.getIntent();
		flightId = intent.getStringExtra("CurrentFlightId");
		flightName = intent.getStringExtra("CurrentFlightName");
		
		flightDeicingInputService.setmHandler(mHandler);
		flightDeicingInputService.setFlightId(flightId);
		flightDeicingInputService.getData();
		
		timePicker = (TimePicker) findViewById(R.id.info_record_input_time_picker);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(this);
		init();
	}

	private void fecthInfo (String currentFlight) {
		showDialog(DIALOG_KEY);
		flightDeicingInputService = new FlightDeicingInputService();
		Intent intent = this.getIntent();
		String flightId = intent.getStringExtra("CurrentFlightId");
		flightDeicingInputService.setmHandler(mHandler);
		flightDeicingInputService.setFlightId(flightId);
		flightDeicingInputService.setSearchNum(currentFlight);
		flightDeicingInputService.getData();
	}
	
	private OnClickListener mListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.info_record_input_one:
				CurrentFlight = "1";
				isVisable = false;
				fecthInfo(CurrentFlight);
				flightBack();
				flightOne.setBackgroundResource(R.drawable.deicing_icon_color_btn);
				break;
			case R.id.info_record_input_two:
				CurrentFlight = "2";
				isVisable = false;
				fecthInfo(CurrentFlight);
				flightBack();
				flightTwo.setBackgroundResource(R.drawable.deicing_icon_color_btn);
				break;
			case R.id.info_record_input_three:
				CurrentFlight = "3";
				fecthInfo(CurrentFlight);
				flightBack();
				flightThree.setBackgroundResource(R.drawable.deicing_icon_color_btn);
				break;
			case R.id.info_record_input_arrive_site_time_edit_time:
				option = R.id.info_record_input_arrive_site_time_edit_time;
				setAllEditTextBackground();
				arriveText.setBackgroundResource(R.drawable.input_color);
				getCurrentServerTime();
				setCurrentEditText(arriveText);
				y1 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_lead_dicing_edit_time:
				option = R.id.info_record_input_lead_dicing_edit_time;
				setAllEditTextBackground();
				leadDicingText.setBackgroundResource(R.drawable.input_color);
				getCurrentServerTime();
				setCurrentEditText(leadDicingText);
				y2 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_arrive_dicing_edit_time:
				option = R.id.info_record_input_arrive_dicing_edit_time;
				setAllEditTextBackground();
				arriveDicingText.setBackgroundResource(R.drawable.input_color);
				getCurrentServerTime();
				setCurrentEditText(arriveDicingText);
				y3 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_start_dicing_edit_time:
				option = R.id.info_record_input_start_dicing_edit_time;
				setAllEditTextBackground();
				startDicingText.setBackgroundResource(R.drawable.input_color);
				getCurrentServerTime();
				setCurrentEditText(startDicingText);
				y4 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_end_dicing_edit_time:
				option = R.id.info_record_input_end_dicing_edit_time;
				setAllEditTextBackground();
				endDicingText.setBackgroundResource(R.drawable.input_color);
				getCurrentServerTime();
				setCurrentEditText(endDicingText);
				y5 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_start_drive_edit_time:
				option = R.id.info_record_input_start_drive_edit_time;
				setAllEditTextBackground();
				startDriveText.setBackgroundResource(R.drawable.input_color);
				getCurrentServerTime();
				setCurrentEditText(startDriveText);
				y6 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_out_drive_edit_time:
				option = R.id.info_record_input_out_drive_edit_time;
				setAllEditTextBackground();
				outDriveText.setBackgroundResource(R.drawable.input_color);
				getCurrentServerTime();
				setCurrentEditText(outDriveText);
				y7 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_arrive_site_time_edit:
				option = R.id.info_record_input_arrive_site_time_edit_time;
				setAllEditTextBackground();
				arriveText.setBackgroundResource(R.drawable.input_color);
				setCurrentEditText(arriveText);
				y1 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_lead_dicing_edit:
				option = R.id.info_record_input_lead_dicing_edit_time;
				setAllEditTextBackground();
				leadDicingText.setBackgroundResource(R.drawable.input_color);
				setCurrentEditText(leadDicingText);
				y2 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_arrive_dicing_edit:
				option = R.id.info_record_input_arrive_dicing_edit_time;
				setAllEditTextBackground();
				arriveDicingText.setBackgroundResource(R.drawable.input_color);
				setCurrentEditText(arriveDicingText);
				y3 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_start_dicing_edit:
				option = R.id.info_record_input_start_dicing_edit_time;
				setAllEditTextBackground();
				startDicingText.setBackgroundResource(R.drawable.input_color);
				setCurrentEditText(startDicingText);
				y4 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_end_dicing_edit:
				option = R.id.info_record_input_end_dicing_edit_time;
				setAllEditTextBackground();
				endDicingText.setBackgroundResource(R.drawable.input_color);
				setCurrentEditText(endDicingText);
				y5 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_start_drive_edit:
				option = R.id.info_record_input_start_drive_edit_time;
				setAllEditTextBackground();
				startDriveText.setBackgroundResource(R.drawable.input_color);
				setCurrentEditText(startDriveText);
				y6 = DataUtil.getCurrentDate();
				break;
			case R.id.info_record_input_out_drive_edit:
				option = R.id.info_record_input_out_drive_edit_time;
				setAllEditTextBackground();
				outDriveText.setBackgroundResource(R.drawable.input_color);
				setCurrentEditText(outDriveText);
				y7 = DataUtil.getCurrentDate();
				break;
			}
		}

		private void flightBack() {
			flightThree.setBackgroundResource(R.drawable.deicint_icon_btn);
			flightOne.setBackgroundResource(R.drawable.deicint_icon_btn);
			flightTwo.setBackgroundResource(R.drawable.deicint_icon_btn);
			
		}

		private void setAllEditTextBackground() {
			arriveText.setBackgroundResource(R.drawable.input_icon);
			leadDicingText.setBackgroundResource(R.drawable.input_icon);
			arriveDicingText.setBackgroundResource(R.drawable.input_icon);
			startDicingText.setBackgroundResource(R.drawable.input_icon);
			endDicingText.setBackgroundResource(R.drawable.input_icon);
			startDriveText.setBackgroundResource(R.drawable.input_icon);
			outDriveText.setBackgroundResource(R.drawable.input_icon);
		}

		private void getCurrentServerTime() {
			showDialog(DIALOG_KEY);
			serverTimeService = new ServerTimeService();
			serverTimeService.setmHandler(mHandler);
			serverTimeService.getData();
			
		}
	};

	public void init() {

		flightOne = (Button) findViewById(R.id.info_record_input_one);
		flightTwo = (Button) findViewById(R.id.info_record_input_two);
		flightThree=(Button) findViewById(R.id.info_record_input_three);
		
		clearButton = (Button) findViewById(R.id.info_record_input_clear_btn);
		confirmButton = (Button) findViewById(R.id.info_record_input_confirm_btn);
		
		clearButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				clearData();
			}
		});
		confirmButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				showDialog(DIALOG_KEY);
						updateService = new UpdateService();
						updateService.setmHandler(mHandler);
						updateService.setFlightId(flightId);
						try {
							updateService.setFlightDeicingInput(getDeicingInput());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						updateService.setDeicingNum(CurrentFlight);
						updateService.updata();
					}
				
		});
		
		mSpinner = (Spinner) findViewById(R.id.info_record_input_deice_site_info);
		
		arriveText = (EditText) findViewById(R.id.info_record_input_arrive_site_time_edit);
		leadDicingText = (EditText) findViewById(R.id.info_record_input_lead_dicing_edit);
		arriveDicingText = (EditText) findViewById(R.id.info_record_input_arrive_dicing_edit);
		startDicingText = (EditText) findViewById(R.id.info_record_input_start_dicing_edit);
		endDicingText = (EditText) findViewById(R.id.info_record_input_end_dicing_edit);
		startDriveText = (EditText) findViewById(R.id.info_record_input_start_drive_edit);
		outDriveText = (EditText) findViewById(R.id.info_record_input_out_drive_edit);

		arriveText.setOnClickListener(mListener);
		leadDicingText.setOnClickListener(mListener);
		arriveDicingText.setOnClickListener(mListener);
		startDicingText.setOnClickListener(mListener);
		endDicingText.setOnClickListener(mListener);
		startDriveText.setOnClickListener(mListener);
		outDriveText.setOnClickListener(mListener);
		
		arriveText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String ss = s.toString();
				if (ss.length() ==2 && !isDelState()) {
					String yy = ss + ":";
					arriveText.setText(ss + ":");
					arriveText.setSelection(yy.length());
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
		
		leadDicingText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String ss = s.toString();
				if (ss.length() ==2 && !isDelState()) {
					String yy = ss + ":";
					leadDicingText.setText(ss + ":");
					leadDicingText.setSelection(yy.length());
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
		arriveDicingText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String ss = s.toString();
				if (ss.length() ==2 && !isDelState()) {
					String yy = ss + ":";
					arriveDicingText.setText(ss + ":");
					arriveDicingText.setSelection(yy.length());
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
		startDicingText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String ss = s.toString();
				if (ss.length() ==2 && !isDelState()) {
					String yy = ss + ":";
					startDicingText.setText(ss + ":");
					startDicingText.setSelection(yy.length());
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
		endDicingText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String ss = s.toString();
				if (ss.length() ==2 && !isDelState()) {
					String yy = ss + ":";
					endDicingText.setText(ss + ":");
					endDicingText.setSelection(yy.length());
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
		startDriveText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String ss = s.toString();
				if (ss.length() ==2 && !isDelState()) {
					String yy = ss + ":";
					startDriveText.setText(ss + ":");
					startDriveText.setSelection(yy.length());
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
		outDriveText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String ss = s.toString();
				if (ss.length() ==2 && !isDelState()) {
					String yy = ss + ":";
					outDriveText.setText(ss + ":");
					outDriveText.setSelection(yy.length());
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
		
		
		buttonArrive = (Button) findViewById(R.id.info_record_input_arrive_site_time_edit_time);
		buttonleadDic = (Button) findViewById(R.id.info_record_input_lead_dicing_edit_time);
		buttonArriveDic = (Button) findViewById(R.id.info_record_input_arrive_dicing_edit_time);
		buttonStDicing = (Button) findViewById(R.id.info_record_input_start_dicing_edit_time);
		buttonEndDicing = (Button) findViewById(R.id.info_record_input_end_dicing_edit_time);
		buttonStDrive = (Button) findViewById(R.id.info_record_input_start_drive_edit_time);
		buttonOutDrive = (Button) findViewById(R.id.info_record_input_out_drive_edit_time);

		buttonArrive.setOnClickListener(mListener);
		buttonleadDic.setOnClickListener(mListener);
		buttonArriveDic.setOnClickListener(mListener);
		buttonStDicing.setOnClickListener(mListener);
		buttonEndDicing.setOnClickListener(mListener);
		buttonStDrive.setOnClickListener(mListener);
		buttonOutDrive.setOnClickListener(mListener);
		
		flightOne.setOnClickListener(mListener);
		flightTwo.setOnClickListener(mListener);
		flightThree.setOnClickListener(mListener);

	}
	
	private void clearData() {
		arriveText.setText(null);
		leadDicingText.setText(null);
		arriveDicingText.setText(null);
		startDicingText.setText(null);
		endDicingText.setText(null);
		outDriveText.setText(null);
		startDriveText.setText(null);
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String warmMsg = "";
			switch (msg.what) {
			case GET_DATA_SUCCESS:
				
				pstnds = new ArrayList<String>();
				
				Log.d("InfoRecordInputActivity", "Get date form server");
				flightDeicingInput = flightDeicingInputService.flightDeicingInput;
				stand = flightDeicingInput.getDeicStnd();
				try {
					setInitData(flightDeicingInput);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				lastUpdate = flightDeicingInput.getLastUpdate();
				parkService = new ParkService();
				parkService.setmHandler(mHandler);
				CheckBoxState checkBoxState = SharedPreferencesUtil.fetchFlightParkSetting(InfoRecordInputActivity.this);
				parkService.setCheckBoxState(checkBoxState);
				parkService.setChange();
				
			break;
			case GET_DATA_FAILD:
				dialog.cancel();
				warmMsg = flightDeicingInputService.flightDeicingInput.getException().getErrMessage();
				toastMessage(warmMsg);
			break;	
			case TIME_UPDATA_SUCCESS:
				dialog.cancel();
				warmMsg = "获取时间成功";
				toastMessage(warmMsg);
				ServerTime serverTime = serverTimeService.serverTime;
				String currentTime = null ;
				if (serverTime != null) {
					currentTime = serverTime.getServerTime();
					try {
						timePicker.setCurrentHour(Integer.valueOf(DataUtil.getCurrentHH(currentTime)));
						timePicker.setCurrentMinute(Integer.valueOf(DataUtil.getCurrentmm(currentTime)));
						getCurrentEditText().setText(DataUtil.getHHmmTime(serverTime.getServerTime()));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			break;
			case TIME_UPDATA_FAILD:
				dialog.cancel();
				warmMsg = "获取时间失败";
				toastMessage(warmMsg);
				break;
			case GET_STND_SUCCESS:
				dialog.cancel();
				mDeicingStnd = parkService.deicingStnd;
				if (stand != null) {
					pstnds.add(stand);
				}
				stand = null;
				pstnds.add("        ");
				List<FlightDeicingInputPitem> pitemList = mDeicingStnd.getFlightDeicingInputPitems();
				List<String> temp = new ArrayList<String>();
				String tempStnd = null;
				if (pitemList != null) {
					for (FlightDeicingInputPitem item : pitemList) {
						for (String stnd : item.getStndList()) {
							if (stand != null && stnd.equals(stand)) {
								tempStnd = stnd;
							} else {
								temp.add(stnd);
							}
						}
					}
				}
				if (tempStnd != null) {
					temp.remove(tempStnd);
				}
				
				for (String stands : temp) {
					pstnds.add(stands);
				}
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(InfoRecordInputActivity.this,android.R.layout.simple_spinner_item, pstnds);
		        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		        mSpinner.setAdapter(adapter);  
		        mSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		        mSpinner.setVisibility(View.VISIBLE);
		        
			break;
			case GET_STND_FAILD:
				dialog.cancel();
				warmMsg = parkService.deicingStnd.getException().getErrMessage();
				Toast.makeText(InfoRecordInputActivity.this, warmMsg, Toast.LENGTH_LONG).show();
				break;
			case UPDATA_SUCCESS:
				dialog.cancel();
				finish();
			break;
			case UPDATA_FAILD:
				dialog.cancel();
				warmMsg = updateService.result.getException().getErrMessage();
				Toast.makeText(InfoRecordInputActivity.this, warmMsg, Toast.LENGTH_LONG).show();
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
	
	@Override
	protected Dialog onCreateDialog(int id) {

		dialog = new ProgressDialog(this);
		dialog.setMessage("数据加载中......");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}
	

	private void setInitData(FlightDeicingInput flightDeicingInput) throws ParseException {
		CurrentFlight = flightDeicingInput.getDeicingSeq();
		arriveText.setText(DataUtil.getHHmmTime(flightDeicingInput.getDihdRDttm()));
		leadDicingText.setText(DataUtil.getHHmmTime(flightDeicingInput.getDipbDttm()));
		arriveDicingText.setText(DataUtil.getHHmmTime(flightDeicingInput.getDiinDttm()));
		startDicingText.setText(DataUtil.getHHmmTime(flightDeicingInput.getStdiDttm()));
		endDicingText.setText(DataUtil.getHHmmTime(flightDeicingInput.getEddiDttm()));
		startDriveText.setText(DataUtil.getHHmmTime(flightDeicingInput.getDiegDttm()));
		outDriveText.setText(DataUtil.getHHmmTime(flightDeicingInput.getDitoDttm()));
		
		dateflightDeicingInput = new FlightDeicingInput();
		dateflightDeicingInput.setDihdRDttm(flightDeicingInput.getDihdRDttm());
		dateflightDeicingInput.setDipbDttm(flightDeicingInput.getDipbDttm());
		dateflightDeicingInput.setDiinDttm(flightDeicingInput.getDiinDttm());
		dateflightDeicingInput.setStdiDttm(flightDeicingInput.getStdiDttm());
		dateflightDeicingInput.setEddiDttm(flightDeicingInput.getEddiDttm());
		dateflightDeicingInput.setDiegDttm(flightDeicingInput.getDiegDttm());
		dateflightDeicingInput.setDitoDttm(flightDeicingInput.getDitoDttm());
		dateflightDeicingInput.setLastUpdate(flightDeicingInput.getLastUpdate());
		
		
		y1 = DataUtil.getyyyMMddTime(dateflightDeicingInput.getDihdRDttm());
		y2 = DataUtil.getyyyMMddTime(dateflightDeicingInput.getDipbDttm());
		y3 = DataUtil.getyyyMMddTime(dateflightDeicingInput.getDiinDttm());
		y4 = DataUtil.getyyyMMddTime(dateflightDeicingInput.getStdiDttm());
		y5 = DataUtil.getyyyMMddTime(dateflightDeicingInput.getEddiDttm());
		y6 = DataUtil.getyyyMMddTime(dateflightDeicingInput.getDiegDttm());
		y7 = DataUtil.getyyyMMddTime(dateflightDeicingInput.getDitoDttm());
		
		TextView mTitle = (TextView) findViewById(R.id.info_record_title);
		mTitle.setText(flightName + "航班信息录入" );
		
		if ("1".equals(flightDeicingInput.getDeicingSeq())) {
			if (isVisable) {
				flightTwo.setVisibility(View.INVISIBLE);
				flightThree.setVisibility(View.INVISIBLE);
			}
			flightOne.setBackgroundResource(R.drawable.deicing_icon_color_btn);
			flightOne.setVisibility(View.VISIBLE);
		} else if ("2".equals(flightDeicingInput.getDeicingSeq())) {
			if (isVisable) {
				flightThree.setVisibility(View.INVISIBLE);
			}
			flightOne.setBackgroundResource(R.drawable.deicint_icon_btn);
			flightTwo.setBackgroundResource(R.drawable.deicing_icon_color_btn);
			flightOne.setVisibility(View.VISIBLE);
			flightTwo.setVisibility(View.VISIBLE);
		}  else if ("3".equals(flightDeicingInput.getDeicingSeq())) {
			flightThree.setBackgroundResource(R.drawable.deicing_icon_color_btn);
			flightOne.setBackgroundResource(R.drawable.deicint_icon_btn);
			flightTwo.setBackgroundResource(R.drawable.deicint_icon_btn);
			flightOne.setVisibility(View.VISIBLE);
			flightTwo.setVisibility(View.VISIBLE);
			flightThree.setVisibility(View.VISIBLE);
		} 
		
	}
	
	private FlightDeicingInput  getDeicingInput() throws ParseException {
		FlightDeicingInput mFlightDeicingInput = new FlightDeicingInput();
		mFlightDeicingInput.setDeicStnd(currentStnd);
		
		
		mFlightDeicingInput.setDihdRDttm(getSubTime(y1, arriveText.getText().toString()));
		mFlightDeicingInput.setDipbDttm(getSubTime(y2,leadDicingText.getText().toString()));
		mFlightDeicingInput.setDiinDttm(getSubTime(y3,arriveDicingText.getText().toString()));
		mFlightDeicingInput.setStdiDttm(getSubTime(y4,startDicingText.getText().toString()));
		mFlightDeicingInput.setEddiDttm(getSubTime(y5,endDicingText.getText().toString()));
		mFlightDeicingInput.setDiegDttm(getSubTime(y6,startDriveText.getText().toString()));
		mFlightDeicingInput.setDitoDttm(getSubTime(y7,outDriveText.getText().toString()));
		mFlightDeicingInput.setLastUpdate(lastUpdate);
		return mFlightDeicingInput;
	}
	
	private String getSubTime(String y, String x) throws ParseException {
		if (y.length() == 0 || x.length() == 0) {
			return " ";
		} else {
			String totalDate = y +" "+ x;
			return DataUtil.getTotalTime(totalDate);
		}
		
	}

	class SpinnerSelectedListener implements OnItemSelectedListener{  

		public void onItemSelected(AdapterView<?> parent, View mView, int index,  
				long id) {  
			currentStnd = pstnds.get(index);
		}  
		public void onNothingSelected(AdapterView<?> arg0) {  
		}  
	}
	
}