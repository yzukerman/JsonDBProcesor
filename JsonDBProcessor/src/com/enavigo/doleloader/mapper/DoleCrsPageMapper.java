package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;






import org.apache.commons.lang3.ArrayUtils;

import com.enavigo.doleloader.pojo.CrsCategoryPage;
import com.enavigo.doleloader.pojo.Recipe;
import com.fasterxml.jackson.databind.JsonNode;

public class DoleCrsPageMapper implements JsonMapper {

	@Override
	public Object mapJson(JsonNode tree) {
		List<CrsCategoryPage> crsPages = new ArrayList<CrsCategoryPage>();

		Iterator<JsonNode> iterator = tree.elements();
		while(iterator.hasNext())
		{
			
			Iterator<JsonNode> i = iterator.next().elements();
			while(i.hasNext())
			{
				JsonNode pageNode = i.next();
				CrsCategoryPage page = processJson(pageNode);
				if (page != null)
					crsPages.add(page);
			}
				
		}
		return crsPages;
	}
	
	/***
	 * Iterates through the JSON data, creating a new Product object
	 * @param node JSON snippet associated with a single product's data 
	 * @return Recipe - the recipe associated with the current node
	 */
	
	private CrsCategoryPage processJson(JsonNode node)
	{
		CrsCategoryPage page = new CrsCategoryPage();
		String title = node.get("title").asText();
		page.setTitle(title);
		page.setBodyHtml(node.get("body_html").asText());
		page.setBodyText(node.get("body_text").asText());
		page.setUrl(node.get("page_url").asText());
		List<CrsCategoryPage> subCategoryPages = getSubCategoryPages(node.get("sub_categories"), page);
		if (subCategoryPages != null && subCategoryPages.size() > 0)
			page.setChildren(subCategoryPages);

		return page;
	}
	

	private List<CrsCategoryPage> getSubCategoryPages(JsonNode node, CrsCategoryPage parent)
	{
		ArrayList<CrsCategoryPage> subPages = new ArrayList<CrsCategoryPage>();

		if(node == null || node.isNull())
			return null;
		Iterator<JsonNode> i = node.elements();
		while (i.hasNext())
		{
			JsonNode curNode = i.next();
			CrsCategoryPage curSubPage = new CrsCategoryPage();
			if(!(curNode.has("title") && curNode.has("page_url")))
			{
				System.out.println("Node Error: " + curNode);
				continue;
			}
			//System.out.println("Page: " + curNode.get("title").asText() + " URL: " + curNode.get("page_url").asText());
			curSubPage.setParent(parent);
			curSubPage.setTitle(curNode.get("title").asText());
			curSubPage.setUrl(curNode.get("page_url").asText());
			
			if(curNode.has("header_image"))
				curSubPage.setHeaderImageUrl(curNode.get("header_image").asText());
			curSubPage.setBodyHtml(curNode.get("body_html").asText());
			curSubPage.setBodyText(curNode.get("body_text").asText());
	
			if(curNode.has("tertiary_pages"))
			{
				JsonNode tPageNode = curNode.get("tertiary_pages");
				List<CrsCategoryPage> tertiaryPages = getSubCategoryPages(tPageNode, curSubPage);
				curSubPage.setChildren(tertiaryPages);
			}
			subPages.add(curSubPage);
		}
		
		return subPages;
	}
}
