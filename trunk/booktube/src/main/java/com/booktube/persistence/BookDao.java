package com.booktube.persistence;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.Book;
import com.booktube.service.BookService.SearchType;

public interface BookDao {
    public Book getBook(Long id);
    public void update(Book book);
    public void insert(Book book);
    public void delete(Book book);
    public List<Book> getAllBooks(int first, int count);
	public List<Book> findBookByTitle(String title, int first, int count);
	public List<Book> findBookByTag(String tag, int first, int count);
	public List<Book> findBookByAuthor(String author, int first, int count);
	public int getCount(SearchType type, String parameter);
    public Iterator<Book> iterator(int first, int count);
    public List<String> getAllTags();
}
