package com.objectivasoftware.BCIA.model;

import java.util.ArrayList;
import java.util.List;

public class FlightDeicingSearch {
	private List<FlightDeicingInput> flightDeicingInputs=new ArrayList<FlightDeicingInput>();

	public List<FlightDeicingInput> getFlightDeicingInputs() {
		return flightDeicingInputs;
	}

	public void setFlightDeicingInputs(List<FlightDeicingInput> flightDeicingInputs) {
		this.flightDeicingInputs = flightDeicingInputs;
	}
	
	public void addFlightDeicingInput(FlightDeicingInput flightDeicingInput){
		this.flightDeicingInputs.add(flightDeicingInput);
	}

}
