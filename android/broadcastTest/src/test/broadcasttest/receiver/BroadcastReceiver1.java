package test.broadcasttest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BroadcastReceiver1 extends BroadcastReceiver {

	public BroadcastReceiver1() {
		super();
		// TODO Auto-generated constructor stub
		Log.i("BroadcastReceiver1", "constructor...");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "成功接收广播：" + intent.getCharSequenceExtra("msg"), Toast.LENGTH_LONG).show();
	}

}
