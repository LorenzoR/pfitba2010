package com.booktube.persistence.noDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.persistence.UserDao;

public class UserNoDBdao implements UserDao {
    private static List<User> users;
    private static Map usersMap;
    static {
    	users = new ArrayList<User>();
    	users.add(new User(new Long(1), "autor10", "password", "Nombre1", "Apellido1", User.Level.USER) );
    	users.add(new User(new Long(2), "autor20", "password", "Nombre2", "Apellido2", User.Level.USER) );
    	users.add(new User(new Long(3), "autor30", "password", "Nombre3", "Apellido3", User.Level.USER) );
    	users.add(new User(new Long(9), "autor90", "password", "Nombre4", "Apellido4", User.Level.USER) );
    	usersMap = new HashMap();
        Iterator<User> iter = users.iterator();
        while( iter.hasNext() ) {
        	User user = (User)iter.next();
            usersMap.put(user.getId(), user );
        }

     }
    public List<User> getAllUsers() {
        return users;
    }
    public Map getUsersMap() {
        return usersMap;
    }
	public User getUser(Integer id)  {
        User user = null;
        Iterator<User> iter = users.iterator();
        while (iter.hasNext()) {
            user = (User)iter.next();
            if (user.getId().equals(id)) {
                break;
            }
        }
        return user;
    }
	
	public User getUser(String username) {
		User user = null;
        Iterator<User> iter = users.iterator();
        while (iter.hasNext()) {
            user = (User)iter.next();
            if (user.getUsername().equals(username)) {
                break;
            }
        }
        return user;
	}
	
	public void update(User user) {
		// TODO Auto-generated method stub
		
	}
	public Long insert(User user) {
		Long lastId = (long) 0;
        Iterator<User> iter = users.iterator();
        while (iter.hasNext()) { 
        	user = (User)iter.next();
            if (user.getId().longValue() > lastId) {
                lastId = user.getId();
            }
        }
        
        user.setId(new Long(lastId + 1));
        users.add(user);
        
        return 1L;
		
	}
	public void delete(Integer id) {
		for (int i = 0; i < users.size(); i++) {
            User tempUser = (User)users.get(i);
            if (tempUser.getId().equals(id)) {
                users.remove(i);
                break;
            }
        }
		
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	public Iterator iterator(int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}
	public void delete(User user) {
		// TODO Auto-generated method stub
		
	}
	public List<User> getAllUsers(int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}
	public User getUser(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean usernameExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}
	public List<User> getUsersByGender(int first, int count, Gender gender) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<User> getUsersByAge(int first, int count, int lowerAge,
			int higherAge) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<User> getUsers(int first, int count, Gender gender,
			int lowerAge, int higherAge) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<User> getUsers(int first, int count, Level level) {
		// TODO Auto-generated method stub
		return null;
	}
}
