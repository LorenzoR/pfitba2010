package com.booktube.service;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.pages.customComponents.panels.MiscFilterOption;
import com.booktube.pages.customComponents.panels.AgeFilterOption;
import com.booktube.pages.customComponents.panels.OriginFilterOption;
import com.booktube.pages.utilities.Mail;
import com.booktube.persistence.UserDao;

public class UserServiceImpl implements UserService {
    private UserDao itemDao;

    public UserServiceImpl() {
        //this.dao = new UserMySQLDBdao();
    }

    public boolean usernameExists(String username) {
    	return itemDao.usernameExists(username);
    }
    
    public boolean emailExists(String email) {
    	return itemDao.emailExists(email);
    }
    
    public List<User> getAllUsers(int first, int count) {
        return itemDao.getAllUsers(first, count);
    }
    
    public List<User> getUsers(int first, int count, Long userId, String username, Gender gender, Integer lowerAge, Integer higherAge, String country, Date lowDate, Date highDate, Level level) {
    	return itemDao.getUsers(first, count, userId, username, gender, lowerAge, higherAge, country, lowDate, highDate, level);
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
    
    public void deleteUsers(List<User> users) {
    	itemDao.deleteUsers(users);
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
			Date highDate, Level level) {
		return itemDao.getCount(userId, username, gender, lowerAge, higherAge, country, lowDate, highDate, level);
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
    
    // Para el proceso de registracion
    
    public String generateSecret() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md;
		String text;
		int primeNumber = 233750197;
		
		Random rand = new Random();
		text = String.valueOf( rand.nextInt(primeNumber) );
		
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		
//		Logger.getLogger("UserServiceImpl.generateSecret()").info("sha1hash: "+sha1hash);
		System.out.println("UserServiceImpl.generateSecret() : sha1hash=["+sha1hash+"]");
		return convertToHex(sha1hash);
	}
	
	public void saveSecret(long id, String secret) throws Exception {
		
	}
	
	public void sendRegistrationMail(User user) throws Exception {
		
		Long id = user.getId();
		String secret = user.getSecret();		
		Logger.getLogger("UserServiceImpl.sendRegistationMail()").info("Secreto guardado para el usuario: "+secret);
		
		String receiver = user.getEmail();		
		String subject = "Registracion BookTube";		
		String bodyMessage = 	"<html><body>Hola, bienvenido a Booktube!!<br>" +
								"Para terminar el proceso de registraci&oacute;n siga este link:<br>" +
								"<a href='http://localhost:8080/confirmation?id="+id+"&code="+secret+"'>confirmaci&oacute;n Booktube</a></body></html>";
		
		Mail.send(receiver, subject, bodyMessage);
		
	}
	
	public void sendAccountInformationMail(User user, String newPassword) throws Exception {
		Long id = user.getId();
		String username = user.getUsername();
//		String password = user.getPassword();
		
		String receiver = user.getEmail();		
		String subject = "Informacion de cuenta BookTube";		
		String bodyMessage = 	"<html><body>Hola, Gracias por elegir Booktube!!<br>" +
								"Los detalles de su cuenta:<br>" +
								"User:"+username+"<br>"+"Password:"+newPassword+"<br>"+
								"<p style='color:red'>Por favor, cambie su password la proxima vez que ingrese, haciendo click en: Editar Perfil -> Cambiar mi contraseña</p></body></html>";
		
		Mail.send(receiver, subject, bodyMessage);
		
		Logger.getLogger("UserServiceImpl.sendAccountInformationMail()").info("Información de la cuenta para usuario id "+id+" enviada al email "+receiver);
	}

	public void changeUserPassword(User user, String newPassword) throws Exception {
		user.setPassword(newPassword);
		updateUser(user);
		Logger.getLogger("UserServiceImpl.changeUserPassword()").info("Nueva contraseña es: "+newPassword);
		return;
	}
	
	public boolean activateUserAccount(long id, String secret) {
		User user = getUser(id);
		Logger.getLogger("UserServiceImpl.activateUserAccount()").info("Secreto recibido como parametro: "+secret);
		Logger.getLogger("UserServiceImpl.activateUserAccount()").info("Secreto recuperado del usuario: "+user.getSecret());
		if( secret.compareToIgnoreCase(user.getSecret()) == 0	 ){			
			user.setIsActive(true);			
			updateUser(user);
			return true;
		}
		return false;
	}
	
	//Usada para crear string SHA-1
	private static String convertToHex(byte[] data) {

		System.out.println("UserServiceImpl.convertToHex(): llego al metodo el dato=["+data+"]");
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
        	int halfbyte = (data[i] >>> 4) & 0x0F;
        	int two_halfs = 0;
        	do {
	            if ((0 <= halfbyte) && (halfbyte <= 9))
	                buf.append((char) ('0' + halfbyte));
	            else
	            	buf.append((char) ('a' + (halfbyte - 10)));
	            halfbyte = data[i] & 0x0F;
        	} while(two_halfs++ < 1);
        }

        System.out.println("UserServiceImpl.convertToHex() : buf.toString()=["+buf.toString()+"]");
        return buf.toString();
    }
	
	
}
