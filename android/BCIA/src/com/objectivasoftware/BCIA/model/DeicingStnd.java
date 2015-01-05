package com.objectivasoftware.BCIA.model;

import java.util.ArrayList;
import java.util.List;

public class DeicingStnd extends Omc{
	private List<FlightDeicingInputPitem> flightDeicingInputPitems = new ArrayList<FlightDeicingInputPitem>();

	public List<FlightDeicingInputPitem> getFlightDeicingInputPitems() {
		return flightDeicingInputPitems;
	}

	public void addFlightDeicingInputPitem(FlightDeicingInputPitem flightDeicingInputPitem) {
		this.flightDeicingInputPitems.add(flightDeicingInputPitem);
	}
}
