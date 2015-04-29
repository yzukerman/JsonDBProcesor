package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		Map<String, Integer> recipeStepIds = new HashMap<String, Integer>();
		recipeStepIds.put("recipeStepId", new Integer(nextRecipeStepId));
		recipeStepIds.put("recipeIngredientStepId", new Integer(nextRecipeStepIngredientId));
		
		for(Recipe recipe : recipes)
		{
			persistRecipe(connection, recipe, nextRecipeId, sourceSite);
			nextRelatedRecipeId = persistRelatedRecipe(connection, 
					recipe.getRelatedRecipes(), nextRecipeId, nextRelatedRecipeId);
			recipeStepIds = persistRecipeSteps(connection, 
					recipe.getRecipeSteps(), nextRecipeId, recipeStepIds);
			nextRecipeIngredientId = persistIngredients(connection,
					recipe.getIngredients(), nextRecipeId, nextRecipeIngredientId);
			nextRecipeId++;
		}
		
		return false;
	}

	/***
	 * Stores a recipe in the database
	 * @param connection the database connection
	 * @param r the recipe to store
	 * @param recipeId the id to assign to the recipe in the database
	 * @param sourceSite the site from which the recipe was extracted
	 * @throws SQLException
	 */
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
		
		//System.out.println("Persisting recipe: " + r.getTitle());
		
		
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
		query.setString(11, r.getServingSize());
		query.setInt(12, r.getCalories());
		query.setString(13, r.getDescription());
		query.setString(14, r.getDifficultyDescription());
		query.setInt(15, r.getDifficultyValue());
		query.setString(16, r.getImageUrl());
		query.setInt(17, r.getVegetableServings());
		query.setString(18, r.getNutritionlabelHtml());
		
		int result = query.executeUpdate();
	}
	
	/**
	 * Associates related recipes with the current recipe
	 * @param connection Database connection
	 * @param recipes List of recipes to associate
	 * @param recipeId The id of the recipe we want to associate the related recipes with; the current recipe
	 * @param relatedRecipeId the next available id in the related recipe table
	 * @return the id to be used for the next recipe
	 * @throws SQLException
	 */
	private int persistRelatedRecipe(Connection connection, 
			List<HashMap<String, String>> recipes, int recipeId, int relatedRecipeId) throws SQLException
	{
		if(recipes == null)
			return relatedRecipeId;
		
		for (HashMap<String, String> recipe : recipes)
		{
			PreparedStatement query =  
					connection.prepareStatement(DoleLoaderConstants.RECIPE_RELATED_RECIPE_INSERT);
			
			query.setInt(1, relatedRecipeId);
			query.setInt(2, recipeId);
			query.setString(3, recipe.get("url"));
			query.setString(4, recipe.get("title"));
			query.setString(5, recipe.get("image"));
			
			int result = query.executeUpdate();
			relatedRecipeId++;
		}
		
		return relatedRecipeId;
		
	}
	
	/***
	 * Stores recipe preparation direction steps in the database, as well as the ingredients
	 * associated with each step (if any were specified in the recipe)
	 * @param connection the database connection
	 * @param steps list containing step information and ingredients
	 * @param recipeId the id of the recipe the steps are assocaited with
	 * @param recipeStepIds a map containing the primary key id values to use for the next step and
	 * ingredients
	 * @return a new map containing the next recipe step and ingredient ids to use
	 * @throws SQLException
	 */
	private Map<String, Integer> persistRecipeSteps(Connection connection, 
			List<HashMap<String, Object>> steps, int recipeId, 
			Map<String, Integer> recipeStepIds) throws SQLException
	{
		if(steps == null)
			return recipeStepIds;
		
		int stepId = recipeStepIds.get("recipeStepId").intValue();
		int ingredientId = recipeStepIds.get("recipeIngredientStepId").intValue();
		
		for (HashMap<String, Object> step : steps)
		{
			PreparedStatement query =  
					connection.prepareStatement(DoleLoaderConstants.RECIPE_STEP_INSERT);
			
			/*
			 * "INSERT INTO recipe_step (recipe_step_id,"
			+ "recipe_id, step_description"
			+ ") VALUES "
			+ "(?, ?, ?)";
			 */
			
			query.setInt(1, stepId);
			query.setInt(2, recipeId);
			query.setString(3, (String)step.get("description"));
			
			query.executeUpdate();
			
			String[] stepIngredients = (String[])step.get("ingredients");
			if(stepIngredients != null)
			{
				for(String ingredient : stepIngredients)
				{
					if(ingredient.length() == 0) continue;
					PreparedStatement ingredientQuery =  
							connection.prepareStatement(
									DoleLoaderConstants.RECIPE_STEP_INGREDIENT_INSERT);
					ingredientQuery.setInt(1, ingredientId);
					ingredientQuery.setInt(2, stepId);
					ingredientQuery.setString(3, ingredient);
					ingredientQuery.executeUpdate();
					ingredientId++;
					ingredientQuery.close();
				}
			}
			
			stepId++;
			query.close();
		}
		
		
		recipeStepIds.put("recipeStepId", new Integer(stepId));
		recipeStepIds.put("recipeIngredientStepId", new Integer(ingredientId));
		return recipeStepIds;
		
	}
	
	
	private int persistIngredients(Connection connection, 
			List<HashMap<String, String>> ingredients, int recipeId, int ingredientId) throws SQLException
	{
		if(ingredients == null)
			return ingredientId;
		
		for (HashMap<String, String> ingredient : ingredients)
		{
			PreparedStatement query =  
					connection.prepareStatement(DoleLoaderConstants.RECIPE_INGREDIENT_INSERT);
			
			//"INSERT INTO recipe_ingredient (recipe_ingredient_id, recipe_recipe_id,"
			//+ "recipe_ingredient_string, recipe_ingredient_title, recipe_ingredient_quantity"
			
			query.setInt(1, ingredientId);
			query.setInt(2, recipeId);
			query.setString(3, ingredient.get("recipeIngredientString"));
			query.setString(4, ingredient.get("title"));
			query.setString(5, ingredient.get("quantity"));
			
			int result = query.executeUpdate();
			ingredientId++;
		}
		
		return ingredientId;
		
	}
	
}
