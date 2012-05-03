package com.booktube.service;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.persistence.UserDao;

public class UserServiceImpl implements UserService {
    private UserDao itemDao;

    public UserServiceImpl() {
        //this.dao = new UserMySQLDBdao();
    }

    public boolean usernameExists(String username) {
    	return itemDao.usernameExists(username);
    }
    
    public List<User> getAllUsers(int first, int count) {
        return itemDao.getAllUsers(first, count);
    }
    
    public List<User> getUsers(int first, int count, Gender gender, Integer lowerAge, Integer higherAge, String country) {
    	return itemDao.getUsers(first, count, gender, lowerAge, higherAge, country);
    }
    
    public List<User> getUsersByCountry(int first, int count, String country) {
    	return itemDao.getUsersByCountry(first, count, country);
    }
    
    public List<User> getUsers(int first, int count, Level level) {
    	return itemDao.getUsers(first, count, level);
    }
    
    public List<User> getUsersByGender(int first, int count, Gender gender) {
    	return itemDao.getUsersByGender(first, count, gender);
    }
    
    public List<User> getUsersByAge(int first, int count, int lowerAge, int higherAge) {
    	return itemDao.getUsersByAge(first, count, lowerAge, higherAge);
    }

    public void updateUser(User user) {
    	itemDao.update(user);
    }

    public void deleteUser(User user) {
    	itemDao.delete(user);
    }

    public User getUser(Long id) {
        return itemDao.getUser(id);
    }
    
    public User getUser(String username) {
    	return itemDao.getUser(username);
    }

    public void insertUser(User user) {
    	itemDao.insert(user);
    }
    
    public UserDao getItemDao() {
        return itemDao;
    }

    public void setItemDao(UserDao itemDao) {
        this.itemDao = itemDao;
    }
    
    public int getCount() {
    	return itemDao.getCount();
    }

	public Iterator<User> iterator(int first, int count) {
		return itemDao.iterator(first, count);
	}
}
