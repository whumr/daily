package com.test.down;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.down.constant.DownloadConstants.DOWNLOAD_STATES;
import com.test.down.handler.DownloadHandler;
import com.test.down.listener.DownloadListener;

public class DownloadActivity extends Activity {
	
	private ProgressBar processbar;
	private TextView processText;
	private DownloadHandler downloadHandler;
	private DownloadListener downloadListener;
	
	private EditText urlText;
	private EditText fileText;
	private Button downButton;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        
        urlText = (EditText)findViewById(R.id.urlText);
        fileText = (EditText)findViewById(R.id.fileText);
        processText = (TextView)findViewById(R.id.processText);
        processbar = (ProgressBar)findViewById(R.id.processbar);
        downButton = (Button)findViewById(R.id.downButton);
        downButton.setText(DOWNLOAD_STATES.START_TEXT);
        
        downloadHandler = new DownloadHandler(this, processbar, processText, downButton); 
        downloadListener = new DownloadListener(this, downloadHandler, downButton, urlText, fileText);
        downButton.setOnClickListener(downloadListener);
    }
    
}