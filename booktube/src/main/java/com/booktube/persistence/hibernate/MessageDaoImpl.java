package com.booktube.persistence.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.model.MessageDetail;
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
		return (List<Message>) getSession().createCriteria(Message.class)
		.add(Restrictions.eq("sender", sender))
		.setFirstResult(first).setMaxResults(count).list();
	}

	public List<Message> getAllMessagesTo(User receiver, int first, int count) {
		Criteria criteria = getSession().createCriteria(Message.class)
				.createCriteria("receiver")
				.add(Restrictions.eq("receiver", receiver))
				.setFirstResult(first).setMaxResults(count);
		
		return (List<Message>) criteria.list();
	}

	public int countMessagesFrom(User sender) {
		Criteria criteria = getSession().createCriteria(Message.class)
				.add(Restrictions.eq("sender", sender));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public int countMessagesTo(User receiver) {
		Criteria criteria = getSession().createCriteria(Message.class)
				.createCriteria("receiver")
				.add(Restrictions.eq("receiver", receiver));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}
	
	public int countUnreadMessagesTo(User receiver) {
		Criteria criteria = getSession().createCriteria(Message.class)
				.createCriteria("receiver")
				.add(Restrictions.eq("receiver", receiver))
				.add(Restrictions.eq("isRead", false));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}
	
	public void setMessageRead(MessageDetail messageDetail) {
		messageDetail.setRead(true);
		getSession().merge(messageDetail);
		getSession().flush();
	}

	public MessageDetail getMessageDetail(Message message, User receiver) {
		Criteria criteria = getSession().createCriteria(MessageDetail.class)
				.add(Restrictions.eq("receiver", receiver))
				.add(Restrictions.eq("message", message));

		return (MessageDetail) criteria.setMaxResults(1).uniqueResult();
	}

}
