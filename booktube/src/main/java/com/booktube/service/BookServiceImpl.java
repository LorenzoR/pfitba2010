package com.booktube.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.booktube.model.Book;
import com.booktube.model.BookTag;
import com.booktube.persistence.BookDao;

public class BookServiceImpl implements BookService {
	private BookDao itemDao;

	public BookServiceImpl() {
		// this.dao = new BookDaoImpl();
	}

	public void updateBook(Book book) {
		itemDao.update(book);
	}

	public void deleteBook(Book book) {
		itemDao.delete(book);
	}

	public Book getBook(Long id) {
		return itemDao.getBook(id);
	}

	public Long insertBook(Book book) {
		return itemDao.insert(book);
	}

	public BookDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(BookDao itemDao) {
		this.itemDao = itemDao;
	}

	public List<Book> getAllBooks(int first, int count) {
		return itemDao.getAllBooks(first, count);
	}

	public List<Book> findBookByTitle(String title, int first, int count) {
		return itemDao.findBookByTitle(title, first, count);
	}

	public List<Book> findBookByTag(String tag, int first, int count) {
		return itemDao.findBookByTag(tag, first, count);
	}

	public List<Book> findBookByAuthor(String author, int first, int count) {
		return itemDao.findBookByAuthor(author, first, count);
	}
	
	public int getCount() {
		return itemDao.getCount();
	}

	public int getCount(SearchType type, String parameter) {
		return itemDao.getCount(type, parameter);
	}

	public Iterator<Book> iterator(int first, int count) {
		return itemDao.iterator(first, count);
	}

	public List<BookTag> getAllTags() {
		return itemDao.getAllTags();
	}

	public List<String> getCategories(int first, int count) {
		return itemDao.getCategories(first, count);
	}

	public List<String> getSubcategories(int first, int count) {
		return itemDao.getSubcategories(first, count);
	}

	public List<Book> getBooks(int first, int count, Long bookId, String author, String title, String tag,
			String category, String subcategory, Date lowPublishDate,
			Date highPublishDate) {
		return itemDao.getBooks(first, count, bookId, author, title, tag, category, subcategory, lowPublishDate, highPublishDate);
	}

	public int getCount(Long bookId, String author, String title, String tag,
			String category, String subcategory, Date lowPublishDate,
			Date highPublishDate) {
		return itemDao.getCount(bookId, author, title, tag, category, subcategory, lowPublishDate, highPublishDate);
	}
}
