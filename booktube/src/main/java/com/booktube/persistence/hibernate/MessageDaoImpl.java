package com.booktube.persistence.hibernate;

import java.util.List;

import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.model.User;
import com.booktube.persistence.MessageDao;

public class MessageDaoImpl extends AbstractDaoHibernate<Message> implements MessageDao {

	protected MessageDaoImpl() {
		super(Message.class);
	}
	
	public void update(Message message) {
		getSession().merge(message);
		getSession().flush();
	}

	public void delete(Message message) {
		getSession().delete(message);
		getSession().flush();
	}

	public Message getMessage(Integer id) {
		return (Message) getSession().getNamedQuery("message.id")
		.setInteger("id", id).setMaxResults(1).uniqueResult();
	}

	public void insert(Message message) {
		getSession().save(message);
		getSession().flush();
	}

	public List<Message> getAllMessagesFrom(User sender, int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Message> getAllMessagesTo(User receiver, int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
