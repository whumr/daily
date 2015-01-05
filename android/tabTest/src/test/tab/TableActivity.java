package test.tab;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.Toast;

public class TableActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);
        
    	TableLayout table = (TableLayout)this.findViewById(R.id.tableA_tl);
    	int rows = table.getChildCount();
    	for(int i=0;i<rows;i++) {
    		table.getChildAt(i).setOnClickListener(this);
//    		table.getChildAt(i).setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					MatchRow matchRow = (MatchRow)v;
//					Toast.makeText(getApplicationContext(), 
//							matchRow.getMid(), 
//							Toast.LENGTH_SHORT).show();
//				}
//    			
//    		});
    		table.getChildAt(i).setOnCreateContextMenuListener(this);
//    		table.getChildAt(i).setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
//
//				@Override
//				public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenuInfo menuInfo) {
//					// TODO Auto-generated method stub
//					menu.setHeaderTitle(v.getClass().getName() + "\n" +
//							v.getParent().getClass().getName());
//					MenuItem viewMenu = menu.add(Menu.NONE, 1, Menu.NONE, "查看");
//			        viewMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//	
//						@Override
//						public boolean onMenuItemClick(MenuItem item) {
//							// TODO Auto-generated method stub
//							MatchRow matchRow = (MatchRow)v;
//							Toast.makeText(getApplicationContext(), 
//									matchRow.getMid(), 
//									Toast.LENGTH_SHORT).show();
//							return false;
//						}
//			        	
//			        });
//					menu.add(Menu.NONE, 2, Menu.NONE, "取消");
//				}
//    			
//    		});
    	}
    	
//    	table.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
//
//			@Override
//			public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
//				// TODO Auto-generated method stub
//				menu.setHeaderTitle("菜单......."); 
//				
//				
//				TableLayout table = (TableLayout)v;
//				Log.d("aaaaaaaaaaa", table.getParent().getClass().getName() +
//						"\n" + table.getChildCount());
//				
//		        MenuItem viewMenu = menu.add(Menu.NONE, 1, Menu.NONE, "查看");
//		        viewMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//
//					@Override
//					public boolean onMenuItemClick(MenuItem item) {
//						// TODO Auto-generated method stub
//						Toast.makeText(getApplicationContext(), 
//								menu.getClass().getName() + "\n" +
//								v.getClass().getName() + "\n", 
//								Toast.LENGTH_SHORT).show();
//						return false;
//					}
//		        	
//		        });
//		        menu.add(Menu.NONE, 2, Menu.NONE, "取消");
//			}
//    		
//    	});
    	
//        addRow();
    }

    @Override
	public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenuInfo menuInfo) {
    	menu.setHeaderTitle(v.getClass().getName() + "\n" +
				v.getParent().getClass().getName());
		MenuItem viewMenu = menu.add(Menu.NONE, 1, Menu.NONE, "查看");
        viewMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				MatchRow matchRow = (MatchRow)v;
				Toast.makeText(getApplicationContext(), 
						matchRow.getMid(), 
						Toast.LENGTH_SHORT).show();
				return false;
			}
        	
        });
		menu.add(Menu.NONE, 2, Menu.NONE, "取消");
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MatchRow matchRow = (MatchRow)v;
		Toast.makeText(getApplicationContext(), 
				matchRow.getMid(), 
				Toast.LENGTH_SHORT).show();
	}
    
    
//	private void addRow() {
//    	TableLayout table = (TableLayout)this.findViewById(R.id.tableLayout);
//    	TableRow row = new TableRow(this);
//    	
//    	
//    	TextView t1 = new TextView(this);
//    	t1.setText("111");
//    	
//    	TextView t2 = new TextView(this);
//    	t2.setText("222");
//    	TextView t3 = new TextView(this);
//    	t3.setText("333");
//    	row.addView(t1);
//    	row.addView(t2);
//    	row.addView(t3);
//    	ImageView i1 = new ImageView(this);
//    	i1.setImageResource(R.drawable.icon);
//    	row.addView(i1, 2);
//
//    	table.addView(row,2);
//    	table.setVerticalGravity(Gravity.CENTER_HORIZONTAL);
//    }
}