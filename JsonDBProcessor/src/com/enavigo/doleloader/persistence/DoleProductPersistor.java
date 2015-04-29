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
		int nextProductId = DoleJsonPersistenceUtils.getMaxId(connection, "PRODUCT", "product_id") + 1;
		int nextRelatedRecipeId = DoleJsonPersistenceUtils.getMaxId(connection, "product_related_recipe", 
									"product_related_recipe_id") + 1;
		int nextIngredientId = DoleJsonPersistenceUtils.getMaxId(connection, "product_ingredient", 
									"product_ingredient_id") + 1;
		int nextRelatedProductId = DoleJsonPersistenceUtils.getMaxId(connection, "related_product", 
				"rp_id") + 1;
		int nextProductBenefitId = DoleJsonPersistenceUtils.getMaxId(connection, "product_benefit", 
				"product_benefit_id") + 1;
		int nextRelatedArticleId = DoleJsonPersistenceUtils.getMaxId(connection, "product_related_article", 
				"related_article_id") + 1;
		for(Product p : products)
		{
			System.out.println("Persisting: " + p.getTitle());
			persistProduct(connection, p, nextProductId, sourceSite);
			nextRelatedRecipeId = persistRelatedRecipe(connection, p.getRelatedRecipes(), nextProductId, 
								nextRelatedRecipeId);
			nextIngredientId = persistIngredients(connection, p.getIngredients(), nextProductId, 
								nextIngredientId);
			nextRelatedProductId = persistRelatedProducts(connection, p.getRelatedProducts(), nextProductId, 
					nextRelatedProductId);
			nextRelatedArticleId = persistRelatedArticles(connection, p.getRelatedArticles(), nextProductId, 
					nextRelatedArticleId);
			nextProductBenefitId = persistBenefits(connection, p.getBenefits(), nextProductId, 
					nextProductBenefitId);
			nextProductId++;
		}
			
		
		return false;
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
		if(recipes == null)
			return recipeId;
		
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
	
	/**
	 * Stores the current product's ingredients in the database
	 * @param connection the database connection
	 * @param ingredients an array containing the product's ingredients
	 * @param productId the id of the product the ingredients will be associated with
	 * @param ingredientId the next available id for the ingredient table's primary key
	 * @return the next available ingredient id
	 * @throws SQLException
	 */
	private int persistIngredients(Connection connection, 
			String[] ingredients, int productId, int ingredientId) throws SQLException
	{	
		if (ingredients != null)
		{
			for(String ingredient : ingredients)
			{
				if(ingredient.length() == 0) continue; 
				PreparedStatement query = 
						connection.prepareStatement(DoleLoaderConstants.PRODUCT_INGREDIENT_INSERT);
				query.setInt(1, ingredientId);
				query.setInt(2, productId);
				//System.out.println("Product Id: " + productId + " | Ingredient: " + ingredient.trim() );
				query.setString(3, ingredient.trim());
				query.executeUpdate();
				ingredientId++;
			}
		}
		
		return ingredientId;
	}
	
	/**
	 * Associates related recipes with a product
	 * @param connection Database connection
	 * @param recipes List of recipes to associate
	 * @param recipeId the next available id in the product related recipe table
	 * @return the id to be used for the next recipe
	 * @throws SQLException
	 */
	private int persistRelatedProducts(Connection connection, 
			List<HashMap<String, String>> recipes, int productId, int relatedProductId) throws SQLException
	{
		if(recipes != null)
		{
			for (HashMap<String, String> recipe : recipes)
			{
				PreparedStatement query =  
						connection.prepareStatement(DoleLoaderConstants.PRODUCT_RELATED_PRODUCT_INSERT);
				
	//			rp_id,"
	//					+ "owner_product_id,"
	// 					+ "related_product_url,"
	//					+ "related_product_title,"
	//					+ "related_product_image
				query.setInt(1, relatedProductId);
				query.setInt(2, productId);
				query.setString(3, recipe.get("url"));
				query.setString(4, recipe.get("title"));
				query.setString(5, recipe.get("image"));
				
				
				int result = query.executeUpdate();
				relatedProductId++;
			}
		}
		
		return relatedProductId;
		
	}
	
	/**
	 * Associates related articles with a product
	 * @param connection Database connection
	 * @param recipes List of articles to associate
	 * @param recipeId the next available id in the product related recipe table
	 * @return the id to be used for the next article
	 * @throws SQLException
	 */
	private int persistRelatedArticles(Connection connection, 
			List<HashMap<String, String>> articles, int productId, int relatedArticleId) throws SQLException
	{
		if(articles != null)
		{
			
			for (HashMap<String, String> article : articles)
			{
				PreparedStatement query =  
						connection.prepareStatement(DoleLoaderConstants.PRODUCT_RELATED_ARTICLE_INSERT);
				
				query.setInt(1, relatedArticleId);
				query.setInt(2, productId);
				query.setString(3, article.get("title"));
				query.setString(4, article.get("url"));
				
				
				int result = query.executeUpdate();
				relatedArticleId++;
				query.close();
			}
		}
		
		return relatedArticleId;
		
	}
	
	private int persistBenefits(Connection connection, 
			String[] benefits, int productId, int productBenefitId) throws SQLException
	{	
		if (benefits != null)
		{
			for(String benefit : benefits)
			{
				PreparedStatement query = 
						connection.prepareStatement(DoleLoaderConstants.PRODUCT_INGREDIENT_INSERT);
				query.setInt(1, productBenefitId);
				query.setInt(2, productId);
				//System.out.println("Product Id: " + productId + " | Ingredient: " + ingredient.trim() );
				query.setString(3, benefit.trim());
				query.executeUpdate();
				productBenefitId++;
			}
		}
		
		return productBenefitId;
	}
	
	
}
