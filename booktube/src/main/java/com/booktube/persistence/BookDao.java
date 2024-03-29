package com.booktube.persistence;

import java.util.Date;

import java.util.Iterator;
import java.util.List;
import com.booktube.model.Book;


public interface BookDao {
    public Book getBook(Long id);
    public void update(Book book);
    public Long insert(Book book);
    public void delete(Book book);
    public List<Book> getAllBooks(int first, int count);
	public List<Book> findBookByTitle(String title, int first, int count);
	public List<Book> findBookByTag(String tag, int first, int count);
	public List<Book> findBookByAuthor(String author, int first, int count);
	public List<Book> getBooks(int first, int count, Long bookId, String author, String title, String tag, String category, String subcategory, Date lowPublishDate, Date highPublishDate, Double lowRating, Double highRating);
	public int getCount();
	public int getCount(Long bookId, String author, String title, String tag, String category, String subcategory, Date lowPublishDate, Date highPublishDate, Double lowRating, Double highRating);
    public Iterator<Book> iterator(int first, int count);
    public List<String> getAllTitles();
    public List<String> getAllTags();
    public List<String> getCategories(int first, int count);
    public List<String> getSubcategories(int first, int count, String category);
}
