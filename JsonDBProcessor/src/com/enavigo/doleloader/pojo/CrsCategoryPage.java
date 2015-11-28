package com.enavigo.doleloader.pojo;

import java.util.ArrayList;
import java.util.List;

public class CrsCategoryPage {

	private String title = null;
	private String url = null;
	private String headerImageUrl = null;
	private String bodyHtml = null;
	private String bodyText = null;
	private CrsCategoryPage parent = null;
	private List<CrsCategoryPage> children = new ArrayList<CrsCategoryPage>();
	
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
	/**
	 * @return the parent
	 */
	public CrsCategoryPage getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(CrsCategoryPage parent) {
		this.parent = parent;
	}
	/**
	 * @return the children
	 */
	public List<CrsCategoryPage> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<CrsCategoryPage> children) {
		this.children = children;
	}
	/**
	 * @return the headerImageUrl
	 */
	public String getHeaderImageUrl() {
		return headerImageUrl;
	}
	/**
	 * @param headerImageUrl the headerImageUrl to set
	 */
	public void setHeaderImageUrl(String headerImageUrl) {
		this.headerImageUrl = headerImageUrl;
	}
	
	
	
}
