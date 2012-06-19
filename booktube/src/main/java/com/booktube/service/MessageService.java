package com.booktube.service;

import java.util.Date;
import java.util.List;

import com.booktube.model.Message;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;

public interface MessageService {
	public void updateMessage(Message message);
    public void deleteMessage(Message message);
    public Message getMessage(Long id);
    //public CampaignDetail getMessageDetail(Message message, User receiver);
    public void insertMessage(Message message);
    public List<Message> getAllMessages(int first, int count);
    public List<Message> getAllMessages(User user, int first, int count);
    public List<Message> getAllMessagesFrom(User sender, int first, int count);
    public List<Message> getAllMessagesTo(User receiver, int first, int count);
    public List<Message> getReplyes(Message message);
    public int countMessages();
    public int countMessages(User user);
    public int countMessagesFrom(User sender);
    public int countMessagesTo(User receiver);
    public int countUnreadMessagesTo(User receiver);
    public List<Message> getMessages(int first, int count, Long messageId, String subject, String sender, String receiver, Date lowDate, Date highDate);
    public int getCount(Long messageId, String subject, String sender, String receiver, Date lowDate, Date highDate);
    public void setMessageRead(CampaignDetail messageDetail);
    public void sendMessages(Message message, List<User> receivers);
}
