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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "CAMPAIGN")
@NamedQueries({
	@NamedQuery(name = "campaign.id", query = "from Campaign c where c.id = :id")})
public class Campaign implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MESSAGE_ID")
	private Long id;
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "SENDER_ID")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User sender;
	
	@OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<CampaignDetail> receiver;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE")
	private Date date;

	@Basic
	@Column(name = "SUBJECT")
	private String subject;

	@Basic
	@Column(name = "TEXT", columnDefinition = "LONGTEXT")
	private String text;
	
	public Campaign() {
		
	}
	
	public Campaign(String subject, String text, User sender, Set<CampaignDetail> receiver) {
		this.subject = subject;
		this.text = text;
		this.sender = sender;
		this.receiver = receiver;
		this.date = Calendar.getInstance().getTime();
	}
	
	public Campaign(String subject, String text, User sender) {
		this.subject = subject;
		this.text = text;
		this.sender = sender;
		this.receiver = new HashSet<CampaignDetail>();
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

	public Set<CampaignDetail> getReceiver() {
		return receiver;
	}

	public void setReceiver(Set<CampaignDetail> receiver) {
		this.receiver = receiver;
	}
	
	public void addReceiver(CampaignDetail receiver) {
		this.receiver.add(receiver);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Campaign other = (Campaign) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
