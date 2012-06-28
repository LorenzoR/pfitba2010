package com.booktube.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.Book;
import com.booktube.model.Campaign;
import com.booktube.model.Message;
import com.booktube.model.Message.Type;
import com.booktube.model.CampaignDetail;
import com.booktube.model.BookTag;
import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.pages.BasePage;
import com.booktube.service.BookService;
import com.booktube.service.CampaignService;
import com.booktube.service.UserService;

public class LoadDataPage extends BasePage {

	@SpringBean
	UserService userService;

	@SpringBean
	BookService bookService;
	
	@SpringBean
	CampaignService campaignService;

	private List<User> users = new ArrayList<User>();
	private User admin;

	public LoadDataPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("loadData");
		parent.setOutputMarkupId(true);
		add(parent);
		String label = null;

		if (userService.getCount() != 0) {
			label = "Ya hay datos";
		} else {
			addUsers();
			addBooks();
			addMessages();
			addCampaigns();
			label = "Cargue datos";
		}

		parent.add(new Label("content", label));
	}

	public void addUsers() {
		
		List<String> countries = new ArrayList<String>();
		countries.add("Country 1");
		countries.add("Country 2");
		countries.add("Country 3");
		countries.add("Country 4");
		countries.add("Country 5");
		countries.add("Country 6");
		
		List<String> cities = new ArrayList<String>();
		cities.add("City 1");
		cities.add("City 2");
		cities.add("City 3");
		cities.add("City 4");
		cities.add("City 5");
		cities.add("City 6");
		cities.add("City 7");
		cities.add("City 8");
		cities.add("City 9");
		
		Gender gender = Gender.MALE;
		
		for ( int i = 0; i < 100; i++ ) {
			
			int year = randBetween(1900, 2010);
	        int month = randBetween(0, 11);
	        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
	        int day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));
	        gc.set(year, month, day);
	        Date birthdate = new Date(gc.getTimeInMillis());
	        
	        //year = randBetween(1900, 2010);	        
	        if( i <=11 )
	        	year = 2008;
	        else if( i>11 && i <=28 )
	        	year = 2009;
	        else if( i>28 && i<=50 )
	        	year = 2010;	        
	        else if( i>50 && i<=80 )
	        	year = 2011;
	        else
	        	year = 2012;
	        
	        month = randBetween(0, 11);
	        gc = new GregorianCalendar(year, month, 1);
	        day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));
	        gc.set(year, month, day);
	        Date registrationDate = new Date(gc.getTimeInMillis());
			
			User user = new User("user" + i, "user" + i, "nombre" + i, "apellido" + i,
					User.Level.USER);
			
			user.setBirthdate(birthdate);
			user.setRegistrationDate(registrationDate);
			user.setGender(gender);
			user.setCountry(countries.get(i % countries.size()));
			user.setCity(cities.get(i % cities.size()));
			userService.insertUser(user);
			users.add(user);
			
			gender = gender == Gender.MALE ? Gender.FEMALE : Gender.MALE;
		}
		
		User user = new User("user", "pass", "nombre", "apellido",
				User.Level.USER);
		user.setBirthdate(new Date());
		user.setGender(Gender.MALE);
		user.setCountry("Country 1");
		user.setCity("City 10");
		user.setRegistrationDate(new Date());
		this.admin = new User("admin", "admin", "nombre", "apellido",
				User.Level.ADMIN);
		this.admin.setBirthdate(new Date());
		this.admin.setGender(Gender.MALE);
		this.admin.setCountry("Country 1");
		this.admin.setCity("City 1");

		userService.insertUser(user);
		users.add(user);
		userService.insertUser(this.admin);

	}

	public void addBooks() {
		
		List<BookTag> bookTags = new ArrayList<BookTag>();
		bookTags.add(new BookTag("tag1"));
		bookTags.add(new BookTag("tag1"));
		bookTags.add(new BookTag("tag1"));
		bookTags.add(new BookTag("tag1"));
		bookTags.add(new BookTag("tag1"));
		bookTags.add(new BookTag("tag1"));
		bookTags.add(new BookTag("tag1"));
		bookTags.add(new BookTag("tag1"));
		
		Random randomGenerator = new Random(System.currentTimeMillis());
		
		System.out.println("Adding books");
		
		for ( int i = 0; i < 100; i++ ) {
			
			Book book = new Book("titulo"+i, "texto"+i, this.users.get(randomGenerator.nextInt(this.users.size())));

			book.setCategory("category" + randomGenerator.nextInt(10));
			book.setSubCategory("subcategory" + randomGenerator.nextInt(50));

			int cantTags = randomGenerator.nextInt(10) + 1;
			
			for ( int j = 0; j < cantTags; j++ ) {
				book.addTag("tag" + randomGenerator.nextInt(20));
			}

			bookService.insertBook(book);
			
		}
		
//		Book book = new Book("titulo", "textoooooooooooooo", this.users.get(0));
//
//		book.setCategory("category");
//		book.setSubCategory("subcategory");
//
//		book.addComment(users.get(0), "comentario1");
//		book.addComment(users.get(0), "comentario2");
//		book.addComment(users.get(0), "comentario3");
//		book.addComment(users.get(0), "comentario4");
//
//		book.addTag("tag1");
//		book.addTag("tag2");
//		book.addTag("tag3");
//
//		bookService.insertBook(book);
//
//		book = new Book("titulo2", "textoooooooooooooo2", users.get(0));
//
//		book.setCategory("category2");
//		book.setSubCategory("subcategory2");
//
//		book.addComment(users.get(1), "comentario21");
//		book.addComment(users.get(2), "comentario22");
//		book.addComment(users.get(3), "comentario23");
//		book.addComment(users.get(0), "comentario24");
//
//		book.addTag("tag1");
//		book.addTag("tag22");
//		book.addTag("tag23");
//
//		bookService.insertBook(book);
//		
//		book = new Book("titulo3", "textoooooooooooooo3", users.get(1));
//
//		book.setCategory("category2");
//		book.setSubCategory("subcategory2");
//
//		book.addComment(users.get(1), "comentario21");
//		book.addComment(users.get(2), "comentario22");
//		book.addComment(users.get(3), "comentario23");
//		book.addComment(users.get(0), "comentario24");
//
//		bookService.insertBook(book);
		
		
	}

	public void addCampaigns() {

		Campaign campaign = new Campaign("subject", "text", admin);

		Set<CampaignDetail> receiverSet = new HashSet<CampaignDetail>();

		receiverSet.add(new CampaignDetail(users.get(0), campaign));
		receiverSet.add(new CampaignDetail(users.get(1), campaign));
		
		campaign.setReceiver(receiverSet);
		
		Set<Message> answerSet = new HashSet<Message>();
		//Campaign answer = new Campaign(Type.FIRST_ANSWER, "RE: subject", "respuesta a text",  users.get(0), admin);
		//answerSet.add(new Campaign(Type.FIRST_ANSWER, "RE: subject", "otra respuesta a text", users.get(1), admin));
		//answerSet.add(answer);
		//campaign.setAnswer(answerSet);
	
		//answerSet = new HashSet<Campaign>();
		//answerSet.add(new Campaign(Type.ANSWER, "RE: RE: subject", "respuesta a repuesta a text", admin, users.get(0)));
		//answer.setAnswer(answerSet);
		
		campaignService.insertCampaign(campaign);
		
		campaign = new Campaign("subject2", "text2", admin);

		receiverSet = new HashSet<CampaignDetail>();

		receiverSet.add(new CampaignDetail(users.get(1), campaign));
		receiverSet.add(new CampaignDetail(users.get(2), campaign));
		receiverSet.add(new CampaignDetail(users.get(3), campaign));
		
		campaign.setReceiver(receiverSet);
		campaignService.insertCampaign(campaign);

	}

	public void addMessages() {
		
		Message message = new Message(Type.PRIVATE_MESSAGE, "subject 0", "text", admin, users.get(0));
		message.setDate(new Date(System.currentTimeMillis() - 10000));
		messageService.insertMessage(message);
		
		message = new Message(Type.PRIVATE_MESSAGE, "subject 3", "text 1", admin, users.get(0));
		messageService.insertMessage(message);
		
		message = new Message(Type.PRIVATE_MESSAGE, "subject 1", "texto de admin a user", admin, users.get(0));
		message.setDate(new Date(System.currentTimeMillis() - 1000000));
		Message answer = new Message(Type.ANSWER, "RE: subject 1", "respuesta de user a admin", users.get(0), admin);
		answer.setDate(new Date(System.currentTimeMillis() - 1000));
		message.setAnswer(answer);
		
		Message answer2 = new Message(Type.ANSWER, "RE: RE: subject 1", "respuesta2 de admin a user", admin, users.get(0));
		answer2.setDate(new Date(System.currentTimeMillis() - 1));
		answer.setAnswer(answer2);

		//receiverSet.add(new MessageDetail(users.get(0), message));
		//receiverSet.add(new MessageDetail(users.get(1), message));
		
		//message.setReceiver(receiverSet);
		
		messageService.insertMessage(message);
		
		message = new Message(Type.PRIVATE_MESSAGE, "subject 2", "text 2", admin, users.get(1));

		//receiverSet = new HashSet<MessageDetail>();

		//receiverSet.add(new MessageDetail(users.get(1), message));
		//receiverSet.add(new MessageDetail(users.get(2), message));
		//receiverSet.add(new MessageDetail(users.get(3), message));
		
		//message.setReceiver(receiverSet);
		messageService.insertMessage(message);
		
		message = new Message(Type.PRIVATE_MESSAGE, "subject 333", "text 333", users.get(0), admin);
		messageService.insertMessage(message);
		
	}
	
	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - Load Data";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
	
	private static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

}