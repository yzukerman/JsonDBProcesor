package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;




import org.apache.commons.lang3.ArrayUtils;

import com.enavigo.doleloader.pojo.Recipe;
import com.fasterxml.jackson.databind.JsonNode;

public class DoleSaladsRecipeMapper implements JsonMapper {
	
	

	@Override
	public Object mapJson(JsonNode tree) {
		List<Recipe> recipes = new ArrayList<Recipe>();

		JsonNode recipePagesNode = tree.get("recipe_pages");
		Iterator<JsonNode> iterator = recipePagesNode.elements();
		while(iterator.hasNext())
		{
			JsonNode categoryNode = iterator.next();
			
			JsonNode recipesNode =  categoryNode.get("recipes");
			Iterator<JsonNode> i = recipesNode.elements();
			// recipes element
			while(i.hasNext())
			{
				
				Iterator<JsonNode> r = i.next().elements();
				while(r.hasNext())
				{
					JsonNode recipeNode = r.next();
					Recipe recipe = processJson(recipeNode);
					if (recipe != null)
						recipes.add(recipe);
				}
			}
				
		}
		return recipes;
	}
	
	/***
	 * Iterates through the JSON data, creating a new Product object
	 * @param node JSON snippet associated with a single product's data 
	 * @return Recipe - the recipe associated with the current node
	 */
	
	private Recipe processJson(JsonNode recipeNode)
	{
		JsonNode node = recipeNode.get(0);
		Recipe recipe = new Recipe();
		String title = node.get("title").asText();
		recipe.setTitle(title);
		System.out.println("Recipe title:" + title);
		recipe.setUrl(node.get("url").asText());
		if(node.has("description"))
			recipe.setDescription(node.get("description").asText());
		if(node.has("duration"))
		{
			String durationString = node.get("duration").asText();
			String[] durationBits = durationString.split(" ");
			recipe.setTotalTimeValue(new Integer(durationBits[0]).intValue());
			recipe.setTotalTimeUnit(durationBits[1]);
		}
		
		if(node.has("servings"))
			recipe.setServingSize(node.get("servings").asText());
		
		if(node.has("veg_servings"))
		{
			String durationString = node.get("veg_servings").asText();
			String[] durationBits = durationString.split(" ");
			recipe.setVegetableServings(new Integer(durationBits[0]).intValue());
		}
		if(node.has("image"))
			recipe.setImageUrl(node.get("image").asText());
		recipe.setNutritionlabelHtml(node.get("nutrition_facts").asText());
		
		
		if(node.has("ingredients"))
			recipe.setIngredients(getIngredientList(node.get("ingredients")));
		List<HashMap<String, Object>> steps = getRecipeSteps(node.get("steps"));
		if (steps != null)
			recipe.setRecipeSteps(steps);
		if (node.has("related_recipes"))
			recipe.setRelatedRecipes(getRelatedRecipes(node.get("related_recipes")));

		return recipe;
	}
	
	/***
	 * Creates a list of recipe ingredients
	 * @param node The node containing the collection of ingredients
	 * @return a collection of recipe ingredients
	 */
	private List<HashMap<String, String>> getIngredientList(JsonNode node)
	{
		ArrayList<HashMap<String, String>> ingredients = 
								new ArrayList<HashMap<String, String>>();
		
		Iterator<JsonNode> i = node.elements();
		while (i.hasNext())
		{
			JsonNode ingredientNode = i.next();
			HashMap<String, String> relatedRecipe = new HashMap<String, String>();
			String ingredient = ingredientNode.get("ingredient").asText();
			String quantity = ingredientNode.get("quantity").asText();
			String recipeIngredientString = quantity + " " + ingredient;
			relatedRecipe.put("recipeIngredientString", recipeIngredientString);
			relatedRecipe.put("title", ingredient);
			relatedRecipe.put("quantity", quantity	);
			
			ingredients.add(relatedRecipe);
		}
		return ingredients;
	}
	

	/***
	 * Creates a list of related recipe steps
	 * @param node The node containing the collection of recipe steps
	 * @return a collection of recipe steps in order
	 */
	private List<HashMap<String, Object>> getRecipeSteps(JsonNode node)
	{
		ArrayList<HashMap<String, Object>> recipeSteps = 
								new ArrayList<HashMap<String, Object>>();
		if(node == null) return null;
		
		Iterator<JsonNode> i = node.elements();
		while (i.hasNext())
		{
			JsonNode recipeStepNode = i.next();
			HashMap<String, Object> recipeStep = new HashMap<String, Object>();
			String description = recipeStepNode.get("direction").asText();
			if (description.trim().length() == 0)
				continue;
			recipeStep.put("description", description);
			String stepIngredients = recipeStepNode.get("ingredient").asText();
			if(stepIngredients != null && stepIngredients.trim().length() > 0)
			{
				Object[] stepIngredientsArray = stepIngredients.split("\n");
				stepIngredientsArray = ArrayUtils.remove(stepIngredientsArray, 0);
				recipeStep.put("ingredients", (String[])stepIngredientsArray);
			}
			
			recipeSteps.add(recipeStep);
		}
		return recipeSteps;
	}
	
	/***
	 * Creates a list of related recipe items
	 * @param node The node containing the collection of related recipes
	 * @return a collection of related recipes
	 */
	private List<HashMap<String, String>> getRelatedRecipes(JsonNode node)
	{
		ArrayList<HashMap<String, String>> relatedRecipes = 
								new ArrayList<HashMap<String, String>>();
		
		Iterator<JsonNode> i = node.elements();
		while (i.hasNext())
		{
			JsonNode relatedRecipeNode = i.next();
			HashMap<String, String> relatedRecipe = new HashMap<String, String>();
			relatedRecipe.put("image", relatedRecipeNode.get("image").asText());
			relatedRecipe.put("title", relatedRecipeNode.get("title").asText());
			relatedRecipes.add(relatedRecipe);
		}
		return relatedRecipes;
	}
	
	
	
}
