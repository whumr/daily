package com.objectivasoftware.BCIA.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
	public static String getTime () {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = format.format(new Date());
		return currentTime;
	}
	
	public static String getSSTime () {
		SimpleDateFormat format = new SimpleDateFormat("ss");
		String currentTime = format.format(new Date());
		return currentTime;
	}
	
	public static String getHHmmTime (String time) throws ParseException {
		if (time == null) {
			return "";
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			SimpleDateFormat sfFormat = new SimpleDateFormat("HH:mm");
			Date date = format.parse(time);
			return sfFormat.format(date);
		}
		
	}
	
	public static String getTotalTime (String time) throws ParseException {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = format.parse(time);
			return format.format(date);
	}
	
	public static String getyyyMMddTime (String time) throws ParseException {
		if (time == null) {
			return "";
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(time);
			String currentTime = format.format(date);
			return currentTime;		
		}
	}
	
	public static String getCurrentHH (String time) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sfFormat = new SimpleDateFormat("HH");
		Date date = format.parse(time);
		return sfFormat.format(date);
	}
	
	public static String getCurrentmm (String time) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sfFormat = new SimpleDateFormat("mm");
		Date date = format.parse(time);
		return sfFormat.format(date);
	}
	
	
	
	public static String getCurrentDate () {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = format.format(new Date());
		return currentTime;
	}
	
}
