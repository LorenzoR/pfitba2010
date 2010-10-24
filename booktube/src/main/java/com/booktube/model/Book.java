package com.booktube.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name = "BOOK")
@NamedQueries({@NamedQuery(name = "book.id", query = "from Book b where b.id = :id"),
    @NamedQuery(name = "book.getByTitle",
            query = "from Book b where b.title LIKE :title")})
public class Book implements Serializable {

	private static final long serialVersionUID = 2241291507547593474L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BOOK_ID")
	private Integer id;

	@Basic
	@Column(name = "TITLE")
	private String title;

	@Basic
	@Column(name = "TEXT", columnDefinition="LONGTEXT")
	private String text;

	@Basic
	@Column(name = "RATING")
	private Double rating;

	@ManyToOne
	private User author;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PUBLISH_DATE")
	private Date publishDate;

	@CollectionOfElements
	@JoinTable(name="BOOK_TAG", joinColumns = @JoinColumn(name = "BOOK_ID"))
	@Column(name = "TAG")
	@OrderBy(clause="TAG")
	private Set<String> tags;

	@Basic
	@Column(name = "CATEGORY")
	private String category;

	@Basic
	@Column(name = "SUBCATEGORY")
	private String subCategory;

	/* @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent") */
	/* @OneToMany */
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy(clause="DATE ASC")
	/*@JoinTable(name = "BOOK_COMMENT", joinColumns = { @JoinColumn(name = "BOOK_ID") }, inverseJoinColumns = { @JoinColumn(name = "COMMENT_ID") })
	*/private Set<Comment> comments;

	public Book() {
	}

	public Book(Integer id, String title, String text, User author) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.author = author;
		this.publishDate = Calendar.getInstance().getTime();
	}

	public Book(String title, String text, User author) {
		this.title = title;
		this.text = text;
		this.author = author;
		//this.id = new Integer(1);
		this.publishDate = Calendar.getInstance().getTime();

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public void addTag(String tag) {
		this.tags.add(tag);
	}

	public String toString() {
		return this.getTitle() + " by " + this.getAuthor();
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date date) {
		this.publishDate = date;
	}

	public Comment addComment(User user, String text) {
		Comment comment = new Comment(user, this, text);
		this.comments.add(comment);
		return comment;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Comment> getComments() {
		return this.comments;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getSubCategory() {
		return subCategory;
	}

}
