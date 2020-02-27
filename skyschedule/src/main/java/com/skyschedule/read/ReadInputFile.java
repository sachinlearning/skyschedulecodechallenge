package com.skyschedule.read;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReadInputFile {

   //function to get the excel workbook object
	public Workbook getWorkBook(String fileNameWithPath) {
		Path excelFileToRead = null;
		InputStream is = null;
		Workbook workbook = null;
		try {
			excelFileToRead = Paths.get(fileNameWithPath);
			is = Files.newInputStream(excelFileToRead);
			workbook = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

}
