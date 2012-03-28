package com.booktube.service;

import java.util.List;

import com.booktube.model.Message;
import com.booktube.model.MessageDetail;
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

	public Message getMessage(Integer id) {
		return itemDao.getMessage(id);
	}

	public void insertMessage(Message message) {
		itemDao.insert(message);

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

	public int countMessagesFrom(User sender) {
		return itemDao.countMessagesFrom(sender);
	}

	public int countMessagesTo(User receiver) {
		return itemDao.countMessagesTo(receiver);
	}
	
	public int countUnreadMessagesTo(User receiver) {
		return itemDao.countUnreadMessagesTo(receiver);
	}
	
	public void setMessageRead(MessageDetail messageDetail) {
		itemDao.setMessageRead(messageDetail);
	}

	public MessageDetail getMessageDetail(Message message, User receiver) {
		return itemDao.getMessageDetail(message, receiver);
	}


}
