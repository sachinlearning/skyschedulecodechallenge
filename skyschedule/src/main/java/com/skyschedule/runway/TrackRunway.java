package com.skyschedule.runway;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.skyschedule.runway.OutputFileForRunway;
import com.skyschedule.read.ReadInputFile;

//class to track the runway
public class TrackRunway {

	private Map<Integer, FlightDetailsOnRunway> flightOnRunways = new HashMap<Integer, FlightDetailsOnRunway>();

	private Set<Date> setForRunway1 = new HashSet<Date>();
	private Set<Date> setForRunway2 = new HashSet<Date>();
	private Set<Date> setForRunway3 = new HashSet<Date>();
	private Set<Date> setForRunway4 = new HashSet<Date>();
	private Set<Date> setForRunway5 = new HashSet<Date>();

	//function to check the availability of runways
	public String checkAvailableRunway() {

		if (setForRunway1.size() == 0 && setForRunway2.size() == 0 && setForRunway3.size() == 0
				&& setForRunway4.size() == 0 && setForRunway5.size() == 0)
			return "Free Runways: 1,2,3,4,5";

		String freeRunways = "Free Runways: ";
		if (setForRunway1.size() == 0)
			freeRunways += 1 + ", ";
		else {
			for (Date getExistingDate : setForRunway1) {
				long differenceInTime = Math.abs((getExistingDate.getTime()) - new Date().getTime());
				long diff = TimeUnit.MINUTES.convert(differenceInTime, TimeUnit.MILLISECONDS);
				System.out.println(diff);
				if (diff > 59) {
					freeRunways += 1 + ", ";
					break;
				}
			}
		}

		if (setForRunway2.size() == 0)
			freeRunways += 2 + ", ";
		else {
			for (Date getExistingDate : setForRunway2) {
				long differenceInTime = Math.abs((getExistingDate.getTime()) - new Date().getTime());
				long diff = TimeUnit.MINUTES.convert(differenceInTime, TimeUnit.MILLISECONDS);
				if (diff > 59) {
					freeRunways += 2 + ", ";
					break;
				}
			}
		}

		if (setForRunway3.size() == 0)
			freeRunways += 3 + ", ";
		else {
			for (Date getExistingDate : setForRunway3) {
				long differenceInTime = Math.abs((getExistingDate.getTime()) - new Date().getTime());
				long diff = TimeUnit.MINUTES.convert(differenceInTime, TimeUnit.MILLISECONDS);
				if (diff > 59) {
					freeRunways += 3 + ", ";
					break;
				}
			}
		}

		if (setForRunway4.size() == 0)
			freeRunways += 4 + ", ";
		else {
			for (Date getExistingDate : setForRunway4) {
				long differenceInTime = Math.abs((getExistingDate.getTime()) - new Date().getTime());
				long diff = TimeUnit.MINUTES.convert(differenceInTime, TimeUnit.MILLISECONDS);
				if (diff > 59) {
					freeRunways += 4 + ", ";
					break;
				}
			}
		}

		if (setForRunway5.size() == 0)
			freeRunways += 5 + ", ";
		else {
			for (Date getExistingDate : setForRunway5) {
				long differenceInTime = Math.abs((getExistingDate.getTime()) - new Date().getTime());
				long diff = TimeUnit.MINUTES.convert(differenceInTime, TimeUnit.MILLISECONDS);
				if (diff > 59) {
					freeRunways += 5 + ", ";
					break;
				}
			}
		}
		return freeRunways;

	}

	//function to allot the runways to the flights
	public List<OutputFileForRunway> allotRunway(List<OutputFileForRunway> listOfFlight) {

		List<OutputFileForRunway> updatedListOfFlight = new ArrayList<OutputFileForRunway>();

		for (OutputFileForRunway opfr : listOfFlight) {
			Date flightArrivalTime = opfr.getDateOfArrival();
			int runwayAssigned = 0;
			if (flightArrivalTime.compareTo(new Date()) > 0) {
				FlightDetailsOnRunway flightDetails = null;
				for (Entry<Integer, FlightDetailsOnRunway> entry : flightOnRunways.entrySet()) {
					int runwayNumber = entry.getKey();
					runwayAssigned = runwayNumber;
					flightDetails = entry.getValue();
					if (flightDetails == null) {
						flightDetails = new FlightDetailsOnRunway();
						flightDetails.setFlightNumber(opfr.getFlightNumber());
						flightDetails.setArrivalTime(flightArrivalTime);
						opfr.setRunwayAvailability("Flight will land on runway " + runwayNumber);
						add(runwayAssigned, flightDetails);
						break;
					} else {
						Set<Date> setToCompare = new HashSet<Date>();
						Date existingTime = flightDetails.getArrivalTime();
						Date currentFlightTime = opfr.getDateOfArrival();
						int timeCompare = existingTime.compareTo(currentFlightTime);
						if (runwayAssigned == 1)
							setToCompare.addAll(setForRunway1);
						else if (runwayAssigned == 2)
							setToCompare.addAll(setForRunway2);
						else if (runwayAssigned == 3)
							setToCompare.addAll(setForRunway3);
						else if (runwayAssigned == 4)
							setToCompare.addAll(setForRunway4);
						else if (runwayAssigned == 5)
							setToCompare.addAll(setForRunway5);

						int runwaycounter = 0;
						if (setToCompare.size() > 0) {
							for (Date getExistingDate : setToCompare) {
								long differenceInTime = Math
										.abs(currentFlightTime.getTime() - (getExistingDate.getTime()));
								long diff = TimeUnit.HOURS.convert(differenceInTime, TimeUnit.MILLISECONDS);
								if (diff < 1) {
									runwaycounter = 1;
								}
							}
						}
						if (runwaycounter == 0) {
							if (timeCompare < 0) {
								long differenceInTime = Math
										.abs(currentFlightTime.getTime() - (existingTime.getTime()));
								long diff = TimeUnit.HOURS.convert(differenceInTime, TimeUnit.MILLISECONDS);
								if (diff >= 1) {
									flightDetails.setFlightNumber(opfr.getFlightNumber());
									flightDetails.setArrivalTime(flightArrivalTime);
									opfr.setRunwayAvailability("Flight will land on runway " + runwayNumber);
									add(runwayAssigned, flightDetails);
									break;
								}
							} else if (timeCompare > 0) {
								long differenceInTime = Math
										.abs(existingTime.getTime() - (currentFlightTime.getTime()));
								long diff = TimeUnit.HOURS.convert(differenceInTime, TimeUnit.MILLISECONDS);
								if (diff >= 1) {
									flightDetails.setFlightNumber(opfr.getFlightNumber());
									flightDetails.setArrivalTime(flightArrivalTime);
									opfr.setRunwayAvailability("Flight will land on runway " + runwayNumber);
									add(runwayAssigned, flightDetails);
									break;
								}
							}
						}

					}
				}
				if (opfr.getRunwayAvailability() == null)
					opfr.setRunwayAvailability("No runway available. Please wait at certain altitude until runway is available");
				flightOnRunways.put(runwayAssigned, flightDetails);
			} else
				opfr.setRunwayAvailability("The flight has already landed");
			updatedListOfFlight.add(opfr);
		}

		return updatedListOfFlight;

	}

	//function for initializing the runways 
	public void init() {
		flightOnRunways.put(1, null);
		flightOnRunways.put(2, null);
		flightOnRunways.put(3, null);
		flightOnRunways.put(4, null);
		flightOnRunways.put(5, null);

	}

	//function for reading input data for alloting runways
	public List<OutputFileForRunway> readDataForRunway(String fileNameWithPath) {
		List<OutputFileForRunway> listOfFlight = new LinkedList<OutputFileForRunway>();
		Iterator<Row> iterator = null;
		Workbook workbook = null;
		Sheet firstSheet = null;
		int noOfRows = 0;
		try {
			workbook = new ReadInputFile().getWorkBook(fileNameWithPath);
			firstSheet = workbook.getSheetAt(0);
			iterator = firstSheet.iterator();
			while (iterator.hasNext()) {
				Row _nextRow = iterator.next();
				Iterator<Cell> cellIterator = _nextRow.cellIterator();
				if (noOfRows > 0) {
					OutputFileForRunway opfr = new OutputFileForRunway();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						switch (cell.getCellTypeEnum()) {
						case STRING:
							String key = cell.getStringCellValue();
							opfr.setFlightNumber(key);
							break;
						case NUMERIC:
							Date arrivalTime = cell.getDateCellValue();
							opfr.setDateOfArrival(arrivalTime);
							break;
						default:
							break;
						}
					}
					listOfFlight.add(opfr);
				}
				noOfRows++;
			}
			if (workbook != null)
				workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (workbook != null)
					workbook.close();
			} catch (Exception ie) {
				ie.printStackTrace();
			}
		}
		return listOfFlight;
	}

	//function for writing output 
	public String writeOutputFile(List<OutputFileForRunway> updatedListOfFlight) {
		String filePath = null;
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			Row row = sheet.createRow(0);
			Cell header1 = row.createCell(0);
			header1.setCellValue("Flight No.");
			Cell header2 = row.createCell(1);
			header2.setCellValue("Time of arrival");
			Cell header3 = row.createCell(2);
			header3.setCellValue("Runway Alloted");
			int rowNo = 1;
			for (OutputFileForRunway opf : updatedListOfFlight) {
				row = sheet.createRow(rowNo++);
				Cell flightNumber = row.createCell(0);
				flightNumber.setCellValue(opf.getFlightNumber());

				Cell timeOfArrival = row.createCell(1);
				CreationHelper creationHelper = workbook.getCreationHelper();
				CellStyle style2 = workbook.createCellStyle();
				style2.setDataFormat(creationHelper.createDataFormat().getFormat("dd-mm-yyyy hh:mm:ss"));
				timeOfArrival.setCellValue(opf.getDateOfArrival());
				timeOfArrival.setCellStyle(style2);

				Cell runwayAvailability = row.createCell(2);
				runwayAvailability.setCellValue(opf.getRunwayAvailability());
			}
			File file = new File("runwayallotedtoflights" + System.currentTimeMillis() + ".xlsx");
			filePath = file.getAbsolutePath();
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			workbook.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}

	
	public void add(int number, FlightDetailsOnRunway flightDetails) {
		if (number == 1)
			setForRunway1.add(flightDetails.getArrivalTime());
		else if (number == 2)
			setForRunway2.add(flightDetails.getArrivalTime());
		else if (number == 3)
			setForRunway3.add(flightDetails.getArrivalTime());
		else if (number == 4)
			setForRunway4.add(flightDetails.getArrivalTime());
		else if (number == 5)
			setForRunway5.add(flightDetails.getArrivalTime());
	}

	public String writingAllotedRunWayData() {
		String filePath = null;
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			Row row = sheet.createRow(0);
			Cell runway1 = row.createCell(0);
			runway1.setCellValue("Runway 1");
			if (setForRunway1.size() == 0)
			{
				Cell runwayStatus = row.createCell(1);
				runwayStatus.setCellValue("No Flights Alloted");
			}
			else {
				int cellNo=1;
				for (Date getExistingDate : setForRunway1) {
					Cell timeOfArrival = row.createCell(cellNo++);
					CreationHelper creationHelper = workbook.getCreationHelper();
					CellStyle style2 = workbook.createCellStyle();
					style2.setDataFormat(creationHelper.createDataFormat().getFormat("dd-mm-yyyy hh:mm:ss"));
					timeOfArrival.setCellValue(getExistingDate);
					timeOfArrival.setCellStyle(style2);
				}
				
			}
			Row row1 = sheet.createRow(1);
			Cell runway2 = row1.createCell(0);
			runway2.setCellValue("Runway 2");
			if (setForRunway2.size() == 0)
			{
				Cell runwayStatus = row1.createCell(1);
				runwayStatus.setCellValue("No Flights Alloted");
			}
			else {
				int cellNo=1;
				for (Date getExistingDate : setForRunway2) {
					Cell timeOfArrival = row1.createCell(cellNo++);
					CreationHelper creationHelper = workbook.getCreationHelper();
					CellStyle style2 = workbook.createCellStyle();
					style2.setDataFormat(creationHelper.createDataFormat().getFormat("dd-mm-yyyy hh:mm:ss"));
					timeOfArrival.setCellValue(getExistingDate);
					timeOfArrival.setCellStyle(style2);
				}
				
			}
			Row row2 = sheet.createRow(2);
			Cell runway3 = row2.createCell(0);
			runway3.setCellValue("Runway 3");
			if (setForRunway3.size() == 0)
			{
				Cell runwayStatus = row2.createCell(1);
				runwayStatus.setCellValue("No Flights Alloted");
			}
			else {
				int cellNo=1;
				for (Date getExistingDate : setForRunway3) {
					Cell timeOfArrival = row2.createCell(cellNo++);
					CreationHelper creationHelper = workbook.getCreationHelper();
					CellStyle style2 = workbook.createCellStyle();
					style2.setDataFormat(creationHelper.createDataFormat().getFormat("dd-mm-yyyy hh:mm:ss"));
					timeOfArrival.setCellValue(getExistingDate);
					timeOfArrival.setCellStyle(style2);
				}
				
			}
			Row row3 = sheet.createRow(3);
			Cell runway4 = row3.createCell(0);
			runway4.setCellValue("Runway 4");
			if (setForRunway4.size() == 0)
			{
				Cell runwayStatus = row3.createCell(1);
				runwayStatus.setCellValue("No Flights Alloted");
			}
			else {
				int cellNo=1;
				for (Date getExistingDate : setForRunway4) {
					Cell timeOfArrival = row3.createCell(cellNo++);
					CreationHelper creationHelper = workbook.getCreationHelper();
					CellStyle style2 = workbook.createCellStyle();
					style2.setDataFormat(creationHelper.createDataFormat().getFormat("dd-mm-yyyy hh:mm:ss"));
					timeOfArrival.setCellValue(getExistingDate);
					timeOfArrival.setCellStyle(style2);
				}
				
			}
			Row row4 = sheet.createRow(4);
			Cell runway5 = row4.createCell(0);
			runway5.setCellValue("Runway 5");
			if (setForRunway5.size() == 0)
			{
				Cell runwayStatus = row4.createCell(1);
				runwayStatus.setCellValue("No Flights Alloted");
			}
			else {
				int cellNo=1;
				for (Date getExistingDate : setForRunway5) {
					Cell timeOfArrival = row4.createCell(cellNo++);
					CreationHelper creationHelper = workbook.getCreationHelper();
					CellStyle style2 = workbook.createCellStyle();
					style2.setDataFormat(creationHelper.createDataFormat().getFormat("dd-mm-yyyy hh:mm:ss"));
					timeOfArrival.setCellValue(getExistingDate);
					timeOfArrival.setCellStyle(style2);
				}
				
			}

			File file = new File("runwayalloted" + System.currentTimeMillis() + ".xlsx");
			filePath = file.getAbsolutePath();
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			workbook.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;

	}
}
