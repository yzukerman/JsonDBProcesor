package com.enavigo.doleloader;

public class DoleLoaderConstants {
	
	public static String PRODUCT_PERSIST_INSERT_NO_NUTRITION = 
			"INSERT INTO product (product_id, title, url, category," +
					"subcategory, source_site, image_url, stat1, stat2, stat3, description," +
					"nutrition_label_html)" +
					"VALUES (" + 
					"?, ?, ?, ?, ?," +
					"?, ?, ?, ?, ?," +
					"?, ?)";
	
	public static String PRODUCT_RELATED_RECIPE_PERSIST_INSERT = 
			"INSERT INTO product_related_recipe (product_related_recipe_id,"
			+ "product_id, related_recipe_url, related_recipe_title, "
			+ "related_recipe_image_url) VALUES "
			+ "(?, ?, ?, ?, ?)";

	public static String PRODUCT_INGREDIENT_INSERT = 
			"INSERT INTO product_ingredient" 
			+ "(product_ingredient_id,"
			+ "product_id,"
			+ "ingredient_title)"
			+ "VALUES (?, ?, ?)";	
	
	public static String PRODUCT_RELATED_PRODUCT_INSERT = 
			"INSERT INTO related_product"
			+ "(rp_id,"
			+ "owner_product_id,"
			+ "related_product_url,"
			+ "related_product_title,"
			+ "related_product_image)"
			+ "VALUES (?,?,?,?,?)";
	
	public static String PRODUCT_RELATED_ARTICLE_INSERT = 
			"INSERT INTO product_related_article"
			+ "(related_article_id,"
			+ "product_id,"
			+ "title,"
			+ "url) "
			+ "VALUES (?,?,?,?)";
	
	public static String PRODUCT_BENEFIT_INSERT = 
			"INSERT INTO product_benefit"
			+ "(product_benefit_id,"
			+ "product_id,"
			+ "benefit) "
			+ "VALUES (?,?,?)";
	
	public static String RECIPE_INSERT_NO_NUTRITION = 
			"INSERT INTO recipe"
			+ "(recipe_id, source_site_id, title, url,"
			+ "category, subcategory, "
			+ "prep_value, prep_unit, total_time_value, total_time_unit,"
			+ "servings, calories, description, difficulty_description,"
			+ "difficulty_value, image_url, vegetable_servings, nutrition_facts_html, cook_time_value, cook_time_unit)"
			+ " VALUES ("
			+ "?, ?, ?, ?, ?," +
			"?, ?, ?, ?, ?," +
			"?, ?, ?, ?, ?," +
			"?, ?, ?, ?, ?)";
	
	public static String RECIPE_RELATED_RECIPE_INSERT = 
			"INSERT INTO related_recipe (rr_id,"
			+ "recipe_id, related_recipe_url, related_recipe_title, "
			+ "related_recipe_image_url) VALUES "
			+ "(?, ?, ?, ?, ?)";
	
	public static String RECIPE_STEP_INSERT = 
			"INSERT INTO recipe_step (recipe_step_id,"
			+ "recipe_id, step_description"
			+ ") VALUES "
			+ "(?, ?, ?)";
	
	public static String RECIPE_STEP_INGREDIENT_INSERT = 
			"INSERT INTO recipe_step_ingredient (recipe_step_ingredient_id, recipe_step_id,"
			+ "ingredient"
			+ ") VALUES "
			+ "(?, ?, ?)";
	
	public static String RECIPE_INGREDIENT_INSERT = 
			"INSERT INTO recipe_ingredient (recipe_ingredient_id, recipe_recipe_id,"
			+ "recipe_ingredient_string, recipe_ingredient_title, recipe_ingredient_quantity"
			+ ") VALUES "
			+ "(?, ?, ?, ?, ?)";
	
	public static String ARTICLE_INSERT = 
			"INSERT INTO article (article_id, title, body,"
			+ "category, byline, source_site, url, image_url, publish_date, subhead"
			+ ") VALUES "
			+ "(?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?)";
	
	public static String ARTICLE_RELATED_RECIPE_INSERT =
			"INSERT INTO article_related_recipe"
			+ "(article_related_recipe_id, article_id, related_recipe_title, related_recipe_url)"
			+ " VALUES "
			+ "(?, ?, ?, ?)";
	
	public static String ARTICLE_TAG_INSERT =
			"INSERT INTO article_tag"
			+ "(article_tag_id, article_id, tag_text)"
			+ " VALUES "
			+ "(?, ?, ?)";
	
	public static String UPDATE_RECIPE_NUTRIENTS = 
			"UPDATE recipe SET "
			+ "serving_size = ?, "
			+ "calories_from_fat = ?, "
			+ "calories = ?, "
			+ "total_fat_grams = ?,"
			+ "total_fat_percent = ?, "
			+ "saturated_fat_grams = ?,"
			+ "saturated_fat_percent = ?, "
			+ "trans_fat_grams = ?, "
			+ "cholesterol_mg = ?, "
			+ "cholesterol_percent = ?,"
			+ "sodium_mg = ?, "
			+ "sodium_percent = ?, "
			+ "total_carbs_grams = ?, "
			+ "total_carbs_percent = ?, "
			+ "fiber_grams = ?, "
			+ "fiber_percent = ?,"
			+ "protein_grams = ?, "
			+ "sugars_grams = ?, "
			+ "servings_per_container = ?"
			+ " WHERE recipe_id = ?";
	
	public static String RECIPE_INSERT_NUTRIENT = 
			"INSERT INTO recipe_nutrient "
			+ "(recipe_nutrient_id, recipe_recipe_id, title, percentage)"
			+ " VALUES "
			+ "(?, ?, ?, ?)";
	
	public static String UPDATE_PRODUCT_NUTRIENTS = 
			"UPDATE product SET "
			+ "serving_size = ?, "
			+ "calories_from_fat = ?, "
			+ "calories = ?, "
			+ "total_fat_grams = ?,"
			+ "total_fat_percent = ?, "
			+ "saturated_fat_grams = ?,"
			+ "saturated_fat_percent = ?, "
			+ "trans_fat_grams = ?, "
			+ "cholesterol_mg = ?, "
			+ "cholesterol_percent = ?,"
			+ "sodium_mg = ?, "
			+ "sodium_percent = ?, "
			+ "total_carbs_grams = ?, "
			+ "total_carbs_percent = ?, "
			+ "fiber_grams = ?, "
			+ "fiber_percent = ?,"
			+ "protein_grams = ?, "
			+ "sugars_grams = ?, "
			+ "servings_per_container = ?,"
			+ "potassium_mg = ?, "
			+ "potassium_percent = ? "
			+ " WHERE product_id = ?";
	
	public static String PRODUCT_INSERT_NUTRIENT = 
			"INSERT INTO product_nutrient "
			+ "(product_nutrient_id, product_product_id, title, percentage)"
			+ " VALUES "
			+ "(?, ?, ?, ?)";
	
	public static String UPDATE_EXCEL_RECIPE_NUTRIENTS = 
			"UPDATE recipe SET "
			+ "serving_size = ?, "
			+ "calories_from_fat = ?, "
			+ "calories = ?, "
			+ "total_fat_grams = ?,"
			+ "total_fat_percent = ?, "
			+ "saturated_fat_grams = ?,"
			+ "saturated_fat_percent = ?, "
			+ "trans_fat_grams = ?, "
			+ "cholesterol_mg = ?, "
			+ "cholesterol_percent = ?,"
			+ "sodium_mg = ?, "
			+ "sodium_percent = ?, "
			+ "total_carbs_grams = ?, "
			+ "total_carbs_percent = ?, "
			+ "fiber_grams = ?, "
			+ "fiber_percent = ?,"
			+ "protein_grams = ?, "
			+ "sugars_grams = ?, "
			+ "polyunsat_fat_grams = ?, "
			+ "monounsat_fat_grams = ?, "
			+ "potassium_grams = ?, "
			+ "potassium_percent = ? "
			+ " WHERE title = ?";
	
	public static String GET_RECIPE_IDS_FOR_TITLE = 
			"SELECT recipe_id FROM recipe WHERE title = ?";
	
	
	public static String GET_ARTICLE_FOR_URL =
			"SELECT article_id FROM article WHERE url = ?";
	
	public static String GET_RECIPE_FOR_URL =
			"SELECT recipe_id FROM recipe WHERE url = ?";
	
	public static String GET_PRODUCT_FOR_URL = 
			"SELECT product_id FROM product WHERE url = ?";
	
	public static String GET_RELEASE_FOR_URL = 
			"SELECT release_id FROM press_release WHERE url = ?";
	
	public static String GET_PAGE_FOR_URL = 
			"SELECT page_url FROM crs_page WHERE page_url = ?";
	
	public static String GET_CRS_RELEASE_FOR_URL = 
			"SELECT page_url FROM crs_press_release WHERE page_url = ?";
	
	public static String INSERT_PRESS_RELEASE = 
			"INSERT INTO press_release (release_id, title, url, body_html, body_text, publish_date, source_site_id) "
			+ " VALUES "
			+ " (?, ?, ?, ?, ?, ?, ?)";
	
	public static String CRS_PAGE_INSERT = 
			"INSERT INTO crs_page (page_id, title, header_image_url, body_html," +
					"body_text, page_url, parent_page_id)" +
					"VALUES (" + 
					"?, ?, ?, ?, ?, ?, ?)";
	
	public static String CRS_PRESS_RELEASE_INSERT =
			"INSERT INTO crs_press_release (release_id, title, sub_title, body_text, page_url, year) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
	
	public static String CRS_PRESS_RELEASE_RELATED_STORIES_INSERT =
			"INSERT INTO crs_press_release_related_stories "
			+ " (related_story_id, title, page_url, release_id) "
			+ "VALUES (?, ?, ?, ?)";
	
}
