package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.enavigo.doleloader.nutrition.html.DoleDotComNutritionHtmlParser;
import com.enavigo.doleloader.nutrition.html.DoleSaladsNutritionHtmlParser;
import com.enavigo.doleloader.pojo.Product;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Creates a collection of Product objects out of data pulled from dolesalads.ca
 * @author yuvalzukerman
 *
 */
public class DoleDotComProductMapper implements JsonMapper {

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
					System.out.println("Product: " + p.getTitle() + ", " + p.getUrl());
					p.setNutrients( 
							DoleDotComNutritionHtmlParser.parseNutritionalInformation(p.getNutritionlabelHtml()));
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
		String url = null;
		// detect error
		if(!node.has("title"))
		{
			return null;
		}
		//System.out.println(node.get("title").asText());
		Product product = new Product();
		product.setTitle(node.get("title").asText());
		product.setUrl(node.get("url").asText());
		if(node.has("image"))
			product.setImageUrl(node.get("image").asText());
		product.setDescription(node.get("description").asText());
		product.setCategory(node.get("category").asText());
		if(node.has("sub_category"))
			product.setSubcategory(node.get("sub_category").asText());
		if(node.has("Stat1"))
			product.setStat1(node.get("Stat1").asText().replace("\n", " "));
		if(node.has("Stat2"))
			product.setStat2(node.get("stat2").asText().replace("\n", " "));
		if(node.has("Stat3"))
			product.setStat3(node.get("stat3").asText().replace("\n", " "));
		if(node.has("benefits"))
		{
			product.setBenefits(getBenefits(node.get("benefits")));
		}
		if(node.has("ingredients"))
		{
			JsonNode ingredientsNode = node.get("ingredients");
			if(ingredientsNode.size() > 0)
			{
				// verify these are ingredients and not another section
				String sectionTitle = node.get("ingredient_section_title").asText();
				if (sectionTitle.equals("Ingredients"))
				{
					String ingredients = node.get("ingredients").asText();
					String[] ingredientArray = ingredients.split(",");
					product.setIngredients(ingredientArray);
				}
			}
		}
		if(node.has("nutrition_facts"))
			product.setNutritionlabelHtml(node.get("nutrition_facts").asText());
		List<HashMap<String, String>> articles = getRelatedArticles(node.get("related_articles")); 
		if (articles != null)
			product.setRelatedArticles(articles);
		List<HashMap<String, String>> relatedProducts = getRelatedProducts(node.get("related_products"));
		if (relatedProducts != null)
			product.setRelatedProducts(relatedProducts);
		return product;
	}
	
	/***
	 * Creates a list of related articles
	 * @param node The node containing the collection of related articles
	 * @return a collection of related articles
	 */
	private List<HashMap<String, String>> getRelatedArticles(JsonNode node)
	{
		if (node ==  null)
		{
			return null;
		}
		ArrayList<HashMap<String, String>> relatedArticles = 
								new ArrayList<HashMap<String, String>>();
		
		Iterator<JsonNode> i = node.elements();
		while (i.hasNext())
		{
			JsonNode relatedArticleNode = i.next();
			HashMap<String, String> relatedArticle = new HashMap<String, String>();
			relatedArticle.put("title", relatedArticleNode.get("article_title").asText());
			relatedArticle.put("url", relatedArticleNode.get("article_url").asText());
			relatedArticles.add(relatedArticle);
		}
		return relatedArticles;
	}
	
	/***
	 * Returns a collection containing the products related to the current product
	 * @param node the JSON Node containing an array of related products
	 * @return a collection containing the related products' information
	 */
	private List<HashMap<String, String>> getRelatedProducts(JsonNode node)
	{
		if(node == null) return null;
		
		ArrayList<HashMap<String, String>> relatedProducts = 
								new ArrayList<HashMap<String, String>>();
		
		Iterator<JsonNode> i = node.elements();
		while (i.hasNext())
		{
			JsonNode relatedProductNode = i.next();
			HashMap<String, String> relatedProduct = new HashMap<String, String>();
			relatedProduct.put("title", relatedProductNode.get("product_title").asText());
			relatedProduct.put("url", relatedProductNode.get("product_url").asText());
			relatedProducts.add(relatedProduct);
		}
		return relatedProducts;
	}
	
	/***
	 * Returns a collection containing the products related to the current product
	 * @param node the JSON Node containing an array of related products
	 * @return a collection containing the related products' information
	 */
	private String[] getBenefits(JsonNode node)
	{
		if(node == null) return null;
		
		String[] benefits = new String[node.size()];
		
		Iterator<JsonNode> i = node.elements();
		int x=0;
		
		while (i.hasNext())
		{
			JsonNode benefitNode = i.next();
			benefits[x] = benefitNode.get("benefit").asText();
			x++;
		}
		return benefits;
	}
	

}
