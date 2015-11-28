package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.enavigo.doleloader.DoleLoaderConstants;
import com.enavigo.doleloader.pojo.CrsCategoryPage;
import com.enavigo.doleloader.pojo.Product;
import com.enavigo.doleloader.pojo.Recipe;

public class DoleCrsPagePersistor implements DoleJsonPersistor {
	
	Connection connection = null;
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public boolean persist(Connection connection, Object objToPersist,
			char sourceSite) throws SQLException {
		
		this.connection = connection;
		
		List<CrsCategoryPage> pages = (List<CrsCategoryPage>)objToPersist;
		int nextCategoryPageId = DoleJsonPersistenceUtils.getMaxId(connection, "crs_category_page", "cat_page_id") + 1;

		for(CrsCategoryPage page : pages)
		{
			if(!crsPageExists(connection, page.getUrl()))
			{
				
				persistPage(connection, page, nextCategoryPageId, 0);
				nextCategoryPageId = DoleJsonPersistenceUtils.getMaxId(connection, "crs_category_page", "cat_page_id") + 1;
			}
		}
		
		return false;
	}

	/***
	 * Stores a recipe in the database
	 * @param connection the database connection
	 * @param r the recipe to store
	 * @param recipeId the id to assign to the recipe in the database
	 * @param sourceSite the site from which the recipe was extracted
	 * @throws SQLException
	 */
	private int persistPage(Connection connection, CrsCategoryPage r, 
					int pageId, int parentPageId) throws SQLException 
	{
		/*
		 *
			"INSERT INTO crs_page (page_id, title, header_image_url, body_html," +
					"body_text, page_url, parent_page_id)" +
					"VALUES (" + 
					"?, ?, ?, ?, ?, ?, ?)";
		 */
		
		pageId = DoleJsonPersistenceUtils.getMaxId(connection, "crs_page", "page_id") + 1;
		System.out.println("Persisting: " + r.getTitle());
		PreparedStatement query = 
				connection.prepareStatement(DoleLoaderConstants.CRS_PAGE_INSERT);
		query.setInt(1, pageId);
		query.setString(2, r.getTitle());
		if(r.getHeaderImageUrl() != null)
			query.setString(3, r.getHeaderImageUrl());
		else
			query.setNull(3, java.sql.Types.NULL);
		query.setString(4, r.getBodyHtml());
		query.setString(5, r.getBodyText());
		query.setString(6, r.getUrl());
		if(parentPageId != 0)
		{
			query.setInt(7, parentPageId);
		}
		else
		{
			query.setNull(7, java.sql.Types.NULL);
		}
		try
		{
			int result = query.executeUpdate();
		}
		catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException micve)
		{
			logger.warning("Duplicate: " + micve.getLocalizedMessage() + "\n" + r.getUrl());
		}
		
		query.close();

		parentPageId = pageId;
		
		// persist children recursively
		List<CrsCategoryPage> children = r.getChildren();
		for(CrsCategoryPage child : children)
		{
			pageId = DoleJsonPersistenceUtils.getMaxId(connection, "crs_page", "page_id") + 1;
			persistPage(connection, child, pageId, parentPageId);
		}
		
		return DoleJsonPersistenceUtils.getMaxId(connection, "crs_page", "page_id") + 1;
	}
	
	/**
	 * Checks whether an recipe already exists in the database by comparing its URL to all existing recipes
	 * @param connection The database connection
	 * @param recipeUrl The URL of the recipe we're checking for existence.
	 * @return true if the recipe already exists; false otherwise
	 * @throws SQLException
	 */
	private boolean crsPageExists(Connection connection, String pageUrl) throws SQLException
	{
		boolean doesPageExist = false;
		
		PreparedStatement query = 
				connection.prepareStatement(DoleLoaderConstants.GET_PAGE_FOR_URL);
		query.setString(1, pageUrl);
		ResultSet r = query.executeQuery();
		if(r.next())
		{
			doesPageExist = true;
		}
		query.close();
		
		return doesPageExist;
	}

}
