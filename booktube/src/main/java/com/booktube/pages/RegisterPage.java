package com.booktube.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.pages.validators.UniqueUsernameValidator;
import com.booktube.service.UserService;

public class RegisterPage extends BasePage {

	private static final long serialVersionUID = 1L;

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

		final TextField<String> birthdateField = new TextField<String>(
				"birthdate", new Model<String>(""));
		
		final PasswordTextField passwordField1 = new PasswordTextField(
				"password1", new Model<String>(""));
		final PasswordTextField passwordField2 = new PasswordTextField(
				"password2", new Model<String>(""));

		List<String> countryList = Arrays.asList(new String[] { "Country 1",
				"...", "Country 2" });

		final DropDownChoice<String> countrySelect = new DropDownChoice<String>("country",
				new PropertyModel<String>(this, ""), countryList);
		
		List<String> genderList = Arrays.asList(new String[] { "Masculino",
				"Femenino" });

		final DropDownChoice<String> genderSelect = new DropDownChoice<String>("gender",
				new PropertyModel<String>(this, ""), genderList);
		
		final TextField<String> cityField = new TextField<String>(
				"city", new Model<String>(""));
		
		
		usernameField.setRequired(true);
		firstnameField.setRequired(true);
		lastnameField.setRequired(true);
		birthdateField.setRequired(true);
		passwordField1.setRequired(true);
		passwordField2.setRequired(true);

		UniqueUsernameValidator usernameValidator = new UniqueUsernameValidator();
		usernameField.add(usernameValidator);

		form.add(new EqualPasswordInputValidator(passwordField1, passwordField2));

		form.add(usernameField);
		form.add(firstnameField);
		form.add(lastnameField);
		form.add(birthdateField);
		form.add(passwordField1);
		form.add(passwordField2);
		form.add(countrySelect);
		form.add(genderSelect);
		form.add(cityField);

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
				String country = countrySelect.getDefaultModelObjectAsString();
				String gender = genderSelect.getDefaultModelObjectAsString();
				String birthdate = birthdateField.getDefaultModelObjectAsString();
				String city = cityField.getDefaultModelObjectAsString();

				User user = new User(username, password, firstname, lastname,
						User.Level.USER);
				
				System.out.println("GENDER: " + gender);
				
				if ( gender.equals("Masculino") ) {
					user.setGender(Gender.MALE);
				}
				else {
					user.setGender(Gender.FEMALE);
				}
				
				if ( StringUtils.isNotBlank(city) ) {
					user.setCity(city);
				}
				
				user.setCountry(country);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				
				try {
					user.setBirthdate(sdf.parse(birthdate));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("USER GENDER: " + user.getGender());
				
				/* Insert user */
				userService.insertUser(user);
				System.out.println("User inserted.");
				System.out.println("Username: " + username);
				System.out.println("Firstname: " + firstname);
				System.out.println("Lastname: " + lastname);
				System.out.println("Country: " + country);
				System.out.println("City: " + city);
				System.out.println("Gender: " + gender);

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
