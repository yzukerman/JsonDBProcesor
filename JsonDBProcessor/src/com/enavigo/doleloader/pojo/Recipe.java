package com.enavigo.doleloader.pojo;

import java.util.HashMap;
import java.util.List;

public class Recipe {
	private String title = null;
	private String url = null;
	private String category = null;
	private String subcategory = null;
	private char soureceSite; 
	private String imageUrl = null;
	// preparation information (e.g. 5 minutes)
	private int prepValue = 0; // 5 (in 5 minutes)
	private String prepUnit = null; // "minutes"
	private int cookTimeValue = 0; // 5 (in 5 minutes)
	private String cookTimeUnit = null; // "minutes"
	private int totalTimeValue = 0;
	private String totalTimeUnit = null;
	private String description = null;
	private String difficultyDescription = null;
	private int difficultyValue = 0;
	private int servings = 0;
	private String servingsString = null; // from nutritional values
	private String servingSize = null; // from the recipe description
	private String vegetableServings = null;
	private int servingsPerContainer = 0;
	private int calories = 0;
	private int caloriesFromFat = 0;
	private int totalFatGrams = 0;
	private int totalFatPercent = 0;
	private int saturatedFatGrams = 0;
	private int saturatedFatPercent = 0;
	private int transdFatGrams = 0;
	private int transFatPercent = 0;
	private int cholesterolMg = 0;
	private int cholesterolPercent = 0;
	private int sodiumMg = 0;
	private int sodiumPercent = 0;
	private int potassiumMg = 0;
	private int potassiumPercent = 0;
	private int totalCarbsGrams = 0;
	private int totalCarbsPercent = 0;
	private int fiberGrams = 0;
	private int fiberPercent = 0;
	private int sugarsGrams = 0;
	private int proteinGrams = 0;
	private String ingredientsText = null;
	private String nutritionlabelHtml = null;
	private List<HashMap<String, String>> ingredients = null;
	private List<HashMap<String, String>> relatedProducts;
	private List<HashMap<String, String>> relatedRecipes;
	private List<HashMap<String, Object>> recipeSteps;
	private List<HashMap<String, Object>> nutrients = null;
	
	
	public int getCookTimeValue() {
		return cookTimeValue;
	}
	public void setCookTimeValue(int cookTimeValue) {
		this.cookTimeValue = cookTimeValue;
	}
	public String getCookTimeUnit() {
		return cookTimeUnit;
	}
	public void setCookTimeUnit(String cookTimeUnit) {
		this.cookTimeUnit = cookTimeUnit;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
	public char getSoureceSite() {
		return soureceSite;
	}
	public void setSoureceSite(char soureceSite) {
		this.soureceSite = soureceSite;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getPrepValue() {
		return prepValue;
	}
	public void setPrepValue(int prepValue) {
		this.prepValue = prepValue;
	}
	public String getPrepUnit() {
		return prepUnit;
	}
	public void setPrepUnit(String prepUnit) {
		this.prepUnit = prepUnit;
	}
	public int getTotalTimeValue() {
		return totalTimeValue;
	}
	public void setTotalTimeValue(int totalTimeValue) {
		this.totalTimeValue = totalTimeValue;
	}
	public String getTotalTimeUnit() {
		return totalTimeUnit;
	}
	public void setTotalTimeUnit(String totalTimeUnit) {
		this.totalTimeUnit = totalTimeUnit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDifficultyDescription() {
		return difficultyDescription;
	}
	public void setDifficultyDescription(String difficultyDescription) {
		this.difficultyDescription = difficultyDescription;
	}
	public int getDifficultyValue() {
		return difficultyValue;
	}
	public void setDifficultyValue(int difficultyValue) {
		this.difficultyValue = difficultyValue;
	}
	public int getServings() {
		return servings;
	}
	public void setServings(int servings) {
		this.servings = servings;
	}
	public String getServingSize() {
		return servingSize;
	}
	public void setServingSize(String servingSize) {
		this.servingSize = servingSize;
	}
	public String getVegetableServings() {
		return vegetableServings;
	}
	public void setVegetableServings(String vegetableServings) {
		this.vegetableServings = vegetableServings;
	}
	public int getServingsPerContainer() {
		return servingsPerContainer;
	}
	public void setServingsPerContainer(int servingsPerContainer) {
		this.servingsPerContainer = servingsPerContainer;
	}
	public int getCalories() {
		return calories;
	}
	public void setCalories(int calories) {
		this.calories = calories;
	}
	public int getCaloriesFromFat() {
		return caloriesFromFat;
	}
	public void setCaloriesFromFat(int caloriesFromFat) {
		this.caloriesFromFat = caloriesFromFat;
	}
	public int getTotalFatGrams() {
		return totalFatGrams;
	}
	public void setTotalFatGrams(int totalFatGrams) {
		this.totalFatGrams = totalFatGrams;
	}
	public int getTotalFatPercent() {
		return totalFatPercent;
	}
	public void setTotalFatPercent(int totalFatPercent) {
		this.totalFatPercent = totalFatPercent;
	}
	public int getSaturatedFatGrams() {
		return saturatedFatGrams;
	}
	public void setSaturatedFatGrams(int saturatedFatGrams) {
		this.saturatedFatGrams = saturatedFatGrams;
	}
	public int getSaturatedFatPercent() {
		return saturatedFatPercent;
	}
	public void setSaturatedFatPercent(int saturatedFatPercent) {
		this.saturatedFatPercent = saturatedFatPercent;
	}
	public int getTransdFatGrams() {
		return transdFatGrams;
	}
	public void setTransdFatGrams(int transdFatGrams) {
		this.transdFatGrams = transdFatGrams;
	}
	public int getTransFatPercent() {
		return transFatPercent;
	}
	public void setTransFatPercent(int transFatPercent) {
		this.transFatPercent = transFatPercent;
	}
	public int getCholesterolMg() {
		return cholesterolMg;
	}
	public void setCholesterolMg(int cholesterolMg) {
		this.cholesterolMg = cholesterolMg;
	}
	public int getCholesterolPercent() {
		return cholesterolPercent;
	}
	public void setCholesterolPercent(int cholesterolPercent) {
		this.cholesterolPercent = cholesterolPercent;
	}
	public int getSodiumMg() {
		return sodiumMg;
	}
	public void setSodiumMg(int sodiumMg) {
		this.sodiumMg = sodiumMg;
	}
	public int getSodiumPercent() {
		return sodiumPercent;
	}
	public void setSodiumPercent(int sodiumPercent) {
		this.sodiumPercent = sodiumPercent;
	}
	public int getPotassiumMg() {
		return potassiumMg;
	}
	public void setPotassiumMg(int potassiumMg) {
		this.potassiumMg = potassiumMg;
	}
	public int getPotassiumPercent() {
		return potassiumPercent;
	}
	public void setPotassiumPercent(int potassiumPercent) {
		this.potassiumPercent = potassiumPercent;
	}
	public int getTotalCarbsGrams() {
		return totalCarbsGrams;
	}
	public void setTotalCarbsGrams(int totalCarbsGrams) {
		this.totalCarbsGrams = totalCarbsGrams;
	}
	public int getTotalCarbsPercent() {
		return totalCarbsPercent;
	}
	public void setTotalCarbsPercent(int totalCarbsPercent) {
		this.totalCarbsPercent = totalCarbsPercent;
	}
	public int getFiberGrams() {
		return fiberGrams;
	}
	public void setFiberGrams(int fiberGrams) {
		this.fiberGrams = fiberGrams;
	}
	public int getFiberPercent() {
		return fiberPercent;
	}
	public void setFiberPercent(int fiberPercent) {
		this.fiberPercent = fiberPercent;
	}
	public int getSugarsGrams() {
		return sugarsGrams;
	}
	public void setSugarsGrams(int sugarsGrams) {
		this.sugarsGrams = sugarsGrams;
	}
	public int getProteinGrams() {
		return proteinGrams;
	}
	public void setProteinGrams(int proteinGrams) {
		this.proteinGrams = proteinGrams;
	}
	public String getIngredientsText() {
		return ingredientsText;
	}
	public void setIngredientsText(String ingredientsText) {
		this.ingredientsText = ingredientsText;
	}
	public String getNutritionlabelHtml() {
		return nutritionlabelHtml;
	}
	public void setNutritionlabelHtml(String nutritionlabelHtml) {
		this.nutritionlabelHtml = nutritionlabelHtml;
	}
	public List<HashMap<String, String>> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<HashMap<String, String>> ingredients) {
		this.ingredients = ingredients;
	}
	public List<HashMap<String, String>> getRelatedRecipes() {
		return relatedRecipes;
	}
	public void setRelatedRecipes(List<HashMap<String, String>> relatedRecipes) {
		this.relatedRecipes = relatedRecipes;
	}
	public List<HashMap<String, Object>> getRecipeSteps() {
		return recipeSteps;
	}
	public void setRecipeSteps(List<HashMap<String, Object>> recipeSteps) {
		this.recipeSteps = recipeSteps;
	}
	public List<HashMap<String, Object>> getNutrients() {
		return nutrients;
	}
	public void setNutrients(List<HashMap<String, Object>> nutrients) {
		this.nutrients = nutrients;
	}
	public List<HashMap<String, String>> getRelatedProducts() {
		return relatedProducts;
	}
	public void setRelatedProducts(List<HashMap<String, String>> relatedProducts) {
		this.relatedProducts = relatedProducts;
	}
	public String getServingsString() {
		return servingsString;
	}
	public void setServingsString(String servingsString) {
		this.servingsString = servingsString;
	}
	
	
	
	
}
