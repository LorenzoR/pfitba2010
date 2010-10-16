package com.booktube.service;

import java.util.List;

import com.booktube.model.Book;
import com.booktube.model.Comment;

public interface BookService {
    public List<Book> getAllBooks();
    public void updateBook(Book book);
    public void deleteBook(Book book);
    public Book getBook(Integer id);
    public void insertBook(Book book);
    public List<Book> findBookByTitle(String title);
}
