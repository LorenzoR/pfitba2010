package com.booktube.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.User;
import com.booktube.service.UserService;

public abstract class BasePage extends WebPage {

	@SpringBean
	UserService userService;

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

		add(new Label("footer",
				"Ayuda | Acerca de | Contacto | Términos y Condiciones"));

		add(new BookmarkablePageLink<String>("title", HomePage.class));
		add(new BookmarkablePageLink<String>("addBook", AddBookPage.class));
		add(new BookmarkablePageLink<String>("showBooks", BooksPage.class));
		add(new BookmarkablePageLink<String>("showWriters", WritersPage.class));
		add(new BookmarkablePageLink<String>("contact", DataViewPage.class));
		BookmarkablePageLink<String> registerLink = new BookmarkablePageLink<String>(
				"registerPage", RegisterPage.class);
		add(registerLink);

		Form<?> loginForm = loginForm("login");

		Link<?> logoutLink = new Link<Object>("logoutLink") {
			private static final long serialVersionUID = -4042618076562731461L;

			public void onClick() {
				WiaSession.get().logInUser(null);
				setResponsePage(HomePage.class);
			}
		};

		add(loginForm);
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

	private Form<Object> loginForm(String label) {
		final Form<Object> form = new Form<Object>(label);
		final TextField<String> username = new TextField<String>("username", new Model<String>(""));
		final PasswordTextField password = new PasswordTextField("password",
				new Model<String>(""));

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

				if (user.getPassword().equals(passwordString)) {
					WiaSession.get().logInUser(user);
				} else {
					System.out.println("Login failed!");
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