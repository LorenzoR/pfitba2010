package com.booktube.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.booktube.WicketApplication;
import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.persistence.UserDao;

public class UserDaoImpl extends AbstractDaoHibernate<User> implements UserDao {

	// public static final SessionFactory SESSION_FACTORY =
	// WicketApplication.SESSION_FACTORY;
	protected UserDaoImpl() {
		super(User.class);
	}

	public List<User> getAllUsers() {
		//User user = (User) getSession().get(User.class, 1);
		List<User> users = (List<User>) getSession().createCriteria(User.class).list();
		//List<User> users = new ArrayList<User>();
		//users.add(user);
		//return (List<User>)((AbstractDaoHibernate<User>) getSession()).loadAll();
		return users;
		/*
		 * Session session = SESSION_FACTORY.openSession(); Criteria crit =
		 * session.createCriteria(User.class);
		 * session.beginTransaction().commit(); List<User> list = crit.list();
		 * session.disconnect(); if (list.isEmpty()) return null; return list;
		 */
		// return null;
	}

	public User getUser(Integer id) {
		/*
		 * Session session = SESSION_FACTORY.openSession(); Criteria crit =
		 * session.createCriteria(User.class);
		 * crit.add(Restrictions.eq("user_id", id));
		 * session.beginTransaction().commit(); List<User> list = crit.list();
		 * session.disconnect(); System.out.println("Desconecto 2"); if
		 * (list.isEmpty()) return null; return (User) list.get(0);
		 */
		return null;
	}

	public User getUser(String username) {
		return (User) getSession().getNamedQuery("user.username")
		.setString("username", username).setMaxResults(1).uniqueResult();
		/*
		 * Session session = SESSION_FACTORY.openSession(); Criteria crit =
		 * session.createCriteria(User.class);
		 * crit.add(Restrictions.eq("username", username));
		 * session.beginTransaction().commit(); List<User> list = crit.list();
		 * session.disconnect(); System.out.println("Desconecto 3"); if
		 * (list.isEmpty()) return null; return (User) list.get(0);
		 */
		//return null;
	}

	public void update(User user) {
		// TODO Auto-generated method stub

	}

	public void insert(User user) {
		getSession().save(user);
		/*
		 * Session session = SESSION_FACTORY.openSession(); session.save(user);
		 * session.beginTransaction().commit(); session.disconnect();
		 * //logger.info("user added: " + newUser.getEmail());
		 */
	}

	public void delete(Integer id) {
		/*
		 * Session session = SESSION_FACTORY.openSession();
		 * session.delete(getUser(id)); session.flush(); session.disconnect();
		 */
	}
}
