package com.londroid.askmyfriends.facade;

import com.londroid.askmyfriends.persistence.greendao.domain.Survey;
import com.londroid.askmyfriends.viewobjects.SurveyDto;

public interface SurveyFacade {

	public void saveSurvey(SurveyDto survey);
	
	public Survey findSurvey(Long surveyId);
	
	public void sendSurvey(Survey survey);
	
	
}
