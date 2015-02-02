package com.londroid.askmyfriends.activities.mapper;

import com.londroid.askmyfriends.persistence.greendao.domain.Answer;
import com.londroid.askmyfriends.persistence.greendao.domain.Juror;
import com.londroid.askmyfriends.persistence.greendao.domain.JurorSurvey;
import com.londroid.askmyfriends.persistence.greendao.domain.Question;
import com.londroid.askmyfriends.persistence.greendao.domain.Survey;
import com.londroid.askmyfriends.viewobjects.AnswerDto;
import com.londroid.askmyfriends.viewobjects.JurorDto;
import com.londroid.askmyfriends.viewobjects.QuestionDto;
import com.londroid.askmyfriends.viewobjects.SurveyDto;
import com.londroid.askmyfriends.viewobjects.SurveyType;

import java.util.ArrayList;
import java.util.List;

public class MapperUtil {

	private static MapperUtil instance;
	
	public static MapperUtil setupAndGet() {
		
		if (instance == null) {
			instance = new  MapperUtil();
		}
		
		return instance;
	}
	
	public List<AnswerDto> mapToAnswerDtoList(List<Answer> sourceList) {
		List<AnswerDto> answersDto = new ArrayList<AnswerDto>();
		
		for (Answer answer : sourceList) {
			answersDto.add(mapToAnswerDto(answer));
		}
		
		return answersDto;
	}
	
	public List<Answer> mapToAnswerList(List<AnswerDto> sourceList) {
		List<Answer> answers = new ArrayList<Answer>();
		
		for (AnswerDto answerDto : sourceList) {
			answers.add(mapToAnswer(answerDto));
		}
		
		return answers;
	}
	
	public List<JurorDto> mapToJurorDtoList(List<Juror> sourceList) {
		List<JurorDto> jurorsDto = new ArrayList<JurorDto>();
		
		for (Juror juror : sourceList) {
			jurorsDto.add(mapToJurorDto(juror));
		}
		
		return jurorsDto;
	}
	
	public List<Juror> mapToJurorList(List<JurorDto> sourceList) {
		List<Juror> jurors = new ArrayList<Juror>();
		
		for (JurorDto jurorDto : sourceList) {
			jurors.add(mapToJuror(jurorDto));
		}
		
		return jurors;
	}
	
	private List<JurorDto> mapJurorSurveyToJurorDto(List<JurorSurvey> jurorsSurvey) {
		List<JurorDto> jurorsDto = new ArrayList<JurorDto>();
		
		for (JurorSurvey jurorSurvey : jurorsSurvey) {
			jurorsDto.add(mapToJurorDto(jurorSurvey.getJuror()));
		}
		return jurorsDto;
	}
	
	
	public SurveyDto mapToSurveyDto(Survey survey) {
		SurveyDto surveyDto = new SurveyDto();
		surveyDto.setId(survey.getId());
		surveyDto.setSurveyType(SurveyType.valueOf(survey.getChoiceType()));
		surveyDto.setQuestion(mapToQuestionDto(survey.getQuestion()));
		surveyDto.setJurors(mapJurorSurveyToJurorDto(survey.getJurors()));
		surveyDto.setAnswers(mapToAnswerDtoList(survey.getAnswers()));
		surveyDto.setTitle(survey.getTitle());
		return surveyDto;
	}
	
	public JurorDto mapToJurorDto(Juror juror) {
		JurorDto jurorDto = new JurorDto();
		jurorDto.setId(juror.getId());
		jurorDto.setName(juror.getName());
		jurorDto.setPhoneNumber(juror.getPhoneNumber());
		return jurorDto;
	}
	
	public AnswerDto mapToAnswerDto(Answer answer) {
		AnswerDto answerDto = new AnswerDto();
		answerDto.setId(answer.getId());
		answerDto.setText(answer.getText());
		answerDto.setListingTag(answer.getListingTag());
		answerDto.setOrder(answer.getOrder());
		return answerDto;
	}
	
	public QuestionDto mapToQuestionDto(Question question) {
		QuestionDto questionDto = new QuestionDto();
		questionDto.setId(question.getId());
		questionDto.setText(question.getText());
		return questionDto;
	}
	
	public Survey mapToSurvey(SurveyDto surveyDto) {
		Survey survey = new Survey();
		survey.setId(surveyDto.getId());
		survey.setChoiceType(surveyDto.getSurveyType().toString());
		survey.setCreationDate(surveyDto.getCreationDate());
		survey.setModificationDate(survey.getModificationDate());
		survey.setTitle(surveyDto.getTitle());
		return survey;
	}
	
	public Juror mapToJuror(JurorDto jurorDto) {
		Juror juror = new Juror();
		juror.setId(jurorDto.getId());
		juror.setName(jurorDto.getName());
		juror.setPhoneNumber(jurorDto.getPhoneNumber());
		return juror;
	}
	
	public Answer mapToAnswer(AnswerDto answerDto) {
		Answer answer = new Answer();
		answer.setId(answerDto.getId());
		answer.setListingTag(answerDto.getListingTag());
		answer.setOrder(answerDto.getOrder());
		answer.setText(answerDto.getText());
		return answer;
	}
	
	public Question mapToQuestion(QuestionDto questionDto) {
		Question question = new Question();
		question.setId(questionDto.getId());
		question.setText(questionDto.getText());
		return question;
	}
	
}
