package test.tab;

import java.util.HashMap;
import java.util.Map;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class TabTestActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
        init();
        setupTabs();
    }
    
    private void setupTabs(){ 
        final TabHost mTabHost = getTabHost();
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_1").setIndicator( 
                "Tab 1").setContent(R.id.tv)); 
        mTabHost.addTab(mTabHost.newTabSpec("tab_2").setIndicator( 
                "Tab 2").setContent(R.id.tv)); 
        mTabHost.addTab(mTabHost.newTabSpec("tab_3").setIndicator( 
                "Tab 3").setContent(R.id.tv)); 
        mTabHost.addTab(mTabHost.newTabSpec("tab_4").setIndicator( 
                "Tab 4").setContent(R.id.tv)); 
        TabWidget tabWidget = mTabHost.getTabWidget();
        System.out.println(tabWidget.getChildCount());
//        ((TabSpec)mTabHost.getTag(1)).setIndicator("aaaaaaaa");
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
//				if(mTabHost.getCurrentTab() == 1) {
//					Toast.makeText(TabTestActivity.this, tabId + "  " + 
//							mTabHost.getCurrentTab(), Toast.LENGTH_SHORT).show();
//					TextView text = (TextView)findViewById(R.id.tv);
//					text.setText(tabId);
//					text.append("  " + mTabHost.getCurrentTab() + " " +
//							mTabHost.getTabContentView().getChildCount());
////							.getChildAt(1).getClass().getName());
//				}
//				System.out.println(mTabHost.getTabWidget().getChildCount());
//				TextView text = new TextView(TabTestActivity.this);
//				((LinearLayout)mTabHost.getTabContentView().getChildAt(1)).addView(text);
//				text.setText(mTabHost.getCurrentTab() + "");
//				TextView text = (TextView)findViewById(R.id.tabText);
//				text.setText(
//						mTabHost.getCurrentTab() + "\n" 
//						+ mTabHost.getCurrentView().getParent().getClass().getName() + "\n" 
//						+ mTabHost.getCurrentView().getParent().getParent().getClass().getName() + "\n" 
//						+ mTabHost.getCurrentView().getParent().getParent().getParent().getClass().getName() + "\n" 
//						+ ((LinearLayout)mTabHost.getChildAt(0)).getChildCount()
//				);
//				for(int i=0;i<((LinearLayout)mTabHost.getChildAt(0)).getChildCount();i++) {
//					text.append("-------------\n" + ((LinearLayout)mTabHost.getChildAt(0)).getChildAt(i).getClass().getName() + "\n");
//				}
				
				LinearLayout l = (LinearLayout)((LinearLayout)mTabHost.getChildAt(0)).getChildAt(2);
				TextView t = null;
				if(l.getChildCount() == 2) {
					t = (TextView)l.getChildAt(1);
				} else {
					t = new TextView(TabTestActivity.this);
					l.addView(t);
				}
				t.setText(tabTextMap.get(mTabHost.getCurrentTabTag()));
				t.setLayoutParams(l.getLayoutParams());
				System.out.println(t.isShown());
			}
        });
        
        }
    private Map<String, String> tabTextMap;
    private String[][] initData = {
    		{"tab_1", "tab1:aaaaaaaaaaaaaa"},
    		{"tab_2", "tab2:11111111111111"},
    		{"tab_3", "tab3:!!!!!!!!!!!!!!"},
    		{"tab_4", "tab4:@@@@@@@@@@@@@@"}
    };
    
    private void init() {
    	if(tabTextMap == null)
    		tabTextMap = new HashMap<String, String>();
    	for(int i=0;i<initData.length;i++) {
    		tabTextMap.put(initData[i][0], initData[i][1]);
    	}
    }
}