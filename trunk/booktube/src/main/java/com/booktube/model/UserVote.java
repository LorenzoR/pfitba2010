package com.booktube.model;


import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "USER_VOTE")
@AssociationOverrides({
		@AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
		@AssociationOverride(name = "pk.book", joinColumns = @JoinColumn(name = "book_id")) })
public class UserVote implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UserVotePk pk = new UserVotePk();

	@EmbeddedId
	private UserVotePk getPk() {
		return pk;
	}
	
	public UserVote() {
		
	}
	
	public UserVote(User user, Book book) {
		setUser(user);
		setBook(book);
	}

//	private void setPk(UserVotePk pk) {
//		this.pk = pk;
//	}

	@Transient
	public User getUser() {
		return getPk().getUser();
	}

	public void setUser(User user) {
		getPk().setUser(user);
	}

	@Transient
	public Book getBook() {
		return getPk().getBook();
	}

	public void setBook(Book book) {
		getPk().setBook(book);
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserVote that = (UserVote) o;

		if (getPk() != null ? !getPk().equals(that.getPk())
				: that.getPk() != null)
			return false;

		return true;
	}

	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
}