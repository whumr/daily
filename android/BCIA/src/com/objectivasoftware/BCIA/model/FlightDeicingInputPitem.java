package com.objectivasoftware.BCIA.model;

import java.util.ArrayList;
import java.util.List;

public class FlightDeicingInputPitem {

	private String pcode;
	private List<String> stndList = new ArrayList<String>();

	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	/**
	 * @return the stndList
	 */
	public List<String> getStndList() {
		return stndList;
	}
	/**
	 * @param stndList the stndList to set
	 */
	public void addStndList(String stnd) {
		this.stndList.add(stnd);
	}
}
