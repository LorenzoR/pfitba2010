package com.booktube.persistence;

import java.util.List;

import com.booktube.model.Book;
import com.booktube.model.Comment;

public interface BookDao {
    public List<Book> getAllBooks();
    public Book getBook(Integer id);
    public void update(Book book);
    public void insert(Book book);
    public void delete(Book book);
	public List<Book> findBookByTitle(String title);
}
