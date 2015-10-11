package com.enavigo.doleloader.mapper;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.enavigo.doleloader.pojo.PressRelease;
import com.fasterxml.jackson.databind.JsonNode;

public class DoleDotComPressReleaseMapper implements JsonMapper {

	@Override
	public Object mapJson(JsonNode tree) {
		List<PressRelease> releases = new ArrayList<PressRelease>();

		Iterator<JsonNode> iterator = tree.elements();
		while(iterator.hasNext())
		{
			
			JsonNode root = iterator.next();
			Iterator<JsonNode> i = root.elements();
			while(i.hasNext())
			{
				JsonNode releaseNode = i.next();
				
				PressRelease release = processJson(releaseNode);
				
				if (release != null)
					releases.add(release);
			}
				
		}
		return releases;
	}
	
	/***
	 * Iterates through the JSON data, creating a new PressRelease object
	 * @param node JSON snippet associated with a single product's data 
	 * @return PressRelease - the press release associated with the current node
	 */
	
	private PressRelease processJson(JsonNode node)
	{
		PressRelease pr = new PressRelease();
		
		if(node.get("title") == null) return null;
		
		System.out.println("Title: " + node.get("title").asText());
		pr.setTitle(node.get("title").asText());
		pr.setUrl(node.get("url").asText());
		pr.setBodyHtml(node.get("page_html").asText());
		pr.setBodyHtml(node.get("page_text").asText());
		
		String dateStr = node.get("date").asText();
		pr.setParsedLocalDate(getPressReleaseDate(dateStr));
		
		
		return pr;
	
	}
	
	
	private LocalDate getPressReleaseDate(String releaseDateText)
	{
		
		LocalDate result = null;
		DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd");
		
		String rawDateString = releaseDateText.substring(releaseDateText.lastIndexOf(" ")+1);
		
		Pattern p = Pattern.compile("\\d{8}");
		Matcher m = p.matcher(releaseDateText);
		
		while(m.find())
		{
			rawDateString = m.group();
		}
		
		try
		{
			result = LocalDate.parse(rawDateString, formatter);
			System.out.println("Source: " + releaseDateText);
			System.out.println("Parsed: " + result.getYear() + "/" + result.getMonthValue() + "/" + result.getDayOfMonth());
		}
		catch (DateTimeParseException dtpe)
		{
			System.out.println("********  Failed to parse date " + releaseDateText + " ************");
		}
		
		return result;
	}
}
