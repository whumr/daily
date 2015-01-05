package test.broadcasttest.activity;

import test.broadcasttest.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BroadcastActivity extends Activity {
	
	public static final String NEW_LIFEFORM_DETECTED = "com.android.broadcasttest.NEW_LIFEFORM";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button bt = (Button)findViewById(R.id.bt);
        bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(NEW_LIFEFORM_DETECTED);
				it.putExtra("msg", "aaaaaaaaaaaaaaaaaa");
				sendBroadcast(it); 
			}
        	
        });
    }
}