package test.tab;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListTestActivity extends ListActivity {

	private static final int viewMenuId = 0;
	private static final int cancelMenuId = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.countries_array)));  
		final ListView lv = getListView();  
		lv.setTextFilterEnabled(true);  
//		registerForContextMenu(lv);
		lv.setOnItemClickListener(new OnItemClickListener() {    
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {      
				// When clicked, show a toast with the TextView text     
//				Toast.makeText(getApplicationContext(), ((TextView) view).getText(),          
//						Toast.LENGTH_SHORT).show();
				x(view);
			}  
		});
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("菜单.......");   
		        MenuItem viewMenu = menu.add(Menu.NONE, viewMenuId, Menu.NONE, "查看");
		        viewMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
//						int mListPos = info.position;
//						Toast.makeText(getApplicationContext(), 
//								info.targetView.getClass().getName() + "\n" +
//								v.getClass().getName() + "\n" + 
//								(info.targetView == v) + "\n" + mListPos, 
//								Toast.LENGTH_SHORT).show();
						x(info.targetView);
						return false;
					}
		        	
		        });
		        menu.add(Menu.NONE, cancelMenuId, Menu.NONE, "取消");
			}
			
		});
	}
	
	private void x(View view) {
		Toast.makeText(getApplicationContext(), ((TextView) view).getText(),          
				Toast.LENGTH_SHORT).show();
	}
	
//	@Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.setHeaderTitle("菜单.......");   
//        menu.add(Menu.NONE, viewMenuId, Menu.NONE, "查看");
//        menu.add(Menu.NONE, cancelMenuId, Menu.NONE, "取消");
//    }

}