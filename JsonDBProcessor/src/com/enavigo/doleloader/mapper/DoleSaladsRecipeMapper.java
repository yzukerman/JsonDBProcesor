package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;







import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import com.enavigo.doleloader.nutrition.html.DoleSaladsNutritionHtmlParser;
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
					{
						recipe.setNutrients( 
								DoleSaladsNutritionHtmlParser.parseNutritionalInformation(recipe.getNutritionlabelHtml()));
//						if(recipe.getTitle().contains("Bliss"))
//							recipe.getNutrients();
						recipes.add(recipe);
					}
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
		recipe.setCategory(node.get("categories").get(2).get("category").asText());
		// see if there is a subcategory
		if(node.get("categories").has(3))
			recipe.setSubcategory(node.get("categories").get(3).get("category").asText());
		if(node.has("description"))
		{
			String description = node.get("description").asText();
			// remove potentially 'read more' text 
			description.replace("...", "");
			description.replace(" Read More", "");
							
			recipe.setDescription(description);
		}
		if(node.has("duration"))
		{
			String durationString = node.get("duration").asText();
			String[] durationBits = durationString.split(" ");
			
			try
			{
				recipe.setTotalTimeValue(new Integer(durationBits[0]).intValue());
				recipe.setTotalTimeUnit(durationBits[1]);
			}
			catch(NumberFormatException nfe)
			{
				// unpack digits from characters
				Pattern pattern = Pattern.compile("\\d+");
				Matcher matcher = pattern.matcher(durationBits[0]);
				if(matcher.find())
				{
					recipe.setTotalTimeValue(new Integer(matcher.group(0)));
				}
				
				pattern = Pattern.compile("\\D+");
				matcher = pattern.matcher(durationBits[0]);
				if(matcher.find())
				{
					recipe.setTotalTimeUnit(matcher.group(0));
				}
			}
		}
		
		if(node.has("servings"))
			recipe.setServingSize(node.get("servings").asText());
		
		if(node.has("veg_servings"))
		{
			String durationString = node.get("veg_servings").asText();
			String[] durationBits = durationString.split(" ");
			recipe.setVegetableServings(durationBits[0]);
		}
		if(node.has("image"))
			recipe.setImageUrl(node.get("image").asText());
		recipe.setNutritionlabelHtml(node.get("nutrition_facts").asText());
		
		
		if(node.has("ingredient_list"))
			recipe.setIngredients(getIngredientList(node.get("ingredient_list")));
		
		List<HashMap<String, Object>> steps = getRecipeSteps(node.get("direction_list"));
		if (steps != null)
			recipe.setRecipeSteps(steps);
		
		if (node.has("related_recipes"))
			recipe.setRelatedRecipes(getRelatedRecipes(node.get("related_recipes"), node.get("related_recipe_images")));

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
			relatedRecipe.put("recipeIngredientString", ingredient);
			
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
			
			recipeSteps.add(recipeStep);
		}
		return recipeSteps;
	}
	
	/***
	 * Creates a list of related recipe items
	 * @param node The node containing the collection of related recipes
	 * @param imagesNode The node containing the collection of related recipes images
	 * @return a collection of related recipes
	 */
	private List<HashMap<String, String>> getRelatedRecipes(JsonNode node, JsonNode imagesNode)
	{
		ArrayList<HashMap<String, String>> relatedRecipes = 
								new ArrayList<HashMap<String, String>>();
		
		Iterator<JsonNode> i = node.elements();
		int x = 0;
		while (i.hasNext())
		{
			JsonNode relatedRecipeNode = i.next();
			HashMap<String, String> relatedRecipe = new HashMap<String, String>();
			relatedRecipe.put("image", imagesNode.get(x).get("url").asText());
			relatedRecipe.put("title", relatedRecipeNode.get("title").asText());
			relatedRecipe.put("url", relatedRecipeNode.get("url").asText());
			relatedRecipes.add(relatedRecipe);
			x++;
		}
		return relatedRecipes;
	}
	
}
