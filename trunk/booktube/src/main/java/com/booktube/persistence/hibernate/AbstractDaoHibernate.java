package com.booktube.persistence.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * AbstractDaoHibernate
 * <p/>
 * Created by: Andrew Lombardi Copyright 2006 Mystic Coders, LLC
 */
public class AbstractDaoHibernate<T> extends HibernateDaoSupport {

	// MEMBERS

	private Class<T> entityClass;


	// CONSTRUCTORS

	protected AbstractDaoHibernate(Class<T> dataClass) {
		super();
		this.entityClass = dataClass;
	}

	// METHODS

	@SuppressWarnings("unchecked")
	private T load(Long id) {
		return (T) getSession().get(entityClass, id);
	}

	private T loadChecked(Long id) {
		T persistedObject = load(id);

		if (persistedObject == null) {
			System.out.println("EXCEPCION!!!");
		}

		return persistedObject;
	}

	@SuppressWarnings("unchecked")
	public List<T> loadAll() {
		return getSession().createCriteria(entityClass).list();
	}
	
	public void merge(T detachedObject) {
		getSession().merge(detachedObject);
	}

	public void saveOrUpdate(T persistedObject) {
		getSession().saveOrUpdate(persistedObject);
	}

	public Long insert(T persistedObject) {
		Long id = (Long) getSession().save(persistedObject);
		getSession().flush();

		return id;
	}
	
	public void delete(T persistedObject) {
		getSession().delete(persistedObject);
		getSession().flush();
	}

	public void delete(Long id) {
		delete(loadChecked(id));
	}
	
	public void update(T persistedObject) {
		getSession().merge(persistedObject);
		getSession().flush();
	}
	
	public int getCount() {
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
