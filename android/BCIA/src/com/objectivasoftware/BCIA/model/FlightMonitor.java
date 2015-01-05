package com.objectivasoftware.BCIA.model;

import java.util.ArrayList;
import java.util.List;

public class FlightMonitor extends Omc{
	private List<FlightInfo> flightInfos = new ArrayList<FlightInfo>();

	/**
	 * @return the flightInfos
	 */
	public List<FlightInfo> getFlightInfos() {
		return flightInfos;
	}

	/**
	 * @param flightInfos the flightInfos to set
	 */
	public void setFlightInfos(List<FlightInfo> flightInfos) {
		this.flightInfos = flightInfos;
	}
	
	/**
	 * @param flightInfos the flightInfos to set
	 */
	public void addFlightInfo(FlightInfo flightInfo) {
		this.flightInfos.add(flightInfo);
	}
}
