package com.enavigo.doleloader.pojo;

import java.time.LocalDate;
import java.util.List;

public class PressRelease {
	private String title;
	private List<String> imageUrls;
	private String Url;
	private String bodyHtml;
	private String bodyText;
	private LocalDate parsedLocalDate;
	
	
	/**
	 * @return the parsedLocalDate
	 */
	public LocalDate getParsedLocalDate() {
		return parsedLocalDate;
	}
	/**
	 * @param parsedLocalDate the parsedLocalDate to set
	 */
	public void setParsedLocalDate(LocalDate parsedLocalDate) {
		this.parsedLocalDate = parsedLocalDate;
	}

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
	 * @return the imageUrls
	 */
	public List<String> getImageUrls() {
		return imageUrls;
	}
	/**
	 * @param imageUrls the imageUrls to set
	 */
	public void setImageUrls(List<String> imageUrls) { 
		this.imageUrls = imageUrls;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return Url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		Url = url;
	}
	/**
	 * @return the bodyHtml
	 */
	public String getBodyHtml() {
		return bodyHtml;
	}
	/**
	 * @param bodyHtml the bodyHtml to set
	 */
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
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
	
	
	
}
