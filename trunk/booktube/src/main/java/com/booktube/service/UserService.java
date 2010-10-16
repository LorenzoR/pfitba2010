package com.booktube.service;

import java.util.List;

import com.booktube.model.User;

public interface UserService {
    public List<User> getAllUsers();
    public void updateUser(User user);
    public void deleteUser(Integer id);
    public User getUser(Integer id);
    public User getUser(String username);
    public void insertUser(User user);
}
