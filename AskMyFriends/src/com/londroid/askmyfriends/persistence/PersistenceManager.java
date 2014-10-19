package com.londroid.askmyfriends.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.londroid.askmyfriends.persistence.greendao.dao.DaoMaster;
import com.londroid.askmyfriends.persistence.greendao.dao.DaoSession;
import com.londroid.askmyfriends.persistence.greendao.domain.Answer;
import com.londroid.askmyfriends.persistence.greendao.domain.Question;
import com.londroid.askmyfriends.persistence.greendao.domain.Survey;

public class PersistenceManager {

	private static PersistenceManager instance;

	private DaoMaster.OpenHelper helper;
	private SQLiteDatabase database;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	
	private static final String ASK_MY_FRIENDS_DB_NAME = "askmyfriends-db";
	
	private PersistenceManager () { }
	
	public void initGreenDaoContext(Context context) {
		helper = new DaoMaster.DevOpenHelper(context, ASK_MY_FRIENDS_DB_NAME, null);
		database = helper.getWritableDatabase();
		daoMaster = new DaoMaster(database);
		daoSession = daoMaster.newSession();
	}
	
	//TODO: should be singleton!!!
	public BaseRepositoryGreenDaoImpl<Answer, Long> getAnswerRepository() {
		return new BaseRepositoryGreenDaoImpl<Answer, Long>(daoSession);
	}
	
	public BaseRepositoryGreenDaoImpl<Question, Long> getQuestionRepository() {
		return new BaseRepositoryGreenDaoImpl<Question, Long>(daoSession);
	}
	
	public BaseRepositoryGreenDaoImpl<Survey, Long> getSurveyRepository() {
		return new BaseRepositoryGreenDaoImpl<Survey, Long>(daoSession);
	}
	
	public static PersistenceManager get() {
		
		if (instance == null) {
			instance = new PersistenceManager();
		}
		
		return instance;	
	}
	
}
