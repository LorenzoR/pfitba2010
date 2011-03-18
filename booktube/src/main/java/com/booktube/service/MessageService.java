package com.booktube.service;

import java.util.List;

import com.booktube.model.Message;
import com.booktube.model.User;

public interface MessageService {
	public void updateMessage(Message message);
    public void deleteMessage(Message message);
    public Message getMessage(Integer id);
    public void insertMessage(Message message);
    public List<Message> getAllMessagesFrom(User sender, int first, int count);
    public List<Message> getAllMessagesTo(User receiver, int first, int count);
    public List<Message> getReplyes(Message message);
    public int countMessagesFrom(User sender);
    public int countMessagesTo(User receiver);
}
