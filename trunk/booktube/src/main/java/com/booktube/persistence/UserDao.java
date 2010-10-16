package com.booktube.persistence;

import java.util.List;
import java.util.Map;

import com.booktube.model.Book;
import com.booktube.model.User;

public interface UserDao {
    public List<User> getAllUsers();
    public User getUser(Integer id);
    public User getUser(String username);
    public void update(User user);
    public void insert(User user);
    public void delete(Integer id);
}
