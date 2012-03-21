package com.booktube.service;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.Book;
import com.booktube.model.Comment;
import com.booktube.persistence.BookDao;
import com.booktube.persistence.UserDao;
import com.booktube.persistence.hibernate.BookDaoImpl;

public class BookServiceImpl implements BookService {
    private BookDao itemDao;
    
    public BookServiceImpl() {
        //this.dao = new BookDaoImpl();
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

    public void insertBook(Book book) {
        itemDao.insert(book);
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
	
	public int getCount(SearchType type, String parameter) {
    	return itemDao.getCount(type, parameter);
    }

	public Iterator<Book> iterator(int first, int count) {
		return itemDao.iterator(first, count);
	}
	
	 public List<String> getAllTags() {
		 return itemDao.getAllTags();
	 }
}
