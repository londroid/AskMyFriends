package com.londroid.askmyfriends.persistence.greendao;



import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.londroid.askmyfriends.persistence.MainRepository;
import com.londroid.askmyfriends.persistence.greendao.dao.DaoMaster;
import com.londroid.askmyfriends.persistence.greendao.dao.DaoSession;

import de.greenrobot.dao.AbstractDao;

public abstract class MainRepositoryGreenDao<E,K> implements MainRepository<E, K> {

	private static DaoMaster.OpenHelper helper;
	private static SQLiteDatabase database;
	private static DaoMaster daoMaster;
	protected static DaoSession daoSession;
	
	private static final String ASK_MY_FRIENDS_DB_NAME = "askmyfriends-db";
	
	protected abstract AbstractDao<E, K> getDao();
		
	public static void init(Context context) {
		// These are all static variables since they should be shared
		if (helper == null) {
			helper = new DaoMaster.DevOpenHelper(context, ASK_MY_FRIENDS_DB_NAME, null);
			database = helper.getWritableDatabase();
			daoMaster = new DaoMaster(database);
			daoSession = daoMaster.newSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void save(E entity) {
		getDao().insertInTx(entity);
	}

	@Override
	public E find(K id) {
		return getDao().load(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(K id) {
		getDao().deleteByKeyInTx(id);
	}
	
	@SuppressWarnings("unchecked")
	public void update(E entity) {
		getDao().updateInTx(entity);
	}
	
	@Override
	public List<E> getAll() {
		return getDao().loadAll();
	}
	
}
