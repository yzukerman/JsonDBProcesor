package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.enavigo.doleloader.DoleLoaderConstants;
import com.enavigo.doleloader.pojo.Article;
import com.enavigo.doleloader.pojo.PressRelease;
import com.enavigo.doleloader.pojo.Product;
import com.enavigo.doleloader.pojo.Recipe;

public class DolePressReleasePersistor implements DoleJsonPersistor {
	
	Connection connection = null;
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public boolean persist(Connection connection, Object objToPersist,
			char sourceSite) throws SQLException {
		this.connection = connection;
				
		List<PressRelease> releases = (List<PressRelease>)objToPersist;
		
		int nextPressReleaseId = DoleJsonPersistenceUtils.getMaxId(connection, "press_release", "release_id") + 1;
		
		for(PressRelease release : releases)
		{
			if(!(releaseExists(connection, release.getUrl())))
			{
				System.out.println("Release Title: " + release.getTitle());
				persistPressRelease(connection, release, nextPressReleaseId, sourceSite);
				nextPressReleaseId++;
			}
		}
				
		return false;
	}
	
	
	/***
	 * Stores a press release in the database
	 * @param connection the database connection
	 * @param a the the press release to store
	 * @param releaseId the id to assign to the article in the database
	 * @param sourceSite the site from which the recipe was extracted
	 * @throws SQLException
	 */
	private void persistPressRelease(Connection connection, PressRelease a, 
					int releaseId, char sourceSite) throws SQLException 
	{
		/*
		 * "INSERT INTO article (article_id, title, body,"
			+ "category, byline, source_site, url, image_url, publish_date, subhead"
		 */
		
		logger.fine("Persisting recipe: " + a.getTitle());
		
		/*
		 "INSERT INTO press_release (release_id, title, url, body_html, body_text, publish_date, source_site_id) "
		 */
		
		PreparedStatement query = 
				connection.prepareStatement(DoleLoaderConstants.INSERT_PRESS_RELEASE);
		query.setInt(1, releaseId);
		query.setString(2, a.getTitle());
		query.setString(3, a.getUrl());
		query.setString(4, a.getBodyHtml());
		query.setString(5, a.getBodyText());
		if(a.getParsedLocalDate() != null)
			query.setDate(6, Date.valueOf(a.getParsedLocalDate()));
		else
			query.setNull(6, Types.DATE);
		query.setString(7, ""+sourceSite);
				
		int result = query.executeUpdate();
		query.close();
	}
	
	
	/**
	 * Checks whether a press release already exists in the database by comparing its URL to all existing releases
	 * @param connection The database connection
	 * @param releaseUrl The URL of the release we're checking for existence.
	 * @return true if the release already exists; false otherwise
	 * @throws SQLException
	 */
	private boolean releaseExists(Connection connection, String releaseUrl) throws SQLException
	{
		boolean doesReleaseExist = false;
		
		PreparedStatement query = 
				connection.prepareStatement(DoleLoaderConstants.GET_RELEASE_FOR_URL);
		query.setString(1, releaseUrl);
		ResultSet r = query.executeQuery();
		if(r.next())
		{
			doesReleaseExist = true;
		}
		r.close();
		query.close();
		
		return doesReleaseExist;
	}
	

}
