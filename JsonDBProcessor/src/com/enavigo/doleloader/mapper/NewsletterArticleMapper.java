package com.enavigo.doleloader.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;








import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;

import com.enavigo.doleloader.pojo.Article;
import com.enavigo.doleloader.pojo.Recipe;
import com.fasterxml.jackson.databind.JsonNode;

public class NewsletterArticleMapper implements JsonMapper {

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
		Article article = new Article();
		String title = node.get("title").asText();
		article.setTitle(title);
		System.out.println("Article title:" + title);
		article.setUrl(node.get("page_url").asText());
		DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		String dateString = node.get("publish_date").asText();
		Date pubDate = null;
		try
		{
			pubDate = format.parse(dateString);
			article.setPublishDate(pubDate);
			
		}
		catch (ParseException pe)
		{
			
		}
		article.setBody(node.get("body").asText());
		if(node.has("subhead"))
			article.setSubhead(node.get("subhead").asText());
		if(node.has("image_url"))
			article.setImageUrl(node.get("image_url").asText());
		
		// pretend like category is a tag
		List<String> tags = getTags(node.get("tags"));
		article.setTags(tags);

		return article;
	}

	
	private List<String> getTags(JsonNode tagNode)
	{
		List<String> tags = new ArrayList<String>();
		
		if(tagNode == null)
			return null;
		Iterator<JsonNode> i = tagNode.elements();
		while (i.hasNext())
		{
			tags.add(i.next().get("tag").asText());
		}
		
		tags.remove(0);
		return tags;
	}
}
