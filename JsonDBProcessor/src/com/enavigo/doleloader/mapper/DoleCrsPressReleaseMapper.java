package com.enavigo.doleloader.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;







import org.apache.commons.lang3.ArrayUtils;

import com.enavigo.doleloader.pojo.CrsCategoryPage;
import com.enavigo.doleloader.pojo.CrsPressRelease;
import com.enavigo.doleloader.pojo.Recipe;
import com.fasterxml.jackson.databind.JsonNode;

public class DoleCrsPressReleaseMapper implements JsonMapper {

	@Override
	public Object mapJson(JsonNode tree) {
		List<CrsPressRelease> releases = new ArrayList<CrsPressRelease>();

		Iterator<JsonNode> iterator = tree.elements();
		while(iterator.hasNext())
		{
			
			Iterator<JsonNode> i = iterator.next().elements();
			while(i.hasNext())
			{
				JsonNode yearNode = i.next();
				processYear(yearNode, releases);
			}
				
		}
		return releases;
	}
	
	/**
	 * Processes a year of press releases
	 * @param yearNode a node containing a collection of press release nodes
	 * @param releases the collection of press releases
	 */
	private void processYear(JsonNode yearNode, List<CrsPressRelease> releases)
	{
		Iterator<JsonNode> releaseIterator = yearNode.get("release").elements();
		int releaseYear = yearNode.get("year").asInt();
		while (releaseIterator.hasNext())
		{
			JsonNode releaseNode = releaseIterator.next();
			CrsPressRelease release = processRelease(releaseNode);
			release.setYear(releaseYear);
			releases.add(release);
		}
	}
	
	/**
	 * Creates a press release object from a Json Node
	 * @param releaseNode The node containing the press release
	 * @return A press release object containing the release information.
	 */
	private CrsPressRelease processRelease(JsonNode releaseNode)
	{
		CrsPressRelease release = new CrsPressRelease();
		release.setTitle(releaseNode.get("title").asText());
		if(releaseNode.has("subtitle"))
			release.setSubtitle(releaseNode.get("subtitle").asText());
		release.setUrl(releaseNode.get("page_url").asText());
		release.setBodyText(getBodyText(releaseNode.get("body_text")));
		release.setRelatedPosts(getRelatedPosts(releaseNode.get("related_posts")));
		return release;
	}
	
	/**
	 * Creates a single string out of a collection of paragraphs
	 * @param paragraphs Node containing a collection of string nodes holding the body text. 
	 * Paragraphs will be separated by a newline character.
	 * @return String with all of the text contained in the paragraphs.
	 */
	private String getBodyText(JsonNode paragraphs)
	{
		String bodyText = new String();
		Iterator<JsonNode> i = paragraphs.elements();
		while (i.hasNext())
		{
			JsonNode paragraph = i.next();
			bodyText = bodyText.concat(paragraph.get("paragraph").asText()).concat("\n");
		}
		
		return bodyText;
	}
	
	/**
	 * Returns a list containing related post information. 
	 * @param relatedPostsNode Node containing a collection of related posts
	 * @return list containing the related posts
	 */
	private ArrayList<HashMap<String, String>> getRelatedPosts(JsonNode relatedPostsNode)
	{
		ArrayList<HashMap<String, String>> posts = new ArrayList<HashMap<String, String>>();
		Iterator<JsonNode> postsIterator = relatedPostsNode.elements();
		
		while(postsIterator.hasNext())
		{
			JsonNode rp = postsIterator.next();
			HashMap<String, String> postMap = new HashMap<String, String>();
			postMap.put("title", rp.get("title").asText());
			postMap.put("url", rp.get("url").asText());
			posts.add(postMap);
		}
		
		return posts;
	}
}
