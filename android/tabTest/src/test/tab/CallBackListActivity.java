package test.tab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class CallBackListActivity extends TabActivity {

	static String loading = "加载中";
	static String[] defaultStrings = {"加载中"};
	TabSpec yesterday;
	TabSpec tomorrow;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui);
		init();
	}
	
	private void init() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
		
		final TabHost tabHost = getTabHost();
		yesterday = tabHost.newTabSpec("yesterday");
		yesterday.setIndicator(loading).setContent(R.id.listView);
		tabHost.addTab(yesterday);
		
		final TabSpec today = tabHost.newTabSpec("today");
		today.setIndicator(dateFormat.format(new Date())).setContent(R.id.listView);
		tabHost.addTab(today);
		
		tomorrow = tabHost.newTabSpec("tomorrow");
		tomorrow.setIndicator(loading).setContent(R.id.listView);
		tabHost.addTab(tomorrow);
		tabHost.setCurrentTab(1);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				ListView list = (ListView)findViewById(R.id.listView);
				list.setAdapter(new ArrayAdapter<String>(CallBackListActivity.this, 
						android.R.layout.simple_list_item_1, defaultStrings));
				new Reporter1(CallBackListActivity.this, list, 3000, tabId).start();
				list.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Toast.makeText(CallBackListActivity.this, view.getClass().getName() + " onItemClick", Toast.LENGTH_SHORT).show();
					}
				});
				RelativeLayout rl = (RelativeLayout)(CallBackListActivity.this.getTabWidget().getChildAt(0));
				Toast.makeText(CallBackListActivity.this, 
						tabHost.getCurrentTab() + "\n" +
						rl.getChildAt(0).getClass().getName() + "\n" +
						rl.getChildAt(1).getClass().getName(), 
						Toast.LENGTH_SHORT).show();
			}
		});
		
//		new Reporter1(this, (ListView)findViewById(R.id.listView), 3000, "today").start();
		new Reporter1(new MyHandler4(this), 3000, "today").start();
	}
	
	
//	public void update(ListAdapter listAdapter) {
//		ListView listView = (ListView)findViewById(R.id.listView);
//		listView.setAdapter(listAdapter);
//	}
}

class MyHandler3 extends Handler {
	ListView list;
	Context context;
	
	public MyHandler3(Context context, ListView list) {
		super();
		this.list = list;
		this.context = context;
	}

	public void handleMessage(Message msg) {
		String[] ss = (String[])msg.obj;
		list.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, ss));
	}
}

class MyHandler4 extends Handler {
	Activity activity;
	
	public MyHandler4(Activity activity) {
		super();
		this.activity = activity;
	}

	public void handleMessage(Message msg) {
		String[] ss = (String[])msg.obj;
		ListView listView = (ListView)activity.findViewById(R.id.listView);
		listView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, ss));
	}
}

class Reporter1 {
	Handler handler;
	long sleep;
	String tag;
	
	public Reporter1(Handler handler, long sleep, String tag) {
		this.handler = handler;
		this.sleep = sleep;
		this.tag = tag;
	}
	
	public Reporter1(Context context, ListView list, long sleep, String tag) {
		this.handler = new MyHandler3(context, list);
		this.sleep = sleep;
		this.tag = tag;
	}

	public void start() {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.obj = getNews();
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	private String[] getNews() {
		return map.get(tag);
	}
	
	static Map<String, String[]> map = new HashMap<String, String[]>();
	
	static {
		map.put("yesterday", new String[] {"公牛对小牛"});
		map.put("today", new String[] {"热火对凯尔特人", "灰熊对雷霆"});
		map.put("tomorrow", new String[] {"湖人对小牛", "马刺对灰熊" ,"马刺对热火"});
	}
}