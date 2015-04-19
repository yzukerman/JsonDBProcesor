package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.enavigo.doleloader.pojo.Product;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Creates a collection of Product objects out of data pulled from dolesalads.ca
 * @author yuvalzukerman
 *
 */
public class DoleSaladsCaProductMapper implements JsonMapper {

	@Override
	public Object mapJson(JsonNode tree) {
		
		List<Product> products = new ArrayList<Product>();
		
		System.out.println("In mapJson");
		
		System.out.println(tree.size());
		Iterator<String> fieldNames = tree.fieldNames();
		while (fieldNames.hasNext())
		{
			System.out.println(fieldNames.next());
		}
		Iterator<JsonNode> iterator = tree.elements();
		while(iterator.hasNext())
		{
			JsonNode currentNode = iterator.next();
			System.out.println("Node size: " + currentNode.size());
			Iterator<JsonNode> productIterator = currentNode.elements();
			while(productIterator.hasNext())
			{
				JsonNode productNode = productIterator.next();
				System.out.println(productNode.get("title"));
				products.add(processJson(productNode));
			}
		}
		return products;
	}
	
	/***
	 * Iterates through the JSON data, creating a new Product object
	 * @param node JSON snippet associated with a single product's data 
	 * @return Product - the product associated with the current node
	 */
	
	private Product processJson(JsonNode node)
	{
		Product product = new Product();
		product.setTitle(node.get("title").asText());
		product.setUrl(node.get("page_url").asText());
		product.setImageUrl(node.get("product_image").asText());
		product.setDescription(node.get("description").asText());
		String ingredients = node.get("ingredients").asText();
		String[] ingredientArray = ingredients.split(",");
		product.setIngredients(ingredientArray);
		product.setNutritionlabelHtml(node.get("nutrition_info").asText());
		product.setRelatedRecipes(getRelatedRecipes(node.get("related_recipes")));
		return product;
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
			relatedRecipe.put("url", relatedRecipeNode.get("url").asText());
			relatedRecipes.add(relatedRecipe);
		}
		return relatedRecipes;
	}

}
