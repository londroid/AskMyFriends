package com.londroid.askmyfriends.facade;

import java.util.List;

import com.londroid.askmyfriends.persistence.greendao.domain.Answer;
import com.londroid.askmyfriends.persistence.greendao.domain.Juror;
import com.londroid.askmyfriends.persistence.greendao.domain.Question;
import com.londroid.askmyfriends.persistence.greendao.domain.Survey;

public interface SurveyFacade {
	
	public Survey findAndInitializeSurvey(Long surveyId);
	
	public void sendSurvey(Survey survey, Question question, List<Juror> jurors, List<Answer> answers);
	
	public Juror findJurorByPhoneNumber(String phoneNumber);
	
	
}
