package com.booktube.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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

	public List<Book> getAllBooks() {
		List<Book> books = (List<Book>) getSession().createCriteria(Book.class)
				.list();
		return books;
		/*
		 * Session session = SESSION_FACTORY.openSession(); Criteria crit =
		 * session.createCriteria(Book.class);
		 * session.beginTransaction().commit(); List<Book> list = crit.list();
		 * System.out.println("Desconecto"); session.disconnect(); if
		 * (list.isEmpty()) return null; return list;
		 */
		// return null;
	}

	public Book getBook(Integer id) {
		return (Book) getSession().getNamedQuery("book.id")
				.setInteger("id", id).setMaxResults(1).uniqueResult();
		/*
		 * Session session = SESSION_FACTORY.openSession(); Criteria crit =
		 * session.createCriteria(Book.class); crit.add(Restrictions.eq("id",
		 * id)); session.beginTransaction().commit(); List<Book> list =
		 * crit.list(); session.disconnect(); if (list.isEmpty()) return null;
		 * else return list.get(0);
		 */
		// return null;
	}
	
	public void update(Book book) {
		/* Parece que anda */
		getSession().merge(book);
		getSession().flush();
		/* Actualiza el libro y agrega muchos comentarios */
		//getSession().saveOrUpdate(book);
		//getSession().flush();
		/* No actualiza el libro y agrega un solo comentario */
		//getSession().saveOrUpdate(book);
		
		/*if (book.getId() != null) { // it is an update
			getSession().merge(book);
			} else { // you are saving a new one
			getSession().saveOrUpdate(book);
			}
		*///getSession().save(book);
		//getSession().flush();
		//getSession().evict(book);
		/*Session session = SESSION_FACTORY.openSession();
		session.update(book);
		session.beginTransaction().commit();
		session.disconnect();
		*/
	}

	public void insert(Book book) {
		getSession().save(book);
		/*
		 * Session session = SESSION_FACTORY.openSession(); session.save(book);
		 * session.beginTransaction().commit(); session.disconnect(); //
		 * logger.info("user added: " + newUser.getEmail());
		 */
	}

	public void delete(Book book) {
		getSession().delete(book);
		getSession().flush();
		/*
		 * Session session = SESSION_FACTORY.openSession();
		 * session.delete(getBook(id)); session.beginTransaction().commit();
		 * session.disconnect();
		 */
	}

	public List<Book> findBookByTitle(String title) {
		/*return (List<Book>) getSession().getNamedQuery("book.getByTitle")
		.setString("title", title);
		*/
		return (List<Book>) getSession().getNamedQuery("book.getByTitle")
		.setString("title", '%' + title + '%').list();
	}
}
