package com.booktube.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.pages.AgeFilterOption;
import com.booktube.pages.MiscFilterOption;
import com.booktube.pages.OriginFilterOption;
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
    
    public List<User> getUsers(int first, int count, Long userId, String username, Gender gender, Integer lowerAge, Integer higherAge, String country, Date lowDate, Date highDate) {
    	return itemDao.getUsers(first, count, userId, username, gender, lowerAge, higherAge, country, lowDate, highDate);
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

	public List<User> getUsersByRegistrationDate(int first, int count,
			Date lowDate, Date highDate) {
		return itemDao.getUsersByRegistrationDate(first, count,
				lowDate, highDate);
	}

	public List<String> getAllCountries() {
		return itemDao.getAllCountries();
	}

	public int getCount(Long userId, String username, Gender gender,
			Integer lowerAge, Integer higherAge, String country, Date lowDate,
			Date highDate) {
		return itemDao.getCount(userId, username, gender, lowerAge, higherAge, country, lowDate, highDate);
	}
	
	 // PARA GENERAR LISTADOS PARA EL FILTRO DE LOS REPORTES	
//	public List<String> getAllCountries() {
//		return itemDao.getAllCountries();		
//	}
	public List<String> getAllCities() {
		return itemDao.getAllCities();		
	}
	public List<String> getAllAges() {
		return itemDao.getAllAges();		
	}

	public List<String> getAllGenders() {
		return itemDao.getAllGenders();
	}
	
	public List<String> getAllRegistrationYears() {	
		return itemDao.getAllRegistrationYears();
	}
	
	// PARA LOS REPORTES
	public List<Object> getUserEvolutionByYear(OriginFilterOption origin, AgeFilterOption age, MiscFilterOption misc){
		return itemDao.getUserEvolutionByYear(origin, age, misc);
	}

	public List<Object> getUserDistributionByCountry(AgeFilterOption age, MiscFilterOption misc) {
		return itemDao.getUserDistributionByCountry(age, misc);
	}

	public List<Object> getUserEvolutionBySex(OriginFilterOption origin, AgeFilterOption age) {
		return itemDao.getUserEvolutionBySex(origin, age);
	}
	
	public List<Object> getWorksByCategory(AgeFilterOption age, MiscFilterOption misc){
		return itemDao.getWorksByCategory(age, misc);
	}
    public List<Object> getMessagesBySubject(AgeFilterOption age, MiscFilterOption misc){
    	return itemDao.getMessagesBySubject(age, misc);
    }
    public List<Object> getMessagesByCountry(AgeFilterOption age, MiscFilterOption misc){
    	return itemDao.getMessagesByCountry(age, misc);
    }
    

	
}
