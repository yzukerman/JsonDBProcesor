package com.enavigo.doleloader.test;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.enavigo.doleloader.nutrition.html.DoleDotComNutritionHtmlParser;

public class DoleDotComNutritionHtmlLoaderTest {

	String sourceHtml = "";
	List<HashMap<String, Object>> result = null;
	
	@Before
	public void setUp() throws Exception {
		Path path = Paths.get("/Users/yuvalzukerman/Dropbox/Enavigo/Clients/Dole/Nutrition/ddc-raisins.html");
		List<String> lines = Files.readAllLines(path);
		for (String x : lines)
			sourceHtml = sourceHtml + x + "\n";
		//System.out.println(sourceHtml);
		result = DoleDotComNutritionHtmlParser.parseNutritionalInformation(sourceHtml.trim());
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testCalories() {
		
		HashMap<String, Object> topResult = result.get(0);
		int calories = (Integer)topResult.get("calories");
		int caloriesFromFat = (Integer)topResult.get("calories_from_fat");
		System.out.println("Calories: " + calories);
		assertTrue(calories == 20);
		assertTrue(caloriesFromFat == 0);
	}
	
	@Test
	public void testServingSize() {
		
		HashMap<String, Object> topResult = result.get(0);
		String value = (String)topResult.get("serving_size");
		//System.out.println("Calories: " + calories);
		assertTrue(value.equals("3 oz. (85g/about 1-1/2 cups)"));
	}
	
	@Test
	public void testTotalFat() {
		
		HashMap<String, Object> topResult = result.get(0);
		System.out.println(topResult);
		int value = (Integer)topResult.get("total_fat");
		int value2 = (Integer)topResult.get("total_fat_percent");
		//System.out.println("Calories: " + calories);
		assertTrue(value == 0);
		assertTrue(value2 == 0);
	}
	
	@Test
	public void testSatFat() {
		
		HashMap<String, Object> topResult = result.get(0);
		System.out.println(topResult);
		int value = (Integer)topResult.get("saturated_fat");
		int value2 = (Integer)topResult.get("saturated_fat_percent");
		//System.out.println("Calories: " + calories);
		assertTrue(value == 0);
		assertTrue(value2 == 0);
		System.out.println(result.get(1));
	}

}
