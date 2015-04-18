package com.enavigo.doleloader.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Product {
	private String page_url = null;
	private String title = null;
	private String description = null;
	private String ingredientsText = null;
	private List ingredients = null;
	private String nutritionInfo = null;
	private List<HashMap<String, String>> relatedRecipes;
	
	@JsonProperty("page_url")
	public String getUrl() {
		return page_url;
	}
	public void setUrl(String url) {
		this.page_url = url;
	}
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Product()
	{
		this.page_url = "";
		this.title = "";
		this.ingredients = new ArrayList();
		this.relatedRecipes = new ArrayList<HashMap<String, String>>();
	}
	public String getPage_url() {
		return page_url;
	}
	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNutritionInfo() {
		return nutritionInfo;
	}
	public void setNutritionInfo(String nutritionInfo) {
		this.nutritionInfo = nutritionInfo;
	}
	public String getIngredientsText() {
		return ingredientsText;
	}
	public void setIngredientsText(String ingredientsText) {
		this.ingredientsText = ingredientsText;
	}
	

	
	
}
