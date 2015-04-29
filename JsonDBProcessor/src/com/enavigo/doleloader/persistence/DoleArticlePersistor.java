package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.enavigo.doleloader.pojo.Article;
import com.enavigo.doleloader.pojo.Product;

public class DoleArticlePersistor implements DoleJsonPersistor {
	
	Connection connection = null;

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
		
		return false;
	}

}
