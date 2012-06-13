package com.booktube.persistence.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.booktube.WicketApplication;
import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.model.User;
import com.booktube.model.Message.Type;
import com.booktube.model.User.Gender;
import com.booktube.persistence.UserDao;
import com.booktube.model.User.Level;

public class UserDaoImpl extends AbstractDaoHibernate<User> implements UserDao {

	protected UserDaoImpl() {
		super(User.class);
	}

	public boolean usernameExists(String username) {
		return getUser(username) != null;
	}

	public List<User> getAllUsers(int first, int count) {
		List<User> users = (List<User>) getSession().createCriteria(User.class)
				.setFirstResult(first).setMaxResults(count).list();

		return users;

	}

	public List<User> getUsers(int first, int count, Gender gender,
			Integer lowerAge, Integer higherAge, String country, Date lowDate, Date highDate) {

		Criteria criteria = getSession().createCriteria(User.class);

		if ( lowDate != null ) {
			criteria.add(Restrictions.ge("registrationDate", lowDate));
		}
		
		if ( highDate != null ) {
			criteria.add(Restrictions.le("registrationDate", highDate));
		}
		
		if (lowerAge != null) {
			criteria.add(Expression
					.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 >= "
							+ lowerAge));
		}

		if (higherAge != null) {
			criteria.add(Expression
					.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 <= "
							+ higherAge));
		}

		if (gender != null) {
			criteria.add(Restrictions.eq("gender", gender));
		}

		if (country != null) {
			criteria.add(Restrictions.eq("country", country));
		}

		return (List<User>) criteria.setFirstResult(first).setMaxResults(count)
				.list();
	}

	public List<User> getUsersByCountry(int first, int count, String country) {
		return (List<User>) getSession().createCriteria(User.class)
				.add(Restrictions.eq("country", country)).setFirstResult(first)
				.setMaxResults(count).list();
	}

	public List<User> getUsers(int first, int count, Level level) {
		List<User> users = (List<User>) getSession().createCriteria(User.class)
				.add(Restrictions.eq("level", level)).setFirstResult(first)
				.setMaxResults(count).list();

		return users;
	}

	public List<User> getUsersByGender(int first, int count, Gender gender) {
		List<User> users = (List<User>) getSession().createCriteria(User.class)
				.add(Restrictions.eq("gender", gender)).setFirstResult(first)
				.setMaxResults(count).list();

		return users;
	}

	public List<User> getUsersByAge(int first, int count, int lowerAge,
			int higherAge) {

		List<User> users = (List<User>) getSession()
				.createCriteria(User.class)
				.add(Expression
						.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 >= "
								+ lowerAge))
				.add(Expression
						.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 <= "
								+ higherAge))
				// .add(Restrictions.gt("age", lowerAge))
				// .add(Restrictions.lt("age", higherAge))
				.setFirstResult(first).setMaxResults(count).list();
		return users;

		// SQLQuery query =
		// getSession().createSQLQuery("SELECT DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 AS age FROM user");

		// return query.list();
	}

	public User getUser(Long id) {
		if (id == null) {
			return null;
		} else {
			return (User) getSession().getNamedQuery("user.id")
					.setLong("id", id).setMaxResults(1).uniqueResult();
		}
	}

	public User getUser(String username) {
		return (User) getSession().getNamedQuery("user.username")
				.setString("username", username).setMaxResults(1)
				.uniqueResult();
	}

	public void update(User user) {
		super.update(user);
		// getSession().merge(user);
		// getSession().flush();

	}

	public Long insert(User user) {
		/*
		 * Long id = (Long) getSession().save(user); getSession().flush();
		 * return id;
		 */
		return super.insert(user);
	}

	public void delete(User user) {
		super.delete(user);
		// getSession().delete(user);
		// getSession().flush();
	}

	public int getCount() {
		// Criteria criteria = getSession().createCriteria(User.class);
		// criteria.setProjection(Projections.rowCount());
		// return ((Number) criteria.uniqueResult()).intValue();
		return super.getCount();
	}

	public Iterator<User> iterator(int first, int count) {
		return (Iterator<User>) getSession().createCriteria(User.class)
				.setFirstResult(first).setMaxResults(count).list().iterator();
	}

	public List<User> getUsersByRegistrationDate(int first, int count,
			Date lowDate, Date highDate) {
		Criteria criteria = getSession()
				.createCriteria(User.class)
				.add(Restrictions.and(
						Restrictions.le("registrationDate", highDate),
						Restrictions.ge("registrationDate", lowDate)))
				.setMaxResults(count);

		return (List<User>) criteria.list();
	}
}
