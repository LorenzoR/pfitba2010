package com.booktube.service;

import java.io.UnsupportedEncodingException;


import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.pages.customComponents.panels.MiscFilterOption;
import com.booktube.pages.customComponents.panels.AgeFilterOption;
import com.booktube.pages.customComponents.panels.OriginFilterOption;



public interface UserService {
	public boolean usernameExists(String username);
	public boolean emailExists(String email);
    public List<User> getAllUsers(int first, int count);
    public List<User> getUsersByGender(int first, int count, Gender gender);
    public List<User> getUsersByAge(int first, int count, int lowerAge, int higherAge);
    public List<User> getUsersByCountry(int first, int count, String country);
    public List<User> getUsersByRegistrationDate(int first, int count, Date lowDate, Date highDate);
    public List<User> getUsers(int first, int count, Long userId, String username, Gender gender, Integer lowerAge, Integer higherAge, String country, Date lowDate, Date highDate, Level level);
    public List<User> getUsers(int first, int count, Level level);
    public List<String> getAllCountries();
    public void updateUser(User user);
    public void deleteUser(User user);
    public void deleteUsers(List<User> users);
    public User getUser(Long id);
    public User getUser(String username);
    public void insertUser(User user);
    public int getCount();
    public int getCount(Long userId, String username, Gender gender, Integer lowerAge, Integer higherAge, String country, Date lowDate, Date highDate, Level level);
    public Iterator<User> iterator(int first, int count);
    
    // Para el filtro usado para generar reportes
    //public List<String> getAllCountries();
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
    
    
    // Para el proceso de registracion, y para recuperar los datos de la cuenta, y para cambiar la contraseña
    public String generateSecret() throws NoSuchAlgorithmException, UnsupportedEncodingException;    
	public void saveSecret(long id, String secret) throws Exception;
	public void sendRegistrationMail(User user) throws Exception;	
	public void changeUserPassword( User user, String newPassword) throws Exception;
	public void sendAccountInformationMail(User user, String newPassword) throws Exception;
	public boolean activateUserAccount(long id, String secret);
	
    
}
