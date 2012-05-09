package com.booktube.persistence.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.model.Message.Type;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.persistence.MessageDao;

public class MessageDaoImpl extends AbstractDaoHibernate<Message> implements
		MessageDao {

	protected MessageDaoImpl() {
		super(Message.class);
	}

	public void update(Message message) {
		getSession().saveOrUpdate(message);
		getSession().flush();
		//super.update(message);
		//getSession().merge(message);
		//getSession().flush();
	}

	public void delete(Message message) {
		super.delete(message);
		//System.out.println("Borro mensaje " + message);
		//getSession().delete(message);
		//getSession().flush();
	}

	public Message getMessage(Long id) {
		return (Message) getSession().getNamedQuery("message.id")
				.setLong("id", id).setMaxResults(1).uniqueResult();
	}

	public Long insert(Message message) {
		/*Long id = (Long) getSession().save(message);
		getSession().flush();
		return id;
		*/
		return super.insert(message);
	}

	public List<Message> getAllMessages(int first, int count) {
		return (List<Message>) getSession().createCriteria(Message.class)
				.setFirstResult(first).setMaxResults(count).list();
	}

	public List<Message> getAllMessagesFrom(User sender, int first, int count) {
		return (List<Message>) getSession().createCriteria(Message.class)
				.add(Restrictions.eq("sender", sender)).setFirstResult(first)
				.setMaxResults(count).list();
	}

	public List<Message> getAllMessagesTo(User receiver, int first, int count) {
		Criteria criteria = getSession().createCriteria(Message.class)
				.add(Restrictions.or(Restrictions.eq("type", Type.PRIVATE_MESSAGE), Restrictions.eq("type", Type.FIRST_ANSWER)))
				.createCriteria("receiver")
				.add(Restrictions.eq("receiver", receiver))
				.setFirstResult(first).setMaxResults(count);

		return (List<Message>) criteria.list();
	}

	public int countMessages() {
		Criteria criteria = getSession().createCriteria(Message.class);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public int countMessagesFrom(User sender) {
		Criteria criteria = getSession().createCriteria(Message.class).add(
				Restrictions.eq("sender", sender));
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

	public void setMessageRead(CampaignDetail messageDetail) {
		messageDetail.setRead(true);
		getSession().merge(messageDetail);
		getSession().flush();
	}

	/*public CampaignDetail getMessageDetail(Message message, User receiver) {
		Criteria criteria = getSession().createCriteria(CampaignDetail.class)
				.add(Restrictions.eq("receiver", receiver))
				.add(Restrictions.eq("message", message));

		return (CampaignDetail) criteria.setMaxResults(1).uniqueResult();
	}*/

	public void sendMessages(Message message, List<User> receivers) {
		//Set<MessageDetail> messageDetail = new HashSet<MessageDetail>();

		for (User aUser : receivers) {
			insert(new Message(message.getType(), message.getSubject(), message.getText(), message.getSender(), aUser));
			//messageDetail.add(new MessageDetail(aUser, message));
		}
		
		//message.setReceiver(messageDetail);

		//insert(message);
	}

	public List<Message> getAllCampaigns(int first, int count) {
		return (List<Message>) getSession().createCriteria(Message.class)
				.add(Restrictions.eq("type", Type.CAMPAIGN))
				.setFirstResult(first).setMaxResults(count).list();
	}

	public int countCampaigns() {
		Criteria criteria = getSession().createCriteria(Message.class)
				.add(Restrictions.eq("type", Type.CAMPAIGN));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
