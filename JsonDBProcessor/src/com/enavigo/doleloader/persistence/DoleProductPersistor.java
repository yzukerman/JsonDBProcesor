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

import com.enavigo.doleloader.DoleLoaderConstants;
import com.enavigo.doleloader.pojo.Product;

/**
 * @author yuvalzukerman
 *
 */
public class DoleProductPersistor implements DoleJsonPersistor {

	Connection connection = null;
	
	
	/* (non-Javadoc)
	 * @see com.enavigo.doleloader.persistence.DoleJsonPersistor#persist(java.lang.Object, char)
	 */
	@Override
	public boolean persist(Connection connection, Object objToPersist, char sourceSite) throws SQLException
	{
		
		this.connection = connection;
		// TODO Auto-generated method stub
		List<Product> products = (List<Product>)objToPersist;
		int nextProductId = getMaxId(connection, "PRODUCT", "product_id") + 1;
		int nextRelatedRecipeId = getMaxId(connection, "product_related_recipe", 
									"product_related_recipe_id") + 1;
		int nextIngredientId = getMaxId(connection, "product_ingredient", 
									"product_ingredient_id") + 1;
		System.out.println ("nextProductId = " + nextProductId + ", nextRelatedRecipeId = " + nextRelatedRecipeId);
		for(Product p : products)
		{
			persistProduct(connection, p, nextProductId, sourceSite);
			nextRelatedRecipeId = persistRelatedRecipe(connection, p.getRelatedRecipes(), nextProductId, 
								nextRelatedRecipeId);
			nextIngredientId = persistIngredients(connection, p.getIngredients(), nextProductId, 
								nextIngredientId);
			nextProductId++;
		}
			
		
		return false;
	}
	
	/***
	 * Returns the highest numbered ID in the products table
	 * @param connection
	 * @param tableName - the table we want to get the maximum ID for
	 * @param idColumn - the column name containing the table's primary key
	 * @return the highest id used in the product table
	 * @throws SQLException
	 */
	private int getMaxId(Connection connection, String tableName, String idColumn) throws SQLException
	{
		String queryString = "SELECT MAX(" + idColumn + ") AS max_id FROM " + tableName;
		PreparedStatement query = connection.prepareStatement(queryString);
		ResultSet rs = query.executeQuery();

		int maxProductId = 0;
		while(rs.next())
		{
			maxProductId = rs.getInt("max_id");
		}
		
		return maxProductId;
	}
	
	
	private void persistProduct(Connection connection, Product p, 
								int productId, char sourceSite) throws SQLException 
	{
//		product_id, title, url, category,
//		subcategory, source_site, image_url, stat1, stat2, stat3, description,
//		nutrition_label_html
		PreparedStatement query = 
				connection.prepareStatement(
						DoleLoaderConstants.PRODUCT_PERSIST_INSERT_NO_NUTRITION);
		query.setInt(1, productId);
		query.setString(2, p.getTitle());
		query.setString(3, p.getUrl());
		query.setString(4, p.getCategory());
		query.setString(5, p.getSubcategory());
		query.setString(6, ""+sourceSite);
		query.setString(7, p.getImageUrl());
		query.setString(8, p.getStat1());
		query.setString(9, p.getStat2());
		query.setString(10, p.getStat3());
		query.setString(11, p.getDescription());
		query.setString(12, p.getNutritionlabelHtml());
		
		int result = query.executeUpdate();
	}
	
	/**
	 * Associates related recipes with a product
	 * @param connection Database connection
	 * @param recipes List of recipes to associate
	 * @param recipeId the next available id in the product related recipe table
	 * @return the id to be used for the next recipe
	 * @throws SQLException
	 */
	private int persistRelatedRecipe(Connection connection, 
			List<HashMap<String, String>> recipes, int productId, int recipeId) throws SQLException
	{
		for (HashMap<String, String> recipe : recipes)
		{
			PreparedStatement query =  
					connection.prepareStatement(DoleLoaderConstants.PRODUCT_RELATED_RECIPE_PERSIST_INSERT);
			
//			product_related_recipe_id,"
//					+ "product_id, related_recipe_url, related_recipe_title, "
//					+ "related_recipe_image_url
			query.setInt(1, recipeId);
			query.setInt(2, productId);
			query.setString(3, recipe.get("url"));
			query.setString(4, recipe.get("title"));
			query.setString(5, recipe.get("image"));
			
			
			int result = query.executeUpdate();
			recipeId++;
		}
		
		return recipeId;
		
	}
	
	
	private int persistIngredients(Connection connection, 
			String[] ingredients, int productId, int ingredientId) throws SQLException
	{
		
		for(String ingredient : ingredients)
		{
			PreparedStatement query = 
					connection.prepareStatement(DoleLoaderConstants.PRODUCT_INGREDIENT_INSERT);
			query.setInt(1, ingredientId);
			query.setInt(2, productId);
			query.setString(3, ingredient.trim());
			query.executeUpdate();
			ingredientId++;
		}
		
		return ingredientId;
	}
	
	
}
