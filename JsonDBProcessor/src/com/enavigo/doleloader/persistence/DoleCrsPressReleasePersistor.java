package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.enavigo.doleloader.DoleLoaderConstants;
import com.enavigo.doleloader.pojo.CrsCategoryPage;
import com.enavigo.doleloader.pojo.CrsPressRelease;
import com.enavigo.doleloader.pojo.Product;
import com.enavigo.doleloader.pojo.Recipe;

public class DoleCrsPressReleasePersistor implements DoleJsonPersistor {
	
	Connection connection = null;
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public boolean persist(Connection connection, Object objToPersist,
			char sourceSite) throws SQLException {
		
		this.connection = connection;
		
		List<CrsPressRelease> releases = (List<CrsPressRelease>)objToPersist;
		int nextCrsReleaseId = DoleJsonPersistenceUtils.getMaxId(connection, "crs_press_release", "release_id") + 1;
		int nextCrsRelatedStoryId = DoleJsonPersistenceUtils.getMaxId(connection, "crs_press_release_related_stories", "related_story_id") + 1;

		for(CrsPressRelease release : releases)
		{
			if(!crsReleaseExists(connection, release.getUrl()))
			{
				persistRelease(connection, release, nextCrsReleaseId);
				nextCrsRelatedStoryId = persistRelatedStories(connection, release, nextCrsReleaseId, nextCrsRelatedStoryId);
				nextCrsReleaseId++;
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
	private void persistRelease(Connection connection, CrsPressRelease r, 
					int releaseId) throws SQLException 
	{
		/*
		 *
			"INSERT INTO crs_press_release (release_id, title, sub_title, body_text, page_url, year) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
		 */
		
		System.out.println("Persisting: " + r.getTitle());
		PreparedStatement query = 
				connection.prepareStatement(DoleLoaderConstants.CRS_PRESS_RELEASE_INSERT);
		query.setInt(1, releaseId);
		query.setString(2, r.getTitle());
		query.setString(3, r.getSubtitle());
		query.setString(4, r.getBodyText());
		query.setString(5, r.getUrl());
		query.setInt(6, r.getYear());

		try
		{
			int result = query.executeUpdate();
		}
		catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException micve)
		{
			logger.warning("Duplicate: " + micve.getLocalizedMessage() + "\n" + r.getUrl());
		}
		
		query.close();

		return;
	}
	
	private int persistRelatedStories(Connection connection, CrsPressRelease release, 
				int crsReleaseId, int nextCrsRelatedStoryId) throws SQLException
	{
		Iterator<HashMap<String, String>> i = release.getRelatedPosts().iterator();
		/*
		 * "INSERT INTO crs_press_release_related_stories "
			+ " (related_story_id, title, page_url, release_id) "
			+ "VALUES (?, ?, ?, ?)";
		 */
		
		while (i.hasNext())
		{
			HashMap<String, String> rp = i.next();
			PreparedStatement query = 
					connection.prepareStatement(DoleLoaderConstants.CRS_PRESS_RELEASE_RELATED_STORIES_INSERT);
			query.setInt(1, nextCrsRelatedStoryId);
			query.setString(2, rp.get("title"));
			query.setString(3, rp.get("url"));
			query.setInt(4, crsReleaseId);
			
			try
			{
				int result = query.executeUpdate();
			}
			catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException micve)
			{
				logger.warning("Duplicate: " + micve.getLocalizedMessage() + "\n" + rp.get("title"));
			}
			
			query.close();
			
			nextCrsRelatedStoryId++;
		}
		
		return nextCrsRelatedStoryId;
	}
	
	/**
	 * Checks whether an recipe already exists in the database by comparing its URL to all existing recipes
	 * @param connection The database connection
	 * @param recipeUrl The URL of the recipe we're checking for existence.
	 * @return true if the recipe already exists; false otherwise
	 * @throws SQLException
	 */
	private boolean crsReleaseExists(Connection connection, String pageUrl) throws SQLException
	{
		boolean doesPageExist = false;
		
		PreparedStatement query = 
				connection.prepareStatement(DoleLoaderConstants.GET_CRS_RELEASE_FOR_URL);
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
