package com.booktube.persistence.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.booktube.model.Book;
import com.booktube.model.BookTag;
import com.booktube.persistence.BookDao;

public class BookDaoImpl extends AbstractDaoHibernate<Book> implements BookDao {

	// public static final SessionFactory SESSION_FACTORY =
	// WicketApplication.SESSION_FACTORY;

	protected BookDaoImpl() {
		super(Book.class);
	}

	public void update(Book book) {
		// getSession().merge(book);
		// getSession().flush();
		super.update(book);
	}

	public Long insert(Book book) {
		/*
		 * System.out.println("COMMENTS: " + book.getComments().toString());
		 * System.out.println("TAGS: " + book.getTags().toString()); Long
		 * newBookId = (Long) getSession().save(book); getSession().flush();
		 * 
		 * return newBookId;
		 */
		return super.insert(book);
	}

	public void delete(Book book) {
		/*
		 * System.out.println("Borro libro " + book); getSession().delete(book);
		 * getSession().flush();
		 */
		super.delete(book);
		// getSession().refresh(book);
		// getSession().delete(book);
		// getSession().flush();

	}

	@SuppressWarnings("unchecked")
	public List<Book> getAllBooks(int first, int count) {
		List<Book> books = (List<Book>) getSession().createCriteria(Book.class)
				.setFirstResult(first).setMaxResults(count)
				.addOrder(Order.asc("title"))
				.list();
		return books;
	}

	public Book getBook(Long id) {
		return (Book) getSession().getNamedQuery("book.id").setLong("id", id)
				.setMaxResults(1).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Book> findBookByTitle(String title, int first, int count) {
		return (List<Book>) getSession().createCriteria(Book.class)
				.add(Restrictions.ilike("title", '%' + title + '%'))
				.setFirstResult(first).setMaxResults(count).list();
	}

	@SuppressWarnings("unchecked")
	public List<Book> findBookByTag(String tag, int first, int count) {
		return (List<Book>) getSession().createCriteria(Book.class)
				.createCriteria("tags").add(Restrictions.eq("value", tag))
				.setFirstResult(first).setMaxResults(count).list();
	}

	@SuppressWarnings("unchecked")
	public List<Book> findBookByAuthor(String author, int first, int count) {
		return (List<Book>) getSession().createCriteria(Book.class)
				.createCriteria("author")
				.add(Restrictions.eq("username", author)).setFirstResult(first)
				.setMaxResults(count).list();

	}

	public int getCount() {
		return super.getCount();
	}

	@SuppressWarnings("unchecked")
	public Iterator<Book> iterator(int first, int count) {
		return (Iterator<Book>) getSession().createCriteria(Book.class)
				.setFirstResult(first).setMaxResults(count).list().iterator();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllTags() {
		return (List<String>) getSession()
				.createCriteria(BookTag.class)
				.setProjection(
						Projections.distinct(Projections.projectionList().add(
								Projections.property("value"), "value")))
				.setFirstResult(0).setMaxResults(Integer.MAX_VALUE)
				.addOrder(Order.asc("value")).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getCategories(int first, int count) {
		return (List<String>) getSession()
				.createCriteria(Book.class)
				.setProjection(
						Projections.distinct(Projections.projectionList().add(
								Projections.property("category"), "category")))
				.setFirstResult(first).setMaxResults(count)
				.addOrder(Order.asc("category")).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getSubcategories(int first, int count, String category) {

		Criteria criteria = getSession().createCriteria(Book.class);

		if (StringUtils.isNotBlank(category)) {
			criteria.add(Restrictions.eq("category", category));
		}

		return (List<String>) criteria
				.setProjection(
						Projections.distinct(Projections.projectionList().add(
								Projections.property("subcategory"),
								"subcategory"))).setFirstResult(first)
				.setMaxResults(count).addOrder(Order.asc("category")).list();
	}

	@SuppressWarnings("unchecked")
	public List<Book> getBooks(int first, int count, Long bookId,
			String author, String title, String tag, String category,
			String subcategory, Date lowPublishDate, Date highPublishDate, Double lowRating, Double highRating) {
		return (List<Book>) createCriteria(bookId, author, title, tag,
				category, subcategory, lowPublishDate, highPublishDate, lowRating, highRating)
				.setFirstResult(first).setMaxResults(count).list();
	}

	public int getCount(Long bookId, String author, String title, String tag,
			String category, String subcategory, Date lowPublishDate,
			Date highPublishDate, Double lowRating, Double highRating) {
		Criteria criteria = createCriteria(bookId, author, title, tag,
				category, subcategory, lowPublishDate, highPublishDate, lowRating, highRating);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	private Criteria createCriteria(Long bookId, String author, String title,
			String tag, String category, String subcategory,
			Date lowPublishDate, Date highPublishDate, Double lowRating, Double highRating) {
		Criteria criteria = getSession().createCriteria(Book.class);

		if (bookId != null) {
			criteria.add(Restrictions.eq("id", bookId));
		}

		if (lowPublishDate != null) {
			System.out.println("**LOW " + lowPublishDate);
			criteria.add(Restrictions.ge("publishDate", lowPublishDate));
		}

		if (highPublishDate != null) {
			System.out.println("**HIGH " + highPublishDate);
			criteria.add(Restrictions.le("publishDate", highPublishDate));
		}

		if (StringUtils.isNotBlank(title)) {
			criteria.add(Restrictions.ilike("title", "%" + title + "%"));
		}

		if (StringUtils.isNotBlank(category)) {
			criteria.add(Restrictions.eq("category", category));
		}

		if (StringUtils.isNotBlank(subcategory)) {
			criteria.add(Restrictions.eq("subcategory", subcategory));
		}

		if (StringUtils.isNotBlank(author)) {
			System.out.println("*** author> " + author);
			criteria.createCriteria("author").add(
					Restrictions.eq("username", author));
		}

		if (StringUtils.isNotBlank(tag)) {
			System.out.println("*** tag> " + tag);
			criteria.createCriteria("tags").add(Restrictions.eq("value", tag));
		}
		
		Criteria ratingCriteria = criteria.createCriteria("rating");
		
		if ( lowRating != null ) {
			System.out.println("*** lowRating> " + lowRating);
			ratingCriteria.add(
					Restrictions.ge("rating", lowRating));
		}
		
		if ( highRating != null ) {
			System.out.println("*** highRating> " + highRating);
			ratingCriteria.add(
					Restrictions.le("rating", highRating));
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllTitles() {
		return (List<String>) getSession()
				.createCriteria(Book.class)
				.setProjection(
						Projections.distinct(Projections.projectionList().add(
								Projections.property("title"), "title")))
				.setFirstResult(0).setMaxResults(Integer.MAX_VALUE)
				.addOrder(Order.asc("title")).list();
	}

}
