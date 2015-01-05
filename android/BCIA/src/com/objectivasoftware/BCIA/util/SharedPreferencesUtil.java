package com.objectivasoftware.BCIA.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.objectivasoftware.BCIA.model.CheckBoxState;
import com.objectivasoftware.BCIA.model.SearchCondition;
import com.objectivasoftware.BCIA.model.UserInfo;

public class SharedPreferencesUtil {
	
	private static String flight_setting_fileName = "bcia_flight_setting_file"; 
	
	/**
	 * save Flight Park Setting
	 * @param activity
	 * @param checkBoxState
	 */
	public static void storeFlightParkSetting(Activity activity, CheckBoxState checkBoxState) {
		SharedPreferences settings = activity.getSharedPreferences(flight_setting_fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("flight_park1", checkBoxState.getCheckP1());
		editor.putString("flight_park2", checkBoxState.getCheckP2());
		editor.putString("flight_park3", checkBoxState.getCheckP3());
		editor.putString("flight_park4", checkBoxState.getCheckP4());
		editor.putString("flight_park5", checkBoxState.getCheckP5());
		editor.putString("flight_park6", checkBoxState.getCheckP6());
		editor.putString("flight_park7", checkBoxState.getCheckP7());
		editor.putString("flight_park8", checkBoxState.getCheckP8());
		editor.putString("flight_park9", checkBoxState.getCheckP9());
		editor.putString("flight_park10", checkBoxState.getCheckP10());
		editor.putString("flight_park11", checkBoxState.getCheckP11());
		editor.putString("flight_park12", checkBoxState.getCheckP12());
		editor.putString("flight_park13", checkBoxState.getCheckP13());
		editor.putString("flight_park14", checkBoxState.getCheckP14());
		editor.putString("flight_park15", checkBoxState.getCheckP15());
		editor.commit();		
	}
	
	/**
	 * fetch Flight Park Setting
	 * @param activity
	 * @return
	 */
	public static CheckBoxState fetchFlightParkSetting(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(flight_setting_fileName, Context.MODE_PRIVATE);
		
		String checkP1 = settings.getString("flight_park1", null);
		String checkP2 = settings.getString("flight_park2", null);
		String checkP3 = settings.getString("flight_park3", null);
		String checkP4 = settings.getString("flight_park4", null);
		String checkP5 = settings.getString("flight_park5", null);
		String checkP6 = settings.getString("flight_park6", null);
		String checkP7 = settings.getString("flight_park7", null);
		String checkP8 = settings.getString("flight_park8", null);
		String checkP9 = settings.getString("flight_park9", null);
		String checkP10 = settings.getString("flight_park10", null);
		String checkP11 = settings.getString("flight_park11", null);
		String checkP12 = settings.getString("flight_park12", null);
		String checkP13 = settings.getString("flight_park13", null);
		String checkP14 = settings.getString("flight_park14", null);
		String checkP15 = settings.getString("flight_park15", null);
		
		CheckBoxState state = new CheckBoxState();
		state.setCheckP1(checkP1);
		state.setCheckP2(checkP2);
		state.setCheckP3(checkP3);
		state.setCheckP4(checkP4);
		state.setCheckP5(checkP5);
		state.setCheckP6(checkP6);
		state.setCheckP7(checkP7);
		state.setCheckP8(checkP8);
		state.setCheckP9(checkP9);
		state.setCheckP10(checkP10);
		state.setCheckP11(checkP11);
		state.setCheckP12(checkP12);
		state.setCheckP13(checkP13);
		state.setCheckP14(checkP14);
		state.setCheckP15(checkP15);
		return state;
	}
	
	/**
	 * store User Info
	 * @param activity
	 * @param userInfo
	 */
	public static void storeUserInfo(Activity activity, UserInfo userInfo) {
		SharedPreferences settings = activity.getSharedPreferences(flight_setting_fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("user_code", userInfo.getUserCode());
		editor.putString("user_name", userInfo.getUserName());
		editor.putString("user_password", userInfo.getUserPassword());
		editor.commit();	
	}
	
	/**
	 * fetch User Info
	 * @param activity
	 * @return UserInfo
	 */
	public static UserInfo fetchUserInfo(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(flight_setting_fileName, Context.MODE_PRIVATE);
		String userCode = settings.getString("user_code", null);
		String userName = settings.getString("user_name", null);
		String userPassword = settings.getString("user_password", null);
		UserInfo userInfo = new UserInfo();
		userInfo.setUserCode(userCode);
		userInfo.setUserName(userName);
		userInfo.setUserPassword(userPassword);
		return userInfo;
	}
	
	/**
	 * store SearchCondition
	 * @param activity
	 * @param searchCondition
	 */
	public static void storeSearchCondition(Activity activity, SearchCondition searchCondition) {
		SharedPreferences settings = activity.getSharedPreferences(flight_setting_fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("data", searchCondition.getData());
		editor.putString("flight_num", searchCondition.getFightNum());
		editor.putString("plane", searchCondition.getPlane());
		editor.putString("sch_num", searchCondition.getSchNum());
		editor.putString("company", searchCondition.getCompany());
		editor.commit();	
	}
	
	/**
	 * fetch SearchCondition
	 * @param activity
	 * @return searchCondition
	 */
	public static SearchCondition fetchSearchCondition(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(flight_setting_fileName, Context.MODE_PRIVATE);
		String data = settings.getString("data", null);
		String plane = settings.getString("plane", null);
		String schNum = settings.getString("sch_num", null);
		String fightNum = settings.getString("flight_num", null);
		String company = settings.getString("company", null);
		
		SearchCondition searchCondition = new SearchCondition();
		searchCondition.setData(data);
		searchCondition.setFightNum(fightNum);
		searchCondition.setPlane(plane);
		searchCondition.setSchNum(schNum);
		searchCondition.setCompany(company);
		return searchCondition;
	}
	
	/**
	 * login or not
	 * @return true:has login, false:haven't login
	 */
	public static boolean isUserLogin(Activity activity) {
		UserInfo userInfo = SharedPreferencesUtil.fetchUserInfo(activity);
		if	(userInfo.getUserName() != null && userInfo.getUserCode() != null && userInfo.getUserPassword() != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * logout user
	 * @param activity
	 */
	public static void logoutUser(Activity activity) {
		SharedPreferencesUtil.storeUserInfo(activity, new UserInfo());
	}
	
	/**
	 * clear SharedPreferences file
	 * @param activity
	 */
	public static void clear(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(flight_setting_fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();  
        editor.commit(); 
	}
}
