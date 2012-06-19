package com.booktube.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.booktube.model.Book;
import com.booktube.model.BookTag;

public interface BookService {
	
	public enum SearchType {ALL, AUTHOR, TAG, TITLE, RATING};
	
    public void updateBook(Book book);
    public void deleteBook(Book book);
    public Book getBook(Long id);
    public Long insertBook(Book book);
    public List<Book> getAllBooks(int first, int count);
    public List<Book> findBookByTitle(String title, int first, int count);
    public List<Book> findBookByTag(String tag, int first, int count);
    public List<Book> findBookByAuthor(String author, int first, int count);
    public List<Book> getBooks(int first, int count, Long bookId, String author, String title, String tag, String category, String subcategory, Date lowPublishDate, Date highPublishDate);
    public int getCount(Long bookId, String author, String title, String tag, String category, String subcategory, Date lowPublishDate, Date highPublishDate);
    public int getCount();
    public int getCount(SearchType type, String parameter);
    public Iterator<Book> iterator(int first, int count);
    public List<BookTag> getAllTags();
    public List<String> getCategories(int first, int count);
    public List<String> getSubcategories(int first, int count);
}
