package com.londroid.askmyfriends.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;

import com.londroid.askmyfriends.persistence.BaseRepositoryGreenDaoImpl;
import com.londroid.askmyfriends.persistence.PersistenceManager;
import com.londroid.askmyfriends.persistence.greendao.dao.JurorSurveyDao.Properties;
import com.londroid.askmyfriends.persistence.greendao.domain.Answer;
import com.londroid.askmyfriends.persistence.greendao.domain.Juror;
import com.londroid.askmyfriends.persistence.greendao.domain.JurorSurvey;
import com.londroid.askmyfriends.persistence.greendao.domain.Owner;
import com.londroid.askmyfriends.persistence.greendao.domain.Question;
import com.londroid.askmyfriends.persistence.greendao.domain.Survey;

import de.greenrobot.dao.query.Query;


/**
 * Facade to expose to clients (Activities). This one uses GreenDao / SQLite
 * as the persistence strategy and SMS to send the survey
 * 
 * @author david
 *
 */
public class SurveyFacadeImpl implements SurveyFacade {

	private static SurveyFacade instance;
	
	private static final int SEND_SURVEY_RETRY_COUNT = 3;
	
	private ExecutorService executor = Executors.newFixedThreadPool(3); 
	
	private Context context;
	
	private BaseRepositoryGreenDaoImpl<Answer, Long> answerRepository;
	private BaseRepositoryGreenDaoImpl<Survey, Long> surveyRepository;
	private BaseRepositoryGreenDaoImpl<Question, Long> questionRepository;
	private BaseRepositoryGreenDaoImpl<Owner, Long> ownerRepository;
	private BaseRepositoryGreenDaoImpl<Juror, Long> jurorRepository;
	private BaseRepositoryGreenDaoImpl<JurorSurvey, Long> jurorSurveyRepository;
	
	private PersistenceManager persistenceManager;

	private SurveyFacadeImpl(Context context) {
		this.persistenceManager = PersistenceManager.get();
		this.context = context;
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
	
	private Owner getOwner() {
		List<Owner> owners = ownerRepository.getAll();
		if (owners.size() > 0) {
			return owners.get(0);
		} else {
			Log.i("AMF", "Returning null owner...");
			return null;
		}
	}
	
	public Survey saveOrUpdateSurvey(Survey survey) {
		surveyRepository.getDao().insertOrReplace(survey);
		return survey;
	}
	
	private void saveOrUpdateQuestion(Question question) {
		questionRepository.getDao().insertOrReplace(question);
	}
	
	private void saveOrUpdateAnswer(Answer answer) {
		answerRepository.getDao().insertOrReplace(answer);
	}
	
	private void saveOrUpdateJuror(Juror juror) {
		jurorRepository.getDao().insertOrReplace(juror);
	}
	
	private void saveOrUpdateJurorSurvey(JurorSurvey jurorSurvey) {
		jurorSurveyRepository.getDao().insertOrReplace(jurorSurvey);
	}

	/**
	 * Return the given survey
	 * 
	 */
	@Override
	public Survey findAndInitializeSurvey(Long surveyId) {
		//TODO: initialize answers and jurors properly
		Survey survey = surveyRepository.find(surveyId);
		survey.getQuestion();
		survey.getAnswers();
		List<JurorSurvey> jurorSurveys = survey.getJurors();
		for (JurorSurvey jurorSurvey : jurorSurveys) {
			jurorSurvey.getJuror();
		}
		return survey;
	}

	/**
	 * Sends the given survey backing it up in DB
	 * 
	 */
	@Override
	public void sendSurvey(final Survey survey, final Question question, final List<Juror> jurors, final List<Answer> answers) {
		
		saveCompleteSurvey(survey, question, jurors, answers);
		
		//TODO: Do this in a background service (For example, IntentService) -- right now NO SMS SENT at all!!
		//handleSmsSendingsInSeparateThread(survey.getId(), question.getText(), jurors, answers);
	}
	
	private void handleSmsSendingsInSeparateThread(final Long surveyId, final String questionText, final List<Juror> jurors, final List<Answer> answers) {
		
		// Handler to communicate with UI Thread
		final Handler handler = new Handler();
		executor.execute(new Runnable() {
	
			@Override
			public void run() {
				
				List<String> failedSendings = sendSurveyViaSmsWithRetry(surveyId, questionText, jurors, answers);
				
				Message msg = handler.obtainMessage();
				
				//TODO: store information in the survey indicating that some jurors didn't receive it
				//TODO: update the UI thread with Toasts indicating the status of the survey sent
				if (failedSendings.size() == 0) {
					// Mark the survey as successfully sent -- SUCCESSFULLY_SENT
					
				} else if (failedSendings.size() == jurors.size()) { 
					// The entire survey was failed to send -- show warning -- SENT_FAILED
				} else {
					// Some sendings were successful, mark as PARTIALLY_SENT 
				}
				
				handler.sendMessage(msg);
			}
		});
	}

	@Override
	public Juror findJurorByPhoneNumber(String phoneNumber) {
		return null;
	}

	private List<String> sendSurveyViaSmsWithRetry(Long surveyId, String question, List<Juror> jurors, List<Answer> answers) {
		
		String message = composeAskMyFriendsSmsMessage(surveyId, question, answers);
		
		// List of phone numbers for which the SMS could not be sent
		List<String> failedSendings = new ArrayList<String>();  
		SmsManager smsManager = SmsManager.getDefault();
			
		for (Juror juror : jurors) {
			
			int retryCount = 0;
			boolean success = false;
			
			while (!success && retryCount < SEND_SURVEY_RETRY_COUNT) {
				try {
					
					ArrayList<String> parts = smsManager.divideMessage(message);
					smsManager.sendMultipartTextMessage(juror.getPhoneNumber(), null, parts, null, null);
					ContentValues values = new ContentValues();
					values.put("address", juror.getPhoneNumber());
					values.put("body", message);
					context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
					success = true;
					
				} catch (Throwable t) {
					Log.w("AMF", "Error sending SMS for juror " + juror.getPhoneNumber() + " -- retrying", t);
					retryCount++;
				}
			}
			
			if (!success) {
				failedSendings.add(juror.getPhoneNumber());
				Log.w("AMF", "The survey " + surveyId + " could not be sent after " + SEND_SURVEY_RETRY_COUNT + " tries -- giving up");
			}
		}
		
		return failedSendings;
	}
	
	private String composeAskMyFriendsSmsMessage(Long surveyId, String question, List<Answer> answers) {
		String message = "#AMF#\n" +
				         "Q: " + question + "?\n";
		
		for (Answer answer : answers) {
			message += answer.getListingTag() + ": " + answer.getText() + "\n";
		}
		
		message +="\n Answer with: AMF#" + surveyId + "#ANSWERTAG"; 
		return message;
	}
	// For testing!
    private void saveOwnerIfNotAlready(final Owner owner) {
        ownerRepository.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                Log.i("AMF", "Starting transaction to persist testing Owner...");
                ownerRepository.getDao().insertOrReplace(owner);
            }
        });
    }

	private void saveCompleteSurvey(final Survey survey, final Question question, final List<Juror> jurors, final List<Answer> answers) {

        saveOwnerIfNotAlready(new Owner(null, "David", "07547898142"));

		surveyRepository.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				Log.i("AMF", "Starting transaction to persist survey...");
				// Owner
				Owner owner = getOwner();
				survey.setOwner(owner);
				
				Log.i("AMF", "Owner set!");
				
				// Question
				saveOrUpdateQuestion(question);
				survey.setQuestion(question);
				Log.i("AMF", "Question added!");
				
				// Survey
				saveOrUpdateSurvey(survey);
				
				Log.i("AMF", "Survey saved!");
				
				// Jurors
				for(Juror juror : jurors) {	
					
					saveOrUpdateJuror(juror);
					
					JurorSurvey jurorSurvey = null;
					if (juror.getId() != null && survey.getId() != null) {
						 jurorSurvey = findJurorSurvey(survey.getId(), juror.getId());
					}
					
					if (jurorSurvey == null) {
						jurorSurvey = new JurorSurvey();
						jurorSurvey.setSurvey(survey);
						jurorSurvey.setJuror(juror);
						saveOrUpdateJurorSurvey(jurorSurvey);

						Log.i("AMF", "Juror survey added");
					}
				
					Log.i("AMF", "Juror added...");	
				}
				

				Log.i("AMF", "All jurors added!!");
				
				
				// Answers
				for (Answer answer : answers) {
					answer.setSurvey(survey);
					saveOrUpdateAnswer(answer);
				}

				Log.i("AMF", "Answers added!!");
				Log.i("AMF", "Survey persisted");
			}
		});
		
		
	}
	
	
	private JurorSurvey findJurorSurvey(Long surveyId, Long jurorId) {
		Query<JurorSurvey> query = jurorSurveyRepository.getDao().queryBuilder()
							 		  .where(Properties.SurveyId.eq(surveyId), Properties.JurorId.eq(jurorId))
							 		  .build();
		
		List<JurorSurvey> jurorSurveys = query.list();
		
		if (jurorSurveys.isEmpty()) {
			return null;
		} else {
			return jurorSurveys.get(0);
		}
	}
	
}
