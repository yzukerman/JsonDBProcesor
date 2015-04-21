package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.enavigo.doleloader.pojo.Recipe;
import com.fasterxml.jackson.databind.JsonNode;

public class DoleSaladsCaRecipeMapper implements JsonMapper {

	@Override
	public Object mapJson(JsonNode tree) {
		List<Recipe> recipes = new ArrayList<Recipe>();
		String category = null;
		String subCategory = null;
		
//		System.out.println(tree.size());
//		Iterator<String> fieldNames = tree.fieldNames();
//		while (fieldNames.hasNext())
//		{
//			System.out.println(fieldNames.next());
//		}
		Iterator<JsonNode> iterator = tree.elements();
		while(iterator.hasNext())
		{
			JsonNode recipeCategoriesNode = iterator.next();
			
			//System.out.println("Node size: " + currentNode.size());
			Iterator<JsonNode> recipeCategoryIterator = recipeCategoriesNode.elements();
			while(recipeCategoryIterator.hasNext())
			{	
				JsonNode currentNode = recipeCategoryIterator.next();
			
				category = currentNode.get("parent_category").asText();
				subCategory = currentNode.get("category").asText();

				if(currentNode.has("recipes"))
				{
					Iterator<JsonNode> recipeIterator = currentNode.get("recipes").elements();
					while(recipeIterator.hasNext())
					{
						JsonNode recipeNode = recipeIterator.next();
						recipes.add(processJson(recipeNode, category, subCategory));
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
	
	private Recipe processJson(JsonNode node, String category, String subCategory)
	{
		Recipe recipe = new Recipe();
		recipe.setTitle(node.get("title").asText());
		recipe.setUrl(node.get("page_url").asText());
		recipe.setCategory(category);
		recipe.setSubcategory(subCategory);
		recipe.setTotalTimeUnit("Minutes");
		if(node.has("servings"))
			recipe.setServingSize(node.get("servings").asText());
		if(node.has("cooking_duration"))
		{
			//System.out.println(node.get("duration").asText());
			recipe.setTotalTimeValue(new Integer(node.get("cooking_duration").asText().split(" ")[0]).intValue());
		}
		if(node.has("image"))
			recipe.setImageUrl(node.get("image").asText());
		if(node.has("subhead"))
			recipe.setDescription(node.get("subhead").asText());
		recipe.setIngredients(getIngredientList(node.get("ingredients")));
		if(node.has("featured_product_title"))
			recipe.setRelatedProducts(getRelatedProduct(
					node.get("featured_product_image").asText(),
					node.get("featured_product_title").asText(),
					node.get("featured_product_url").asText()));
		recipe.setRecipeSteps(getRecipeSteps(node.get("directions")));
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
			relatedRecipe.put("recipeIngredientString", ingredientNode.get("ingredient").asText());
			
			ingredients.add(relatedRecipe);
		}
		return ingredients;
	}
	
	/***
	 * Returns a collection containing the products related to the current recipe
	 * @param node the JSON Node containing an array of related products
	 * @return a collection containing the related products' information
	 */
	private List<HashMap<String, String>> getRelatedProduct(String image, String title, String url)
	{
		ArrayList<HashMap<String, String>> relatedProducts = 
								new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> relatedProduct = new HashMap<String, String>();
		relatedProduct.put("image", image);
		relatedProduct.put("title", title);
		relatedProduct.put("url", url);
		relatedProducts.add(relatedProduct);
		return relatedProducts;
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
		
		Iterator<JsonNode> i = node.elements();
		while (i.hasNext())
		{
			JsonNode recipeStepNode = i.next();
			HashMap<String, Object> recipeStep = new HashMap<String, Object>();
			recipeStep.put("description", recipeStepNode.get("direction").asText());
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
			relatedRecipe.put("image", relatedRecipeNode.get("related_recipe_image").asText());
			relatedRecipe.put("title", relatedRecipeNode.get("related_recipe_title").asText());
			relatedRecipe.put("url", relatedRecipeNode.get("related_recipe_url").asText());
			relatedRecipes.add(relatedRecipe);
		}
		return relatedRecipes;
	}
}
