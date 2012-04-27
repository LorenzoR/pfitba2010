package com.booktube.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.OrderBy;
import org.hibernate.mapping.Value;

@Entity
@Table(name = "BOOK")
@NamedQueries({
		@NamedQuery(name = "book.id", query = "from Book b where b.id = :id"),
		@NamedQuery(name = "book.getByTitle", query = "from Book b where b.title LIKE :title")})
public class Book implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BOOK_ID")
	private Long id;

	@Basic
	@Column(name = "TITLE", nullable = false)
	private String title;

	@Basic
	@Column(name = "TEXT", nullable = false, columnDefinition = "LONGTEXT")
	private String text;

	/*@Basic
	@Column(name = "RATING")
	*/
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "RATING_ID")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Rating rating;

	//@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private User author;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PUBLISH_DATE", nullable = false)
	private Date publishDate;

	/*@CollectionOfElements
	@JoinTable(name = "BOOK_TAG", joinColumns = @JoinColumn(name = "BOOK_ID"))
	@Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE})
	@Column(name = "TAG")
	@OrderBy(clause = "TAG")
	private Set<String> tags;
	*/
	
	/*@ElementCollection
	@JoinTable(name = "BOOK_TAG", joinColumns = @JoinColumn(name = "BOOK_ID"))
	@Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN-})
	@Column(name = "TAG")
	@OrderBy(clause = "TAG")
	*/
	/* CASI ANDAAAAA
	 * @ElementCollection
	@OrderBy(clause = "TAGS ASC")
	@Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private Set<String> tags;
	*/
	
	@OneToMany(mappedBy = "book", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
	@OnDelete(action=OnDeleteAction.CASCADE)
	@OrderBy(clause = "TEXT ASC")
	private Set<BookTag> tags;
	
	/* @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent") */
	/* @OneToMany */
	@OneToMany(mappedBy = "book", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
	@OnDelete(action=OnDeleteAction.CASCADE)
	@OrderBy(clause = "DATE ASC")
	private Set<Comment> comments;
	/*
	 * @JoinTable(name = "BOOK_COMMENT", joinColumns = { @JoinColumn(name =
	 * "BOOK_ID") }, inverseJoinColumns = { @JoinColumn(name = "COMMENT_ID") })
	 */
	
	/*@OneToMany(cascade=CascadeType.ALL, mappedBy="customer", fetch=FetchType.EAGER, targetEntity = String.class)
    //@JoinTable(name = "BOOK_TAG", joinColumns = @JoinColumn(name = "BOOK_ID"))
    //@Sort(type = SortType.COMPARATOR, comparator = TicketComparator.class)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<String> tags;
*/
	
	@Basic
	@Column(name = "CATEGORY", nullable = false)
	private String category;

	@Basic
	@Column(name = "SUBCATEGORY")
	private String subCategory;

	@Basic
	@Column(name = "HITS")
	private Long hits;
	
	public Book() {
	}

	public Book(Long id, String title, String text, User author) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.author = author;
		this.publishDate = Calendar.getInstance().getTime();
		this.rating = new Rating();
		this.tags = new HashSet<BookTag>();
		this.comments = new HashSet<Comment>();
	}

	public Book(String title, String text, User author) {
		this.title = title;
		this.text = text;
		this.author = author;
		this.publishDate = Calendar.getInstance().getTime();
		this.rating = new Rating();
		this.tags = new HashSet<BookTag>();
		this.comments = new HashSet<Comment>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Set<BookTag> getTags() {
		return tags;
	}

	public void setTags(Set<BookTag> tags) {
		this.tags = tags;
	}

	public BookTag addTag(String text) {
		BookTag tag = new BookTag(text, this);
		this.tags.add(tag);
		System.out.println(this.tags.toString());
		return tag;
	}

	public String toString() {
		if ( tags != null ) {
			return getTitle() + " by " + this.getAuthor() + "Tags: " + Arrays.toString(tags.toArray());
		}
		else {
			return getTitle() + " by " + this.getAuthor();
		}
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
		System.out.println(this.comments.toString());
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

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public Long getHits() {
		return hits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((hits == null) ? 0 : hits.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((publishDate == null) ? 0 : publishDate.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result
				+ ((subCategory == null) ? 0 : subCategory.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Book other = (Book) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (hits == null) {
			if (other.hits != null)
				return false;
		} else if (!hits.equals(other.hits))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (publishDate == null) {
			if (other.publishDate != null)
				return false;
		} else if (!publishDate.equals(other.publishDate))
			return false;
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else if (!rating.equals(other.rating))
			return false;
		if (subCategory == null) {
			if (other.subCategory != null)
				return false;
		} else if (!subCategory.equals(other.subCategory))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	
	
}
