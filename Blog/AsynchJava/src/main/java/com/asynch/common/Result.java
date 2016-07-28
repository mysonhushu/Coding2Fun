package com.asynch.common;

public class Result {

	private final Article article;
	private String person, organizations, locations, dates;

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

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
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

	public String toString() {
		return article != null ? article.toString(): "Empty Found";
	}

}
