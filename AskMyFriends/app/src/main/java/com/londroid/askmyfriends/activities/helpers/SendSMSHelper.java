package com.londroid.askmyfriends.activities.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.londroid.askmyfriends.activities.mapper.MapperUtil;
import com.londroid.askmyfriends.facade.SurveyFacade;
import com.londroid.askmyfriends.facade.SurveyFacadeImpl;
import com.londroid.askmyfriends.persistence.greendao.domain.Answer;
import com.londroid.askmyfriends.persistence.greendao.domain.Juror;
import com.londroid.askmyfriends.persistence.greendao.domain.Question;
import com.londroid.askmyfriends.persistence.greendao.domain.Survey;
import com.londroid.askmyfriends.viewobjects.SurveyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendSMSHelper {

	private static SendSMSHelper instance;

	private static final String QUESTION_RESULTS_PREFERENCE_KEY = "results";
	
	private Context activityContext;
	
	private SharedPreferences preferences;
	
	private SurveyFacade surveyFacade;
	
	private MapperUtil mapperUtil;
	
	private SendSMSHelper() {}
	
	public static SendSMSHelper setupAndGet(Context context) {
		
		if (instance == null) {
			instance = new SendSMSHelper();
			instance.setContext(context);
			instance.setupMapper();
		}
		
		return instance;
	}
	
	private void setupMapper() {
		this.mapperUtil = MapperUtil.setupAndGet();
	}
	
	private void setContext(Context context) {
		this.activityContext= context;
		surveyFacade = SurveyFacadeImpl.get(context);
		initPreferences();
	}

	private void initPreferences() {
		this.preferences = activityContext.getSharedPreferences(QUESTION_RESULTS_PREFERENCE_KEY, Context.MODE_PRIVATE);
	}
	
	public void sendSMS(SurveyDto surveyDto) {

		mapToSurveyAndSend(surveyDto);
		
	}
	
	
	//TODO: Validator
	public boolean validateSurvey(SurveyDto surveyDto) {
		return true;
	}
 	
	private String composeMessage(String question, Map<String, String> options) {
		String message = 
				"Sent using AskMyFriends app for Android. " + 
	            "I want to ask you something. To vote for an option send me back " + 
				"a single-letter sms. e.g. to vote A text back A. \n \n";
		
		message += " " + question + "? ";
		
		for (String optionLetter : options.keySet()) {
			message += optionLetter + " - " + options.get(optionLetter) + "; \n";
		}
		
		return message;
	}

	public void resetResults() {
		
		preferences.edit().clear().commit();
	}

	public void saveOptionsInPreferences(String question, Map<String, String> optionMap) {
		Editor editor = preferences.edit();
		
		editor.putString("question", question);
		
		for (String optionLetter : optionMap.keySet()) {
			editor.putString("option" + optionLetter, optionMap.get(optionLetter));
		}
		
		// Commit the edits!
		editor.commit();
	}
	
	private void mapToSurveyAndSend(SurveyDto surveyDto) {
		//TODO: Save as well a flag saying if the SMS was successfully sent or not
		Log.i("AMF", "About to persist survey");
		
		//TODO: check validation errors
		validateSurvey(surveyDto);
		surveyDto.setTitle("Title of survey");
		
		Survey survey = mapperUtil.mapToSurvey(surveyDto);
		Log.i("AMF", "Survey mapped");
		
		// Question
		Question question = mapperUtil.mapToQuestion(surveyDto.getQuestion());
		Log.i("AMF", "Question mapped");
		
		// Jurors
		List<Juror> jurors = mapperUtil.mapToJurorList(surveyDto.getJurors());
		Log.i("AMF", "Jurors mapped");
		
		// Answers
		List<Answer> answers = mapperUtil.mapToAnswerList(surveyDto.getAnswers());
		Log.i("AMF", "Answers mapped");
		
		Log.i("AMF", "About to send survey");
		
		surveyFacade.sendSurvey(survey, question, jurors, answers);
		
		Log.i("AMF", "Survey successfully persisted");
	}
	
	public SurveyDto getSurvey(Long surveyId) {
		Survey survey = surveyFacade.findAndInitializeSurvey(surveyId);
		// Map back -- Check jurors and answers to see if they are properly mapped back
		return mapperUtil.mapToSurveyDto(survey);
	}






















































}
