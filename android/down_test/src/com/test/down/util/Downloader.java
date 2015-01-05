package com.test.down.util;

import static com.test.down.constant.DownloadConstants.DOWNLOAD_STATES.FINISHED;
import static com.test.down.constant.DownloadConstants.DOWNLOAD_STATES.NOT_START;
import static com.test.down.constant.DownloadConstants.DOWNLOAD_STATES.PAUSED;
import static com.test.down.constant.DownloadConstants.DOWNLOAD_STATES.STARTED;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.test.down.constant.DownloadConstants.CONFIG;
import com.test.down.handler.DownloadHandler;

public class Downloader {

	private int downloadState = NOT_START;
	private DownloadHandler downloadHandler;
	private String urlName;
	private String fileName;
	private URL url;
	private int downloadedSize;
	private int size;
	private String filePath;
	private Timer timer;
	private ProcessTask processTask;
	
	public Downloader(DownloadHandler downloadHandler) {
		super();
		this.downloadHandler = downloadHandler;
	}

	public Downloader(DownloadHandler downloadHandler, String url,
			String fileName) {
		super();
		this.downloadHandler = downloadHandler;
		this.urlName = url;
		this.fileName = fileName;
	}

	public void start() throws IOException {
		downloadState = STARTED;
		int filestate = DownloadUtil.checkFile(fileName, urlName);
		url = new URL(urlName);
		size = DownloadUtil.getRemoteFileSize(url);
		downloadHandler.sendSizeMessage(size);
		//continue with not finished download task
		if(filestate == CONFIG.TEMP_FILE_EXIST_URL_EQUAL) {
			filePath = CONFIG.DOWNLOAD_PATH + fileName + CONFIG.TEMP_FILE_SUFFIX;
			downloadedSize = DownloadUtil.getLocalFileSize(filePath);
			downloadHandler.sendDownloadedMessage(downloadedSize);
		//start new task
		} else {
			filePath = DownloadUtil.prepareFile(fileName, urlName);
		}
		down(url);
	}
	
	private void down(URL url) throws IOException {
		//start downloading
		if(downloadedSize < size) {
			RandomAccessFile fileOutStream = new RandomAccessFile(filePath, "rw");
			fileOutStream.seek(downloadedSize);
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setRequestProperty("Range", "bytes=" + downloadedSize + "-" + size);
			
			if(urlConn.getContentLength() > 0) {
				
				timer = new Timer();
				processTask = new ProcessTask();
				timer.schedule(processTask, CONFIG.PROCESS_UPDATE_PEROID, CONFIG.PROCESS_UPDATE_PEROID);
				
				DownloadThread downloadThread = new DownloadThread(urlConn, fileOutStream);
				downloadThread.start();
			} else {
				fileOutStream.close();
				DownloadUtil.finishDownload(filePath);
			}
		}
	}
	
	public void pause() {
		downloadState = PAUSED;
	}
	
	public void resume() throws IOException {
		downloadState = STARTED;
		down(url);
	}
	
	public void finish() {
		downloadState = FINISHED;
		DownloadUtil.finishDownload(filePath);
		downloadHandler.sendFinishMessage();
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getDownloadState() {
		return downloadState;
	}
	
	private class ProcessTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			switch(downloadState) {
				case STARTED:
					downloadHandler.sendDownloadedMessage(downloadedSize);
					break;
				case PAUSED:
				case FINISHED:
					timer.cancel();
					break;
			}
		}
		
	}
	
	private class DownloadThread extends Thread {

		private HttpURLConnection urlConn;
		private RandomAccessFile fileOutStream;
		private Throwable throwable;
		
		public DownloadThread(HttpURLConnection urlConn, RandomAccessFile fileOutStream) {
			super();
			// TODO Auto-generated constructor stub
			this.urlConn = urlConn;
			this.fileOutStream = fileOutStream;
		}

		public void run() {
			InputStream in;
			try {
				in = urlConn.getInputStream();
				byte[] buffer = new byte[CONFIG.BUFFER_SIZE];
				int length = 0;
				while(((length = in.read(buffer)) > 0) && downloadState == STARTED) {
					fileOutStream.write(buffer, 0, length);
					downloadedSize += length;
				}
				in.close();
				fileOutStream.close();
				urlConn.disconnect();
				if(downloadState == STARTED)
					finish();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throwable = e;
			}
		}

		@SuppressWarnings("unused")
		public Throwable getThrowable() {
			return throwable;
		}
		
	}
}
