package com.enavigo.doleloader;

public class DoleLoaderConstants {
	
	public static String PRODUCT_PERSIST_GET_MAX_ID = 
			"SELECT MAX(product_id) AS max_product_id FROM ";
	
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


}
