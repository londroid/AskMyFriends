package com.londroid.askmyfriends.persistence;

import android.content.Context;

import com.londroid.askmyfriends.persistence.greendao.AnswerRepository;
import com.londroid.askmyfriends.persistence.greendao.MainRepositoryGreenDao;
import com.londroid.askmyfriends.persistence.greendao.domain.Answer;

public class PersistenceManager {

	private static PersistenceManager instance;
	
	private static MainRepositoryGreenDao<Answer, Long> answerRepository;
	
	private PersistenceManager () { }
	
	public void initGreenDaoContext(Context context) {
		MainRepositoryGreenDao.init(context);
	}
	
	public static  MainRepositoryGreenDao< Answer, Long> getAnswerRepository() {
		if (answerRepository == null) {
			answerRepository = AnswerRepository.get();
		}
		return answerRepository;
	}
	
	//TODO: Other repositories

	public static PersistenceManager get() {
		
		if (instance == null) {
			instance = new PersistenceManager();
		}
		
		return instance;
		
	}
	
}
