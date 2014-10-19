package com.londroid.askmyfriends.persistence.greendao;

import java.util.List;

import com.londroid.askmyfriends.persistence.MainRepository;

import de.greenrobot.dao.AbstractDao;

public abstract class MainRepositoryGreenDao<E,K> implements MainRepository<E, K> {

	protected abstract AbstractDao<E,K> getDao();
	
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
		
	public void runInTransaction(Runnable runnable) {
		getDao().getSession().runInTx(runnable);
	}
}
