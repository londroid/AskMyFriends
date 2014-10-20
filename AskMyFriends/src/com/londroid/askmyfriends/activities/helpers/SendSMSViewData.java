package com.londroid.askmyfriends.activities.helpers;

import java.util.List;
import java.util.Map;

public class SendSMSViewData {

	private String question;
	private List<String> phoneNumbers;
	private Map<String, String> answers;
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	
	public Map<String, String> getOptions() {
		return answers;
	}
	
	public void setAnswers(Map<String, String> options) {
		this.answers = options;
	}
}
