/**
 * 
 */
package com.enavigo.doleloader.nutrition.excel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.enavigo.doleloader.persistence.DoleJsonPersistenceUtils;

/**
 * @author yuvalzukerman
 *
 */
public class ExcelFileLoader {
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	
	public int processExcelFiles(String folder, Connection connection) throws SQLException, InvalidFormatException, IOException
	{
		int count = 0;
		try
    	{
    		// Open directory and iterate over files
			File fileFolder = new File(folder);
			String[] fileNames = fileFolder.list();
			int nextNutrientId = DoleJsonPersistenceUtils.getMaxId(connection, "recipe_nutrient", "recipe_nutrient_id") +1;
			
			for(String curFile : fileNames)
			{
				System.out.println("Processing File: " + curFile);
				List<HashMap<String, Object>> nutrients = processFile(folder+curFile);
				if(nutrients != null)
					nextNutrientId = DoleJsonPersistenceUtils.presistExcelRecipeNutrients(connection, nutrients, nextNutrientId);
				else
				{
					System.out.println("Bad source file: " + curFile);
					logger.warning("Bad source file: " + curFile);
				}
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
		Row row = sheet.getRow(0);
		Cell cell = row.getCell(0);
		int offset = 0;
		
		// sanity check - cell A23 needs to be blank
		if(sheet.getRow(22) != null)
		{
			wb.close();
			return null;
		}
		
		topNutrients.put("recipeTitle", cell.getStringCellValue());
		
		// serving size
		cell = sheet.getRow(3).getCell(0);
		String value = cell.getStringCellValue().replaceAll("\\D","");
		topNutrients.put("serving_size", value + "g");

		// calories
		cell = sheet.getRow(7).getCell(1);
		double dvalue = cell.getNumericCellValue();
		topNutrients.put("calories", (int)dvalue);
		
		// calories from fat
		cell = sheet.getRow(8).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("calories_from_fat", (int)dvalue);
		
		// total fat
		cell = sheet.getRow(10).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("total_fat", (int)dvalue);

		// total fat percentage
		cell = sheet.getRow(10).getCell(3);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("total_fat_percentage", (int)dvalue);

		// saturated fat
		cell = sheet.getRow(11).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("saturated_fat", dvalue);

		cell = sheet.getRow(11).getCell(3);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("saturated_fat_percentage", (int)dvalue);
		
		// trans fat
		cell = sheet.getRow(12).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("trans_fat", (int)dvalue);

		// polyunsaturated fat DOUBLE
		cell = sheet.getRow(13).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("poly_fat", dvalue);

		// mono fat
		cell = sheet.getRow(14).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("mono_fat", dvalue);
	
		
		// cholesterol
		cell = sheet.getRow(15).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("cholesterol", (int)dvalue);

		cell = sheet.getRow(15).getCell(3);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("cholesterol_percentage", (int)dvalue);

		// sodium
		cell = sheet.getRow(16).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("sodium", (int)dvalue);

		cell = sheet.getRow(16).getCell(3);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("sodium_percentage", (int)dvalue);
		
		// potassium
		cell = sheet.getRow(17).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("potassium", (int)dvalue);

		cell = sheet.getRow(17).getCell(3);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("potassium_percentage", (int)dvalue);
		
		// carbs
		cell = sheet.getRow(18).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("carbs", (int)dvalue);

		cell = sheet.getRow(18).getCell(3);
		if(cell != null)
		{	
			dvalue = cell.getNumericCellValue();
			topNutrients.put("carbs_percentage", (int)dvalue);
		}

		// fiber
		cell = sheet.getRow(19).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("fiber", (int)dvalue);

		cell = sheet.getRow(19).getCell(3);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("fiber_percentage", (int)dvalue);

		// sugars
		cell = sheet.getRow(20).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("sugars", (int)dvalue);

		// protein
		cell = sheet.getRow(21).getCell(1);
		dvalue = cell.getNumericCellValue();
		topNutrients.put("protein", (int)dvalue);

		String nutName = null;
		// variable nutrients
		for(int x = 23; sheet.getRow(x) != null; x++)
		{
			nutName = sheet.getRow(x).getCell(0).getStringCellValue();
			dvalue = sheet.getRow(x).getCell(3).getNumericCellValue();
			bottomNutrients.put(nutName, (int)dvalue);
		}
		
		wb.close();
		nutrients.add(topNutrients);
		nutrients.add(bottomNutrients);
		return nutrients;
	}
	

	public static void main(String[] args)
	{
		ExcelFileLoader efl = new ExcelFileLoader();
		Connection connection = null;
		//efl.processExcelFiles("/Users/yuvalzukerman/Dropbox/Enavigo/Clients/Dole/Nutrition/nutritionLabels", null);
		try
		{
			connection = DriverManager.getConnection("jdbc:mysql://localhost/dole_db?" +
                    "user=root&password=ima711");
			efl.processExcelFiles("/Users/yuvalzukerman/Dropbox/Enavigo/Clients/Dole/Nutrition/nutritionLabels/", connection);
			/*
			List<HashMap<String, Object>> nutrients = 
					efl.processFile("/Users/yuvalzukerman/Dropbox/Enavigo/Clients/Dole/Nutrition/nutritionLabels/All-American Turkey Meatloaf.xlsx");
//			System.out.println(nutrients.get(0));
//			System.out.println(nutrients.get(1));
			int nextNutrientId = DoleJsonPersistenceUtils.getMaxId(connection, "recipe_nutrient", "recipe_nutrient_id") +1; 
			DoleJsonPersistenceUtils.presistExcelRecipeNutrients(connection, nutrients, nextNutrientId);
			*/
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
		catch(SQLException se)
		{
			System.out.println("SE: " + se.getLocalizedMessage());
			se.printStackTrace();
		}
		finally
		{
			try
			{
				if (connection != null)
				{
					connection.close();
					System.out.println("DB Connection closed");
				}
			}
			catch(Exception e)
			{
				System.out.println("Issue closing DB connection: " + e.getMessage());
			}
		}
	}
}
