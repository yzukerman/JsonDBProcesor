/**
 * 
 */
package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.enavigo.doleloader.DoleLoaderConstants;

/**
 * A collection of shared methods for persistence
 * @author yuvalzukerman
 *
 */
public class DoleJsonPersistenceUtils {
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public enum NutrientType {
		RECIPE, PRODUCT, EXCEL
	}

	/***
	 * Returns the highest numbered ID in the products table
	 * @param connection
	 * @param tableName - the table we want to get the maximum ID for
	 * @param idColumn - the column name containing the table's primary key
	 * @return the highest id used in the product table
	 * @throws SQLException
	 */
	public static int getMaxId(Connection connection, String tableName, String idColumn) throws SQLException
	{
		String queryString = "SELECT MAX(" + idColumn + ") AS max_id FROM " + tableName;
		PreparedStatement query = connection.prepareStatement(queryString);
		ResultSet rs = query.executeQuery();

		int maxProductId = 0;
		while(rs.next())
		{
			maxProductId = rs.getInt("max_id");
		}
		
		query.close();
		
		return maxProductId;
	}
	
	/**
	 * Persists nutrition information 
	 * @param connection the database connection
	 * @param nutrients a collection containing two array of nutrient data. The top collection involves 
	 * nutrients that are always on labels. The bottom collection contains variable information about
	 * nutrients that may or may not appear in a recipe.
	 * @param recipeId the id of the recipe we are associating nutrients with
	 * @param nextNutrientId the id of the next nutrient to use when persisting
	 * @return the next nutrient id
	 * @throws SQLException
	 */
	public static int persistNutrients(Connection connection, List<HashMap<String, Object>> nutrients, 
									int recipeId, int nextNutrientId, NutrientType type) 
		throws SQLException
	{
		if(nutrients == null)
		{
			return nextNutrientId;
		}
		HashMap<String, Object> topNutrients = nutrients.get(0);
		HashMap<String, Object> bottomNutrients = nutrients.get(1);
		
		persistTopNutrients(connection, topNutrients, recipeId, type);
		nextNutrientId = persistBottomNutrients(connection, bottomNutrients, recipeId, nextNutrientId, type);
		
		return nextNutrientId;
	}
	
	private static void persistTopNutrients(
			Connection connection, Map<String, Object> nutrients, int recipeId, NutrientType type) 
					throws SQLException
	{
		if (nutrients == null || nutrients.size() == 0)
			return;
		
		logger.fine(nutrients.toString());
		
		/*
		 * "UPDATE recipe SET "
			+ "serving_size = ?, "
			+ "calories_from_fat = ?, "
			+ "calories = ?, "
			+ "total_fat_grams = ?,"
			+ "total_fat_percent = ?, "
			+ "saturated_fat_grams = ?,"
			+ "saturated_fat_percent = ?, "
			+ "trans_fat_grams = ?, "
			+ "cholesterol_mg = ?, "
			+ "cholesterol_percent = ?,"
			+ "sodium_mg = ?, "
			+ "sodium_percent = ?, "
			+ "total_carbs_grams = ?, "
			+ "total_carbs_percent = ?, "
			+ "fiber_grams = ?, "
			+ "fiber_percent = ?,"
			+ "protein_grams = ?, "
			+ "sugars_grams = ?, "
			+ "servings_per_container = ?,"
			+ "potassium_grams = ?, "
			+ "potassium_percent = ?"
			+ " WHERE recipe_id = ?";
	
		 */
		PreparedStatement query =  null;
		if(type == NutrientType.RECIPE)
			query = connection.prepareStatement(DoleLoaderConstants.UPDATE_RECIPE_NUTRIENTS);
		else
			query = connection.prepareStatement(DoleLoaderConstants.UPDATE_PRODUCT_NUTRIENTS);
		
		query.setString(1, (String)nutrients.get("serving_size")); 
		if(nutrients.containsKey("calories_from_fat"))
			query.setInt(2, (Integer)nutrients.get("calories_from_fat"));
		else
			query.setInt(2, -1);
		query.setInt(3, (Integer)nutrients.get("calories"));
		query.setDouble(4, (Double)nutrients.get("total_fat"));
		query.setInt(5, (Integer)nutrients.get("total_fat_percent"));
		query.setDouble(6, (Double)nutrients.get("saturated_fat"));
		query.setInt(7, (Integer)nutrients.get("saturated_fat_percent"));
		if(nutrients.get("trans_fat") != null)
			query.setInt(8, (Integer)nutrients.get("trans_fat"));
		else
			query.setInt(8, 0);
		query.setInt(9, (Integer)nutrients.get("cholesterol_mg"));
		query.setInt(10, (Integer)nutrients.get("cholesterol_percent"));
		query.setInt(11, (Integer)nutrients.get("sodium_mg"));
		query.setInt(12, (Integer)nutrients.get("sodium_percent"));
		query.setInt(13, (Integer)nutrients.get("total_carbs"));
		query.setInt(14, (Integer)nutrients.get("total_carbs_percent"));
		query.setInt(15, (Integer)nutrients.get("dietary_fiber"));
		query.setInt(16, (Integer)nutrients.get("dietary_fiber_percent"));
		query.setInt(17, (Integer)nutrients.get("protein"));
		Integer sugars = (Integer)nutrients.get("sugars");
		if(sugars != null && sugars.intValue() > 0)
			query.setInt(18, sugars);
		else
			query.setInt(18, 0);
		if (nutrients.containsKey("servings_per_container"))
		{
			Double spc = (Double)nutrients.get("servings_per_container");
//		System.out.println("SPC: '" + spc + "'");
			if(spc != null)
				query.setDouble(19, (spc));
			else
				query.setDouble(19, 0);
		}
		else
			query.setDouble(19, 0);
		
		if(type == NutrientType.PRODUCT)
		{
			// potassium
			if (nutrients.get("potassium_g") != null)
				query.setInt(20, (Integer)nutrients.get("potassium_g"));
			else
				query.setNull(20, 0);
			
			if (nutrients.get("potassium_percent") != null)
				query.setInt(21, (Integer)nutrients.get("potassium_percent"));
			else
				query.setNull(21, 0);
		
			query.setInt(22, recipeId);
		}
		else
		{
			query.setInt(20, recipeId);
		}
		
		query.executeUpdate();
		
		query.close();
		
		
	}
	
	private static int persistBottomNutrients(Connection connection, Map<String, Object> nutrients, 
										int recipeId, int nextNutrientId, NutrientType type) throws SQLException
	{
		if(nutrients != null)
		{
			PreparedStatement query = null;
			
			if(type == NutrientType.RECIPE)
				query = connection.prepareStatement(DoleLoaderConstants.RECIPE_INSERT_NUTRIENT);
			else
				query = connection.prepareStatement(DoleLoaderConstants.PRODUCT_INSERT_NUTRIENT);
			
			for(String nutrientName : nutrients.keySet())
			{
				int nutrientValue = (Integer)nutrients.get(nutrientName);
				
				/*
				 * "INSERT INTO recipe_nutrient "
			+ "(recipe_nutrient_id, recipe_recipe_id, title, percentage)"
			+ " VALUES "
			+ "(?, ?, ?, ?)";
				 */
				query.setInt(1, nextNutrientId);
				query.setInt(2, recipeId);
				query.setString(3, nutrientName);
				query.setInt(4, nutrientValue);
				query.executeUpdate();
				nextNutrientId++;
			}
			
			query.close();
			
			
		}
		
		return nextNutrientId;
	}
	
	private static void persistExcelTopNutrients(
			Connection connection, Map<String, Object> nutrients) 
					throws SQLException
	{
		
		PreparedStatement query =  null;
		query = connection.prepareStatement(DoleLoaderConstants.UPDATE_EXCEL_RECIPE_NUTRIENTS);
		
		/*
		 *"UPDATE recipe SET "
			+ "serving_size = ?, "
			+ "calories_from_fat = ?, "
			+ "calories = ?, "
			+ "total_fat_grams = ?,"
			+ "total_fat_percent = ?, "
			+ "saturated_fat_grams = ?,"
			+ "saturated_fat_percent = ?, "
			+ "trans_fat_grams = ?, "
			+ "cholesterol_mg = ?, "
			+ "cholesterol_percent = ?,"
			+ "sodium_mg = ?, "
			+ "sodium_percent = ?, "
			+ "total_carbs_grams = ?, "
			+ "total_carbs_percent = ?, "
			+ "fiber_grams = ?, "
			+ "fiber_percent = ?,"
			+ "protein_grams = ?, "
			+ "sugars_grams = ?, "
			+ "polyunsat_fat_grams = ?, "
			+ "monounsat_fat_grams = ?, "
			+ "potassium_grams = ?, "
			+ "potassium_percent = ? "
			+ " WHERE title = ?";
		 */
		query.setString(1, (String)nutrients.get("serving_size")); 
		if(nutrients.containsKey("calories_from_fat"))
			query.setInt(2, (Integer)nutrients.get("calories_from_fat"));
		else
			query.setInt(2, -1);
		query.setInt(3, (Integer)nutrients.get("calories"));
		query.setInt(4, (Integer)nutrients.get("total_fat"));
		query.setInt(5, (Integer)nutrients.get("total_fat_percentage"));
		query.setDouble(6, (Double)nutrients.get("saturated_fat"));
		query.setInt(7, (Integer)nutrients.get("saturated_fat_percentage"));
		if(nutrients.get("trans_fat") != null)
			query.setInt(8, (Integer)nutrients.get("trans_fat"));
		else
			query.setInt(8, 0);
		query.setInt(9, (Integer)nutrients.get("cholesterol"));
		query.setInt(10, (Integer)nutrients.get("cholesterol_percentage"));
		query.setInt(11, (Integer)nutrients.get("sodium"));
		query.setInt(12, (Integer)nutrients.get("sodium_percentage"));
		query.setInt(13, (Integer)nutrients.get("carbs"));
		query.setInt(14, (Integer)nutrients.get("carbs_percentage"));
		query.setInt(15, (Integer)nutrients.get("fiber"));
		query.setInt(16, (Integer)nutrients.get("fiber_percentage"));
		query.setInt(17, (Integer)nutrients.get("protein"));
		query.setInt(18, (Integer)nutrients.get("sugars"));
		query.setDouble(19, (Double)nutrients.get("poly_fat"));
		query.setDouble(20, (Double)nutrients.get("mono_fat"));
		query.setInt(21, (Integer)nutrients.get("potassium"));
		query.setInt(22, (Integer)nutrients.get("potassium_percentage"));
		query.setString(23, (String)nutrients.get("recipeTitle"));
		
		int affectedRows = query.executeUpdate();
		logger.fine("Updated " + affectedRows + " recipes titled " + (String)nutrients.get("recipeTitle"));
		query.close();
	}
	
	public static int presistExcelRecipeNutrients(Connection connection, 
													List<HashMap<String, Object>> nutrients, 
													int nextNutrientId) 
													throws SQLException
	{
		
		HashMap<String, Object> topNutrients = nutrients.get(0);
		HashMap<String, Object> bottomNutrients = nutrients.get(1);
		
		persistExcelTopNutrients(connection, topNutrients);

		// get recipe ids
		PreparedStatement query = connection.prepareStatement(DoleLoaderConstants.GET_RECIPE_IDS_FOR_TITLE);
		query.setString(1, (String)topNutrients.get("recipeTitle"));
		
		ResultSet rs = query.executeQuery();

		List<Integer> recipeIds = new LinkedList<Integer>();
		while(rs.next())
		{
			recipeIds.add(new Integer(rs.getInt("recipe_id")));
		}
		rs.close();
		query.close();
		
		if(recipeIds.isEmpty())
			System.out.println("MISSING RECIPE: " + (String)topNutrients.get("recipeTitle"));
		
		for(Integer recipeId : recipeIds)
		{
			nextNutrientId = persistBottomNutrients(connection, bottomNutrients, recipeId.intValue(), nextNutrientId, NutrientType.RECIPE);
		}
		
		return nextNutrientId;
	}
}
