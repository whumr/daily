package com.objectivasoftware.BCIA.model;

import java.util.ArrayList;
import java.util.List;

public class Parks extends Omc{

	private List<Park> parkList = new ArrayList<Park>();

	/**
	 * @return the parkList
	 */
	public List<Park> getParkList() {
		return parkList;
	}

	/**
	 * @param parkList the parkList to set
	 */
	public void addParkList(Park park) {
		this.parkList.add(park);
	}
}
