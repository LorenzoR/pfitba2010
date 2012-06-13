package com.booktube.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.EnumType.*;

@Entity
@Table(name = "USER")
@NamedQueries({
		@NamedQuery(name = "user.username", query = "from User u where u.username like :username"),
		@NamedQuery(name = "user.id", query = "from User u where u.id = :id") })
public class User implements Serializable {
	
	public enum Level { ADMIN, USER }
	public enum Gender { MALE, FEMALE }

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "GENDER", nullable = false)
	private Gender gender;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BIRTHDATE", nullable = false)
	private Date birthdate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGISTRATION_DATE", nullable = false)
	private Date registrationDate;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "LEVEL", nullable = false)
	private Level level;
	
	public User() {

	}

	public User(Long id, String username, String password, String firstname,
			String lastname, Level level) {
		this.username = username;
		this.password = hash(password, "SHA-1");
		this.firstname = firstname;
		this.lastname = lastname;
		this.id = id;
		this.level = level;
		this.registrationDate = Calendar.getInstance().getTime();
	}

	public User(String username, String password, String firstname,
			String lastname, Level level) {
		this.username = username;
		this.password = hash(password, "SHA-1");
		this.firstname = firstname;
		this.lastname = lastname;
		this.level = level;
		this.registrationDate = Calendar.getInstance().getTime();
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
		this.password = password;
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
	
}
