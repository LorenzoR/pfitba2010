package com.booktube.service;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;

public interface UserService {
    public List<User> getAllUsers();
    public void updateUser(User user);
    public void deleteUser(User user);
    public User getUser(Integer id);
    public User getUser(String username);
    public void insertUser(User user);
    public int getCount();
    public Iterator<User> iterator(int first, int count);
}
