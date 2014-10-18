package com.londroid.askmyfriends.persistence;

import java.util.List;

public interface MainRepository<E, K> {

	public void save(E entity);
	
	public E find(K id);
	
	public void delete(K id);
	
	public void update(E entity);
	
	public List<E> getAll();
	
}
