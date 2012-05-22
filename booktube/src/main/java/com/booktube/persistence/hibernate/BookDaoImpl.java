package com.booktube.persistence.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.mortbay.jetty.security.SSORealm;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import com.booktube.WicketApplication;
import com.booktube.model.Book;
import com.booktube.model.BookTag;
import com.booktube.model.Campaign;
import com.booktube.model.Message;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.persistence.BookDao;
import com.booktube.service.BookService.SearchType;

public class BookDaoImpl extends AbstractDaoHibernate<Book> implements BookDao {

	// public static final SessionFactory SESSION_FACTORY =
	// WicketApplication.SESSION_FACTORY;

	protected BookDaoImpl() {
		super(Book.class);
	}

	public void update(Book book) {
		//getSession().merge(book);
		//getSession().flush();
		super.update(book);
	}

	public Long insert(Book book) {
		/*System.out.println("COMMENTS: " + book.getComments().toString());
		System.out.println("TAGS: " + book.getTags().toString());
		Long newBookId = (Long) getSession().save(book);
		getSession().flush();

		return newBookId;*/
		return super.insert(book);
	}

	public void delete(Book book) {
		/*System.out.println("Borro libro " + book);
		getSession().delete(book);
		getSession().flush();*/
		super.delete(book);
	}

	public List<Book> getAllBooks(int first, int count) {
		List<Book> books = (List<Book>) getSession().createCriteria(Book.class)
				.setFirstResult(first).setMaxResults(count).list();
		return books;
	}

	public Book getBook(Long id) {
		return (Book) getSession().getNamedQuery("book.id").setLong("id", id)
				.setMaxResults(1).uniqueResult();
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
		return (List<Book>) getSession().createCriteria(Book.class)
				.createCriteria("tags")
				.add(Restrictions.eq("value", tag))
				.setFirstResult(first)
				.setMaxResults(count)
				.list();
	}

	public List<Book> findBookByAuthor(String author, int first, int count) {
		return (List<Book>) getSession().createCriteria(Book.class)
				.createCriteria("author")
				.add(Restrictions.eq("username", author)).setFirstResult(first)
				.setMaxResults(count).list();

	}
	
	public int getCount() {
		return super.getCount();
	}

	public int getCount(SearchType type, String parameter) {
		Criteria criteria;

		switch (type) {
		case TAG:
			
			return ((Number) getSession().createCriteria(Book.class)
			.createCriteria("tags")
			.add(Restrictions.eq("value", parameter)).setProjection(Projections.rowCount()).uniqueResult()).intValue();

		case TITLE:
			return ((Long) getSession()
					.createQuery(
							"select count(*) from Book book where "
									+ "book.title LIKE :title")
					.setString("title", "%" + parameter + "%").uniqueResult())
					.intValue();
		case AUTHOR:
			criteria = getSession().createCriteria(Book.class)
					.createCriteria("author")
					.add(Restrictions.eq("username", parameter));
			criteria.setProjection(Projections.rowCount());
			return ((Number) criteria.uniqueResult()).intValue();
		default:
			criteria = getSession().createCriteria(Book.class);
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

	public List<BookTag> getAllTags() {
		/*
		 * ANDA BIEN SQLQuery query =
		 * getSession().createSQLQuery("SELECT text FROM booktag ORDER BY text"
		 * );
		 * 
		 * return query.list();
		 */

		Criteria criteria = getSession().createCriteria(BookTag.class)
				.setFirstResult(0).setMaxResults(Integer.MAX_VALUE);

		return (List<BookTag>) criteria.list();

		/*
		 * CriteriaQuery <String> q = getSession().createQuery(Book.class);
		 * Root<Book> c = q.from(Book.class);
		 * q.select(c.get("currency")).distinct(true);
		 */
		/*
		 * Criteria crit = getSession().createCriteria(Book.class)
		 * .setFirstResult(0).setMaxResults(99);
		 * 
		 * ProjectionList proList = Projections.projectionList();
		 * //proList.add(Projections.property("tags"), "tags"); //proList.add(
		 * Projections.rowCount() ); //proList.add(
		 * Projections.property("category")); proList.add(
		 * Projections.property("comments")); crit.setProjection(proList);
		 * crit.setResultTransformer(Transformers.aliasToBean(Book.class));
		 * 
		 * System.out.println("Criteria es " + crit);
		 * 
		 * System.out.println("LISTA: " + crit.list().toString());
		 * 
		 * 
		 * 
		 * return crit.list();
		 */
		/*
		 * List<String> tags = (List<String>) getSession()
		 * .createCriteria(Book.class) .setProjection(
		 * Projections.distinct(Projections.projectionList().add(
		 * Projections.property("tags"), "tags"))).list(); return tags;
		 */
	}

	public List<String> getCategories(int first, int count) {
		return (List<String>) getSession()
				.createCriteria(Book.class)
				.setProjection(
						Projections.distinct(Projections.projectionList().add(
								Projections.property("category"), "category")))
				.setFirstResult(first).setMaxResults(count).list();
	}

	public List<String> getSubcategories(int first, int count) {
		return (List<String>) getSession()
				.createCriteria(Book.class)
				.setProjection(
						Projections.distinct(Projections.projectionList().add(
								Projections.property("subcategory"),
								"subcategory"))).setFirstResult(first)
				.setMaxResults(count).list();
	}

}
