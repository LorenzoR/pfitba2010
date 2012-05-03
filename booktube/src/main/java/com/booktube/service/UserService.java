package com.booktube.service;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;

public interface UserService {
	public boolean usernameExists(String username);
    public List<User> getAllUsers(int first, int count);
    public List<User> getUsersByGender(int first, int count, Gender gender);
    public List<User> getUsersByAge(int first, int count, int lowerAge, int higherAge);
    public List<User> getUsersByCountry(int first, int count, String country);
    public List<User> getUsers(int first, int count, Gender gender, Integer lowerAge, Integer higherAge, String country);
    public List<User> getUsers(int first, int count, Level level);
    public void updateUser(User user);
    public void deleteUser(User user);
    public User getUser(Long id);
    public User getUser(String username);
    public void insertUser(User user);
    public int getCount();
    public Iterator<User> iterator(int first, int count);
}
