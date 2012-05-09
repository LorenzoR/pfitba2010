package com.booktube.persistence;

import java.util.List;

import com.booktube.model.Message;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;

public interface MessageDao {

	public void update(Message message);
    public void delete(Message message);
    public Message getMessage(Long id);
    //public CampaignDetail getMessageDetail(Message message, User receiver);
    public Long insert(Message message);
    public List<Message> getAllMessages(int first, int count);
    public List<Message> getAllMessagesFrom(User sender, int first, int count);
    public List<Message> getAllMessagesTo(User receiver, int first, int count);
    public List<Message> getAllCampaigns(int first, int count);
    public int countCampaigns();
    public int countMessages();
    public int countMessagesFrom(User sender);
    public int countMessagesTo(User receiver);
    public int countUnreadMessagesTo(User receiver);
    public void setMessageRead(CampaignDetail messageDetail);
    public void sendMessages(Message message, List<User> receivers);
}
