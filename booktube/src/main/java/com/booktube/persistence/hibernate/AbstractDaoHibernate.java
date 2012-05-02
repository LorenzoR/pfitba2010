package com.booktube.persistence.hibernate;

/**
 * @author <a href="mailto:gcastro@mysticcoders.com">Guillermo Castro</a>
 * @version $Revision$ $Date$
 */

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.booktube.model.Message;
import com.booktube.model.User;

/**
 * AbstractDaoHibernate
 * <p/>
 * Created by: Andrew Lombardi Copyright 2006 Mystic Coders, LLC
 */
public class AbstractDaoHibernate<T> extends HibernateDaoSupport {

	// MEMBERS

	private Class entityClass;

	private SessionFactory sessionFactory;

	// CONSTRUCTORS

	protected AbstractDaoHibernate(Class dataClass) {
		super();
		this.entityClass = dataClass;
	}

	// METHODS

	@SuppressWarnings("unchecked")
	private T load(Long id) {
		return (T) getSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	private T loadChecked(Long id) {
		T persistedObject = load(id);

		if (persistedObject == null) {
			System.out.println("EXCEPCION!!!");
		}

		return persistedObject;
	}

	public List<T> loadAll() {
		List<T> resp = new ArrayList<T>();
		resp = getSession().createCriteria(entityClass).list();
		return resp;
	}
	
	public void merge(T detachedObject) {
		getSession().merge(detachedObject);
	}

	public void save(T persistedObject) {
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
