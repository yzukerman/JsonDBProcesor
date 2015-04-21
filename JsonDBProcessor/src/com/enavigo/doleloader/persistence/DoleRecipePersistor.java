package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.enavigo.doleloader.DoleLoaderConstants;
import com.enavigo.doleloader.pojo.Product;
import com.enavigo.doleloader.pojo.Recipe;

public class DoleRecipePersistor implements DoleJsonPersistor {
	
	Connection connection = null;
	
	@Override
	public boolean persist(Connection connection, Object objToPersist,
			char sourceSite) throws SQLException {
		// TODO Auto-generated method stub
		this.connection = connection;
		// TODO Auto-generated method stub
		List<Recipe> recipes = (List<Recipe>)objToPersist;
		int nextRecipeId = DoleJsonPersistenceUtils.getMaxId(connection, "recipe", "recipe_id") + 1;
		int nextRecipeStepId = DoleJsonPersistenceUtils.getMaxId(connection, 
												"recipe_step", "recipe_step_id") + 1;
		int nextRecipeIngredientId = DoleJsonPersistenceUtils.getMaxId(connection, 
				"recipe_ingredient", "recipe_ingredient_id") + 1;
		int nextRecipeStepIngredientId = DoleJsonPersistenceUtils.getMaxId(connection, 
				"recipe_step_ingredient", "recipe_step_ingredient_id") + 1;
		int nextRelatedRecipeId = DoleJsonPersistenceUtils.getMaxId(connection, 
				"related_recipe", "rr_id") + 1;
		
		for(Recipe recipe : recipes)
		{
			persistRecipe(connection, recipe, nextRecipeId, sourceSite);
			nextRecipeId++;
		}
		
		return false;
	}

	
	private void persistRecipe(Connection connection, Recipe r, 
					int recipeId, char sourceSite) throws SQLException 
	{
		/*
		 * recipe"
			+ "recipe_id, source_site_id, title, url,"
			+ "category, subcategory, "
			+ "prep_value, prep_unit, total_time_value, total_time_unit,"
			+ "servings, calories, description, difficulty_description,"
			+ "difficulty_value, image_url, vegetable_servings, nutrition_facts_html
		 */
		
		PreparedStatement query = 
				connection.prepareStatement(DoleLoaderConstants.RECIPE_INSERT_NO_NUTRITION);
		query.setInt(1, recipeId);
		query.setString(2, ""+sourceSite);
		query.setString(3, r.getTitle());
		query.setString(4, r.getUrl());
		query.setString(5, r.getCategory());
		query.setString(6, r.getSubcategory());
		query.setInt(7, r.getPrepValue());
		query.setString(8, r.getPrepUnit());
		query.setInt(9, r.getTotalTimeValue());
		query.setString(10, r.getTotalTimeUnit());
		query.setInt(11, r.getServings());
		query.setInt(12, r.getCalories());
		query.setString(13, r.getDescription());
		query.setString(14, r.getDifficultyDescription());
		query.setInt(15, r.getDifficultyValue());
		query.setString(16, r.getImageUrl());
		query.setInt(17, r.getVegetableServings());
		query.setString(18, r.getNutritionlabelHtml());
		
		int result = query.executeUpdate();
	}
	
}
