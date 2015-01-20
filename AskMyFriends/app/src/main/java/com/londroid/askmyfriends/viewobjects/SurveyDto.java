package com.londroid.askmyfriends.viewobjects;

import java.util.Date;
import java.util.List;

public class SurveyDto {

	private Long id;
	private String title;
	private QuestionDto question;
	private List<AnswerDto> answers;
	private SurveyType surveyType;
	private List<JurorDto> jurors;
	private Date creationDate;
	private Date modificationDate;
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	public List<JurorDto> getJurors() {
		return jurors;
	}
	public void setJurors(List<JurorDto> jurors) {
		this.jurors = jurors;
	}
	public QuestionDto getQuestion() {
		return question;
	}
	public void setQuestion(QuestionDto question) {
		this.question = question;
	}
	
	public List<AnswerDto> getAnswers() {
		return answers;
	}
	public void setAnswers(List<AnswerDto> answers) {
		this.answers = answers;
	}
	public SurveyType getSurveyType() {
		return surveyType;
	}
	public void setSurveyType(SurveyType surveyType) {
		this.surveyType = surveyType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
} 
