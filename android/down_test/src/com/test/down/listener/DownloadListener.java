package com.test.down.listener;

import static com.test.down.constant.DownloadConstants.DOWNLOAD_STATES.*;
import static com.test.down.constant.DownloadConstants.MESSAGE.*;

import java.io.IOException;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.test.down.handler.DownloadHandler;
import com.test.down.util.Downloader;

public class DownloadListener implements OnClickListener {

	private Downloader downloader;
	private Context context;
	private Button button;
	private EditText urlText;
	private EditText fileText;
	
	public DownloadListener(Context context, DownloadHandler downloadHandler, Button button, EditText urlText, EditText fileText) {
		super();
		this.context = context;
		this.button = button;
		this.downloader = new Downloader(downloadHandler);
		this.urlText = urlText;
		this.fileText = fileText;
	}

	public DownloadListener(Downloader downloader, Context context,
			Button button) {
		super();
		this.downloader = downloader;
		this.context = context;
		this.button = button;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(downloader.getDownloadState()) {
			case NOT_START:
				try {
					start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case STARTED:
				pause();
				break;
			case PAUSED:
				try {
					resume();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case FINISHED:
				finish();
				break;
		}
	}

	public void start() throws IOException {
		downloader.setUrlName(urlText.getText().toString());
		downloader.setFileName(fileText.getText().toString());
		downloader.start();
		button.setText(PAUSE_TEXT);
		Toast.makeText(context, MESSAGE_STARTED, Toast.LENGTH_SHORT).show();
	}
	
	public void pause() {
		downloader.pause();
		button.setText(CONTINUE_TEXT);
		Toast.makeText(context, MESSAGE_PAUSEED, Toast.LENGTH_SHORT).show();
	}
	
	public void resume() throws IOException {
		downloader.resume();
		button.setText(PAUSE_TEXT);
		Toast.makeText(context, MESSAGE_CONTINUEED, Toast.LENGTH_SHORT).show();
		
	}
	
	public void finish() {
		downloader.finish();
	}

	public Downloader getDownloader() {
		return downloader;
	}

	public void setDownloader(Downloader downloader) {
		this.downloader = downloader;
	}
}
