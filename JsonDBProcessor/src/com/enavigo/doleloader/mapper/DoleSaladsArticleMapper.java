package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;






import org.apache.commons.lang3.ArrayUtils;

import com.enavigo.doleloader.pojo.Article;
import com.enavigo.doleloader.pojo.Recipe;
import com.fasterxml.jackson.databind.JsonNode;

public class DoleSaladsArticleMapper implements JsonMapper {

	@Override
	public Object mapJson(JsonNode tree) {
		List<Article> articles = new ArrayList<Article>();

		Iterator<JsonNode> iterator = tree.elements();
		while(iterator.hasNext())
		{
			
			JsonNode root = iterator.next();
			Iterator<JsonNode> i = root.elements();
			while(i.hasNext())
			{
				JsonNode articleNode = i.next();
				Article article = processJson(articleNode);
				if (article != null)
					articles.add(article);
			}
				
		}
		return articles;
	}
	
	/***
	 * Iterates through the JSON data, creating a new Product object
	 * @param node JSON snippet associated with a single product's data 
	 * @return Recipe - the recipe associated with the current node
	 */
	
	private Article processJson(JsonNode node)
	{
		List<HashMap<String, String>> relatedRecipes = null;
		Article article = new Article();
		String title = node.get("title").asText();
		article.setTitle(title);
		System.out.println("Article title:" + title);
		if(node.has("introduction"))
			article.setSubhead(node.get("introduction").asText());
		article.setUrl(node.get("page_url").asText());
		article.setBody(node.get("body_html").asText());
		if(node.has("image_url"))
			article.setImageUrl(node.get("image_url").asText());
		if(node.has("related_recipes"))
			relatedRecipes = getRelatedRecipes(node.get("related_recipes"));
		if (relatedRecipes != null)
			article.setRelatedRecipes(relatedRecipes);

		return article;
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
			relatedRecipe.put("image", relatedRecipeNode.get("image_url").asText());
			relatedRecipe.put("title", relatedRecipeNode.get("title").asText());
			relatedRecipe.put("url", relatedRecipeNode.get("url").asText());
			relatedRecipes.add(relatedRecipe);
		}
		return relatedRecipes;
	}
}
