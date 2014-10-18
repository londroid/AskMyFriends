package com.londroid.askmyfriends.persistence.greendao;

import com.londroid.askmyfriends.persistence.greendao.domain.Answer;

import de.greenrobot.dao.AbstractDao;

public class AnswerRepository extends MainRepositoryGreenDao<Answer, Long> {

	private static MainRepositoryGreenDao<Answer, Long> instance;
	

	public static MainRepositoryGreenDao<Answer, Long> get() {
	
		if (instance == null) {
			instance = new AnswerRepository();
		}
		
		return instance;
	}

	@Override
	protected AbstractDao<Answer, Long> getDao() {
		return daoSession.getAnswerDao();
	}

	
}
