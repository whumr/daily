package test.tab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class UIActivity extends TabActivity {

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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui);
		init();
	}
	
	private void init() {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
		
		final TabHost tabHost = getTabHost();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		TabSpec yesterday = tabHost.newTabSpec("yesterday");
		yesterday.setIndicator(dateFormat.format(calendar.getTime())).setContent(R.id.listView);
		tabHost.addTab(yesterday);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		TabSpec today = tabHost.newTabSpec("today");
		today.setIndicator(dateFormat.format(calendar.getTime())).setContent(R.id.listView);
		tabHost.addTab(today);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		TabSpec tomorrow = tabHost.newTabSpec("tomorrow");
		tomorrow.setIndicator(dateFormat.format(calendar.getTime())).setContent(R.id.listView);
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
						System.out.println(view.getClass().getName() + " onItemClick");
						Toast.makeText(UIActivity.this, view.getClass().getName() + " onItemClick", Toast.LENGTH_SHORT).show();
					}
				});
				list.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						System.out.println(view.getClass().getName() + " onItemLongClick");
						Toast.makeText(UIActivity.this, view.getClass().getName() + " onItemLongClick", Toast.LENGTH_SHORT).show();
						return false;
					}
					
				});
				list.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						System.out.println(view.getClass().getName() + " onItemSelected");
						Toast.makeText(UIActivity.this, view.getClass().getName() + " onItemSelected", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						System.out.println("onNothingSelected");
						Toast.makeText(UIActivity.this, "onNothingSelected", Toast.LENGTH_SHORT).show();
					}
					
				});
				list.setAdapter(new MyListAdapter(UIActivity.this, l, R.layout.list));
			}
		});
//		
//		List<String> l = new ArrayList<String>();
//		l.add("xxx");
//		l.add("aaa");
//		l.add("ccc");
//		
//		ListView list = (ListView)findViewById(R.id.listView);
//		list.setAdapter(new ArrayAdapter<String>(this,  android.R.layout.simple_expandable_list_item_1, l));
		
	}
	
	class MyListAdapter extends BaseAdapter {

		public MyListAdapter() {
			super();
			// TODO Auto-generated constructor stub
		}

		public MyListAdapter(Context context, List<String> list, int layoutId) {
			super();
			this.list = list;
			this.layoutId = layoutId;
			this.context = context;
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null)
				convertView = mInflater.inflate(layoutId, null);
			
			TextView text = (TextView)convertView.findViewById(R.id.title);
			text.setText(list.get(position));
			Button button = (Button)convertView.findViewById(R.id.view_btn);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					x(position);
				}
			});
			return convertView;
		}
		
		void x(int i) {
			Toast.makeText(context, "查看 " + list.get(i), Toast.LENGTH_SHORT).show();
		}
		
		LayoutInflater mInflater;
		int layoutId;
		List<String> list;
		Context context;

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
		}
	}
}