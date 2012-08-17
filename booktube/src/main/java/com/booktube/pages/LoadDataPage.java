package com.booktube.pages;

import java.util.ArrayList;

import java.util.Calendar;
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
import com.booktube.model.Rating;
import com.booktube.model.User;
import com.booktube.model.UserVote;
import com.booktube.model.User.Gender;
import com.booktube.pages.BasePage;
import com.booktube.service.BookService;
import com.booktube.service.CampaignService;
import com.booktube.service.UserService;

public class LoadDataPage extends BasePage {

	private final int CANT_CAMPAIGNS = 5;
	private final int CANT_USERS = 50;
	private final int CANT_BOOKS = 200;
	private final int CANT_MSG = 10;

	private static final long serialVersionUID = 1L;

	private final Random randomGenerator = new Random(
			System.currentTimeMillis());

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
		}

		if (bookService.getCount() != 0) {

		} else {
			addBooks();
		}

		users.add(admin);

		if (messageService.getCount(null, null, null, null, null, null) != 0) {

		} else {
			addMessages();
		}

		if (campaignService.getCount(null, null, null, null, null, null) != 0) {

		} else {
			addCampaigns();
		}

		label = "Cargue datos";

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

		for (int i = 0; i < CANT_USERS; i++) {

			int year = randBetween(1900, 2010);
			int month = randBetween(0, 11);
			GregorianCalendar gc = new GregorianCalendar(year, month, 1);
			int day = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
			gc.set(year, month, day);
			Date birthdate = new Date(gc.getTimeInMillis());

			// year = randBetween(1900, 2010);
			if (i <= 11)
				year = 2008;
			else if (i > 11 && i <= 28)
				year = 2009;
			else if (i > 28 && i <= 50)
				year = 2010;
			else if (i > 50 && i <= 80)
				year = 2011;
			else
				year = 2012;

			month = randBetween(0, 11);
			gc = new GregorianCalendar(year, month, 1);
			day = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
			gc.set(year, month, day);
			Date registrationDate = new Date(gc.getTimeInMillis());

			int letter = (i % 26) + 97;
			System.out.println(i + ": " + (char) i);
			String username = (char) letter + "user" + i;
			User user = new User(username, username, "nombre" + i, "apellido"
					+ i, User.Level.USER);

			user.setBirthdate(birthdate);
			user.setRegistrationDate(registrationDate);
			user.setGender(gender);
			user.setCountry(countries.get(i % countries.size()));
			user.setCity(cities.get(i % cities.size()));
			user.setEmail(username + "@mail.com");
			user.setIsActive(true);
			userService.insertUser(user);
			users.add(user);

			gender = gender == Gender.MALE ? Gender.FEMALE : Gender.MALE;
		}

		User user = new User("user", "user", "nombre", "apellido",
				User.Level.USER);
		user.setBirthdate(new Date());
		user.setGender(Gender.MALE);
		user.setCountry("Country 1");
		user.setCity("City 10");
		user.setEmail(user.getUsername() + "@mail.com");
		user.setRegistrationDate(new Date());
		this.admin = new User("admin", "admin", "nombre", "apellido",
				User.Level.ADMIN);
		this.admin.setBirthdate(new Date());
		this.admin.setGender(Gender.MALE);
		this.admin.setCountry("Country 1");
		this.admin.setCity("City 1");
		this.admin.setEmail(this.admin.getUsername() + "@mail.com");
		this.admin.setIsActive(true);
		
		User admin2 = new User("admin2", "admin2", "nombreAdmin", "apellidoAdmin",
				User.Level.ADMIN);
		admin2.setBirthdate(new Date());
		admin2.setGender(Gender.MALE);
		admin2.setCountry("Country 1");
		admin2.setCity("City 1");
		admin2.setEmail(admin2.getUsername() + "@mail.com");
		admin2.setIsActive(true);
		userService.insertUser(admin2);
		
		
		userService.insertUser(user);
		users.add(user);
		userService.insertUser(this.admin);

	}

	public void addBooks() {

		System.out.println("Adding books");

		long publishDate = System.currentTimeMillis();
		
		for (int i = 0; i < CANT_BOOKS; i++) {

			double rating = randomGenerator.nextDouble() * 5;
			int numOfVotes = randomGenerator.nextInt(5000);
			int sumOfRating = (int) rating * numOfVotes;

			User newUser = userService.getUser(this.users.get(
					randomGenerator.nextInt(this.users.size())).getId());

			String title = bookTitles[i % bookTitles.length];

			Book book = new Book(title, bookText, newUser);
			book.setPublishDate(new Date(publishDate -= 100000000));

			int categoryIndex = i%categories.length;
			
			book.setCategory(categories[categoryIndex]);
			book.setSubCategory("subcategory" + randomGenerator.nextInt(50));
			// book.addUserVote(users.get(0));
			// book.addUserVote(users.get(1));
			// book.setUserVotes(userSet);
			// book.addUserVote(new UserVote(users.get(0), book));

			book.setRating(new Rating(sumOfRating, numOfVotes, rating, book));

			int cantTags = randomGenerator.nextInt(10) + 1;

			for (int j = 0; j < cantTags; j++) {
				book.addTag("tag" + randomGenerator.nextInt(20));
			}

			newUser.addBook(book);
			// newUser.addVote(book);

			// userService.updateUser(newUser);

			for (User aUser : this.users) {
				UserVote userVote = new UserVote(aUser, book);
				// userVote.setBook(book);
				// userVote.setUser(aUser);
				book.addUserVote(userVote);
				// aUser.addUserVote(userVote);
				// userService.updateUser(aUser);
			}

			bookService.insertBook(book);

		}

	}

	public void addCampaigns() {

		for (int i = 0; i < CANT_CAMPAIGNS; i++) {

			Campaign campaign = new Campaign("subject " + i, "text " + i, admin);

			Set<CampaignDetail> receiverSet = new HashSet<CampaignDetail>();

			int cantReceivers = randomGenerator.nextInt(CANT_USERS + 1);

			for (int j = 0; j < cantReceivers; j++) {
				receiverSet.add(new CampaignDetail(users.get(j), campaign));
			}

			campaign.setReceiver(receiverSet);

			campaignService.insertCampaign(campaign);

		}

		// campaign = new Campaign("subject2", "text2", admin);
		//
		// receiverSet = new HashSet<CampaignDetail>();
		//
		// receiverSet.add(new CampaignDetail(users.get(1), campaign));
		// receiverSet.add(new CampaignDetail(users.get(2), campaign));
		// receiverSet.add(new CampaignDetail(users.get(3), campaign));
		//
		// campaign.setReceiver(receiverSet);
		// campaignService.insertCampaign(campaign);

	}

	public void addMessages() {

		Long time = 0L;

		for (int i = 0; i < CANT_MSG; i++) {

			String subject = "subject " + i;
			User sender = users.get(randomGenerator.nextInt(CANT_USERS + 1));

			User receiver = users.get(randomGenerator.nextInt(CANT_USERS + 1));

			Message message = new Message(Type.PRIVATE_MESSAGE, subject,
					"text " + i, sender, receiver);
			message.setDate(new Date(time += 100000));

			// messageService.insertMessage(message);

			int cantAns = randomGenerator.nextInt(10);

			Message auxMessage = message;

			for (int j = 0; j < cantAns; j++) {

				User auxUser = sender;
				sender = receiver;
				receiver = auxUser;

				Message answer = new Message(Type.ANSWER, "RE: " + subject,
						"respuesta " + j, sender, receiver);
				answer.setDate(new Date(time += 100000));
				auxMessage.setAnswer(answer);
				auxMessage = answer;

			}

			messageService.insertMessage(message);

		}

		// Message message = new Message(Type.PRIVATE_MESSAGE, "subject 0",
		// "text", admin, users.get(0));
		// message.setDate(new Date(System.currentTimeMillis() - 10000));
		// messageService.insertMessage(message);
		//
		// message = new Message(Type.PRIVATE_MESSAGE, "subject 3", "text 1",
		// admin, users.get(0));
		// messageService.insertMessage(message);
		//
		// message = new Message(Type.PRIVATE_MESSAGE, "subject 1",
		// "texto de admin a user", admin, users.get(0));
		// message.setDate(new Date(System.currentTimeMillis() - 1000000));
		// Message answer = new Message(Type.ANSWER, "RE: subject 1",
		// "respuesta de user a admin", users.get(0), admin);
		// answer.setDate(new Date(System.currentTimeMillis() - 1000));
		// message.setAnswer(answer);
		//
		// Message answer2 = new Message(Type.ANSWER, "RE: RE: subject 1",
		// "respuesta2 de admin a user", admin, users.get(0));
		// answer2.setDate(new Date(System.currentTimeMillis() - 1));
		// answer.setAnswer(answer2);
		//
		// // receiverSet.add(new MessageDetail(users.get(0), message));
		// // receiverSet.add(new MessageDetail(users.get(1), message));
		//
		// // message.setReceiver(receiverSet);
		//
		// messageService.insertMessage(message);
		//
		// message = new Message(Type.PRIVATE_MESSAGE, "subject 2", "text 2",
		// admin, users.get(1));
		//
		// // receiverSet = new HashSet<MessageDetail>();
		//
		// // receiverSet.add(new MessageDetail(users.get(1), message));
		// // receiverSet.add(new MessageDetail(users.get(2), message));
		// // receiverSet.add(new MessageDetail(users.get(3), message));
		//
		// // message.setReceiver(receiverSet);
		// messageService.insertMessage(message);
		//
		// message = new Message(Type.PRIVATE_MESSAGE, "subject 333",
		// "text 333",
		// users.get(0), admin);
		// messageService.insertMessage(message);

	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - Load Data";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

	private static int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

	private String bookText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vel orci sit amet lacus facilisis dignissim. Etiam id ante non quam porta bibendum vel eu leo. Duis scelerisque tellus at eros faucibus quis placerat lacus posuere. Aliquam interdum tempus dignissim. Fusce pellentesque adipiscing ipsum, sed tincidunt diam hendrerit sed. Cras pharetra facilisis nunc sit amet mattis. Sed volutpat gravida risus, ut lacinia tortor posuere eu. Vestibulum quis dapibus tortor. Aliquam nisi ipsum, vestibulum sed viverra sit amet, sodales quis nulla. Etiam enim turpis, molestie vel volutpat vehicula, pretium a mauris. Quisque purus nunc, placerat at consequat nec, rhoncus at nisi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eleifend, nisi id semper facilisis, tellus neque faucibus tellus, sed ultrices sapien metus in libero. Nunc volutpat tempus mauris a vulputate. Suspendisse volutpat sodales fringilla. Nunc ac sapien sit amet arcu vestibulum cursus."
			+ "\n\n Praesent nec ligula neque, id sollicitudin ante. Aliquam rhoncus congue mauris id pellentesque. Donec non metus nulla, et rutrum velit. Nulla ac nibh eget arcu molestie pellentesque. Donec sed sem in nibh egestas mollis. Donec ornare lobortis suscipit. Vestibulum lacinia lobortis varius. Suspendisse ac luctus turpis. Suspendisse eget turpis tortor. Maecenas at aliquet velit. Nam lobortis massa at justo mattis tristique. Vestibulum consequat enim a est blandit a luctus neque aliquet. Morbi sed volutpat enim. Vivamus eget neque sit amet justo condimentum tincidunt non id lacus. Mauris et ipsum ac tortor malesuada ornare vulputate pharetra nunc. Vivamus sollicitudin euismod pellentesque."
			+ "\n\n Pellentesque eu mauris volutpat sem lacinia ultrices non ut ante. Donec pretium adipiscing magna sed tempor. Curabitur in diam massa, ac malesuada dui. Aenean ac auctor ipsum. Phasellus condimentum semper erat rutrum lobortis. Nullam fermentum mattis leo vitae cursus. Integer nec massa a purus tempus euismod. Aenean ut velit metus. Nam non quam eget sem posuere commodo. Donec mattis malesuada faucibus. Donec vestibulum euismod pellentesque. Fusce commodo lobortis augue. Nulla imperdiet, metus non auctor fermentum, velit nulla eleifend ligula, eget fringilla purus dolor sed quam."
			+ "\n\n Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Integer posuere iaculis turpis, vitae adipiscing felis aliquam et. Nulla quis placerat ipsum. Aliquam et erat quis lectus fermentum pulvinar. Nunc nec lectus purus, a fermentum nulla. Vestibulum aliquet facilisis justo, ac pharetra urna aliquam in. Proin eget mauris a odio tincidunt ornare a vitae neque. Nunc vehicula auctor nunc eu laoreet. Donec pretium mollis nisi, et bibendum odio facilisis ut."
			+ "\n\n Vestibulum sit amet consectetur mauris. Sed fringilla iaculis interdum. Maecenas ultrices elementum congue. Morbi id velit tellus, adipiscing convallis est. Phasellus eros augue, eleifend et suscipit ut, mattis non lacus. Quisque orci eros, iaculis sit amet volutpat ut, euismod adipiscing leo. Sed eu quam ac libero tincidunt elementum nec et nunc. Maecenas nisi eros, consectetur et vulputate sed, feugiat volutpat metus. Phasellus vitae justo velit. Quisque tincidunt libero id purus facilisis gravida iaculis mauris tempor. Morbi at augue eget nisi condimentum lobortis a sit amet massa. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam iaculis, elit sed suscipit bibendum, eros sapien tempor quam, ut ullamcorper justo metus in lorem.";

	private String[] bookTitles = { "Forgotten Luck", "The Which Servant",
			"Words of Sons", "The Night's World", "The Dream of the Prophecy",
			"Shards in the Shores", "Purple Ice", "The Wild Slaves",
			"Girl of Flowers", "The Bridges's Mage",
			"The Streams of the Flame", "Truth in the Something",
			"Purple Dying", "The Smooth Sky", "Secrets of Ice",
			"The Edge's Lord", "The Return of the Time", "Birch in the Snake",
			"Sleeping Obsession", "The Silent Nothing", "Tears of Predator",
			"The Destiny's Princess", "The Danger of the Wizard",
			"Hunter in the Memory", "Silent Illusion", "The Smooth Word",
			"Abyss of Emerald", "The Visions's Storm", "The Wizards's Slaves",
			"The Mist of the Force", "Consort in the Gate",
			"Magnificent Thief", "The Lonely Flower", "Healer of Lover",
			"The Flame's Hunter", "The Wizards of the Ashes",
			"Winter in the Alien", "Broken Scent", "The Lost Misty",
			"Emperor of Pirates", "The Consort's Dream",
			"The Healer of the Night", "Tower in the Slaves", "Grey Voyagers",
			"The Whispering Force", "Wings of Prophecy",
			"The Worlds's Dreamer", "The Secret of the Voyagers",
			"Witch in the Ice", "Smooth Flying", "Witches of Birch",
			"The Missing Wizard", "The Women of the Silence",
			"Nobody in the Prince", "Prized Secrets", "The First Lord",
			"Angel of Lover", "The Snow's Body", "The Door of the Wizards",
			"Rose in the Slave" };

	private String[] categories = { "Self", "Special Interest",
			"Health & Living", "General Interest", "History", "Fiction",
			"Law & Order", "Hobbies", "Business & Finance",
			"Entertainment & Leisure", "Travel" };

}