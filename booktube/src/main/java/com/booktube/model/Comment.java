package com.booktube.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "COMMENT")
public class Comment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7829701461360796100L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "COMMENT_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private User user;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="BOOK_ID", updatable = false)
	private Book book;
	
	@Basic
	@Column(name = "TEXT")
	private String text;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE")
	private Date date;
	
	public Comment() {
		
	}
	
	public Comment(Integer id, User user, Book book, String text) {
		this.id = id;
		this.user = user;
		this.book = book;
		this.text = text;
		this.date = Calendar.getInstance().getTime();
	}
	
	public Comment(User user, Book book, String text) {
		this.user = user;
		this.book = book;
		this.text = text;
		//this.id = new Integer(1);
		this.date = Calendar.getInstance().getTime();
	}

	public Book getBook() {
		return book;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return this.date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
	//	result = prime * result + ((book == null) ? 0 : book.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		/*if (book == null) {
			if (other.book != null)
				return false;
		} else if (!book.equals(other.book))
			return false;
		*/
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	
	
	
}
