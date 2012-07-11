package com.booktube.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class UserVotePk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private User user;
    private Book book;
 
    @ManyToOne
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
 
    @ManyToOne
    public Book getBook() {
        return book;
    }
 
    public void setBook(Book book) {
        this.book = book;
    }
 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        UserVotePk that = (UserVotePk) o;
 
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (book != null ? !book.equals(that.book) : that.book != null)
            return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (user != null ? user.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        return result;
    }
}
