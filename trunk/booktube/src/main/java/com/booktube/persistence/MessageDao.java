package com.booktube.persistence;

import java.util.List;

import com.booktube.model.Message;
import com.booktube.model.User;

public interface MessageDao {

	public void update(Message message);
    public void delete(Message message);
    public Message getMessage(Integer id);
    public void insert(Message message);
    public List<Message> getAllMessagesFrom(User sender, int first, int count);
    public List<Message> getAllMessagesTo(User receiver, int first, int count);
    public int countMessagesFrom(User sender);
    public int countMessagesTo(User receiver);
}
