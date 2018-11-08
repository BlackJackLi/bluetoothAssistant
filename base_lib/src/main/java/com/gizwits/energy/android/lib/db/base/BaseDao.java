package com.gizwits.energy.android.lib.db.base;

import android.support.annotation.Nullable;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by black-Gizwits on 2016/02/24.
 */
public abstract class BaseDao<T> {
	protected DbManager dbManager;
	private Class<T> tableClass;

	protected BaseDao(DbManager dbManager, Class<T> tableClass) {
		this.dbManager = dbManager;
		this.tableClass = tableClass;
	}


	@Nullable
	final public List<T> listAll() {
		List<T> list = null;
		try {
			list = dbManager.findAll(tableClass);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Nullable
	final public T findById(String id) {
		try {
			return dbManager.findById(tableClass, id);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
	}

	final public void insertOrUpdate(T object) {
		try {
			dbManager.saveOrUpdate(object);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	final public void insertOrUpdateAll(List<T> objects) {
		try {
			dbManager.saveOrUpdate(objects);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	final public void delete(T object) {
		try {
			dbManager.delete(object);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	final public void deleteAll(List<T> objects) {
		try {
			dbManager.delete(objects);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	final public void clearTable() {
		try {
			dbManager.delete(tableClass);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	final public void dropTable() {
		try {
			dbManager.dropTable(tableClass);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
}
