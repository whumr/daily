package test.broadcasttest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends ListActivity {

	@Override
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		
		List<Class<? extends Activity>> list = new ArrayList<Class<? extends Activity>>();
		String[] classNames = getResources().getStringArray(R.array.class_array);
		for(String className : classNames) {
			try {
				list.add((Class<? extends Activity>)Class.forName(className));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setListAdapter(new MyArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
				getResources().getStringArray(R.array.activity_array), list));  
		ListView lv = getListView();  
		lv.setTextFilterEnabled(true);  
		lv.setOnItemClickListener(new OnItemClickListener() {    
			public void onItemClick(AdapterView<?> parent, View view,        
					int position, long id) {      
				// When clicked, show a toast with the TextView text     
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(),          
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(Main.this, ((MyArrayAdapter<String>)Main.this.getListAdapter()).getClass(position));  
				startActivity(intent);  

			}  
		});
	}
	
	class MyArrayAdapter<T> extends ArrayAdapter<T> {
		List<Class<? extends Activity>> list;
		
		public MyArrayAdapter(Context context, int textViewResourceId, T[] objects, List<Class<? extends Activity>> list) {
			super(context, textViewResourceId, objects);
			this.list = list;
		}
		
		public Class<? extends Activity> getClass(int index) {
			return list.get(index);
		}
	}
}
