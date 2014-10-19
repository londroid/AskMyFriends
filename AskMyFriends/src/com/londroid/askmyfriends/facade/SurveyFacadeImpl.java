package com.londroid.askmyfriends.facade;

import java.util.Date;

import android.content.Context;

import com.londroid.askmyfriends.persistence.BaseRepositoryGreenDaoImpl;
import com.londroid.askmyfriends.persistence.PersistenceManager;
import com.londroid.askmyfriends.persistence.greendao.domain.Answer;
import com.londroid.askmyfriends.persistence.greendao.domain.Question;
import com.londroid.askmyfriends.persistence.greendao.domain.Survey;
import com.londroid.askmyfriends.viewobjects.AnswerDto;
import com.londroid.askmyfriends.viewobjects.QuestionDto;
import com.londroid.askmyfriends.viewobjects.SurveyDto;


/**
 * Facade to expose to clients (Activities). This one uses GreenDao / SQLite
 * as the persistence strategy
 * 
 * @author david
 *
 */
public class SurveyFacadeImpl implements SurveyFacade {

	private static SurveyFacade instance;

	private BaseRepositoryGreenDaoImpl<Answer, Long> answerRepository;
	private BaseRepositoryGreenDaoImpl<Survey, Long> surveyRepository;
	private BaseRepositoryGreenDaoImpl<Question, Long> questionRepository;
	private PersistenceManager persistenceManager;

	private SurveyFacadeImpl(Context context) {
		this.persistenceManager = PersistenceManager.get();
		persistenceManager.initGreenDaoContext(context);
		
		this.answerRepository = persistenceManager.getAnswerRepository();
		this.surveyRepository = persistenceManager.getSurveyRepository();
		this.questionRepository = persistenceManager.getQuestionRepository();
	}
	
	public static SurveyFacade get(Context context) {
		if (instance == null) {
			new SurveyFacadeImpl(context);
		}
		return instance;
	}

	@Override
	public void saveSurvey(final SurveyDto surveyView) {
		
		surveyRepository.runInTransactionWithoutResult(new Runnable() {
	
			@Override
			public void run() {
				
				// Insert / update question
				QuestionDto questionView = surveyView.getQuestion();
				Question question = null;
				
				if (questionView.getId() == null) {
					question = new Question();
					question.setText(questionView.getText());
					questionRepository.save(question);
				} else {
					question = questionRepository.find(questionView.getId());
				}
				
				Survey survey = null;
				
				if (surveyView.getId() == null) {
					survey = new Survey();
				} else {
					survey = surveyRepository.find(surveyView.getId());
				}
				
				survey.setChoiceType(surveyView.getSurveyType().name().toString());
				survey.setCreationDate(new Date());
				survey.setModificationDate(new Date());
				survey.setQuestion(question);
				survey.setTitle(surveyView.getTitle());	
				
				// Insert / update answers
				int i = 0;
				for (AnswerDto answerView : surveyView.getAnswers()) {
					Answer answer = null;
					if (answerView.getId() == null) {
						answer = new Answer();
						answer.setOrder(i);
						answer.setText(answerView.getText());
						answer.setSurvey(survey);
						answerRepository.save(answer);
					} else {
						answer = answerRepository.find(answerView.getId());
						answer.setSurvey(survey);
						answerRepository.update(answer);
					}
					
					i++;
				}
				
				if (surveyView.getId() == null) {
					surveyRepository.save(survey);
				} else {
					surveyRepository.update(survey);
				}
			}
			
		});
	}
	
	public Question saveOrUpdateQuestion(QuestionDto questionView) {
		Question question = new Question();
		question.setId(questionView.getId());
		question.setText(questionView.getText());
		questionRepository.getDao().insertOrReplaceInTx(question);
		return question;
	}
	
	public Answer saveOrUpdateAnswer(AnswerDto answerView) {
		Answer answer = new Answer();
		answer.setId(answerView.getId());
		answer.setOrder(answerView.getOrder());
		answer.setText(answerView.getText());
		answerRepository.getDao().insertOrReplace(answer);
		return answer;
	}

}
