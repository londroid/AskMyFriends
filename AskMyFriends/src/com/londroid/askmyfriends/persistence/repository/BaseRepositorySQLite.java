package com.londroid.askmyfriends.persistence.repository;

import android.database.sqlite.SQLiteDatabase;

public abstract class BaseRepositorySQLite  {

	protected static SQLiteDatabase database;
	
	protected BaseRepositorySQLite() {
	}
	
	public static void setDb(SQLiteDatabase db) {
		database = db;
	}
	
	protected static SQLiteDatabase getDb() {
		return database;
	}
}
