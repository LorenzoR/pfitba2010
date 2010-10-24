package com.booktube.persistence.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.booktube.WicketApplication;
import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.persistence.UserDao;

public class UserDaoImpl extends AbstractDaoHibernate<User> implements UserDao {

	protected UserDaoImpl() {
		super(User.class);
	}

	public List<User> getAllUsers() {
		List<User> users = (List<User>) getSession().createCriteria(User.class)
				.list();

		return users;

	}

	public User getUser(Integer id) {
		return (User) getSession().getNamedQuery("user.id")
				.setInteger("id", id).setMaxResults(1).uniqueResult();
	}

	public User getUser(String username) {
		return (User) getSession().getNamedQuery("user.username")
				.setString("username", username).setMaxResults(1)
				.uniqueResult();
	}

	public void update(User user) {
		getSession().merge(user);
		getSession().flush();

	}

	public void insert(User user) {
		getSession().save(user);
	}

	public void delete(User user) {
		getSession().delete(user);
		getSession().flush();
	}

	public int getCount() {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public Iterator<User> iterator(int first, int count) {
		return (Iterator<User>) getSession().createCriteria(User.class)
				.setFirstResult(first).setMaxResults(count).list().iterator();
	}
}
