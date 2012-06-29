package com.booktube.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
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

	private Form<User> registerForm(final WebMarkupContainer parent,
			final FeedbackPanel feedback) {

		Form<User> form = new Form<User>("form");
		
		final User newUser = new User();

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				newUser);

		form.setDefaultModel(model);

		final TextField<User> usernameField = new TextField<User>("username");
		final TextField<User> firstnameField = new TextField<User>("firstname");
		final TextField<User> lastnameField = new TextField<User>("lastname");

		final TextField<Date> birthdateField = new TextField<Date>(
				"birthdate");
		
		final PasswordTextField passwordField1 = new PasswordTextField(
				"password1", new PropertyModel<String>(model, "password"));
		final PasswordTextField passwordField2 = new PasswordTextField(
				"password2", new Model<String>(""));

		List<String> countryList = userService.getAllCountries();
		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
				"country", countryList);
		
		final DropDownChoice<Gender> genderSelect = new DropDownChoice<Gender>(
				"gender", Arrays.asList(Gender.values()),
				new EnumChoiceRenderer<Gender>(this));
		
		final TextField<User> cityField = new TextField<User>("city");
		
		
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

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// comments.add(new Comment(new
				// User(ddc.getDefaultModelObjectAsString()),
				// editor.getDefaultModelObjectAsString()));
				// editor.setModel(new Model(""));
				// target.addComponent(parent);
				// target.focusComponent(editor);
				// System.out.println("ACA 1");
//				String username = usernameField.getDefaultModelObjectAsString();
//				String firstname = firstnameField
//						.getDefaultModelObjectAsString();
//				String lastname = lastnameField.getDefaultModelObjectAsString();
//				String country = countrySelect.getDefaultModelObjectAsString();
//				String gender = genderSelect.getDefaultModelObjectAsString();
//				String city = cityField.getDefaultModelObjectAsString();
//				String password = passwordField1
//						.getDefaultModelObjectAsString();
				String birthdate = birthdateField.getDefaultModelObjectAsString();
				

//				User user = new User(username, password, firstname, lastname,
//						User.Level.USER);
				
//				System.out.println("GENDER: " + gender);
				
//				if ( gender.equals("Masculino") ) {
//					user.setGender(Gender.MALE);
//				}
//				else {
//					user.setGender(Gender.FEMALE);
//				}
//				
//				if ( StringUtils.isNotBlank(city) ) {
//					user.setCity(city);
//				}
				
				//user.setCountry(country);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				
//				try {
//					newUser.setBirthdate(sdf.parse(birthdate));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				newUser.setLevel(Level.USER);
				//newUser.setPassword(password);
				
				/* Insert user */
				userService.insertUser(newUser);
				System.out.println("User inserted.");
				System.out.println("Username: " + newUser.getUsername());
				System.out.println("Firstname: " + newUser.getFirstname());
				System.out.println("Lastname: " + newUser.getLastname());
				System.out.println("Country: " + newUser.getCountry());
				System.out.println("City: " + newUser.getCity());
				System.out.println("Gender: " + newUser.getGender());

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
