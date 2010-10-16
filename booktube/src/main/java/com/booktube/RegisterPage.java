package com.booktube;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.User;
import com.booktube.service.UserService;

public class RegisterPage extends BasePage {

	@SpringBean
	UserService userService;

	public RegisterPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("register");
		parent.setOutputMarkupId(true);
		add(parent);

		parent.add(registerForm(parent));

	}

	private Form registerForm(final WebMarkupContainer parent) {

		Form form = new Form("form");

		final TextField usernameField = new TextField("username", new Model(""));
		final TextField firstnameField = new TextField("firstname", new Model(
				""));
		final TextField lastnameField = new TextField("lastname", new Model(""));

		final PasswordTextField password1Field = new PasswordTextField(
				"password1", new Model(""));
		final PasswordTextField password2Field = new PasswordTextField(
				"password2", new Model(""));

		form.add(usernameField);
		form.add(firstnameField);
		form.add(lastnameField);
		form.add(password1Field);
		form.add(password2Field);

		form.add(new AjaxSubmitLink("save") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// comments.add(new Comment(new
				// User(ddc.getDefaultModelObjectAsString()),
				// editor.getDefaultModelObjectAsString()));
				// editor.setModel(new Model(""));
				// target.addComponent(parent);
				// target.focusComponent(editor);
				// System.out.println("ACA 1");
				String username = usernameField.getDefaultModelObjectAsString();
				String firstname = firstnameField
						.getDefaultModelObjectAsString();
				String lastname = lastnameField.getDefaultModelObjectAsString();

				User user = new User(username, firstname, lastname);

				
				/* Insert user */
				userService.insertUser(user);
				System.out.println("User inserted.");
				System.out.println("Username: " + username);
				System.out.println("Firstname: " + firstname);
				System.out.println("Lastname: " + lastname);

				target.addComponent(parent);
				setResponsePage(HomePage.class);

			}
		});

		return form;
	}

}
