package com.enavigo.doleloader;

public class DoleLoaderConstants {
	
	public static String PRODUCT_PERSIST_GET_MAX_ID = 
			"SELECT MAX(product_id) AS max_product_id FROM product";
	
	public static String PRODUCT_PERSIST_INSERT_NO_NUTRITION = 
			"INSERT INTO product (product_id, title, url, category," +
					"subcategory, source_site, image_url, stat1, stat2, stat3, description," +
					"nutrition_label_html)" +
					"VALUES (" + 
					"?, ?, ?, ?, ?," +
					"?, ?, ?, ?, ?," +
					"?, ?)";
}
