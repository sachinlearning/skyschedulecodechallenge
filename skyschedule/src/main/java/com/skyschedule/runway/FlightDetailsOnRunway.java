package com.skyschedule.runway;

import java.util.Date;

//bean class for tracking file details on runway
public class FlightDetailsOnRunway {

	private String flightNumber;
	private Date arrivalTime;

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

}
