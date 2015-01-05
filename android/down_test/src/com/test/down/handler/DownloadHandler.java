package com.test.down.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.down.constant.DownloadConstants.DOWNLOAD_STATES;
import com.test.down.constant.DownloadConstants.MESSAGE;

public class DownloadHandler extends Handler {

	private Context context;
	private ProgressBar downloadProcessBar;
	private TextView downloadProcessText;
	private Button downButton;
	
	public DownloadHandler(Context context,
			ProgressBar downloadProcessBar, TextView downloadProcessText, Button downButton) {
		super();
		this.context = context;
		this.downloadProcessBar = downloadProcessBar;
		this.downloadProcessText = downloadProcessText;
		this.downButton = downButton;
	}

	public void handleMessage(Message msg) {
		int size = msg.getData().getInt(MESSAGE.SIZE_VALUE_KEY);
		switch (msg.what) {
			case MESSAGE.GET_CONTENT_LENGTH:
				downloadProcessBar.setMax(size);
				break;
			case MESSAGE.UPDATE_DOWNLOAD_PROCESS:
				downloadProcessBar.setProgress(size);
				float result = (float) downloadProcessBar.getProgress()
						/ (float) downloadProcessBar.getMax();
				int percent = (int) (result * 100);
				downloadProcessText.setText(percent + "%");
				if (downloadProcessBar.getProgress() == downloadProcessBar.getMax())
					Toast.makeText(context, MESSAGE.MESSAGE_FINISHED, 0).show();
				break;
			case MESSAGE.DOWNLOAD_FINISHED:
				downloadProcessBar.setProgress(downloadProcessBar.getMax());
				downloadProcessText.setText("100%");
				downButton.setText(DOWNLOAD_STATES.RESTART_TEXT);
				Toast.makeText(context, MESSAGE.MESSAGE_FINISHED, 0).show();
				break;
		}
	}
	
	public void sendSizeMessage(int size) {
		Message msg = new Message();
		msg.what = MESSAGE.GET_CONTENT_LENGTH;
		msg.getData().putInt(MESSAGE.SIZE_VALUE_KEY, size);	
		sendMessage(msg);
	}
	
	public void sendFinishMessage() {
		Message msg = new Message();
		msg.what = MESSAGE.DOWNLOAD_FINISHED;
		sendMessage(msg);
	}
	
	public void sendDownloadedMessage(int size) {
		Message msg = new Message();
		msg.what = MESSAGE.UPDATE_DOWNLOAD_PROCESS;
		msg.getData().putInt(MESSAGE.SIZE_VALUE_KEY, size);	
		sendMessage(msg);
	}
}
