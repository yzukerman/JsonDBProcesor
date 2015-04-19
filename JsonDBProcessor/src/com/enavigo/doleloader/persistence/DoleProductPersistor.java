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
		int nextProductId = getMaxId(connection, "PRODUCT") + 1;
		int nextRelatedRecipeId = getMaxId(connection, "product_related_recipe") + 1;
		
		for(Product p : products)
		{
			persistProduct(connection, p, nextProductId, sourceSite);
			nextRelatedRecipeId = persistRelatedRecipe(connection, p.getRelatedRecipes(), nextProductId, 
								nextRelatedRecipeId);
			nextProductId++;
		}
			
		
		return false;
	}
	
	/***
	 * Returns the highest numbered ID in the products table
	 * @param connection
	 * @param tableName - the table we want to get the maximum ID for
	 * @return the highest id used in the product table
	 * @throws SQLException
	 */
	private int getMaxId(Connection connection, String tableName) throws SQLException
	{
		String queryString = DoleLoaderConstants.PRODUCT_PERSIST_GET_MAX_ID + tableName;
		PreparedStatement query = connection.prepareStatement(queryString);
		
		ResultSet rs = query.executeQuery();
		System.out.println("Resultset size " + rs.getFetchSize());
		int maxProductId = 0;
		while(rs.next())
		{
			maxProductId = rs.getInt("max_product_id");
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
		System.out.println("Product Insert Result = " + result);
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
			System.out.println("Product Related Recipe Insert Result = " + result);
			recipeId++;
		}
		
		return recipeId;
		
	}
	
	
}
