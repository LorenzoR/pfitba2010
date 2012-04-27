package com.booktube.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.ListItemOptions;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.User;
import com.booktube.pages.validators.UniqueUsernameValidator;
import com.booktube.service.UserService;

public class RegisterPage extends BasePage {

	@SpringBean
	UserService userService;

	private static Dialog dialog;

	public RegisterPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("register");
		parent.setOutputMarkupId(true);
		add(parent);

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		parent.add(feedback);

		// parent.add(new FeedbackPanel("feedback").setOutputMarkupId(true));

		parent.add(registerForm(parent, feedback));

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				// do your cancel logic here
				System.out.println("BUTTON CLICKED!!");
				setResponsePage(HomePage.class);

			}
		};

		dialog = new Dialog("success_dialog");
//		dialog.setOpenEvent(JsScopeUiEvent.quickScope(new JsStatement().self()
//				.chain("parents", "'.ui-dialog:first'")
//				.chain("find", "'.ui-dialog-titlebar-close'").chain("hide")
//				.render())); // When we open the dialog, we remove the close
//								// button on the title :)
		dialog.setButtons(ok);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));

		parent.add(dialog);

		// Button button = new Button("open-dialog");
		// button.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public JsScope callback() {
		// return JsScope.quickScope(dialog.open().render());
		// }
		//
		// }));
		// parent.add(button);

	}

	private Form<Object> registerForm(final WebMarkupContainer parent,
			final FeedbackPanel feedback) {

		Form<Object> form = new Form<Object>("form");

		final TextField<String> usernameField = new TextField<String>(
				"username", new Model<String>(""));
		final TextField<String> firstnameField = new TextField<String>(
				"firstname", new Model<String>(""));
		final TextField<String> lastnameField = new TextField<String>(
				"lastname", new Model<String>(""));

		final PasswordTextField passwordField1 = new PasswordTextField(
				"password1", new Model<String>(""));
		final PasswordTextField passwordField2 = new PasswordTextField(
				"password2", new Model<String>(""));

		usernameField.setRequired(true);
		firstnameField.setRequired(true);
		lastnameField.setRequired(true);
		passwordField1.setRequired(true);
		passwordField2.setRequired(true);

		UniqueUsernameValidator usernameValidator = new UniqueUsernameValidator();
		usernameField.add(usernameValidator);

		form.add(new EqualPasswordInputValidator(passwordField1, passwordField2));

		form.add(usernameField);
		form.add(firstnameField);
		form.add(lastnameField);
		form.add(passwordField1);
		form.add(passwordField2);

		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 5647152324594190069L;

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
				String password = passwordField1
						.getDefaultModelObjectAsString();

				User user = new User(username, password, firstname, lastname,
						User.Level.USER);

				/* Insert user */
				userService.insertUser(user);
				System.out.println("User inserted.");
				System.out.println("Username: " + username);
				System.out.println("Firstname: " + firstname);
				System.out.println("Lastname: " + lastname);

				target.add(parent);

				dialog.open(target);

			}

			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedback);
			}
		});

		return form;
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - Register";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
