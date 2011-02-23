package com.booktube.service;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.Book;

public interface BookService {
    public List<Book> getAllBooks();
    public void updateBook(Book book);
    public void deleteBook(Book book);
    public Book getBook(Integer id);
    public void insertBook(Book book);
    public List<Book> findBookByTitle(String title);
    public List<Book> findBookByTag(String tag);
    public List<Book> findBookByAuthor(Integer authorId);
    public int getCount();
    public Iterator<Book> iterator(int first, int count);
}
