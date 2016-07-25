package com.asynch.common;

public class Result {

	private final Article article;
	private String organizations, locations, dates;

	public Result(final Article article) {
		this.article = article;
	}

	public String getOrganizations() {
		return organizations;
	}

	public String getLocations() {
		return locations;
	}

	public String getDates() {
		return dates;
	}

	public Article getArticle() {
		return article;
	}

	public void setOrganizations(String organizations) {
		this.organizations = organizations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

}
