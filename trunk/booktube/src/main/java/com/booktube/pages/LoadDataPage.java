package com.booktube.pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.pages.BasePage;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class LoadDataPage extends BasePage {

	@SpringBean
	UserService userService;
	
	@SpringBean
	BookService bookService;

	private User user;
	
	public LoadDataPage() {
		
		final WebMarkupContainer parent = new WebMarkupContainer("loadData");
		parent.setOutputMarkupId(true);
		add(parent);
		String label = null;
		
		if ( userService.getCount() != 0 ) {
			label = "Ya hay datos";
		}
		else {
			addUsers();
			label = "Cargue datos";
		}
		
		parent.add(new Label("content", label));
	}

	public void addUsers() {

		this.user = new User("user", "pass", "nombre", "apellido",
				User.Level.USER);
		User admin = new User("admin", "admin", "nombre", "apellido",
				User.Level.ADMIN);

		userService.insertUser(this.user);
		userService.insertUser(admin);
	}
	
	public void addBooks() {
		Book book = new Book("titulo", "textoooooooooooooo", this.user);
		
		book.addComment(this.user, "comentario1");
		book.addComment(this.user, "comentario2");
		book.addComment(this.user, "comentario3");
		book.addComment(this.user, "comentario4");
		
		book.addTag("tag1");
		book.addTag("tag2");
		book.addTag("tag3");
		
		bookService.insertBook(book);
	}

}