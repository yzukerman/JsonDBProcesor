package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;





import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.enavigo.doleloader.pojo.Recipe;
import com.fasterxml.jackson.databind.JsonNode;

public class NewsletterRecipeMapper implements JsonMapper {

	@Override
	public Object mapJson(JsonNode tree) {
		List<Recipe> recipes = new ArrayList<Recipe>();

		Iterator<JsonNode> iterator = tree.elements();
		while(iterator.hasNext())
		{
			
			Iterator<JsonNode> i = iterator.next().elements();
			while(i.hasNext())
			{
				JsonNode recipeNode = i.next();
				Recipe recipe = processJson(recipeNode);
				if (recipe != null)
					recipes.add(recipe);
			}
				
		}
		return recipes;
	}
	
	/***
	 * Iterates through the JSON data, creating a new Product object
	 * @param node JSON snippet associated with a single product's data 
	 * @return Recipe - the recipe associated with the current node
	 */
	
	private Recipe processJson(JsonNode node)
	{
		Recipe recipe = new Recipe();
		String title = node.get("title").asText();
		recipe.setTitle(title);
		System.out.println("Recipe title:" + title);
		recipe.setUrl(node.get("page_url").asText());
		recipe.setCategory(node.get("category").asText());
//		recipe.setSubcategory(subCategory);
		
		processMetadata(recipe, node.get("metadata").asText());
		recipe.setTotalTimeValue(recipe.getPrepValue() + recipe.getCookTimeValue());
		recipe.setTotalTimeUnit(recipe.getCookTimeUnit());
		
		if(node.has("image_url"))
			recipe.setImageUrl(node.get("image_url").asText());
		if(node.has("ingredients"))
			recipe.setIngredients(getIngredientList(node.get("ingredients")));
		
		// get recipe steps
		JsonNode appendixNode = null;
		List<HashMap<String, Object>> steps = null;
		if(node.has("directions_appendix"))
		{
			appendixNode = node.get("directions_appendix");
		}
		
		if(node.has("directions"))
		{
			steps = getRecipeSteps(node.get("directions"), appendixNode);
		}
		else if(node.has("directions_1"))
		{
			steps = getRecipeSteps(node.get("directions_1"), appendixNode);
		}
		else if(node.has("directions_2"))
		{
			steps = getRecipeSteps(node.get("directions_2"), appendixNode);
		}
		else
		{
			steps = processRecipeStepsBackup(node.get("directions_backup").asText());
		}
		
		if (steps != null)
			recipe.setRecipeSteps(steps);

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
			relatedRecipe.put("recipeIngredientString", ingredientNode.get("ingredient").asText());
			ingredients.add(relatedRecipe);
		}
		return ingredients;
	}
	

	/***
	 * Creates a list of related recipe steps
	 * @param node The node containing the collection of recipe steps
	 * @param appendinx Text containing the additional content about the recipe; will be added as an additional direction
	 * @return a collection of recipe steps in order
	 */
	private List<HashMap<String, Object>> getRecipeSteps(JsonNode node, JsonNode appendixNode)
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
		
		String appendix = null;
		if(appendixNode != null)
		{
			appendix = appendixNode.textValue();
		}
		
		if(appendix != null)
		{
			HashMap<String, Object> recipeStep = new HashMap<String, Object>();
			recipeStep.put("description", appendix);
			recipeSteps.add(recipeStep);
		}			
		
		return recipeSteps;
	}
	
	
	private void processMetadata(Recipe r, String metadata)
	{
		String[] metadataArray = metadata.split("\\|");
		// get servings
		String[] servingsArray = metadataArray[0].split(" ");
		if(servingsArray.length > 1) // see http://newsletter.dole.com/items/champion-chia-trail-mix/
			r.setServingSize(servingsArray[1]);
		// preparation time
		String[] prepArray = metadataArray[1].trim().split(" ");
		// check if there is a value for the prep minutes
		if(prepArray.length > 3)
		{
			int prepMinutes = new Integer(prepArray[2]);
			r.setPrepValue(prepMinutes);
			String prepUnit = prepArray[3];
			r.setPrepUnit(prepUnit);
		}
		// cook time
		String[] cookArray = metadataArray[2].trim().split(" ");
		if(cookArray.length > 3)
		{
			if(StringUtils.isAlpha(cookArray[3]) && StringUtils.isNumeric(cookArray[2])) //need because of http://newsletter.dole.com/items/beet-dip-2/
			{
				int cookMinutes = new Integer(cookArray[2]).intValue();
				String cookUnit = cookArray[3];
				r.setCookTimeValue(cookMinutes);
				r.setCookTimeUnit(cookUnit);
			}
		}
	}
	
	/**
	 * Handles processing of recipe directions when the web extraction tool failed to detect the directions block reliably.
	 * Assume each direction appears on its own line
	 * @param directionsString A string containing the directions text block
	 * @return list containing recipe directions
	 */
	private List<HashMap<String, Object>> processRecipeStepsBackup(String directionsString)
	{
		ArrayList<HashMap<String, Object>> recipeSteps = 
								new ArrayList<HashMap<String, Object>>();
		
		String[] steps = directionsString.split("\n");
		for(String step : steps)
		{
			HashMap<String, Object> recipeStep = new HashMap<String, Object>();
			recipeStep.put("description", step);
			recipeSteps.add(recipeStep);
		}
		
		return recipeSteps;
	}
}
