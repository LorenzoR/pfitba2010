package com.booktube.service;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;
import com.booktube.persistence.UserDao;

public class UserServiceImpl implements UserService {
    private UserDao itemDao;

    public UserServiceImpl() {
        //this.dao = new UserMySQLDBdao();
    }

    public List<User> getAllUsers() {
        return itemDao.getAllUsers();
    }

    public void updateUser(User user) {
    	itemDao.update(user);
    }

    public void deleteUser(User user) {
    	itemDao.delete(user);
    }

    public User getUser(Integer id) {
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
