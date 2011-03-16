package com.booktube.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public abstract class BasePage extends WebPage {

	@SpringBean
	UserService userService;

	@SpringBean
	BookService bookService;

	public BasePage() {

		// WebMarkupContainer group = new WebMarkupContainer("loginContainer");
		if (WiaSession.get().isAuthenticated()) {
			add(new Label("welcome", "Bienvenido "
					+ WiaSession.get().getLoggedInUser().getUsername() + " | "));
		} else {
			add(new Label("welcome"));
		}
		
		
		// Label welcomeLabel = new Label("welcome");
		// add(welcomeLabel);
		// welcomeLabel.set
		// group.add(new Label("welcome", "Welcome " +
		// WiaSession.get().getLoggedInUser().getUsername()));
		// add(group);
		// group.setVisible(false);

		final PageParameters parameters = new PageParameters();
		parameters.put("type", "all");

		add(new Label("footer",
				"Ayuda | Acerca de | Contacto | Términos y Condiciones"));

		add(new BookmarkablePageLink<String>("title", HomePage.class));
		add(new BookmarkablePageLink<String>("addBook", AddBookPage.class));
		add(new BookmarkablePageLink<String>("showBooks", BooksPage.class,
				parameters));
		add(new BookmarkablePageLink<String>("showWriters", WritersPage.class));
		add(new BookmarkablePageLink<String>("contact", Contact.class));
		BookmarkablePageLink<String> registerLink = new BookmarkablePageLink<String>(
				"registerPage", RegisterPage.class);
		add(registerLink);

		Form<?> loginForm = loginForm("login");

		Form<?> searchForm = searchBookForm("searchBook");

		Link<?> logoutLink = new Link<Object>("logoutLink") {
			private static final long serialVersionUID = -4042618076562731461L;

			public void onClick() {
				WiaSession.get().logInUser(null);
				setResponsePage(HomePage.class);
			}
		};

		add(loginForm);
		add(searchForm);
		add(logoutLink);

		// loginForm.setVisible(false);

		if (WiaSession.get().isAuthenticated()) {
			// System.out.println("Logueado con username " +
			// WiaSession.get().getUser().getUsername());
			loginForm.setVisible(false);
			registerLink.setVisible(false);

		} else {
			// System.out.println("NO esta logueado");
			// add(new Label("login", "Welcome " +
			// WiaSession.get().getUser().getUsername()));
			logoutLink.setVisible(false);
		}

	}

	private Form<Object> searchBookForm(String label) {

		final Form<Object> form = new Form<Object>(label);

		final RadioGroup<String> radioGroup = new RadioGroup<String>("group", new Model<String>(""));
		radioGroup.setOutputMarkupId(true);
		
		Radio<String> radio = new Radio<String>("author", new Model<String>("author"));
	
		
		radioGroup.add(new Radio<String>("author", new Model<String>("author")));
		radioGroup.add(new Radio<String>("title", new Model<String>("title")));
		radioGroup.add(new Radio<String>("rating", new Model<String>("rating")));
		radioGroup.add(new Radio<String>("tag", new Model<String>("tag")));
		form.add(radioGroup);
		
		final AutoCompleteTextField<String> bookTitle = new AutoCompleteTextField<String>(
				"bookTitle", new Model<String>("")) {

			private static final long serialVersionUID = 2977239698122401133L;

			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input)) {
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}
				
				System.out.println("RadioGroupValue: " + radioGroup.getModelObject() );
				
				List<Book> books = null;
				
				List<String> choices = new ArrayList<String>(10);

				if ( radioGroup.getValue().equals("author") ) {
					books = null;
				}
				else if ( radioGroup.getValue().equals("rating") ) {
					books = null;
				}
				else if ( radioGroup.getValue().equals("tag") ) {
					books = null;
				}
				else {
					books = bookService.getAllBooks(0, Integer.MAX_VALUE);
				}
				

				for (final Book book : books) {
					final String title = book.getTitle();

					if (title.toUpperCase().startsWith(input.toUpperCase())) {
						choices.add(title);
						if (choices.size() == 10) {
							break;
						}
					}
				}

				return choices.iterator();
			}
		};

		form.add(bookTitle);
		
		form.add(new Button("find", new Model<String>("")) {

			private static final long serialVersionUID = 7667432284192053894L;

			@Override
			public void onSubmit() {
				String bookTitleString = bookTitle
						.getDefaultModelObjectAsString();

				bookTitle.setModel(new Model<String>(""));

				System.out.println("Book Title: " + bookTitleString);

				System.out.println("Radio Button: " + radioGroup.getValue());
				
				if (!continueToOriginalDestination()) {
					final PageParameters parameters = new PageParameters();
					
					if ( radioGroup.getValue().equals("author") ) {
						parameters.put("type", "author");
						parameters.put("author", bookTitleString);
					}
					else if ( radioGroup.getValue().equals("rating") ) {
						parameters.put("type", "rating");
						parameters.put("rating", bookTitleString);
					}
					else if ( radioGroup.getValue().equals("tag") ) {
						parameters.put("type", "tag");
						parameters.put("tag", bookTitleString);
					}
					else {
						System.out.println("Radio Button: " + radioGroup.getValue());
						parameters.put("type", "title");
						parameters.put("title", bookTitleString);
					}
					
					setResponsePage(BooksPage.class, parameters);
				}

			}
		});

		return form;
	}

	private Form<Object> loginForm(String label) {
		final Form<Object> form = new Form<Object>(label);
		final TextField<String> username = new TextField<String>("username",
				new Model<String>(""));
		final PasswordTextField password = new PasswordTextField("password",
				new Model<String>(""));

		/*final Label loginMsg = new Label("loginMsg", "Nombre de usuario o password incorrecto");
		loginMsg.setVisible(false);
		form.add(loginMsg);*/
		
		// Add a FeedbackPanel for displaying our messages
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        form.add(feedbackPanel);
		
		
		form.add(username);
		form.add(password);
		
		form.add(new Button("button1", new Model<String>("")) {
			private static final long serialVersionUID = 6743737357599494567L;

			@Override
			public void onSubmit() {
				String userString = username.getDefaultModelObjectAsString();
				String passwordString = password
						.getDefaultModelObjectAsString();

				username.setModel(new Model<String>(""));

				System.out.println("User: " + userString + " Pass: "
						+ passwordString);

				User user = userService.getUser(userString);

				if (user != null && user.getPassword().equals(passwordString)) {
					WiaSession.get().logInUser(user);
				} else {
					/* TERMINAR MENSAJE DE LOGIN INCORRECTO */
					System.out.println("Login failed!");
					info("aaaaaaaaaaaa");
					//loginMsg.setVisible(true);
				}

				if (!continueToOriginalDestination()) {
					setResponsePage(HomePage.class);
				}

				// setResponsePage(BasePage.this);

			}
		});

		return form;
	}

}