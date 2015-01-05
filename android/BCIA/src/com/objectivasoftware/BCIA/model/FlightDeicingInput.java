package com.objectivasoftware.BCIA.model;

import java.util.ArrayList;
import java.util.List;


public class FlightDeicingInput extends Omc {
	private String FlightId;
	private String deicingSeq;
	private String deicStnd;
	private String dihdRDttm;
	private String dipbDttm;
	private String diinDttm;
	private String stdiDttm;
	private String eddiDttm;
	private String diegDttm;
	private String ditoDttm;
	private String lastUpdate;
	private List<FlightDeicingInputPitem> pitemsList = new ArrayList<FlightDeicingInputPitem>();
	/**
	 * @return the flightId
	 */
	public String getFlightId() {
		return FlightId;
	}
	/**
	 * @param flightId the flightId to set
	 */
	public void setFlightId(String flightId) {
		FlightId = flightId;
	}
	/**
	 * @return the deicingSeq
	 */
	public String getDeicingSeq() {
		return deicingSeq;
	}
	/**
	 * @param deicingSeq the deicingSeq to set
	 */
	public void setDeicingSeq(String deicingSeq) {
		this.deicingSeq = deicingSeq;
	}
	/**
	 * @return the deicStnd
	 */
	public String getDeicStnd() {
		return deicStnd;
	}
	/**
	 * @param deicStnd the deicStnd to set
	 */
	public void setDeicStnd(String deicStnd) {
		this.deicStnd = deicStnd;
	}
	/**
	 * @return the dihdRDttm
	 */
	public String getDihdRDttm() {
		return dihdRDttm;
	}
	/**
	 * @param dihdRDttm the dihdRDttm to set
	 */
	public void setDihdRDttm(String dihdRDttm) {
		this.dihdRDttm = dihdRDttm;
	}
	/**
	 * @return the dipbDttm
	 */
	public String getDipbDttm() {
		return dipbDttm;
	}
	/**
	 * @param dipbDttm the dipbDttm to set
	 */
	public void setDipbDttm(String dipbDttm) {
		this.dipbDttm = dipbDttm;
	}
	/**
	 * @return the diinDttm
	 */
	public String getDiinDttm() {
		return diinDttm;
	}
	/**
	 * @param diinDttm the diinDttm to set
	 */
	public void setDiinDttm(String diinDttm) {
		this.diinDttm = diinDttm;
	}
	/**
	 * @return the stdiDttm
	 */
	public String getStdiDttm() {
		return stdiDttm;
	}
	/**
	 * @param stdiDttm the stdiDttm to set
	 */
	public void setStdiDttm(String stdiDttm) {
		this.stdiDttm = stdiDttm;
	}
	/**
	 * @return the eddiDttm
	 */
	public String getEddiDttm() {
		return eddiDttm;
	}
	/**
	 * @param eddiDttm the eddiDttm to set
	 */
	public void setEddiDttm(String eddiDttm) {
		this.eddiDttm = eddiDttm;
	}
	/**
	 * @return the diegDttm
	 */
	public String getDiegDttm() {
		return diegDttm;
	}
	/**
	 * @param diegDttm the diegDttm to set
	 */
	public void setDiegDttm(String diegDttm) {
		this.diegDttm = diegDttm;
	}
	/**
	 * @return the ditoDttm
	 */
	public String getDitoDttm() {
		return ditoDttm;
	}
	/**
	 * @param ditoDttm the ditoDttm to set
	 */
	public void setDitoDttm(String ditoDttm) {
		this.ditoDttm = ditoDttm;
	}
	/**
	 * @return the lastUpdate
	 */
	public String getLastUpdate() {
		return lastUpdate;
	}
	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	/**
	 * @return the pitemsList
	 */
	public List<FlightDeicingInputPitem> getPitemsList() {
		return pitemsList;
	}
	/**
	 * @param pitemsList the pitemsList to set
	 */
	public void addPitemsList(FlightDeicingInputPitem pitem) {
		pitemsList.add(pitem);
	}
}
