package com.skyschedule.read;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.skyschedule.runway.OutputFileForRunway;
import com.skyschedule.runway.TrackRunway;

public class FileAction {

	// to check if it is actually a file
	public String isValidPath(String path) {
		File file = new File(path);
		if (file.exists()) {
			if (file.isFile())
				return "OK";
			else if (file.isDirectory())
				return "It is a directory";
		}
		return "The file doesn't exist";
	}

	// to get the extension of the file
	public String getExtension(String fileName) {
		char ch;
		int len;
		if (fileName == null || (len = fileName.length()) == 0 || (ch = fileName.charAt(len - 1)) == '/' || ch == '\\'
				|| // in the case of a directory
				ch == '.') // in the case of . or ..
			return "";
		int dotInd = fileName.lastIndexOf('.'),
				sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (dotInd <= sepInd)
			return "";
		else
			return fileName.substring(dotInd + 1).toLowerCase();
	}

	// to validate the header
	public List<String> excelFileValidation(String fileName, int caseNo) {
		List<String> errorMessages = new ArrayList<>();
		List<String> headerForFileOne = new ArrayList<>();
		if (caseNo == 2) {
			headerForFileOne.add("Flight No.");
			headerForFileOne.add("Time of arrival");
		} else if (caseNo == 4) {
			headerForFileOne.add("S.No");
			headerForFileOne.add("Flight No");
		}
		try {
			errorMessages.addAll(validatingHeader(fileName, headerForFileOne));
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}
		return errorMessages;
	}

	//code to validate the headers
	public List<String> validatingHeader(String fileName, List<String> header)
			throws EncryptedDocumentException, IOException {
		List<String> errorMessage = new ArrayList<>();
		File file = new File(fileName);
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheetAt(0);
		Row headerRow = sheet.getRow(0);
		if (headerRow == null)
			errorMessage.add("No Header found in file " + file.getName());
		else {
			Iterator<Cell> cells = headerRow.cellIterator();
			List<String> cellValue = new ArrayList<String>();
			while (cells.hasNext()) {
				Cell cell = (Cell) cells.next();
				if (cell.getCellTypeEnum() == CellType.STRING)
					cellValue.add(cell.getStringCellValue());
			}
			if (cellValue.size() > 0) {
				List<String> foundHeader = new ArrayList<>();
				for (String head : header) {
					if (cellValue.contains(head))
						foundHeader.add(head);
				}
				header.removeAll(foundHeader);
			} else
				errorMessage.add("No Header found in file " + file.getName());
			if (header.size() > 0) {
				for (String head : header) {
					errorMessage.add("Column " + head + " not found in file " + file.getName());
				}
			}
		}
		wb.close();
		return errorMessage;

	}

	public String readExcelFile(int caseNo, TrackRunway trunway) {
		Scanner in = new Scanner(System.in);
		System.out.println("Hi Kindly upload the input excel file");
		String filePath = in.nextLine();

		// Activities to validate the file

		// validating whether it is a file or not
		String isValidFile = isValidPath(filePath);

		if (isValidFile.equalsIgnoreCase("OK")) {

			// checking whether it is an excel file or not
			String extensionOfFile = getExtension(filePath);

			if (extensionOfFile.equalsIgnoreCase("xls") || extensionOfFile.equalsIgnoreCase("xlsx")) {

				// validating header
				List<String> errors = excelFileValidation(filePath, caseNo);
				if (errors.size() > 0) {
					System.out.println(errors.toString());
				} else {

					String pathOfOutputFile=null;
					// reading the file
					if (caseNo == 2) {
						List<OutputFileForRunway> listOfFlight=trunway.readDataForRunway(filePath);
						if(listOfFlight.size()>0) {
							List<OutputFileForRunway> updatedListOfFlight=trunway.allotRunway(listOfFlight);
							pathOfOutputFile=trunway.writeOutputFile(updatedListOfFlight);
						}
						return "The outputfile has been generated at "+pathOfOutputFile;
					} else
						return "OOPs No data found";
				}
			} else
				return "It is not an excel file. Kindly upload an excel file";
		} else
			return isValidFile;
		return isValidFile;
	}
}
