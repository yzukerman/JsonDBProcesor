package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.enavigo.doleloader.nutrition.html.DoleDotComNutritionHtmlParser;
import com.enavigo.doleloader.nutrition.html.DoleSaladsCaNutritionHtmlParser;
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
		
//		System.out.println(tree.size());
//		Iterator<String> fieldNames = tree.fieldNames();
//		while (fieldNames.hasNext())
//		{
//			System.out.println(fieldNames.next());
//		}
		Iterator<JsonNode> iterator = tree.elements();
		while(iterator.hasNext())
		{
			JsonNode currentNode = iterator.next();
			//System.out.println("Node size: " + currentNode.size());
			Iterator<JsonNode> productIterator = currentNode.elements();
			while(productIterator.hasNext())
			{
				JsonNode productNode = productIterator.next();
				System.out.println(productNode.get("title"));
				Product p = processJson(productNode);
				p.setNutrients(DoleSaladsCaNutritionHtmlParser.parseNutritionalInformation(p.getNutritionlabelHtml()));
				products.add(p);
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
		if(node.has("product_image"))
			product.setImageUrl(node.get("product_image").asText());
		product.setDescription(node.get("description").asText());
		String ingredients = node.get("ingredients").asText();
		String[] ingredientArray = ingredients.split(",");
		product.setIngredients(ingredientArray);
		if(node.has("nutrition_info"))
			product.setNutritionlabelHtml(node.get("nutrition_info").asText());
		product.setRelatedRecipes(getRelatedRecipes(node.get("related_recipes")));
		product.setRelatedProducts(getRelatedProducts(node.get("related_products")));
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
	
	/***
	 * Returns a collection containing the products related to the current product
	 * @param node the JSON Node containing an array of related products
	 * @return a collection containing the related products' information
	 */
	private List<HashMap<String, String>> getRelatedProducts(JsonNode node)
	{
		ArrayList<HashMap<String, String>> relatedProducts = 
								new ArrayList<HashMap<String, String>>();
		
		Iterator<JsonNode> i = node.elements();
		while (i.hasNext())
		{
			JsonNode relatedProductNode = i.next();
			HashMap<String, String> relatedProduct = new HashMap<String, String>();
			relatedProduct.put("image", relatedProductNode.get("image").asText());
			relatedProduct.put("title", relatedProductNode.get("title").asText());
			relatedProduct.put("url", relatedProductNode.get("url").asText());
			relatedProducts.add(relatedProduct);
		}
		return relatedProducts;
	}
	
	

}
