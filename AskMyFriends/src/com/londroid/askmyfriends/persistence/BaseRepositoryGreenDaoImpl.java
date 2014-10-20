package com.londroid.askmyfriends.persistence;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.londroid.askmyfriends.persistence.greendao.dao.DaoSession;

import de.greenrobot.dao.AbstractDao;


/**
 * Generic repository class. Receives generic parameters with entity and id classes
 * 
 * @author david
 *
 * @param <E> entity class
 * @param <K> id class
 */
public class BaseRepositoryGreenDaoImpl<E, K> implements MainRepository<E, K> {

	private Class<E> entityClass;
	
	private DaoSession daoSession;
	
	private AbstractDao<E, K> dao;
	
	@SuppressWarnings("unchecked")
	public BaseRepositoryGreenDaoImpl(DaoSession daoSession, Class<E> entityClass) {
		this.daoSession = daoSession;
		this.entityClass = entityClass;
		this.dao = (AbstractDao<E,K>)daoSession.getDao(entityClass);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void save(E entity) {
		dao.insert(entity);
	}

	@Override
	public E find(K id) {
		return dao.load(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(K id) {
		dao.deleteByKeyInTx(id);
	}
	
	@SuppressWarnings("unchecked")
	public void update(E entity) {
		dao.update(entity);
	}
	
	@Override
	public List<E> getAll() {
		return dao.loadAll();
	}
		
	public void runInTransactionWithoutResult(Runnable runnable) {
		daoSession.runInTx(runnable);
	}
	
	public AbstractDao<E, K> getDao() {
		return dao;
	}
	
	public DaoSession getSession() {
		return daoSession;
	}
}
