package com.booktube.persistence;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.Book;

public interface BookDao {
    public List<Book> getAllBooks();
    public Book getBook(Integer id);
    public void update(Book book);
    public void insert(Book book);
    public void delete(Book book);
	public List<Book> findBookByTitle(String title);
	public List<Book> findBookByTag(String tag);
	public List<Book> findBookByAuthor(String author);
	public int getCount();
    public Iterator<Book> iterator(int first, int count);
}
