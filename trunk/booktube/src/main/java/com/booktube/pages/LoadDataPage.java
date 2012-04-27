package com.booktube.pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.model.MessageDetail;
import com.booktube.model.BookTag;
import com.booktube.model.User;
import com.booktube.pages.BasePage;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class LoadDataPage extends BasePage {

	@SpringBean
	UserService userService;

	@SpringBean
	BookService bookService;

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
			label = "Cargue datos";
		}

		parent.add(new Label("content", label));
	}

	public void addUsers() {

		User user = new User("user", "pass", "nombre", "apellido",
				User.Level.USER);
		this.admin = new User("admin", "admin", "nombre", "apellido",
				User.Level.ADMIN);

		userService.insertUser(user);
		users.add(user);
		userService.insertUser(this.admin);

		user = new User("user2", "user2", "user2", "user2",
				User.Level.USER);
		userService.insertUser(user);
		users.add(user);

		user = new User("user3", "user3", "user3", "user3",
				User.Level.USER);
		userService.insertUser(user);
		users.add(user);

		user = new User("user4", "user4", "user4", "user4",
				User.Level.USER);
		userService.insertUser(user);
		users.add(user);

	}

	public void addBooks() {
		System.out.println("Adding books");
		Book book = new Book("titulo", "textoooooooooooooo", this.users.get(0));

		book.setCategory("category");
		book.setSubCategory("subcategory");

		book.addComment(users.get(0), "comentario1");
		book.addComment(users.get(0), "comentario2");
		book.addComment(users.get(0), "comentario3");
		book.addComment(users.get(0), "comentario4");

		book.addTag("tag1");
		book.addTag("tag2");
		book.addTag("tag3");

		bookService.insertBook(book);

		book = new Book("titulo2", "textoooooooooooooo2", users.get(0));

		book.setCategory("category2");
		book.setSubCategory("subcategory2");

		book.addComment(users.get(1), "comentario21");
		book.addComment(users.get(2), "comentario22");
		book.addComment(users.get(3), "comentario23");
		book.addComment(users.get(0), "comentario24");

		book.addTag("tag1");
		book.addTag("tag22");
		book.addTag("tag23");

		bookService.insertBook(book);
		
		book = new Book("titulo3", "textoooooooooooooo3", users.get(1));

		book.setCategory("category2");
		book.setSubCategory("subcategory2");

		book.addComment(users.get(1), "comentario21");
		book.addComment(users.get(2), "comentario22");
		book.addComment(users.get(3), "comentario23");
		book.addComment(users.get(0), "comentario24");

		bookService.insertBook(book);
		
		
	}

	public void addMessages() {

		Message message = new Message("subject", "text", admin);

		Set<MessageDetail> receiverSet = new HashSet<MessageDetail>();

		receiverSet.add(new MessageDetail(users.get(0), message));
		receiverSet.add(new MessageDetail(users.get(1), message));
		
		message.setReceiver(receiverSet);
		messageService.insertMessage(message);
		
		message = new Message("subject", "text", admin);

		receiverSet = new HashSet<MessageDetail>();

		receiverSet.add(new MessageDetail(users.get(1), message));
		receiverSet.add(new MessageDetail(users.get(2), message));
		receiverSet.add(new MessageDetail(users.get(3), message));
		
		message.setReceiver(receiverSet);
		messageService.insertMessage(message);

	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - Load Data";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}