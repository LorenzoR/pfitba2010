package com.booktube.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.wicket.IClusterable;

@Entity
@Table(name = "RATING")
public class Rating implements IClusterable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3044456736050790746L;
	/**
	 * Rating model for storing the ratings, typically this comes from a
	 * database.
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RATING_ID")
	private Integer id;

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
	
	public Rating (Integer id, Integer sumOfRatings, Integer nrOfVotes, Double rating) {
		this.id = id;
		this.sumOfRatings = sumOfRatings;
		this.nrOfVotes = nrOfVotes;
		this.rating = rating;
	}
	
	public Rating (Integer sumOfRatings, Integer nrOfVotes, Double rating) {
		this.sumOfRatings = sumOfRatings;
		this.nrOfVotes = nrOfVotes;
		this.rating = rating;
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

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
