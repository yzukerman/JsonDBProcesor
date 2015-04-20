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
}
