package com.booktube.persistence.hibernate;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

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
		// super.update(message);
		// getSession().merge(message);
		// getSession().flush();
	}

	public void delete(Message message) {
		super.delete(message);
		// System.out.println("Borro mensaje " + message);
		// getSession().delete(message);
		// getSession().flush();
	}

	public Message getMessage(Long id) {
		return (Message) getSession().getNamedQuery("message.id")
				.setLong("id", id).setMaxResults(1).uniqueResult();
	}

	public Long insert(Message message) {
		/*
		 * Long id = (Long) getSession().save(message); getSession().flush();
		 * return id;
		 */
		return super.insert(message);
	}

	@SuppressWarnings("unchecked")
	public List<Message> getAllMessages(int first, int count) {
		return (List<Message>) getSession().createCriteria(Message.class)
				.add(Restrictions.eq("type", Type.PRIVATE_MESSAGE))
				.addOrder(Order.desc("date"))
				.setFirstResult(first).setMaxResults(count).list();
	}

	@SuppressWarnings("unchecked")
	public List<Message> getAllMessagesFrom(User sender, int first, int count) {
		return (List<Message>) getSession().createCriteria(Message.class)
				.add(Restrictions.eq("sender", sender)).setFirstResult(first)
				.add(Restrictions.eq("type", Type.PRIVATE_MESSAGE))
				.setMaxResults(count).list();
	}

	@SuppressWarnings("unchecked")
	public List<Message> getAllMessagesTo(User receiver, int first, int count) {
		return  (List<Message>) getSession()
				.createCriteria(Message.class)
				.add(Restrictions.eq("type", Type.PRIVATE_MESSAGE))
				.add(Restrictions.eq("receiver", receiver))
				.addOrder(Order.desc("date")).setFirstResult(first)
				.setMaxResults(count).list();
	}

	public int countMessages() {
		Criteria criteria = getSession().createCriteria(Message.class)
				.add(Restrictions.eq("type", Type.PRIVATE_MESSAGE));
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
		Criteria criteria = getSession().createCriteria(Message.class).add(
				Restrictions.eq("receiver", receiver));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public int countUnreadMessagesTo(User receiver) {
		Criteria criteria = getSession().createCriteria(Message.class)
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

	/*
	 * public CampaignDetail getMessageDetail(Message message, User receiver) {
	 * Criteria criteria = getSession().createCriteria(CampaignDetail.class)
	 * .add(Restrictions.eq("receiver", receiver))
	 * .add(Restrictions.eq("message", message));
	 * 
	 * return (CampaignDetail) criteria.setMaxResults(1).uniqueResult(); }
	 */

	public void sendMessages(Message message, List<User> receivers) {
		// Set<MessageDetail> messageDetail = new HashSet<MessageDetail>();

		for (User aUser : receivers) {
			insert(new Message(message.getType(), message.getSubject(),
					message.getText(), message.getSender(), aUser));
			// messageDetail.add(new MessageDetail(aUser, message));
		}

		// message.setReceiver(messageDetail);

		// insert(message);
	}

	/*public List<Message> getAllCampaigns(int first, int count) {
		return (List<Message>) getSession().createCriteria(Message.class)
				.add(Restrictions.eq("type", Type.CAMPAIGN))
				.setFirstResult(first).setMaxResults(count).list();
	}

	public int countCampaigns() {
		Criteria criteria = getSession().createCriteria(Message.class).add(
				Restrictions.eq("type", Type.CAMPAIGN));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}*/

	public List<Message> getAllMessages(User user, int first, int count) {
		List<Message> messages = getAllMessagesFrom(user, first, count);
		messages.addAll(getAllMessagesTo(user, first, count));

		Collections.sort(messages, Message.getDateComparatorAsc());

		return messages;
	}

	public int countMessages(User user) {
		Criteria criteria = getSession()
				.createCriteria(Message.class)
				.add(Restrictions.or(Restrictions.eq("sender", user),
						Restrictions.eq("receiver", user)))
				.add(Restrictions.eq("type", Type.PRIVATE_MESSAGE));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public int getCount(Long messageId, String subject, String sender,
			String receiver, Date lowDate, Date highDate) {
		Criteria criteria = createCriteria(messageId, subject, sender, receiver, lowDate, highDate);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Message> getMessages(int first, int count, Long messageId,
			String subject, String sender, String receiver, Date lowDate,
			Date highDate) {
		return (List<Message>) createCriteria(messageId, subject, sender, receiver, lowDate, highDate).setFirstResult(first)
				.setMaxResults(count).list();
	}
	
	private Criteria createCriteria(Long messageId,
			String subject, String sender, String receiver, Date lowDate,
			Date highDate) {
		
		Criteria criteria = getSession().createCriteria(Message.class);
		
		if (messageId != null) {
			criteria.add(Restrictions.eq("id", messageId));
		}

		if (!StringUtils.isBlank(subject)) {
			criteria.add(Restrictions.ilike("subject", "%" + subject + "%"));
		}
		
		if (StringUtils.isNotBlank(sender)) {
			criteria.createCriteria("sender").add(
					Restrictions.eq("username", sender));
		}
		
		if (StringUtils.isNotBlank(receiver)) {
			criteria.createCriteria("receiver").add(
					Restrictions.eq("username", receiver));
		}
		
		return criteria;
		
	}

}
