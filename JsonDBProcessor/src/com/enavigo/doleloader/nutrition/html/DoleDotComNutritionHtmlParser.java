/**
 * 
 */
package com.enavigo.doleloader.nutrition.html;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author yuvalzukerman
 *
 */
public class DoleDotComNutritionHtmlParser {
	
	public static List<HashMap<String, Object>> parseNutritionalInformation(String sourceHtml) 
							
	{
		List<HashMap<String, Object>> nutrientResult = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> topResult = new HashMap<String, Object>(); // top part of the label, containing constant values
		HashMap<String, Object> bottomResult =  new HashMap<String, Object>(); 
		try
		{
			sourceHtml = sourceHtml.replaceAll("&nbsp;", " ");
			sourceHtml = sourceHtml.replaceAll("<br>", "\n");
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);
			dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(sourceHtml.getBytes()));
			System.out.println("Parsed");
			
			NodeList spans = doc.getElementsByTagName("span");
			System.out.println("Span:" + spans.item(2).getTextContent());
			String servingsLine = spans.item(2).getTextContent();
			if(servingsLine != null)
			{
				servingsLine = servingsLine.replace("Serving Size ", "");
				int closeParenIndex = servingsLine.indexOf(")");
				topResult.put("serving_size", servingsLine.substring(0, closeParenIndex+1));
				servingsLine = servingsLine.substring(closeParenIndex);
				if(servingsLine.length() > 3)
				{
					topResult.put("servings_per_container", getDoubleValue(servingsLine, ""));
				}
			}
			
			NodeList tableRows = doc.getElementsByTagName("tr");
//			for(int tr = 0; tr < tableRows.getLength(); tr++)
//			{
//				Node curTr = tableRows.item(tr);
//				System.out.println("Item [" + tr + "]: " + curTr.getTextContent());
//			}
			
			/*
			Node servingSizeNode = doc.getElementsByTagName("h6").item(0);
			if (servingSizeNode != null)
			{
				topResult.put("serving_size", servingSizeNode.getTextContent().replace("Serving Size ", "")); 
			}
			Node servingsPerContainer = doc.getElementsByTagName("h7").item(0);
			if (servingsPerContainer != null)
			{
				String spc = servingsPerContainer.getTextContent().replace("Servings Per Container ", "");
				topResult.put("servings_per_container", getDoubleValue(spc, ""));
			}
			
			
			NodeList nutrientList = doc.getElementsByTagName("li");
			for(int x = 0; x < nutrientList.getLength(); x++)
				System.out.println("Node " + x + " value: '" + nutrientList.item(x).getTextContent() +"'");
			
//			for(int x = 0; x < nutrientList.item(1).getChildNodes().getLength(); x++)
//			{
//				System.out.println("Node " + x + " type: " + nutrientList.item(1).getChildNodes().item(x).getNodeType());
//				System.out.println("Node " + x + " value: '" + nutrientList.item(1).getChildNodes().item(x).getTextContent()+"'");
//			}
			
			// calories
			NodeList calorieList = nutrientList.item(1).getChildNodes();
			int calories = getNumericValue(calorieList.item(1).getTextContent(), null);
			int caloriesFromFat = getNumericValue(calorieList.item(3).getTextContent(), null);
			if(calories >= 0)
			{
				topResult.put("calories", calories);
				topResult.put("calories_from_fat", caloriesFromFat);
			}
			
			// fat
			String totalFatStr = nutrientList.item(3).getTextContent();
			int totalFat = getNumericValue(totalFatStr, "g*");
			int totalFatPercent = getNumericValue(totalFatStr, "%");
			if(totalFat >= 0 || totalFatPercent >= 0)
			{
				topResult.put("total_fat", totalFat);
				topResult.put("total_fat_percent", totalFatPercent);
			}
			
			// saturated fat
			String satFatStr = nutrientList.item(4).getTextContent();
			double satFat = getDoubleValue(satFatStr, "g*");
			int satFatPercent = getNumericValue(satFatStr, "%");
			if(satFat >= 0.0)
			{
				topResult.put("saturated_fat", satFat);
				topResult.put("saturated_fat_percent", satFatPercent);
			}
			
			// transfat
			String tFatStr = nutrientList.item(5).getTextContent();
			int tFat = getNumericValue(tFatStr, "g");
			if(tFat >= 0)
			{
				topResult.put("trans_fat", tFat);
			}
			
			// cholesterol
			String curStr = nutrientList.item(6).getTextContent();
			int gramVal = getNumericValue(curStr, "mg");
			int percentVal = getNumericValue(curStr, "%");
			if(gramVal >= 0)
			{
				topResult.put("cholesterol_mg", gramVal);
				topResult.put("cholesterol_percent", percentVal);
			}
			else 
			{
				gramVal = getNumericValue(curStr, "g");
				topResult.put("cholesterol_mg", gramVal);
				topResult.put("cholesterol_percent", percentVal);
			}
			
			// sodium
			curStr = nutrientList.item(7).getTextContent();
			gramVal = getNumericValue(curStr, "m*g");
			percentVal = getNumericValue(curStr, "%");
			if(gramVal >= 0)
			{
				topResult.put("sodium_mg", gramVal);
				topResult.put("sodium_percent", percentVal);
			}
			
			// potassium
//			curStr = nutrientList.item(8).getTextContent();
//			gramVal = getNumericValue(curStr, "g");
//			percentVal = getNumericValue(curStr, "%");
//			if(gramVal >= 0)
//			{
//				topResult.put("potassium_g", gramVal);
//				topResult.put("potassium_percent", percentVal);
//			}	
			// carbs
			curStr = nutrientList.item(9).getTextContent();
			gramVal = getNumericValue(curStr, "g");
			percentVal = getNumericValue(curStr, "%");
			if(gramVal >= 0)
			{
				topResult.put("total_carbs", gramVal);
				topResult.put("total_carbs_percent", percentVal);
			}
			// fiber
			curStr = nutrientList.item(10).getTextContent();
			gramVal = getNumericValue(curStr, "g");
			percentVal = getNumericValue(curStr, "%");
			if(gramVal >= 0)
			{
				topResult.put("dietary_fiber", gramVal);
				topResult.put("dietary_fiber_percent", percentVal);
			}
			// sugars
			curStr = nutrientList.item(11).getTextContent();
			gramVal = getNumericValue(curStr, "g");
			if(gramVal >= 0)
			{
				topResult.put("sugars", gramVal);
			}
			// protein
			curStr = nutrientList.item(12).getTextContent();
			gramVal = getNumericValue(curStr, "g");
			if(gramVal >= 0)
			{
				topResult.put("protein", gramVal);
			}
			
			String varNutrients = nutrientList.item(13).getTextContent().trim();
			String[] varNutrientArray = varNutrients.split("\n");
			String nutName = null;
			int nutPercent = 0;
			for (String curNutrient : varNutrientArray)
			{
				if(curNutrient.contains("%"))
				{
					nutPercent = Integer.parseInt(curNutrient.replaceAll("\\D", ""));
					nutName = curNutrient.replaceAll("\\s\\d*%","").trim();
					bottomResult.put(nutName, nutPercent);
				}
			}*/
			
		}
		catch (Exception e) // throws ParserConfigurationException, IOException, SAXException
		{
			e.printStackTrace();
		}
		
		nutrientResult.add(topResult);
		nutrientResult.add(bottomResult);
		
		return nutrientResult;
	}
	
	/***
	 * Returns the numeric value in a string
	 * @param source the string to extract the numeric value from
	 * @param type 'g' - gram value, '%' - percent value, 'n' - none
	 * @return
	 */
	private static int getNumericValue(String source, String type)
	{
		int value = -1;
		String patternString = "\\d+\\.*\\d*"; //\\d+\\.*\\d*m*g
		if(type != null)
			patternString = patternString.concat(type);
		Pattern p = Pattern.compile(patternString);
		Matcher m = p.matcher(source);
		if(m.find())
		{
			String result = m.group();
			//remove characters in case we have them
			result = result.replaceAll("\\D", "");
			value = Integer.parseInt(result);
		}		
		
		return value;
	}
	
	private static double getDoubleValue(String source, String type)
	{
		double value = -1;
		String patternString = "\\d+\\.*\\d*"; //\\d+\\.*\\d*m*g
		if(type != null)
			patternString = patternString.concat(type);
		Pattern p = Pattern.compile(patternString);
		Matcher m = p.matcher(source);
		if(m.find())
		{
			String result = m.group();
			//remove characters in case we have them
			result = result.replaceAll("[a-z]", "");
			value = Double.parseDouble(result);
		}		
		
		return value;
	}
	
	
}
