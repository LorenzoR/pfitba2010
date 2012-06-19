package com.booktube.persistence;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.booktube.model.Book;
import com.booktube.model.BookTag;
import com.booktube.model.Message;
import com.booktube.model.User;
import com.booktube.service.BookService.SearchType;

public interface BookDao {
    public Book getBook(Long id);
    public void update(Book book);
    public Long insert(Book book);
    public void delete(Book book);
    public List<Book> getAllBooks(int first, int count);
	public List<Book> findBookByTitle(String title, int first, int count);
	public List<Book> findBookByTag(String tag, int first, int count);
	public List<Book> findBookByAuthor(String author, int first, int count);
	public List<Book> getBooks(int first, int count, Long bookId, String author, String title, String tag, String category, String subcategory, Date lowPublishDate, Date highPublishDate);
	public int getCount();
	public int getCount(Long bookId, String author, String title, String tag, String category, String subcategory, Date lowPublishDate, Date highPublishDate);
	public int getCount(SearchType type, String parameter);
    public Iterator<Book> iterator(int first, int count);
    public List<BookTag> getAllTags();
    public List<String> getCategories(int first, int count);
    public List<String> getSubcategories(int first, int count);
}
