package com.enavigo.doleloader.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;  

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.enavigo.doleloader.mapper.DoleDotComRecipeMapper;
import com.enavigo.doleloader.pojo.Recipe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestRecipeLoader {

	Connection connection;
	ObjectMapper mapper;
	JsonNode tree;
	String src;
	
	@Before
	public void setUp() throws Exception {
		connection = mock(Connection.class);
		mapper = new ObjectMapper();
		tree = mapper.readTree(new File("/Users/yuvalzukerman/Dropbox/Enavigo/Clients/Dole/Extracts/Dole.com/recipes/test-recipe.json"));
		src = "{\"recipes\":[{\"Category\":\"Dessert\",\"title\":\"Pineapple Almond Torte\",\"image\":\"http://dolerecipe.dole.com/RecipeImages/3437/15-010%20Pineapple_Almond_Torte%201000x383635567523234985638.jpg\",\"ingredient_count\":\"10\",\"prep_value\":\"20\",\"prep_unit\":\"minutes\",\"total_time_value\":\"1\",\"total_time_unit\":\"hours\",\"difficulty_description\":\"Easy\",\"difficulty_value\":\"1.0\",\"calories\":\"0\",\"servings\":\"12\",\"about_text\":\"A gluten-free cake featuring pineapple and almonds.\",\"ingredients\":[{\"ingredient\":\"egg(s)\",\"quantity\":\"5\"},{\"ingredient\":\"sugar, divided\",\"quantity\":\"3/4 cup, plus 1 tablespoon\"},{\"ingredient\":\"almond extract\",\"quantity\":\"1/2 teaspoon\"},{\"ingredient\":\"almond meal\",\"quantity\":\"2-1/3 cups\"},{\"ingredient\":\"baking powder\",\"quantity\":\"1-1/2 teaspoons\"},{\"ingredient\":\"salt\",\"quantity\":\"1/4 teaspoon\"},{\"ingredient\":\"DOLE® Crushed Pineapple, drained, reserve juice\",\"quantity\":\"1 can (20 oz.)\"},{\"ingredient\":\"DOLE Crushed Pineapple\",\"quantity\":\"1 can (8 oz.)\"},{\"ingredient\":\"cornstarch\",\"quantity\":\"1 teaspoon\"},{\"ingredient\":\"orange liqueur (optional)\",\"quantity\":\"1 tablespoon\"}],\"steps\":[{\"direction\":\"Preheat oven to 375°F. Spray 9 to 10-inch springform pan with cooking spray and line bottom with parchment or wax paper.\",\"ingredient\":\"\"},{\"direction\":\"Combine eggs, 1/4 cup reserved pineapple juice, sugar, and almond extract in large bowl. Mix with hand mixer for two minutes.\",\"ingredient\":\"5 eggs b>¾ cup sugarb>½ tsp almond extract\"},{\"direction\":\"Stir baking powder, almond meal, and salt in a separate bowl with a whisk. Combine dry ingredients into bowl with wet ingredients. Stir in drained pineapple and mix well.\",\"ingredient\":\"\"},{\"direction\":\"Pour batter into pan and bake for 40 minutes, or until toothpick inserted into the center of the cake comes out clean. Cool cake completely on a wire rack.\",\"ingredient\":\"\"},{\"direction\":\"Combine undrained pineapple, sugar, cornstarch, and orange liqueur in medium saucepan. Bring to boil, stirring occasionally until sauce thickens.\",\"ingredient\":\"\"},{\"direction\":\"\",\"ingredient\":\"\"},{\"direction\":\"\",\"ingredient\":\"\"},{\"direction\":\"Remove springform ring and spread topping on top of cake. Garnish with sliced almonds, if desired.\",\"ingredient\":\"\"}],\"page_url\":\"http://www.dole.com/Recipes/Pineapple-Almond-Torte\"}]}";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws JsonProcessingException, IOException {
		
		
		
		
		DoleDotComRecipeMapper ddcrm = new DoleDotComRecipeMapper();
		Object result = ddcrm.mapJson(tree);
		List<Recipe> recipes = (List<Recipe>) result;
		assertTrue(recipes.size() == 1);
		Recipe testRecipe = recipes.get(0);
		assertTrue(testRecipe.getRecipeSteps().size() == 6);
		List<HashMap<String, Object>> steps = testRecipe.getRecipeSteps();
		assertNull(steps.get(0).get("ingredients"));
		String[] stepIngredients = (String[])steps.get(2).get("ingredients");
		assertNotNull(stepIngredients);
		assertTrue(stepIngredients.length == 3);
	}
	
	

}
