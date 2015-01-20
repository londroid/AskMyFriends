package com.londroid.askmyfriends.activities;

public enum ActionType {

	EDIT_SURVEY("Edit survey in a form"),
	CREATE_SURVEY ("Create a new survey"),
	PREVIEW_SURVEY("Previews survey before sending"),
	LIST_SURVEYS("List existing surveys for the owner");
	
	private String description;
	
	private ActionType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
