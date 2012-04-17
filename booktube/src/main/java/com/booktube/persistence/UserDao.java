package com.booktube.persistence;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;

public interface UserDao {
	public boolean usernameExists(String username);
    public List<User> getAllUsers(int first, int count);
    public User getUser(Long id);
    public User getUser(String username);
    public void update(User user);
    public void insert(User user);
    public void delete(User user);
    public int getCount();
    public Iterator<User> iterator(int first, int count);
}
