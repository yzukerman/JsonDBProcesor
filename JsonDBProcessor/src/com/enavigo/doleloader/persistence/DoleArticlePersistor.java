package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.enavigo.doleloader.DoleLoaderConstants;
import com.enavigo.doleloader.pojo.Article;
import com.enavigo.doleloader.pojo.Product;
import com.enavigo.doleloader.pojo.Recipe;

public class DoleArticlePersistor implements DoleJsonPersistor {
	
	Connection connection = null;
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public boolean persist(Connection connection, Object objToPersist,
			char sourceSite) throws SQLException {
		this.connection = connection;
		
		List<Article> articles = (List<Article>)objToPersist;
		
		int nextArticleId = DoleJsonPersistenceUtils.getMaxId(connection, "article", "article_id") + 1;
		int nextArticleTagId = DoleJsonPersistenceUtils.getMaxId(connection, "article_tag", 
									"article_tag_id") + 1;
		int nextArticleRecipeId = DoleJsonPersistenceUtils.getMaxId(connection, "article_related_recipe", 
				"article_related_recipe_id") + 1;
		
		for(Article article : articles)
		{
			persistArticle(connection, article, nextArticleId, sourceSite);
			nextArticleRecipeId = persistRelatedRecipes(connection, 
					article.getRelatedRecipes(), nextArticleId, nextArticleRecipeId);
			nextArticleTagId = persistArticleTags(connection, 
					article.getTags(), nextArticleId, nextArticleTagId);
			
			nextArticleId++;
		}
				
		return false;
	}
	
	
	/***
	 * Stores an article in the database
	 * @param connection the database connection
	 * @param a the article to store
	 * @param articleId the id to assign to the article in the database
	 * @param sourceSite the site from which the recipe was extracted
	 * @throws SQLException
	 */
	private void persistArticle(Connection connection, Article a, 
					int articleId, char sourceSite) throws SQLException 
	{
		/*
		 * "INSERT INTO article (article_id, title, body,"
			+ "category, byline, source_site, url, image_url, publish_date, subhead"
		 */
		
		logger.fine("Persisting recipe: " + a.getTitle());
		
		
		PreparedStatement query = 
				connection.prepareStatement(DoleLoaderConstants.ARTICLE_INSERT);
		query.setInt(1, articleId);
		query.setString(2, a.getTitle());
		query.setString(3, a.getBody());
		query.setString(4, a.getCategory());
		query.setString(5, a.getByline());
		query.setString(6, ""+sourceSite);
		query.setString(7, a.getUrl());
		query.setString(8, a.getImageUrl());
		java.sql.Date sqlDate = null;
		if(a.getPublishDate() != null)
		{
			sqlDate = new java.sql.Date(a.getPublishDate().getTime());
		}
		query.setDate(9, sqlDate);
		query.setString(10, a.getSubhead());
		
		int result = query.executeUpdate();
		query.close();
	}
	
	/***
	 * Creates records for recipes related to the article
	 * @param connection the database connection
	 * @param relatedRecipes a collection of related recipes
	 * @param articleId the id of the article the recipes will be associated with
	 * @param articleRecipeId the next id to use for the recipe
	 * @return the next id to use for a recipe
	 * @throws SQLException
	 */
	private int persistRelatedRecipes(Connection connection, List<HashMap<String, String>> relatedRecipes,
										int articleId, int articleRecipeId) throws SQLException
	{
		if(relatedRecipes != null)
		{
			for(HashMap<String, String> recipe : relatedRecipes)
			{

				PreparedStatement query = 
						connection.prepareStatement(DoleLoaderConstants.ARTICLE_RELATED_RECIPE_INSERT);
//				"INSERT INTO article_related_recipe"
//				+ "(article_related_recipe_id, article_id, related_recipe_title, related_recipe_url)"
				
				query.setInt(1, articleRecipeId);
				query.setInt(2, articleId);
				query.setString(3, recipe.get("title"));
				query.setString(4, recipe.get("url"));
				
				query.executeUpdate();
				query.close();
				
				articleRecipeId++;
			}
		}
		
		return articleRecipeId; 
	}
	
	/***
	 * Creates records for recipes related to the article
	 * @param connection the database connection
	 * @param relatedRecipes a collection of related recipes
	 * @param articleId the id of the article the recipes will be associated with
	 * @param articleRecipeId the next id to use for the recipe
	 * @return the next id to use for a recipe
	 * @throws SQLException
	 */
	private int persistArticleTags(Connection connection, List<String> articleTags,
										int articleId, int tagId) throws SQLException
	{
		if(articleTags != null)
		{
			for(String tag : articleTags)
			{

				PreparedStatement query = 
						connection.prepareStatement(DoleLoaderConstants.ARTICLE_TAG_INSERT);
				
				query.setInt(1, tagId);
				query.setInt(2, articleId);
				query.setString(3, tag);
				
				query.executeUpdate();
				query.close();
				
				tagId++;
			}
		}
		
		return tagId; 
	}
	

}
