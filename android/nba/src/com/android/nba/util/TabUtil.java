package com.android.nba.util;

import java.text.ParseException;

import android.widget.RelativeLayout;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * 包含对tab进行操作的一些方法
 * @author lming
 *
 */
public class TabUtil {

	/**
	 * 修改tab的显示文字
	 * @param tabWidget
	 * @param index
	 * @param label
	 */
	public static void updateTabLabel(TabWidget tabWidget, int index, String label) {
		RelativeLayout rl = (RelativeLayout)(tabWidget.getChildAt(index));
		((TextView)(rl.getChildAt(1))).setText(label);
	}
	
	/**
	 * 修改tab的显示文字
	 * @param tabWidget
	 * @param index
	 * @param label
	 * @throws ParseException 
	 */
	public static void updateTabLabelExt(TabWidget tabWidget, int index, String label) throws ParseException {
		RelativeLayout rl = (RelativeLayout)(tabWidget.getChildAt(index));
		((TextView)(rl.getChildAt(1))).setText(label.substring(4,6) + "月" + label.substring(6,8) + "日");
		//弃用dateformat
//		((TextView)(rl.getChildAt(1))).setText(
//				System_DateFormat.DEFAULT_DISPLAY_DATEFORMAT.format(
//						System_DateFormat.URL_DATEFORMAT.parse(label)
//				));
	}
	
	/**
	 * 禁用tab
	 * @param tabWidget
	 * @param index
	 */
	public static void disableTab(TabWidget tabWidget, int index) {
		ableTab(tabWidget, index, false);
	}
	
	/**
	 * 禁用所有tab
	 * @param tabWidget
	 * @param index
	 */
	public static void disableAllTab(TabWidget tabWidget) {
		for(int i = 0; i < tabWidget.getChildCount(); i ++)
			disableTab(tabWidget, i);
	}
	
	/**
	 * 启用tab
	 * @param tabWidget
	 * @param index
	 */
	public static void enableTab(TabWidget tabWidget, int index) {
		ableTab(tabWidget, index, true);
	}
	
	/**
	 * 启用所有tab
	 * @param tabWidget
	 * @param index
	 */
	public static void enableAllTab(TabWidget tabWidget) {
		for(int i = 0; i < tabWidget.getChildCount(); i ++)
			enableTab(tabWidget, i);
	}
	
	/**
	 * @param tabWidget
	 * @param index
	 * @param flag
	 */
	public static void ableTab(TabWidget tabWidget, int index, boolean flag) {
		RelativeLayout rl = (RelativeLayout)(tabWidget.getChildAt(index));
		rl.setEnabled(flag);
	}
}