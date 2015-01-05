package com.test.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.demo.adapter.AppSearchListAdapter;
import com.test.demo.domain.App;
import com.test.demo.saxHandler.AppSaxHandler;

public class List_demoActivity extends Activity {
	private static final int TEST_SEARCH_STRING1 = R.string.search1;
	private static final int TEST_SEARCH_STRING2 = R.string.search2;
	
	private static List<App> appList = new ArrayList<App>();
	private AppSaxHandler appSaxHandler;
	
	static {
		appList.add(new App("a.jpg", 3, "飞信", "中国移动", "description111", 2.3D, "MM软件应用", 16868L));
		appList.add(new App("a.jpg", 3, "手机QQ", "QQ", "大师级大师觉得", 2.3D, "MM软件应用", 432L));
		appList.add(new App("a.jpg", 3, "网易新闻", "163.com", "啊啊啊的我企鹅去", 2.3D, "MM软件应用", 22L));
		appList.add(new App("a.jpg", 3, "新浪微博", "新浪", "sadasdasd", 2.3D, "MM软件应用", 1642868L));
		appList.add(new App("a.jpg", 3, "test1123", "中国移动", "253464564564", 2.3D, "MM软件应用", 333338L));
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView appSearchText1 = (TextView)findViewById(R.id.appSearchText1);
        TextView appSearchText2 = (TextView)findViewById(R.id.appSearchText2);
        
        appSearchText1.setText(TEST_SEARCH_STRING1);
        appSearchText2.setText(TEST_SEARCH_STRING2);
        
        ListView list = (ListView)findViewById(R.id.appListView);
        list.setAdapter(new AppSearchListAdapter(this, appList));
        
        
        if(appSaxHandler == null)
			appSaxHandler = new AppSaxHandler(new SearchHandler());
        
        Button searchButton = (Button)findViewById(R.id.appSearchButton);
        searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("ButtonClick", "ButtonClick");
				Toast.makeText(List_demoActivity.this, "onclicked", Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						HttpClient client = new DefaultHttpClient();
						HttpPost request = new HttpPost("http://10.32.148.160/test.xml");
						try {
							HttpResponse response = client.execute(request);
							InputStream in = response.getEntity().getContent();
							
							SAXParserFactory factory = SAXParserFactory.newInstance();
							SAXParser parser = factory.newSAXParser();
							parser.parse(in, appSaxHandler);
//							parser.parse(in, new AppSaxHandler(new SearchHandler()));
							in.close();
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}).start();
			}
        	
        });
        Button appWriteButton = (Button)findViewById(R.id.appWriteButton);
        appWriteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				File f = new File(Environment.getExternalStorageDirectory().getPath(), "test.txt");
				try {
//					FileOutputStream fout = List_demoActivity.this.openFileOutput("test.txt", 
//							Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
//					fout.write("test".getBytes());
//					fout.flush();
//					fout.close();
					FileOutputStream out = new FileOutputStream(f); 
					out.write("test".getBytes());
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Toast.makeText(List_demoActivity.this, f.getAbsolutePath() + "\t" + f.exists(), Toast.LENGTH_LONG).show();
			}
        	
        });
        
    }
    
    private class SearchHandler extends Handler {
    	
    	public void handleMessage(Message msg) {
    		List<App> appList = appSaxHandler.getAppList();
    		ListView list = (ListView)findViewById(R.id.appListView);
            list.setAdapter(new AppSearchListAdapter(List_demoActivity.this, appList));
    		
    	}
    }
}