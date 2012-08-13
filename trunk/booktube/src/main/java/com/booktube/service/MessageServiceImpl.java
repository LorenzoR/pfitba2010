package com.booktube.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.booktube.model.Message;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.persistence.MessageDao;

public class MessageServiceImpl implements MessageService {

	private MessageDao itemDao;
	
	public MessageDao getItemDao() {
		return itemDao;
	}
	
	public void setItemDao(MessageDao itemDao) {
		this.itemDao = itemDao;
	}
	
	public void updateMessage(Message message) {
		itemDao.update(message);

	}

	public void deleteMessage(Message message) {
		itemDao.delete(message);

	}

	public Message getMessage(Long id) {
		return itemDao.getMessage(id);
	}

	public void insertMessage(Message message) {
		itemDao.insert(message);

	}
	
	public List<Message> getAllMessages(int first, int count) {
		return itemDao.getAllMessages(first, count);
	}
	
	public List<Message> getAllMessagesTo(User receiver, int first, int count) {
		return itemDao.getAllMessagesTo(receiver, first, count);
	}
	
	public List<Message> getAllMessagesFrom(User sender, int first, int count) {
		return itemDao.getAllMessagesFrom(sender, first, count);
	}
	
	public List<Message> getReplyes(Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	public int countMessages() {
		return itemDao.countMessages();
	}
	
	public int countMessagesFrom(User sender) {
		return itemDao.countMessagesFrom(sender);
	}

	public int countMessagesTo(User receiver) {
		return itemDao.countMessagesTo(receiver);
	}
	
	public int countUnreadMessagesTo(User receiver) {
		return itemDao.countUnreadMessagesTo(receiver);
	}
	
	public void setMessageRead(CampaignDetail messageDetail) {
		itemDao.setMessageRead(messageDetail);
	}

	/*public CampaignDetail getMessageDetail(Message message, User receiver) {
		return itemDao.getMessageDetail(message, receiver);
	}*/

	public void sendMessages(Message message, List<User> receivers) {
		itemDao.sendMessages(message, receivers);
	}

	public List<Message> getAllMessages(User user, int first, int count) {
		return itemDao.getAllMessages(user, first, count);
	}

	public int countMessages(User user) {
		return itemDao.countMessages(user);
	}

	public int getCount(Long messageId, String subject, String sender,
			String receiver, Date lowDate, Date highDate) {
		return itemDao.getCount(messageId, subject, sender, receiver, lowDate, highDate);
	}

	public List<Message> getMessages(int first, int count, Long messageId,
			String subject, String sender, String receiver, Date lowDate,
			Date highDate) {
		return itemDao.getMessages(first, count, messageId, subject, sender, receiver, lowDate, highDate);
	}
	
	public Map<Long, User> getMessagesByOperator() {
		return itemDao.getMessagesByOperator();
	}

}
