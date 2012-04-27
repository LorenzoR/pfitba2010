package com.booktube.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

	@OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "SENDER_ID")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User sender;

	/*@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "MSG_RCV", joinColumns = { @JoinColumn(name = "MESSAGE_ID") }, inverseJoinColumns = { @JoinColumn(name = "USER_ID") })
	private Set<User> receiver;
	*/
	
	@OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<MessageDetail> receiver;
	
	/*@Basic
	@Column(name = "REPLY_TO")
	private Integer replyTo;
	*/
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "ANSWER", joinColumns = { @JoinColumn(name = "MESSAGE_ID") })
	private Set<Message> answer;
	
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
	
	public Message (String subject, String text, User sender) {
		this.subject = subject;
		this.text = text;
		this.sender = sender;
		this.date = Calendar.getInstance().getTime();
		this.receiver = new HashSet<MessageDetail>();
	}
	
	public Message (String subject, String text, User sender, User receiver) {
		this.subject = subject;
		this.text = text;
		this.sender = sender;
		this.date = Calendar.getInstance().getTime();
		this.receiver = new HashSet<MessageDetail>();
		this.receiver.add(new MessageDetail(receiver, this));
	}
	
	public Message (String subject, String text, User sender, Set<MessageDetail> receiver) {
		this.subject = subject;
		this.text = text;
		this.sender = sender;
		this.receiver = receiver;
		this.date = Calendar.getInstance().getTime();
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

	public Set<MessageDetail> getReceiver() {
		return receiver;
	}

	public void setReceiver(Set<MessageDetail> receiver) {
		this.receiver = receiver;
	}
	
	public void addReceiver(User receiver) {
		this.receiver.add(new MessageDetail(receiver, this));
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

	public void setAnswer(Set<Message> answer) {
		this.answer = answer;
	}

	public Set<Message> getAnswer() {
		return answer;
	}
	
	/*public void setIsRead(User user) {
		Iterator<MessageDetail> iterator = this.receiver.iterator();
		
		while ( iterator.hasNext() ) {
			MessageDetail messageDetail = iterator.next();
			if ( user.getId().equals(messageDetail.getReceiver().getId()) ) {
				messageDetail.setRead(true);
				break;
			}
		}
	}*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		Message other = (Message) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
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
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	

}
