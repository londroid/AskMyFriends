package com.londroid.askmyfriends.facade;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.londroid.askmyfriends.persistence.BaseRepositoryGreenDaoImpl;
import com.londroid.askmyfriends.persistence.PersistenceManager;
import com.londroid.askmyfriends.persistence.greendao.domain.Answer;
import com.londroid.askmyfriends.persistence.greendao.domain.Juror;
import com.londroid.askmyfriends.persistence.greendao.domain.JurorSurvey;
import com.londroid.askmyfriends.persistence.greendao.domain.Owner;
import com.londroid.askmyfriends.persistence.greendao.domain.Question;
import com.londroid.askmyfriends.persistence.greendao.domain.Survey;
import com.londroid.askmyfriends.viewobjects.AnswerDto;
import com.londroid.askmyfriends.viewobjects.JurorDto;
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
	private BaseRepositoryGreenDaoImpl<Owner, Long> ownerRepository;
	private BaseRepositoryGreenDaoImpl<Juror, Long> jurorRepository;
	private BaseRepositoryGreenDaoImpl<JurorSurvey, Long> jurorSurveyRepository;
	
	private PersistenceManager persistenceManager;

	private SurveyFacadeImpl(Context context) {
		this.persistenceManager = PersistenceManager.get();
		persistenceManager.initGreenDaoContext(context);
		
		this.answerRepository = persistenceManager.getRepository(Answer.class);
		this.surveyRepository = persistenceManager.getRepository(Survey.class);
		this.questionRepository = persistenceManager.getRepository(Question.class);
		this.ownerRepository = persistenceManager.getRepository(Owner.class);
		this.jurorRepository = persistenceManager.getRepository(Juror.class);
		this.jurorSurveyRepository = persistenceManager.getRepository(JurorSurvey.class);
	}
	
	public static SurveyFacade get(Context context) {
		if (instance == null) {
			instance = new SurveyFacadeImpl(context);
		}
		return instance;
	}
	
	@Override
	public void saveSurvey(final SurveyDto surveyDto) {
		
		surveyRepository.getSession().runInTx(new Runnable() {
	
			@Override
			public void run() {
				
				try {
				Log.i("AMF", "Starting transaction to persist survey...");
				Owner owner = getOwner();
				
				if (owner == null) {
					owner = new Owner();
					owner.setName("David");
					owner.setPhoneNumber("07547898142");
					ownerRepository.save(owner);
					Log.i("AMF", "Owner saved");
				}
				
				// Insert / update question
				QuestionDto questionDto = surveyDto.getQuestion();
				Question question = null;
				
				if (questionDto.getId() == null) {
					question = new Question();
					question.setText(questionDto.getText());
					questionRepository.save(question);
					Log.i("AMF", "Question saved");
				} else {
					question = questionRepository.find(questionDto.getId());
				}
				
				Survey survey = null;
				
				if (surveyDto.getId() == null) {
					survey = new Survey();
					Log.i("AMF", "Instantiating new survey");
				} else {
					survey = surveyRepository.find(surveyDto.getId());
				}
				
				survey.setChoiceType(surveyDto.getSurveyType().name().toString());
				survey.setCreationDate(new Date());
				survey.setModificationDate(new Date());
				survey.setQuestion(question);
				survey.setTitle(surveyDto.getTitle());
				survey.setOwner(owner);
				if (surveyDto.getId() == null) {
					Log.i("AMF", "about to save survey...");
					surveyRepository.save(survey);
					Log.i("AMF", "Survey Saved");
				} else {
					surveyRepository.update(survey);
				}
				
				// Insert / update answers
				int i = 0;
				for (AnswerDto answerView : surveyDto.getAnswers()) {
					Answer answer = null;
					if (answerView.getId() == null) {
						answer = new Answer();
						answer.setOrder(i);
						answer.setText(answerView.getText());
						answer.setSurvey(survey);
						Log.i("AMF", "Saving answer...");
						answerRepository.save(answer);
						Log.i("AMF", "Answer saved");
						
					} else {
						answer = answerRepository.find(answerView.getId());
						answer.setSurvey(survey);
						answerRepository.update(answer);
					}
					
					i++;
				}
				
				for (JurorDto jurorDto : surveyDto.getJurors()) {
					Juror juror = new Juror();
					juror.setId(jurorDto.getId());
					juror.setName(jurorDto.getName());
					juror.setPhoneNumber(jurorDto.getPhoneNumber());
					jurorRepository.save(juror);
					JurorSurvey jurorSurvey = new JurorSurvey();
					jurorSurvey.setJuror(juror);
					jurorSurvey.setSurvey(survey);
					Log.i("AMF", "Saving juror...");
					
					Log.i("AMF", "Juror saved");
					Log.i("AMF", "Saving Many to many....");
					jurorSurveyRepository.save(jurorSurvey);
					Log.i("AMF", "Many to many saved");
				}
				
				Log.i("AMF", "DONE!! AMAZING!!");
				
				} catch (Throwable t) {
					Log.i("AMF", "Error executing transaction to save survey " + t.getMessage() );
					t.printStackTrace();
				}
				
			}
			
		});
	}
	
	public Owner getOwner() {
		List<Owner> owners = ownerRepository.getAll();
		
		if (owners.size() > 0) {
			return owners.get(0);
		} else {
			Log.i("AMF", "Returning null owner...");
			return null;
		}
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

	/**
	 * Retrieve the given survey from DB and convert it to Dto to
	 * give it back to the UI
	 * 
	 */
	@Override
	public Survey findSurvey(Long surveyId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Sends the given survey backing it up in DB
	 * 
	 */
	@Override
	public void sendSurvey(Survey survey) {
		// TODO Auto-generated method stub
		
	}

}
