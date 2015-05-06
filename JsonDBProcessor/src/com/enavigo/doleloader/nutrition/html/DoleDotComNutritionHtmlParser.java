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
			sourceHtml = sourceHtml.replaceAll("<br>", "");
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);
			dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(sourceHtml.getBytes()));
			System.out.println("Parsed");
			
			NodeList spans = doc.getElementsByTagName("span");
			System.out.println("Span:" + spans.item(2).getTextContent());
			if(spans.item(2).getChildNodes().getLength() > 3) return null;
			String servingsLine = spans.item(2).getTextContent();
			if(servingsLine != null)
			{
				servingsLine = servingsLine.replace("Serving Size ", "");
				int closeParenIndex = servingsLine.indexOf("Servings Per");
				if(closeParenIndex > 0)
				{
					topResult.put("serving_size", servingsLine.substring(0, closeParenIndex).trim());
					servingsLine = servingsLine.substring(closeParenIndex);
					if(servingsLine.length() > 3)
					{
						servingsLine = servingsLine.replace("Servings Per Container", "");
						topResult.put("servings_per_container", getDoubleValue(servingsLine, ""));
					}
				}
				else
					topResult.put("serving_size", servingsLine.trim());
			}
			
			NodeList tdNodes = doc.getElementsByTagName("td");
			
			// calories
			int value = getNumericValue(tdNodes.item(2).getTextContent(), null);
			topResult.put("calories", value);
			value = getNumericValue(tdNodes.item(3).getTextContent(), null);
			topResult.put("calories_from_fat", value);
			
			// total fat
			Double dvalue = getDoubleValue(tdNodes.item(4).getTextContent(), "g*");
			topResult.put("total_fat", dvalue);
			value = getNumericValue(tdNodes.item(5).getTextContent(), "%");
			topResult.put("total_fat_percent", value);
			
			// saturated fat
			
			dvalue = getDoubleValue(tdNodes.item(6).getTextContent(), "g*");
			topResult.put("saturated_fat", dvalue);
			value = getNumericValue(tdNodes.item(7).getTextContent(), "%");
			topResult.put("saturated_fat_percent", value);
			
			// trans fat
			value = getNumericValue(tdNodes.item(8).getTextContent(), "g*");
			topResult.put("trans_fat", value);
			value = getNumericValue(tdNodes.item(9).getTextContent(), "%");
			topResult.put("trans_fat_percent", value);
			
			// cholesterol
			value = -1;
			value = getNumericValue(tdNodes.item(10).getTextContent(), "mg");
			if(value < 0)
			{
				value = getNumericValue(tdNodes.item(10).getTextContent(), "g");
			}
			topResult.put("cholesterol_mg", value);
			value = getNumericValue(tdNodes.item(11).getTextContent(), "%");
			topResult.put("cholesterol_percent", value);	
			
			// sodium
			value = getNumericValue(tdNodes.item(12).getTextContent(), "mg");
			topResult.put("sodium_mg", value);
			value = getNumericValue(tdNodes.item(13).getTextContent(), "%");
			topResult.put("sodium_percent", value);
			
			// potassium
			value = getNumericValue(tdNodes.item(14).getTextContent(), "g");
			if(value == -1)
				value = getNumericValue(tdNodes.item(14).getTextContent(), "mg");
			topResult.put("potassium_g", value);
			value = getNumericValue(tdNodes.item(15).getTextContent(), "%");
			topResult.put("potassium_percent", value);	
			
			// carbs
			value = getNumericValue(tdNodes.item(16).getTextContent(), "g");
			topResult.put("total_carbs", value);
			value = getNumericValue(tdNodes.item(17).getTextContent(), "%");
			topResult.put("total_carbs_percent", value);				

			// fiber
			value = getNumericValue(tdNodes.item(18).getTextContent(), "g");
			topResult.put("dietary_fiber", value);
			value = getNumericValue(tdNodes.item(19).getTextContent(), "%");
			topResult.put("dietary_fiber_percent", value);
			
			// sugars
			value = getNumericValue(tdNodes.item(20).getTextContent(), "g");
			topResult.put("sugars", value);
			
			// protein
			value = getNumericValue(tdNodes.item(22).getTextContent(), "g");
			topResult.put("protein", value);
			
			// variable nutrients
			Node varNode = doc.getElementsByTagName("table").item(1).getChildNodes().item(1);
			String varValues = varNode.getTextContent().trim();
			String[] varStrs = varValues.split("\n");
			int nutPercent = 0;
			String nutName = null;
			
			for(String curNutrient : varStrs)
			{
				curNutrient = curNutrient.trim();
				System.out.println("CurNutrients: '"+ curNutrient + "'");
				if(curNutrient.length() > 1)
				{
					nutPercent = Integer.parseInt(curNutrient.replaceAll("\\D", ""));
					nutName = curNutrient.replaceAll("\\s\\d*%*","").trim();
					bottomResult.put(nutName, nutPercent);
				}
			}
			
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
