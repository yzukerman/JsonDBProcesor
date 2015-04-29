package com.enavigo.doleloader.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Article {

	private String title;
	private String category;
	private String byline;
	private String body;
	private String url;
	private String imageUrl;
	private Date publishDate;
	private String subhead;
	private List<String> tags;
	private List<HashMap<String, String>> relatedRecipes;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getByline() {
		return byline;
	}
	public void setByline(String byline) {
		this.byline = byline;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getSubhead() {
		return subhead;
	}
	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<HashMap<String, String>> getRelatedRecipes() {
		return relatedRecipes;
	}
	public void setRelatedRecipes(List<HashMap<String, String>> relatedRecipes) {
		this.relatedRecipes = relatedRecipes;
	}
	
	
	
	
}
