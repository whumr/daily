package test.service.activity;


import test.service.R;
import test.service.service.TimeService;
import test.service.service.TimerServiceConnection;
import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TimeServiceActivity extends Activity {

	private final ServiceConnection serviceConnection = new TimerServiceConnection(this);
	private static final String LOG = "TimeServiceActivity";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeservicelayout);
        bindListener();
	}
	
	private void bindListener() {
		Button startButton = (Button)findViewById(R.id.startButton);
		Button stopButton = (Button)findViewById(R.id.stopButton);
		Button bindButton = (Button)findViewById(R.id.bindButton);
		Button unBindButton = (Button)findViewById(R.id.unBindButton);
		
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TimeServiceActivity.this, TimeService.class);
				TimeServiceActivity.this.startService(intent);
				Log.d(LOG, "start service");
			}
		});
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TimeServiceActivity.this, TimeService.class);
				TimeServiceActivity.this.stopService(intent);
				Log.d(LOG, "stop service");
			}
		});
		bindButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TimeServiceActivity.this, TimeService.class);
				TimeServiceActivity.this.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
				Log.d(LOG, "bind service");
			}
		});
		unBindButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeServiceActivity.this.unbindService(serviceConnection);
				Log.d(LOG, "unbind service");
			}
		});
	}
}
