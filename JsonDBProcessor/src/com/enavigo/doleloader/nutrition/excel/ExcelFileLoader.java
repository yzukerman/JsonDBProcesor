/**
 * 
 */
package com.enavigo.doleloader.nutrition.excel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author yuvalzukerman
 *
 */
public class ExcelFileLoader {
	
	public int processExcelFiles(String folder, Connection connection)
	{
		int count = 0;
		try
    	{
    		// Open directory and iterate over files
			File fileFolder = new File(folder);
			String[] fileNames = fileFolder.list();
			for(String curFile : fileNames)
			{
				System.out.println("File: " + curFile);
//				List<HashMap<String, Object>> nutrients = processFile(curFile);
//				persistNutrients(nutrients, connection);
				count++;
			}
    	}
    	catch (Exception e)
    	{
    		System.out.println("Exception: " + e.getMessage());
    		e.printStackTrace();
    	}
		
		
		return count;
	}
	
	private List<HashMap<String, Object>> processFile(String filename) throws InvalidFormatException, IOException
	{
		List<HashMap<String, Object>> nutrients = new ArrayList<HashMap<String, Object>> ();
		HashMap<String, Object> topNutrients = new HashMap<String, Object>();
		HashMap<String, Object> bottomNutrients = new HashMap<String, Object>();
		
		
		OPCPackage pkg = OPCPackage.open(new File(filename));
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		XSSFSheet sheet = wb.getSheetAt(0);
		System.out.println(sheet.getSheetName());
		Row row = sheet.getRow(0);
		Cell cell = row.getCell(0);
		
		System.out.println(cell.getStringCellValue());
		topNutrients.put("recipeTitle", cell.getStringCellValue());
		// serving size
		cell = sheet.getRow(3).getCell(0);
		
		
		nutrients.add(topNutrients);
		nutrients.add(bottomNutrients);
		return nutrients;
	}
	
	private void persistNutrients(List<HashMap<String, Object>> nutrients, Connection connection)
	{
		
	}

	public static void main(String[] args)
	{
		ExcelFileLoader efl = new ExcelFileLoader();
		//efl.processExcelFiles("/Users/yuvalzukerman/Dropbox/Enavigo/Clients/Dole/Nutrition/nutritionLabels", null);
		try
		{
			efl.processFile("/Users/yuvalzukerman/Dropbox/Enavigo/Clients/Dole/Nutrition/nutritionLabels/Baker Beach Salad.xlsx");
		}
		catch(InvalidFormatException ife)
		{
			System.out.println("IFE: " + ife.getLocalizedMessage());
			ife.printStackTrace();
		}
		catch(IOException ioe)
		{
			System.out.println("IOE: " + ioe.getLocalizedMessage());
			ioe.printStackTrace();
		}
	}
}
