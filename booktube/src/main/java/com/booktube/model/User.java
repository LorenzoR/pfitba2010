package com.booktube.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "USER")
@NamedQueries({
		@NamedQuery(name = "user.username", query = "from User u where u.username like :username"),
		@NamedQuery(name = "user.id", query = "from User u where u.id = :id") })
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum Level { ADMIN, USER }
	public enum Gender { MALE, FEMALE }

	@Id
	@GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
	@Column(name = "USER_ID")
	private Long id;

	@Basic
	@Column(name = "USERNAME", nullable = false, unique = true, length = 100)
	private String username;

	@Basic
	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Basic
	@Column(name = "FIRSTNAME", nullable = false)
	private String firstname;

	@Basic
	@Column(name = "LASTNAME", nullable = false)
	private String lastname;

	@Basic
	@Column(name = "COUNTRY", nullable = false)
	private String country;
	
	@Basic
	@Column(name = "CITY", nullable = false)
	private String city;
	
	@Basic
	@Column(name = "EMAIL", nullable = false)
	private String email;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "GENDER", nullable = false)
	private Gender gender;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDATE", nullable = false)
	private Date birthdate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGISTRATION_DATE", nullable = false)
	private Date registrationDate;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "LEVEL", nullable = false)
	private Level level;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name="USER_ID")
	private List<Book> books;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.user")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private List<UserVote> userVotes = new LinkedList<UserVote>();
	
    public List<UserVote> getUserVotes() {
        return this.userVotes;
    }
 
    public void setUserVotes(List<UserVote> userVotes) {
        this.userVotes = userVotes;
    }
    
    public void addUserVote(UserVote userVote) {
    	this.userVotes.add(userVote);
    }
    
////	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE},
////		      mappedBy="userVotes")
////	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
////	//	  @OnDelete(action=OnDeleteAction.CASCADE)
////	private Set<Book> votes;
//	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
//	@JoinTable(name = "USERVOTES", joinColumns = { @JoinColumn(name = "USER_ID", unique = false) }, inverseJoinColumns = { @JoinColumn(name = "BOOK_ID", unique = false) })
//	private Set<Book> votes;
	
	public User() {
		this.registrationDate = Calendar.getInstance().getTime();
		this.books = new ArrayList<Book>();
		//this.votes = new HashSet<Book>();
	}

	public User(Long id, String username, String password, String firstname,
			String lastname, Level level) {
		this();
		this.username = username;
		this.password = hash(password, "SHA-1");
		this.firstname = firstname;
		this.lastname = lastname;
		this.id = id;
		this.level = level;	
	}

	public User(String username, String password, String firstname,
			String lastname, Level level) {
		this();
		this.username = username;
		this.password = hash(password, "SHA-1");
		this.firstname = firstname;
		this.lastname = lastname;
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String toString() {
		return username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public void setPassword(String password) {
		this.password = hash(password, "SHA-1");
	}

	public String getPassword() {
		return password;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public static String hash(String s, String hashFunction) {
		MessageDigest m;
		String shaM = s;
		try {
			m = MessageDigest.getInstance(hashFunction);
			m.update(s.getBytes(),0,s.length());
			shaM = new BigInteger(1,m.digest()).toString(16);
			if ( shaM.length() < 40 ) {
				shaM = '0' + shaM;
			}
		    System.out.println("MD5: " + shaM);
		    
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return shaM;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	public void addBook(Book book) {
		this.books.add(book);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isAdmin() {
		return this.level == Level.ADMIN;
	}

//	public Set<Book> getVotes() {
//		return votes;
//	}
//
//	public void setVotes(Set<Book> votes) {
//		this.votes = votes;
//	}
//	
//	public void addVote(Book book) {
//		this.votes.add(book);
//	}
//	
}
