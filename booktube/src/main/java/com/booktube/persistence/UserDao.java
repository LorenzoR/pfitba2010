package com.booktube.persistence;

import java.util.Date;

import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.pages.customComponents.panels.MiscFilterOption;
import com.booktube.pages.customComponents.panels.AgeFilterOption;
import com.booktube.pages.customComponents.panels.OriginFilterOption;

public interface UserDao {
	public boolean usernameExists(String username);
	public boolean emailExists(String email);
    public List<User> getAllUsers(int first, int count);
    public List<User> getUsersByGender(int first, int count, Gender gender);
    public List<User> getUsersByAge(int first, int count, int lowerAge, int higherAge);
    public List<User> getUsersByCountry(int first, int count, String country);
    public List<User> getUsersByRegistrationDate(int first, int count, Date lowDaysNumber, Date highDaysNumber);
    public List<User> getUsers(int first, int count, Long userId, String username,Gender gender, Integer lowerAge, Integer higherAge, String country, Date lowDate, Date highDate, Level level);
    public List<User> getUsers(int first, int count, Level level);
    public List<String> getAllCountries();
    public User getUser(Long id);
    public User getUser(String username);
    public void update(User user);
    public Long insert(User user);
    public void delete(User user);
    public void deleteUsers(List<User> users);
    public int getCount();
    public int getCount(Long userId, String username, Gender gender, Integer lowerAge, Integer higherAge, String country, Date lowDate, Date highDate, Level level);
    public Iterator<User> iterator(int first, int count);
    
    // Para el filtro usado para generar reportes
   // public List<String> getAllCountries();
    public List<String> getAllCities();
    public List<String> getAllAges();
    public List<String> getAllGenders();
    public List<String> getAllRegistrationYears();
    
    // Para los reportes
    public List<Object> getUserEvolutionByYear(OriginFilterOption origin, AgeFilterOption age, MiscFilterOption misc);
    public List<Object> getUserDistributionByCountry(AgeFilterOption age, MiscFilterOption misc);
    public List<Object> getUserEvolutionBySex(OriginFilterOption origin, AgeFilterOption age);
    public List<Object> getWorksByCategory(AgeFilterOption age, MiscFilterOption misc);
    public List<Object> getMessagesBySubject(AgeFilterOption age, MiscFilterOption misc);
    public List<Object> getMessagesByCountry(AgeFilterOption age, MiscFilterOption misc);
    
}
