package com.skyschedule.runway;

import java.util.Date;


//bean class to write the output of runway alloted
public class OutputFileForRunway {

	private String flightNumber;
	private Date dateOfArrival;
	private String runwayAvailability;


	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Date getDateOfArrival() {
		return dateOfArrival;
	}

	public void setDateOfArrival(Date dateOfArrival) {
		this.dateOfArrival = dateOfArrival;
	}

	public String getRunwayAvailability() {
		return runwayAvailability;
	}

	public void setRunwayAvailability(String runwayAvailability) {
		this.runwayAvailability = runwayAvailability;
	}

}
