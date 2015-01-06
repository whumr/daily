package com.p2p.www.common;


public class Common_Constants {

	public static final int PAGE_SIZE = 10;
	
	public static String CLIENT_ID = "f4068d8c2d21d6cd";
	public static String CLIENT_SECRET = "0c16026990dcb8cf1848ca8d751b0ef4";

	public static class URL {
		private static String KEY_USER_NAME = "&user_name=";
		private static String KEY_USER_ID = "&user_id=";
		//用户
		private static String YOUKU_USER_INFO_URL = "https://openapi.youku.com/v2/users/show.json?client_id=" + CLIENT_ID;
		public static String YOUKU_USER_INFO_URL_BY_NAME = YOUKU_USER_INFO_URL + KEY_USER_NAME;
		public static String YOUKU_USER_INFO_URL_BY_ID = YOUKU_USER_INFO_URL + KEY_USER_ID;
		//视频
		private static String YOUKU_USER_VIDEOS_URL = "https://openapi.youku.com/v2/videos/by_user.json?client_id=" + CLIENT_ID;
		public static String YOUKU_USER_VIDEOS_URL_BY_NAME = YOUKU_USER_VIDEOS_URL + KEY_USER_NAME;
		public static String YOUKU_USER_VIDEOS_URL_BY_ID = YOUKU_USER_VIDEOS_URL + KEY_USER_ID;
		//专辑
		private static String YOUKU_USER_PLAYLISTS_URL = "https://openapi.youku.com/v2/playlists/by_user.json?client_id=" + CLIENT_ID;
		public static String YOUKU_USER_PLAYLISTS_URL_BY_NAME = YOUKU_USER_PLAYLISTS_URL + KEY_USER_NAME;
		public static String YOUKU_USER_PLAYLISTS_URL_BY_ID = YOUKU_USER_PLAYLISTS_URL + KEY_USER_ID;
	}
	
	public static class KEYS {
		public static String ERROR = "error";
		public static String VIDEOS = "videos";
		public static String PLAYLISTS = "playlists";
	}
}