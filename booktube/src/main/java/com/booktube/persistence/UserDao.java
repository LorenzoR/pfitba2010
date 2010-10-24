package com.booktube.persistence;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;

public interface UserDao {
    public List<User> getAllUsers();
    public User getUser(Integer id);
    public User getUser(String username);
    public void update(User user);
    public void insert(User user);
    public void delete(User user);
    public int getCount();
    public Iterator<User> iterator(int first, int count);
}
