package com.booktube.persistence.hibernate;

import java.text.DateFormat;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.apache.commons.lang.StringUtils;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
//import org.hibernate.annotations.FetchMode;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.pages.customComponents.panels.FilterOption;
import com.booktube.pages.customComponents.panels.MiscFilterOption;
import com.booktube.pages.customComponents.panels.AgeFilterOption;
import com.booktube.pages.customComponents.panels.DropDownElementPanel;
import com.booktube.pages.customComponents.panels.OriginFilterOption;
import com.booktube.persistence.UserDao;
import com.booktube.model.User.Level;

@SuppressWarnings("deprecation")
public class UserDaoImpl extends AbstractDaoHibernate<User> implements UserDao {

	protected UserDaoImpl() {
		super(User.class);
	}

	public boolean usernameExists(String username) {
		getYearAndField();
		return getUser(username) != null;
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers(int first, int count) {
		return (List<User>) getSession().createCriteria(User.class)
				.setFirstResult(first).setMaxResults(count)
				.addOrder(Order.asc("username"))
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsers(int first, int count, Long userId,
			String username, Gender gender, Integer lowerAge,
			Integer higherAge, String country, Date lowDate, Date highDate) {
		return (List<User>) createCriteria(userId, username, gender, lowerAge,
				higherAge, country, lowDate, highDate).setFirstResult(first)
				.setMaxResults(count).list();
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsersByCountry(int first, int count, String country) {
		return (List<User>) getSession().createCriteria(User.class)
				.add(Restrictions.eq("country", country)).setFirstResult(first)
				.setMaxResults(count).list();
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsers(int first, int count, Level level) {
		List<User> users = (List<User>) getSession().createCriteria(User.class)
				.add(Restrictions.eq("level", level)).setFirstResult(first)
				.setMaxResults(count).list();

		return users;
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsersByGender(int first, int count, Gender gender) {
		return (List<User>) getSession().createCriteria(User.class)
				.add(Restrictions.eq("gender", gender)).setFirstResult(first)
				.setMaxResults(count).list();
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsersByAge(int first, int count, int lowerAge,
			int higherAge) {

		List<User> users = (List<User>) getSession()
				.createCriteria(User.class)
				.add(Expression
						.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 >= "
								+ lowerAge))
				.add(Expression
						.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 <= "
								+ higherAge))
				// .add(Restrictions.gt("age", lowerAge))
				// .add(Restrictions.lt("age", higherAge))
				.setFirstResult(first).setMaxResults(count).list();
		return users;
	}

	public User getUser(Long id) {
		if (id == null) {
			return null;
		} else {
			return (User) getSession().getNamedQuery("user.id")
					.setLong("id", id).setMaxResults(1).uniqueResult();
		}
	}

	public User getUser(String username) {
		return (User) getSession().getNamedQuery("user.username")
				.setString("username", username).setMaxResults(1)
				.uniqueResult();
	}

	public void update(User user) {
		super.update(user);
		// getSession().merge(user);
		// getSession().flush();

	}

	public Long insert(User user) {
		/*
		 * Long id = (Long) getSession().save(user); getSession().flush();
		 * return id;
		 */
		return super.insert(user);
	}

	public void delete(User user) {
		super.delete(user);
		// getSession().delete(user);
		// getSession().flush();
	}

	public int getCount() {
		// Criteria criteria = getSession().createCriteria(User.class);
		// criteria.setProjection(Projections.rowCount());
		// return ((Number) criteria.uniqueResult()).intValue();
		return super.getCount();
	}

	@SuppressWarnings("unchecked")
	public Iterator<User> iterator(int first, int count) {
		return (Iterator<User>) getSession().createCriteria(User.class)
				.setFirstResult(first).setMaxResults(count).list().iterator();
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsersByRegistrationDate(int first, int count,
			Date lowDate, Date highDate) {
		return (List<User>) getSession()
				.createCriteria(User.class)
				.add(Restrictions.and(
						Restrictions.le("registrationDate", highDate),
						Restrictions.ge("registrationDate", lowDate)))
				.setMaxResults(count).list();
	}

	public int getCount(Long userId, String username, Gender gender,
			Integer lowerAge, Integer higherAge, String country, Date lowDate,
			Date highDate) {
		Criteria criteria = createCriteria(userId, username, gender, lowerAge,
				higherAge, country, lowDate, highDate);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	private Criteria createCriteria(Long userId, String username,
			Gender gender, Integer lowerAge, Integer higherAge, String country,
			Date lowDate, Date highDate) {

		Criteria criteria = getSession().createCriteria(User.class);

		if (userId != null) {
			criteria.add(Restrictions.eq("id", userId));
		}

		if (StringUtils.isNotBlank(username)) {
			criteria.add(Restrictions.ilike("username", "%" + username + "%"));
		}

		if (lowDate != null) {
			criteria.add(Restrictions.ge("registrationDate", lowDate));
		}

		if (highDate != null) {
			criteria.add(Restrictions.le("registrationDate", highDate));
		}

		if (lowerAge != null) {
			criteria.add(Expression
					.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 >= "
							+ lowerAge));
		}

		if (higherAge != null) {
			criteria.add(Expression
					.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 <= "
							+ higherAge));
		}

		if (gender != null) {
			criteria.add(Restrictions.eq("gender", gender));
		}

		if (StringUtils.isNotBlank(country)) {
			criteria.add(Restrictions.eq("country", country));
		}

		return criteria;
	}

	// Para el filtro usado para generar reportes

	@SuppressWarnings("unchecked")
	public List<String> getAllCountries() {
		return (List<String>) getSession()
				.createCriteria(User.class)
				.setProjection(
						Projections.distinct(Projections.projectionList().add(
								Projections.property("country"), "country")))
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllCities() {
		return (List<String>) getSession()
				.createCriteria(User.class)
				.setProjection(
						Projections.distinct(Projections.projectionList().add(
								Projections.property("city"), "city"))).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllAges() {
		List<Double> ages = (List<Double>) getSession()
				.createSQLQuery(
						"SELECT DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(birthdate)), '%Y')+0 AS age FROM user GROUP BY age")
				.list();
		List<String> resp = new ArrayList<String>();
		for (Double age : ages) {
			resp.add(String.valueOf(Math.round(age)));
		}
		return resp;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllGenders() {
		List<Integer> genders = (List<Integer>) getSession().createSQLQuery(
				"SELECT gender FROM user GROUP BY gender").list();
		List<String> resp = new ArrayList<String>();
		for (Integer g : genders) {
			if (g == 0)
				resp.add("Masculino");
			else
				resp.add("Femenino");
		}
		return resp;
	}

	
	@SuppressWarnings("unchecked")
	public List<String> getAllRegistrationYears() {
		List<String> years = (List<String>) getSession().createSQLQuery("SELECT year(registration_date) as years FROM user GROUP BY years").list();
		return years;
	}
	
	public List<Object> getUserEvolutionByYear(OriginFilterOption origin, AgeFilterOption age, MiscFilterOption misc) {
		String whereClause = SqlUtilities.generateWhereClause(origin, age, misc);
		String sql = "select year(registration_date) as year, count(user_id) as total from user "+whereClause+" group by year";
		
		SQLQuery query = getSession().createSQLQuery(sql)
				.addScalar("year", Hibernate.STRING)
				.addScalar("total", Hibernate.STRING);

		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Object> data = (List<Object>) query.list();
		return data;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getUserDistributionByCountry(AgeFilterOption age, MiscFilterOption misc) {				
		Integer lowerAge = ( age.getSelectedMinAge() != FilterOption.listFirstOption )? Integer.valueOf(age.getSelectedMinAge()) : null;
		Integer higherAge =( age.getSelectedMaxAge() != FilterOption.listFirstOption )? Integer.valueOf(age.getSelectedMaxAge()) : null; 	
		
		Gender gender = null;
		String registrationYear = null;
		for (DropDownElementPanel element : misc.getElements()) {
			String value = element.getSelectedValue();
			if( value != FilterOption.listFirstOption ){
				if( element.getTableFieldName() == "gender")				
					gender = ( value == "Masculino")? Gender.MALE : Gender.FEMALE;				
				if( element.getTableFieldName() == "registration_date") 
					registrationYear = value;
			}						
		}
				
		Criteria criteria = createFilterCriteria(User.class, gender, lowerAge, higherAge, "", "", registrationYear);		
		criteria.setProjection( Projections.projectionList()
				.add(Projections.alias(Projections.rowCount(), "total"))				
				.add(Projections.alias(Projections.groupProperty("country"), "country"))										
				)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		return (List<Object>)criteria.list();
	}
	
	public List<Object> getUserEvolutionBySex(OriginFilterOption origin, AgeFilterOption age) {
		String whereClause = SqlUtilities.generateWhereClause(origin, age, null);
		String sql = "SELECT year(registration_date) as year,COALESCE(SUM(CASE WHEN gender = 0 THEN 1 END), 0) as female,COALESCE(SUM(CASE WHEN gender = 1 THEN 1 END), 0) as male FROM user "+whereClause+" GROUP BY year";
		
		SQLQuery query = getSession().createSQLQuery(sql)
				.addScalar("year", Hibernate.STRING)
				.addScalar("male", Hibernate.INTEGER)
				.addScalar("female", Hibernate.INTEGER);

		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Object> data = (List<Object>) query.list();
		return data;

	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getWorksByCategory(AgeFilterOption age,	MiscFilterOption misc) {
		Integer lowerAge = ( age.getSelectedMinAge() != FilterOption.listFirstOption )? Integer.valueOf(age.getSelectedMinAge()) : null;
		Integer higherAge =( age.getSelectedMaxAge() != FilterOption.listFirstOption )? Integer.valueOf(age.getSelectedMaxAge()) : null; 	
		
		Gender gender = null;
		String registrationYear = null;
		for (DropDownElementPanel element : misc.getElements()) {
			String value = element.getSelectedValue();
			if( value != FilterOption.listFirstOption ){
				if( element.getTableFieldName() == "gender")				
					gender = ( value == "Masculino")? Gender.MALE : Gender.FEMALE;				
				if( element.getTableFieldName() == "registration_date") 
					registrationYear = value;
			}						
		}
				
		Criteria criteria = createFilterCriteria(Book.class, gender, lowerAge, higherAge, "", "", registrationYear);		
		criteria.setProjection( Projections.projectionList()
				.add(Projections.alias(Projections.rowCount(), "total"))				
				.add(Projections.alias(Projections.groupProperty("category"), "category"))										
				)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		return (List<Object>)criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getMessagesBySubject(AgeFilterOption age, MiscFilterOption misc) {
		Integer lowerAge = ( age.getSelectedMinAge() != FilterOption.listFirstOption )? Integer.valueOf(age.getSelectedMinAge()) : null;
		Integer higherAge =( age.getSelectedMaxAge() != FilterOption.listFirstOption )? Integer.valueOf(age.getSelectedMaxAge()) : null; 	
		
		Gender gender = null;
		String registrationYear = null;
		for (DropDownElementPanel element : misc.getElements()) {
			String value = element.getSelectedValue();
			if( value != FilterOption.listFirstOption ){
				if( element.getTableFieldName() == "gender")				
					gender = ( value == "Masculino")? Gender.MALE : Gender.FEMALE;				
				if( element.getTableFieldName() == "registration_date") 
					registrationYear = value;
			}						
		}
				
		Criteria criteria = createFilterCriteria(Message.class, gender, lowerAge, higherAge, "", "", registrationYear);		
		criteria.setProjection( Projections.projectionList()
				.add(Projections.alias(Projections.rowCount(), "total"))				
				.add(Projections.alias(Projections.groupProperty("subject"), "subject"))										
				)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		return (List<Object>)criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getMessagesByCountry(AgeFilterOption age, MiscFilterOption misc) {
		
		String whereClause = SqlUtilities.generateWhereClause(null, age, misc);	
		String sql = "select country, count(message_id) as total from user join message on user_id=sender_id "+whereClause+" group by country";
		SQLQuery query = getSession().createSQLQuery(sql)
				.addScalar("country", Hibernate.STRING)
				.addScalar("total", Hibernate.INTEGER);

		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Object> data = (List<Object>) query.list();
		return data;

//		Integer lowerAge = ( age.getSelectedMinAge() != FilterOption.listFirstOption )? Integer.valueOf(age.getSelectedMinAge()) : null;
//		Integer higherAge =( age.getSelectedMaxAge() != FilterOption.listFirstOption )? Integer.valueOf(age.getSelectedMaxAge()) : null; 	
//		
//		Gender gender = null;
//		String registrationYear = null;
//		for (DropDownElementPanel element : misc.getElements()) {
//			String value = element.getSelectedValue();
//			if( value != FilterOption.listFirstOption ){
//				if( element.getTableFieldName() == "gender")				
//					gender = ( value == "Masculino")? Gender.MALE : Gender.FEMALE;				
//				if( element.getTableFieldName() == "registration_date") 
//					registrationYear = value;
//			}						
//		}
		
				
//		System.out.println("Messages by COuntry : A PUNTO DE REALIZAR QUERY HIBERNATE");
//		Criteria criteria = getSession().createCriteria(User.class)
//										.setFetchMode("Message", org.hibernate.FetchMode.EAGER)										
//										.setProjection( Projections.projectionList()
//												.add(Projections.alias(Projections.rowCount(), "total"))				
//												.add(Projections.alias(Projections.groupProperty("country"), "country"))										
//										)
//										.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//		
//		
//		List<Object > data = (List<Object>)criteria.list();
//		for(Object object : data){
//           Map<?, ?> row = (Map<?, ?>)object;
//           System.out.println("country="+(String)row.get("country")+"  total="+(Long)row.get("total"));
//           
//        }
		
		
//		Criteria criteria = createFilterCriteria(Message.class, gender, lowerAge, higherAge, "", "", registrationYear);		
//		criteria.setProjection( Projections.projectionList()
//				.add(Projections.alias(Projections.rowCount(), "total"))				
//				.add(Projections.alias(Projections.groupProperty("country"), "country"))										
//				)
//				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
//		return (List<Object>)criteria.list();
	}


	// METODO PRIVADO PARA CREAR CRITERIOS HIBERNATE PARA LOS QUERIES DE LOS FILTROS	
	private Criteria createFilterCriteria(Class<?> sourceClass, Gender gender, Integer lowerAge, Integer higherAge, String country, String city, String registrationYear) {
		
		Criteria criteria = getSession().createCriteria(sourceClass);

		if (lowerAge != null) {
			criteria.add(Expression
					.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 >= "
							+ lowerAge));
		}

		if (higherAge != null) {
			criteria.add(Expression
					.sql("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0 <= "
							+ higherAge));
		}

		if (gender != null) {
			criteria.add(Restrictions.eq("gender", gender));
		}

		if (StringUtils.isNotBlank(country)) {
			criteria.add(Restrictions.eq("country", country));
		}

		if (StringUtils.isNotBlank(city)) {
			criteria.add(Restrictions.eq("city", city));
		}
		if (registrationYear != null) {			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date lowDate=null, highDate=null;
			try {
				lowDate = (Date) format.parse(registrationYear+"-01-01 00:00:00");
				highDate = (Date) format.parse(registrationYear+"-12-31 23:59:59");
			} catch (ParseException e) {
				e.printStackTrace();
			}            
            criteria.add(Restrictions.between("registrationDate", lowDate, highDate));
			criteria.add(Restrictions.ge("registrationDate", lowDate));
			criteria.add(Restrictions.le("registrationDate", highDate));
		}
		
		return criteria;
	}


	


	@SuppressWarnings("unchecked")
	private List<Object> getYearAndField() {
		ProjectionList projList = Projections.projectionList();

		Criteria criteria = getSession().createCriteria(User.class);
		projList.add(Projections.property("id"), "id");
		projList.add(Projections.sqlProjection("year(birthdate) AS year",
				new String[] { "year" }, new Type[] { Hibernate.INTEGER }));

		criteria.setProjection(projList);

		List<Object> list = criteria.list();

		/* ASI SE ITERA SOBRE LA LISTA DE RESULTADOS */
		for(Object r: list){
		  Object[] row = (Object[]) r;
		  System.out.println("USER_ID: " + row[0] + "\tYEAR: " + row[1]);
		}
		
		return list;
	}		
}
