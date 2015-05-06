/**
 * 
 */
package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enavigo.doleloader.DoleLoaderConstants;

/**
 * A collection of shared methods for persistence
 * @author yuvalzukerman
 *
 */
public class DoleJsonPersistenceUtils {
	
	public enum NutrientType {
		RECIPE, PRODUCT
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
		if (nutrients == null)
			return;
		
		System.out.println(nutrients);
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
			+ "potassium_grams = ?, "
			+ "potassium_percent = ?,"
			+ "total_carbs_grams = ?, "
			+ "total_carbs_percent = ?, "
			+ "fiber_grams = ?, "
			+ "fiber_percent = ?,"
			+ "protein_grams = ?, "
			+ "sugars_grams = ?, "
			+ "servings_per_container = ?"
			+ " WHERE recipe_id = ?";
	
		 */
		PreparedStatement query =  null;
		if(type == NutrientType.RECIPE)
			query = connection.prepareStatement(DoleLoaderConstants.UPDATE_RECIPE_NUTRIENTS);
		else
			query = connection.prepareStatement(DoleLoaderConstants.UPDATE_PRODUCT_NUTRIENTS);
		
		query.setString(1, (String)nutrients.get("serving_size")); 
		query.setInt(2, (Integer)nutrients.get("calories_from_fat"));
		query.setInt(3, (Integer)nutrients.get("calories"));
		query.setInt(4, (Integer)nutrients.get("total_fat"));
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
		Double spc = (Double)nutrients.get("servings_per_container");
//		System.out.println("SPC: '" + spc + "'");
		if(spc != null)
			query.setDouble(19, (spc));
		else
			query.setDouble(19, 0);
		query.setInt(20, recipeId);
		
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
}
