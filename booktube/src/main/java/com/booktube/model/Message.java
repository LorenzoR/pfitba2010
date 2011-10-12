package com.booktube.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "MESSAGE")
@NamedQueries({
		@NamedQuery(name = "message.id", query = "from Message m where m.id = :id")})
public class Message implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MESSAGE_ID")
	private Long id;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "SENDER_ID")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User sender;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "MSG_RCV", joinColumns = { @JoinColumn(name = "MESSAGE_ID") }, inverseJoinColumns = { @JoinColumn(name = "USER_ID") })
	private Set<User> receiver;

	/*@Basic
	@Column(name = "REPLY_TO")
	private Integer replyTo;
	*/
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "ANSWER", joinColumns = { @JoinColumn(name = "MESSAGE_ID") })
	private Set<Message> answer;
	
	@Basic
	@Column(name = "IS_READ")
	private Boolean isRead;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE")
	private Date date;

	@Basic
	@Column(name = "SUBJECT")
	private String subject;

	@Basic
	@Column(name = "TEXT", columnDefinition = "LONGTEXT")
	private String text;
	
	public Message () {
		
	}
	
	public Message (String subject, String text, User sender, User receiver) {
		this.subject = subject;
		this.text = text;
		this.sender = sender;
		this.receiver = new HashSet<User>();
		this.receiver.add(receiver);
		this.date = Calendar.getInstance().getTime();
		this.isRead = false;
	}
	
	public Message (String subject, String text, User sender, Set<User> receiver) {
		this.subject = subject;
		this.text = text;
		this.sender = sender;
		this.receiver = receiver;
		this.date = Calendar.getInstance().getTime();
		this.isRead = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public Set<User> getReceiver() {
		return receiver;
	}

	public void setReceiver(Set<User> receiver) {
		this.receiver = receiver;
	}
	
	public User addReceiver(User receiver) {
		this.receiver.add(receiver);
		return receiver;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setRead(Boolean read) {
		this.isRead = read;
	}

	public Boolean isRead() {
		return isRead;
	}

	public void setAnswer(Set<Message> answer) {
		this.answer = answer;
	}

	public Set<Message> getAnswer() {
		return answer;
	}

}
