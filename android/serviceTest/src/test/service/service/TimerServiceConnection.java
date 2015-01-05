package test.service.service;

import java.util.Timer;
import java.util.TimerTask;

import test.service.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class TimerServiceConnection implements ServiceConnection {

	private static final String LOG = "TimerServiceConnection";
	private Context context;
	private Timer timer;
	
	public TimerServiceConnection(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		TimeService timerService = (TimeService)(((TimeBinder)service).getService());
		TextView textView = (TextView)((Activity)context).findViewById(R.id.resultText);
		if(timer == null)
			timer = new Timer();
		timer.schedule(new TimerUpdateTask(textView, timerService, 
				new TimerUpdateHandler(textView)), 0L, 3000L);
		textView.setText(timerService.getData());
		Log.i(LOG, "onServiceConnected");
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		Log.i(LOG, "onServiceDisconnected");
	}
}

class TimerUpdateTask extends TimerTask {

	TextView textView;
	TimeService timerService;
	Handler handler;
	public TimerUpdateTask(TextView textView, TimeService timerService,
			Handler handler) {
		super();
		this.textView = textView;
		this.timerService = timerService;
		this.handler = handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.obj = timerService.getData();
		handler.sendMessage(msg);
	}
	
}

class TimerUpdateHandler extends Handler {
	TextView textView;
	public TimerUpdateHandler(TextView textView) {
		super();
		this.textView = textView;
	}
	public void handleMessage(Message msg) {
		String result = msg.obj.toString();
		textView.setText(result);
	}
}