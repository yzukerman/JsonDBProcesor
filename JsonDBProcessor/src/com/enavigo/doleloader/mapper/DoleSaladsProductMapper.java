package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.enavigo.doleloader.nutrition.html.DoleSaladsNutritionHtmlParser;
import com.enavigo.doleloader.pojo.Product;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Creates a collection of Product objects out of data pulled from dolesalads.ca
 * @author yuvalzukerman
 *
 */
public class DoleSaladsProductMapper implements JsonMapper {

	@Override
	public Object mapJson(JsonNode tree) {
		
		List<Product> products = new ArrayList<Product>();
		

		
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
				Product p = processJson(productNode);
				if (p != null)
				{
					p.setNutrients( 
							DoleSaladsNutritionHtmlParser.parseNutritionalInformation(p.getNutritionlabelHtml()));
					products.add(p);
				}
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
		
		//System.out.println(node.get("title").asText());
		Product product = new Product();
		product.setTitle(node.get("title").asText());
		product.setUrl(node.get("page_url").asText());
		if(node.has("category"))
			product.setCategory(node.get("category").asText());
		if(node.has("product_image"))
			product.setImageUrl(node.get("product_image").asText());
		product.setDescription(node.get("description").asText());
		
		if(node.has("ingredients"))
		{
			
			String ingredients = node.get("ingredients").asText();
			String[] ingredientArray = ingredients.split(",");
			product.setIngredients(ingredientArray);
			
		}
		if(node.has("nutrition_facts"))
			product.setNutritionlabelHtml(node.get("nutrition_facts").asText());
		List<HashMap<String, String>> recipes = getRelatedRecipes(node.get("related_recipes"), node.get("related_recipe_images")); 
		if (recipes != null)
			product.setRelatedRecipes(recipes);
		List<HashMap<String, String>> relatedProducts = getRelatedProducts(node.get("related_products"), node.get("related_product_links"));
		if (relatedProducts != null)
			product.setRelatedProducts(relatedProducts);
		return product;
	}
	
	/***
	 * Creates a list of related articles
	 * @param node The node containing the collection of related recipes
	 * @param imagesNode The node containing the collection of related recipes images
	 * @return a collection of related recipes
	 */
	private List<HashMap<String, String>> getRelatedRecipes(JsonNode node, JsonNode imagesNode)
	{
		if (node ==  null)
		{
			return null;
		}
		ArrayList<HashMap<String, String>> relatedRecipes = 
								new ArrayList<HashMap<String, String>>();
		
		Iterator<JsonNode> i = node.elements();
		int x = 0;
		while (i.hasNext())
		{
			JsonNode relatedRecipeNode = i.next();
			HashMap<String, String> relatedRecipe = new HashMap<String, String>();
			String title = relatedRecipeNode.get("title").asText();
			// remove 'Family-Size' prefix
			title = title.substring(12);
			relatedRecipe.put("title", title);
			relatedRecipe.put("url", relatedRecipeNode.get("url").asText());
			relatedRecipe.put("image", imagesNode.get(x).get("url").asText());
			relatedRecipes.add(relatedRecipe);
			x++;
		}
		return relatedRecipes;
	}
	
	/***
	 * Returns a collection containing the products related to the current product
	 * @param node the JSON Node containing an array of related products
	 * @param node the JSON Node containing an array of related product URLs
	 * @return a collection containing the related products' information
	 */
	private List<HashMap<String, String>> getRelatedProducts(JsonNode node, JsonNode linkNode)
	{
		if(node == null) return null;
		
		ArrayList<HashMap<String, String>> relatedProducts = 
								new ArrayList<HashMap<String, String>>();
		
		Iterator<JsonNode> i = node.elements();
		int x = 0;
		while (i.hasNext())
		{
			JsonNode relatedProductNode = i.next();
			HashMap<String, String> relatedProduct = new HashMap<String, String>();
			relatedProduct.put("title", relatedProductNode.get("title").asText());
			relatedProduct.put("image", relatedProductNode.get("image_url").asText());
			relatedProduct.put("url", linkNode.get(x).get("url").asText());
			relatedProducts.add(relatedProduct);
		}
		return relatedProducts;
	}
	

	

}
