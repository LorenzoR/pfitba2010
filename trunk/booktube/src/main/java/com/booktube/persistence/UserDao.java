package com.booktube.persistence;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;

public interface UserDao {
	public boolean usernameExists(String username);
    public List<User> getAllUsers(int first, int count);
    public List<User> getUsersByGender(int first, int count, Gender gender);
    public List<User> getUsersByAge(int first, int count, int lowerAge, int higherAge);
    public List<User> getUsersByCountry(int first, int count, String country);
    public List<User> getUsersByRegistrationDate(int first, int count, Date lowDaysNumber, Date highDaysNumber);
    public List<User> getUsers(int first, int count, Gender gender, Integer lowerAge, Integer higherAge, String country, Date lowDate, Date highDate);
    public List<User> getUsers(int first, int count, Level level);
    public User getUser(Long id);
    public User getUser(String username);
    public void update(User user);
    public Long insert(User user);
    public void delete(User user);
    public int getCount();
    public Iterator<User> iterator(int first, int count);
}
