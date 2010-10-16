package com.booktube;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;


public abstract class BasePage extends WebPage {
	public BasePage() {

		add(new Label("footer",
				"Ayuda | Acerca de | Contacto | Términos y Condiciones"));

		add(new BookmarkablePageLink("title", HomePage.class));
		add(new BookmarkablePageLink("addBook", AddBookPage.class));
		add(new BookmarkablePageLink("showBooks", BooksPage.class));
		add(new BookmarkablePageLink("showWriters", WritersPage.class));
		add(new BookmarkablePageLink("contact", BooksPage.class));
		add(new BookmarkablePageLink("registerPage", RegisterPage.class));
		
		add(loginForm());

	}

	private Form loginForm() {
		final Form form = new Form("login") {
			@Override
			protected void onSubmit() {
				System.out.println("Form onSubmit is called");
			}
		};
		final TextField username = new TextField("username", new Model(""));
		final PasswordTextField password = new PasswordTextField("password",
				new Model(""));

		form.add(username);
		form.add(password);

		form.add(new Button("button1", new Model("")) {
			@Override
			public void onSubmit() {
				String userString = username.getDefaultModelObjectAsString();
				String passwordString = password
						.getDefaultModelObjectAsString();

				username.setModel(new Model(""));
				System.out.println("Button 1's onSubmit is called");
				System.out.println("User: " + userString + " Pass: "
						+ passwordString);
			}
		});
		
		return form;
	}
	
	public boolean login(String username, String password) {
		return username.equals(password);
	}
}