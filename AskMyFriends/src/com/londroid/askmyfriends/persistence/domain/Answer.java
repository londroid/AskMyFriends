package com.londroid.askmyfriends.persistence.domain;

public class Answer {

	private Long id;
	private String optionTag;
	private String text;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOptionTag() {
		return optionTag;
	}
	public void setOptionTag(String optionTag) {
		this.optionTag = optionTag;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
