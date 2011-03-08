package com.booktube.persistence.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.mortbay.jetty.security.SSORealm;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import com.booktube.WicketApplication;
import com.booktube.model.Book;
import com.booktube.model.Comment;
import com.booktube.model.User;
import com.booktube.persistence.BookDao;

public class BookDaoImpl extends AbstractDaoHibernate<Book> implements BookDao {

	// public static final SessionFactory SESSION_FACTORY =
	// WicketApplication.SESSION_FACTORY;

	protected BookDaoImpl() {
		super(Book.class);
	}

	public List<Book> getAllBooks(int first, int count) {
		List<Book> books = (List<Book>) getSession().createCriteria(Book.class)
				.setFirstResult(first).setMaxResults(count).list();
		return books;
	}

	public Book getBook(Integer id) {
		return (Book) getSession().getNamedQuery("book.id")
				.setInteger("id", id).setMaxResults(1).uniqueResult();
	}

	public void update(Book book) {
		getSession().merge(book);
		getSession().flush();
	}

	public void insert(Book book) {
		getSession().save(book);
	}

	public void delete(Book book) {
		getSession().delete(book);
		getSession().flush();
	}

	public List<Book> findBookByTitle(String title, int first, int count) {

		/*
		 * return (List<Book>) getSession().getNamedQuery("book.getByTitle")
		 * .setString("title", '%' + title + '%').setFirstResult(first)
		 * .setMaxResults(count).list();
		 */
		return (List<Book>) getSession().createCriteria(Book.class)
				.add(Restrictions.ilike("title", '%' + title + '%'))
				.setFirstResult(first).setMaxResults(count).list();
	}

	public List<Book> findBookByTag(String tag, int first, int count) {

		return (List<Book>) getSession()
				.createQuery(
						"from Book book " + "where :x in elements(book.tags)")
				.setString("x", tag).setFirstResult(first).setMaxResults(count)
				.list();

	}

	public List<Book> findBookByAuthor(String author, int first, int count) {
		return (List<Book>) getSession().createCriteria(Book.class)
				.createCriteria("author")
				.add(Restrictions.eq("username", author)).setFirstResult(first)
				.setMaxResults(count).list();

	}

	public int getCount(String type, String parameter) {

		if (type.equals("tag")) {
			return ((Long) getSession()
					.createQuery(
							"select count(*) from Book book "
									+ "where :x in elements(book.tags)")
					.setString("x", parameter).uniqueResult()).intValue();
		} else if (type.equals("title")) {
			return ((Long) getSession()
					.createQuery(
							"select count(*) from Book where "
									+ "Book.title LIKE :title")
					.setString("title", parameter).uniqueResult()).intValue();
		} else if (type.equals("author")) {
			Criteria criteria = getSession().createCriteria(Book.class)
					.createCriteria("author")
					.add(Restrictions.eq("username", parameter));
			criteria.setProjection(Projections.rowCount());
			return ((Number) criteria.uniqueResult()).intValue();
		} else {
			Criteria criteria = getSession().createCriteria(Book.class);
			criteria.setProjection(Projections.rowCount());
			return ((Number) criteria.uniqueResult()).intValue();
		}

	}

	public int getCountByTag(String tag) {
		Query criteria = getSession().createQuery(
				"from Book book " + "where :x in elements(book.tags)")
				.setString("x", tag);
		return criteria.list().size();

	}

	public Iterator<Book> iterator(int first, int count) {
		return (Iterator<Book>) getSession().createCriteria(Book.class)
				.setFirstResult(first).setMaxResults(count).list().iterator();
	}

}
