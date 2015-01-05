package test.service.service;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TimeService extends Service {
	
	private String data; 
	private static final String LOG = "TimeService";
	Timer timer = new Timer();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new TimeBinder(this);
	}
	
	public void onStart(Intent intent, int startId) {  
		super.onStart(intent, startId);
		timer.schedule(new TimerTask() {
			char[] c = new char[10];
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Random r = new Random();
				for(int i = 0; i < c.length; i++)
					c[i] = (char)(r.nextInt(90) + 1);
				setData(new String(c));
			}
			
		}, 0, 3000L);
		Log.i(LOG, "onStart");
	}
	
	 public void onDestroy() {
		 super.onDestroy();
		 timer.cancel();
		 Log.i(LOG, "onDestroy");
	 } 


	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}

class TimeBinder extends Binder {
	private TimeService service;
	public TimeBinder(TimeService service) {
		super();
		this.service = service;
	}
	public TimeService getService() {
		return service;
	}
}
