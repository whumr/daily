package com.objectivasoftware.BCIA.model;

import java.util.ArrayList;
import java.util.List;

public class FlightDeicing extends Omc{
	private List<FlightDeicingInfo> flightDeicingInfos =  new ArrayList<FlightDeicingInfo>();

	/**
	 * @return the flightDeicingInfos
	 */
	public List<FlightDeicingInfo> getFlightDeicingInfos() {
		return flightDeicingInfos;
	}

	/**
	 * @param flightDeicingInfos the flightDeicingInfos to set
	 */
	public void setFlightDeicingInfos(List<FlightDeicingInfo> flightDeicingInfos) {
		this.flightDeicingInfos = flightDeicingInfos;
	}
	
	/**
	 * @param flightDeicingInfos the flightDeicingInfos to add
	 */
	public void addFlightDeicingInfos(FlightDeicingInfo flightDeicingInfo) {
		this.flightDeicingInfos.add(flightDeicingInfo);
	}
}
