package com.test.down.constant;

public class DownloadConstants {

	public static final class DOWNLOAD_STATES {
		public static final int NOT_START = 0;
		public static final int STARTED = 1;
		public static final int PAUSED = 2;
		public static final int FINISHED = 3;
		public static final int ERROR = 99;
		
		public static final String START_TEXT = "开始下载";
		public static final String PAUSE_TEXT = "暂停下载";
		public static final String CONTINUE_TEXT = "继续下载";
		public static final String RESTART_TEXT = "重新下载";
	}
	
	public static final class CONFIG {
		public static final String DOWNLOAD_PATH = "/sdcard/down/";
		public static final String TEMP_FILE_SUFFIX = ".tmp";
		public static final String URL_FILE_SUFFIX = ".url";
		public static final int BUFFER_SIZE = 4096;
		public static final int NEW_FILE_START_INDEX = 1;

		public static final int FILE_EXIST = 0;
		public static final int TEMP_FILE_EXIST_URL_EQUAL = 1;
		public static final int TEMP_FILE_EXIST_URL_NOT_EQUAL = 2;
		public static final int FILE_NOT_EXIST = 3;
		
		public static final long PROCESS_UPDATE_PEROID = 1000;
	}
	
	public static final class MESSAGE {
		public static final int GET_CONTENT_LENGTH = 0;
		public static final int UPDATE_DOWNLOAD_PROCESS = 1;
		public static final int DOWNLOAD_FINISHED = 2;
		public static final String SIZE_VALUE_KEY = "size";
		public static final String MESSAGE_FINISHED = "下载完成";
		public static final String MESSAGE_PAUSEED = "下载暂停";
		public static final String MESSAGE_STARTED = "下载开始";
		public static final String MESSAGE_CONTINUEED = "下载继续";
	}
}
