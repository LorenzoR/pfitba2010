package com.booktube.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.wicket.IClusterable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "RATING")
public class Rating implements IClusterable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Rating model for storing the ratings, typically this comes from a
	 * database.
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RATING_ID")
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "BOOK_ID")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Book book;

	@Basic
	@Column(name = "NR_OF_VOTES")
	private Integer nrOfVotes = 0;

	@Basic
	@Column(name = "SUM_OF_RATINGS")
	private Integer sumOfRatings = 0;

	@Basic
	@Column(name = "RATING")
	private Double rating = (double) 0;

	public Rating() {
		
	}
	
	public Rating(Book book) {
		this.book = book;
	}
	
	public Rating (Long id, Integer sumOfRatings, Integer nrOfVotes, Double rating, Book book) {
		this.id = id;
		this.sumOfRatings = sumOfRatings;
		this.nrOfVotes = nrOfVotes;
		this.rating = rating;
		this.book = book;
	}
	
	public Rating (Integer sumOfRatings, Integer nrOfVotes, Double rating, Book book) {
		this.sumOfRatings = sumOfRatings;
		this.nrOfVotes = nrOfVotes;
		this.rating = rating;
		this.book = book;
	}
	
	public void setNrOfVotes(int nrOfVotes) {
		this.nrOfVotes = nrOfVotes;
	}

	public void setSumOfRatings(int sumOfRatings) {
		this.sumOfRatings = sumOfRatings;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	/**
	 * Returns whether the star should be rendered active.
	 * 
	 * @param star
	 *            the number of the star
	 * @return true when the star is active
	 */
	public boolean isActive(int star) {
		return star < ((int) (rating + 0.5));
	}

	/**
	 * Gets the number of cast votes.
	 * 
	 * @return the number of cast votes.
	 */
	public Integer getNrOfVotes() {
		return nrOfVotes;
	}

	/**
	 * Adds the vote from the user to the total of votes, and calculates the
	 * rating.
	 * 
	 * @param nrOfStars
	 *            the number of stars the user has cast
	 */
	public void addRating(int nrOfStars) {
		nrOfVotes++;
		sumOfRatings += nrOfStars;
		rating = sumOfRatings / (1.0 * nrOfVotes);
	}

	/**
	 * Gets the rating.
	 * 
	 * @return the rating
	 */
	public Double getRating() {
		return rating;
	}

	/**
	 * Returns the sum of the ratings.
	 * 
	 * @return the sum of the ratings.
	 */
	public int getSumOfRatings() {
		return sumOfRatings;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nrOfVotes == null) ? 0 : nrOfVotes.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result
				+ ((sumOfRatings == null) ? 0 : sumOfRatings.hashCode());
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
		Rating other = (Rating) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nrOfVotes == null) {
			if (other.nrOfVotes != null)
				return false;
		} else if (!nrOfVotes.equals(other.nrOfVotes))
			return false;
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else if (!rating.equals(other.rating))
			return false;
		if (sumOfRatings == null) {
			if (other.sumOfRatings != null)
				return false;
		} else if (!sumOfRatings.equals(other.sumOfRatings))
			return false;
		return true;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	

}
