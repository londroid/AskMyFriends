package com.londroid.askmyfriends.viewobjects;

public class AnswerDto {

	private Long id;
	private String text;
	private Integer order;
	private String listingTag;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getListingTag() {
		return listingTag;
	}
	public void setListingTag(String listingTag) {
		this.listingTag = listingTag;
	}
}
