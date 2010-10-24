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

    public List<Book> getAllBooks() {
        return itemDao.getAllBooks();
    }

    public void updateBook(Book book) {
        itemDao.update(book);
    }

    public void deleteBook(Book book) {
        itemDao.delete(book);
    }

    public Book getBook(Integer id) {
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

	public List<Book> findBookByTitle(String title) {
		return itemDao.findBookByTitle(title);
	}
	
	public int getCount() {
    	return itemDao.getCount();
    }

	public Iterator<Book> iterator(int first, int count) {
		return itemDao.iterator(first, count);
	}
}
