package com.enavigo.doleloader.pojo;

import java.util.ArrayList;
import java.util.HashMap;

public class CrsPressRelease {

	private String title = null;
	private String subtitle = null;
	private String bodyText = null;
	private String url = null;
	private int year = 0;
	private ArrayList<HashMap<String, String>> relatedPosts = new ArrayList<HashMap<String, String>>();
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}
	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	/**
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}
	/**
	 * @param bodyText the bodyText to set
	 */
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the relatedPosts
	 */
	public ArrayList<HashMap<String, String>> getRelatedPosts() {
		return relatedPosts;
	}
	/**
	 * @param relatedPosts the relatedPosts to set
	 */
	public void setRelatedPosts(ArrayList<HashMap<String, String>> relatedPosts) {
		this.relatedPosts = relatedPosts;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	
	
}
