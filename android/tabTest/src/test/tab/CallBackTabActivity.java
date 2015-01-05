package test.tab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class CallBackTabActivity extends TabActivity {
	
//	private static final String TAG = "test";

	static String loading = "加载中";
	
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
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				List<String> l = map.get(tabId);
				ListView list = (ListView)findViewById(R.id.listView);
				list.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Toast.makeText(CallBackTabActivity.this, view.getClass().getName() + " onItemClick", Toast.LENGTH_SHORT).show();
					}
				});
				String[] ss = new String[l.size()];
				for(int i=0;i<l.size();i++)
					ss[i] = l.get(i);
				list.setAdapter(new ArrayAdapter<String>(CallBackTabActivity.this, 
						android.R.layout.simple_list_item_1, ss));
				
				
//				tabHost.getCurrentTab();
				
				RelativeLayout rl = (RelativeLayout)(CallBackTabActivity.this.getTabWidget().getChildAt(0));
				Toast.makeText(CallBackTabActivity.this, 
						tabHost.getCurrentTab() + "\n" +
						rl.getChildAt(0).getClass().getName() + "\n" +
						rl.getChildAt(1).getClass().getName(), 
						Toast.LENGTH_SHORT).show();
//				((TextView)(rl.getChildAt(1))).setText("111111111111");
			}
		});
		
//		Handler handler1 = new MyHandler(yesterday);
//		Handler handler2 = new MyHandler(tomorrow);
//		Handler handler1 = new Handler() {
//			public void handleMessage(Message msg) {
//				RelativeLayout rl = (RelativeLayout)(CallBackTabActivity.this.getTabWidget().getChildAt(0));
//				((TextView)(rl.getChildAt(1))).setText((String)msg.obj);
////				CallBackTabActivity.this.yesterday.setIndicator((String)msg.obj);
////				x(yesterday, (String)msg.obj);
//				Log.d(TAG, "yesterday.setIndicator " + msg.obj);
//			}
//		};
//		
//		Handler handler2 = new Handler() {
//			public void handleMessage(Message msg) {
//				RelativeLayout rl = (RelativeLayout)(CallBackTabActivity.this.getTabWidget().getChildAt(2));
//				((TextView)(rl.getChildAt(1))).setText((String)msg.obj);
////				CallBackTabActivity.this.tomorrow.setIndicator((String)msg.obj);
////				x(tomorrow, (String)msg.obj);
//				Log.d(TAG, "tomorrow.setIndicator " + msg.obj);
//			}
//		};
		
//		MyHandler myHandler1 = new MyHandler(0);
//		MyHandler myHandler2 = new MyHandler(2);
//		MyHandler2 myHandler1 = new MyHandler2(getTabWidget(), 0);
//		MyHandler2 myHandler2 = new MyHandler2(getTabWidget(), 2);
		
//		new Reporter(myHandler1, 3000).start();
//		new Reporter(myHandler2, 8000).start();
//		new Reporter(getTabWidget(), 0, 3000).start();
//		new Reporter(getTabWidget(), 2, 8000).start();
//		new Reporter(handler1, 3000).start();
//		new Reporter(handler2, 8000).start();
		new MixHandler(getTabWidget(), 0, 3000).start();
		new MixHandler(getTabWidget(), 2, 8000).start();
		
	}
	
//	public void x(TabSpec tabSpec, String msg) {
//		tabSpec.setIndicator(msg);
//	}
	
	static Map<String, List<String>> map = new HashMap<String, List<String>>();
	
	static {
		List<String> l1 = new ArrayList<String>();
		l1.add("公牛对小牛");
		map.put("yesterday", l1);
		List<String> l2 = new ArrayList<String>();
		l2.add("热火对凯尔特人");
		l2.add("灰熊对雷霆");
		map.put("today", l2);
		List<String> l3 = new ArrayList<String>();
		l3.add("湖人对小牛");
		l3.add("马刺对灰熊");
		l3.add("马刺对热火");
		map.put("tomorrow", l3);
	}
}

//class MyHandler extends Handler {
//	TabWidget tabWidget;
//	int index;
//	
//	public MyHandler(TabWidget tabWidget, int index) {
//		super();
//		this.tabWidget = tabWidget;
//		this.index = index;
//	}
//
//	public MyHandler(int index) {
//		super();
//		this.index = index;
//	}
//
//	public void handleMessage(Message msg) {
//		RelativeLayout rl = (RelativeLayout)(CallBackTabActivity.this.getTabWidget().getChildAt(index));
//		((TextView)(rl.getChildAt(1))).setText((String)msg.obj);
//		Log.d(TAG, "tomorrow.setIndicator " + msg.obj);
//	}
//}

class MyHandler2 extends Handler {
	TabWidget tabWidget;
	int index;
	
	public MyHandler2(TabWidget tabWidget, int index) {
		super();
		this.tabWidget = tabWidget;
		this.index = index;
	}

	public MyHandler2(int index) {
		super();
		this.index = index;
	}

	public void handleMessage(Message msg) {
		RelativeLayout rl = (RelativeLayout)(tabWidget.getChildAt(index));
		((TextView)(rl.getChildAt(1))).setText((String)msg.obj);
		Log.d("test", "tomorrow.setIndicator " + msg.obj);
	}
}

class Reporter {
	Handler handler;
	long sleep;
	
	public Reporter(Handler handler, long sleep) {
		this.handler = handler;
		this.sleep = sleep;
	}
	
	public Reporter(TabWidget tabWidget, int index, long sleep) {
		this.handler = new MyHandler2(tabWidget, index);
		this.sleep = sleep;
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
	
	private String getNews() {
		Random random = new Random();
		return new StringBuilder()
		.append(random.nextInt(12) + 1)
		.append("月")
		.append(random.nextInt(31) + 1)
		.append("日")
		.toString();
	}
	
}

class MixHandler extends Handler {
	long sleep;
	TabWidget tabWidget;
	int index;
	
	public MixHandler(TabWidget tabWidget, int index, long sleep) {
		super();
		this.tabWidget = tabWidget;
		this.index = index;
		this.sleep = sleep;
	}

	public void handleMessage(Message msg) {
		RelativeLayout rl = (RelativeLayout)(tabWidget.getChildAt(index));
		((TextView)(rl.getChildAt(1))).setText((String)msg.obj);
		Log.d("test", "tomorrow.setIndicator " + msg.obj);
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
				MixHandler.this.sendMessage(msg);
			}
		}.start();
	}
	
	private String getNews() {
		Random random = new Random();
		return new StringBuilder()
		.append(random.nextInt(12) + 1)
		.append("月")
		.append(random.nextInt(31) + 1)
		.append("日")
		.toString();
	}
}