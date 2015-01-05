package com.objectivasoftware.BCIA.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.objectivasoftware.BCIA.model.CheckBoxState;
import com.objectivasoftware.BCIA.model.FlightDeicingInput;
import com.objectivasoftware.BCIA.model.SearchCondition;

public class XmlUtil {

	private static String getBaseXML() {
		return  "<meta>" + "<userCode><![CDATA[tamcc]]></userCode>" +  "<userPassword><![CDATA[678fdc0394af78204fdccfcecb621785]]></userPassword>"
				+ "<device>" 
				+ "<ip>" 
				+ DeviceInfo.getDeviceIP()
				+ "</ip>"
				+ "<mac>"
				+ DeviceInfo.getDeviceMac()
				+ "</mac>"
				+ "<osVersion>"
				+ DeviceInfo.getDeviceVersion()
				+ "</osVersion>"
				+ "<udid>" 
				+ DeviceInfo.getDeviceUdid()
				+ "</udid>" + "</device>" + "</meta>";
	}


	public static String getLoginXml (String userName, String password) throws NoSuchAlgorithmException {
		String xmldata = "<omc>" 
				+ getBaseXML() 
				+ "<content>" + "<!-- 业务交互数据 -->" + "<user>"
				+ "<userCode><![CDATA[" + userName + "]]></userCode>"
				+ "<userPassword><![CDATA[" + MD5.getMD5(password) + "]]></userPassword>"
				+ "</user>" + "</content>" + "</omc>";
		return xmldata;
	}
	
	
	public static String getSerchViewXml(SearchCondition searchCondition) {

		String xmlData = "<omc>"
		+ getBaseXML() 
		+ "<content>"
		+ "<filters>" + "<baseFilters>" 
		+ "<filter connector=\"and\" name=\"operation_date\" operation=\"eq\">"
		+ getCurrentData(searchCondition)
		+ "</filter>"+"<filter connector=\"and\" name=\"reg_code\" operation=\"in\">"
		+ "<item>"+(searchCondition.getFightNum() == null? "" : searchCondition.getFightNum())
		+ "</item>"+"</filter>"
		+ "<filter connector=\"and\" name=\"actype_code\" operation=\"like\">"
		+ (searchCondition.getPlane() == null? "" : searchCondition.getPlane())
		+ "</filter>"
		+ "<filter connector=\"and\" name=\"flgt_no\" operation=\"like\">"
		+ (searchCondition.getSchNum() == null? "" : searchCondition.getSchNum())
		+ "</filter>"
		+ "<filter connector=\"and\" name=\"airline_code\" operation=\"like\">"
		+ (searchCondition.getCompany() == null? "" : searchCondition.getCompany())
		+ "</filter>"
		+ "</baseFilters>" + "<pagesFilters>"
		+ "<filter name=\"startPage\">" + 1 +"</filter>"
		+ "<filter name=\"pageSize\">"+ 1000 +"</filter>"
		+ "</pagesFilters>" + "<orderFilters>"
		+"<filter name=\"sch_dttm\" operation=\"ASC\"/>"
		+ "<filter name=\"airline_code\" operation=\"DESC\"/>"
		+ "</orderFilters>" + "</filters>" + "</content>" + "</omc>";
		return xmlData;
	}
	
	private static String getCurrentData(SearchCondition searchCondition) {
		String data;
		if (searchCondition.getData() == null) {
			data = (DataUtil.getTime ().toString()).replace("-", "");
		} else {
			data =  (searchCondition.getData().toString()).replace("-", "");
		}
		return data;
	}


	public static String getDeicingInfoXML(List<String> mList) {
		String xmlData = "<omc>"
				+ getBaseXML()
				+ "<content>" + "<!-- 业务交互数据 -->" + "<filters>"
				+ "<baseFilters>"+"<filter name="+"\"flgt_id\""+ " operation=\"in\">"
				+ getItem(mList)
				+ "</filter>"+"</baseFilters>" + "</filters>"+"</content>" + "</omc>";
		return xmlData;
	}
	
	public static String getInputDataXML(String flightId, String searchNum) {
		String xmlData = "<omc>"
					+ getBaseXML()
					+ "<content>" + "<!-- 业务交互数据 -->" 
					+ "<filters>"
					+ "<baseFilters>" 
					+ "<filter connector=\"and \" name=" + "\"flgt_id\""
					+ " operation=" + "\"eq\">" 
					+ flightId+"</filter>" 
					+ "<filter connector=\"and \" name=" + "\"deicing_seq\""
					+ " operation=" + "\"eq\">" 
					+ searchNum 
					+ "</filter>" 
					+ "</baseFilters>"
					+ "</filters>" + "</content>" + "</omc>";
		return xmlData;
	}
	
	public static String getServerTimeXML() {
		
		String xmlData = "<omc>"
				+ getBaseXML()+"<content><servernow/></content></omc>";
		
		return xmlData;
	}
	
	public static String inputDataXML(FlightDeicingInput flightDeicingInput, String flightId, String currentDei) throws ParseException {
		
		String xmlData = "<omc>"
				+ getBaseXML()
				+ "<content><DMAN><DGRP>24</DGRP><FLTR><FLID>"+flightId 
				+ "</FLID><DESN>"
				+ currentDei + "</DESN><DEIS>"
				+ flightDeicingInput.getDeicStnd() + "</DEIS><DIHD>" 
				+ flightDeicingInput.getDihdRDttm() + "</DIHD><DIPB>"
				+ flightDeicingInput.getDipbDttm() + "</DIPB><DIIN>"
				+ flightDeicingInput.getDiinDttm() + "</DIIN><STDI>"
				+ flightDeicingInput.getStdiDttm() + "</STDI><EDDI>"
				+ flightDeicingInput.getEddiDttm() + "</EDDI><DIEG>"
				+ flightDeicingInput.getDiegDttm() + "</DIEG><DITO>"
				+ flightDeicingInput.getDitoDttm() + "</DITO><LUDT>"
				+ flightDeicingInput.getLastUpdate() + "</LUDT></FLTR></DMAN></content></omc>";
		return xmlData;
	}
	
	public static String getParkstateXml(CheckBoxState checkBoxState) {
		
		ArrayList<String> mList = new ArrayList<String>();
		if (checkBoxState.getCheckP1() != null) {
			mList.add("P1");
		}
		if (checkBoxState.getCheckP2() != null) {
			mList.add("P2");
		}
		if (checkBoxState.getCheckP3() != null) {
			mList.add("P3");
		}
		if (checkBoxState.getCheckP4() != null) {
			mList.add("P4");
		}
		if (checkBoxState.getCheckP5() != null) {
			mList.add("P5");
		}
		if (checkBoxState.getCheckP6() != null) {
			mList.add("P6");
		}
		if (checkBoxState.getCheckP7() != null) {
			mList.add("P7");
		}
		if (checkBoxState.getCheckP8() != null) {
			mList.add("P8");
		}
		if (checkBoxState.getCheckP9() != null) {
			mList.add("P9");
		}
		if (checkBoxState.getCheckP10() != null) {
			mList.add("P10");
		}
		if (checkBoxState.getCheckP11() != null) {
			mList.add("P11");
		}
		if (checkBoxState.getCheckP12() != null) {
			mList.add("P12");
		}
		if (checkBoxState.getCheckP13() != null) {
			mList.add("P13");
		}
		if (checkBoxState.getCheckP14() != null) {
			mList.add("P14");
		}
		if (checkBoxState.getCheckP15() != null) {
			mList.add("P15");
		}
		
		String xmlData = "<omc>"
				+ getBaseXML()
				+ "<content><filters><baseFilters><filter name=\"deicingpark_code\" operation=\"in\">"
				+ getItem(mList)
				+"</filter></baseFilters></filters>"
				+"</content></omc>";
		return xmlData;
	}
	
	public static String getConfDataXML() {
		String xmlData = "<omc>"
				+ getBaseXML()
				+ "<content><udid>" 
				+ DeviceInfo.getDeviceUdid()
				+"</udid> </content></omc>";
		return xmlData;
	}
	
	private static String getItem(List<String> mList) {
		
		String itemData = "";
		for(String item : mList) {
			itemData += "<item>"+ item + "</item>";
		}
		return itemData;
	}

	
	public static String inputStreamToString(InputStream is) {
      String line = "";
      StringBuilder total = new StringBuilder();
      
      // Wrap a BufferedReader around the InputStream
      BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

      // Read response until the end
      try {
			while ((line = rd.readLine()) != null) { 
			    total.append(line); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
      // Return full string
      return total.toString();
  }

}
