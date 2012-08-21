package com.booktube.pages;

import java.io.File;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.apache.wicket.datetime.PatternDateConverter;

import com.booktube.WiaSession;
import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.pages.validators.BirthdayValidator;
import com.booktube.pages.validators.UniqueEmailValidator;
import com.booktube.pages.validators.UniqueUsernameValidator;
import com.booktube.service.UserService;

public class RegisterPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	UserService userService;

	private static Dialog dialog;

	private final String AVATAR_DIR = "avatar/";
	private final String IMG_DIR_NAME = "img";
	
	public RegisterPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("register");
		parent.setOutputMarkupId(true);
		add(parent);

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		parent.add(feedback);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", RegisterPage.class), new ResourceModel("registerPageTitle").getObject());
		
		// parent.add(new FeedbackPanel("feedback").setOutputMarkupId(true));

		parent.add(registerForm(parent, feedback));

		dialog = new SuccessDialog<HomePage>("success_dialog",
				new ResourceModel("newUser").getObject(), HomePage.class, null);

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
		form.setMultiPart(true);

		final User newUser = new User();

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				newUser);

		form.setDefaultModel(model);

		final RequiredTextField<User> usernameField = new RequiredTextField<User>(
				"username");
		final RequiredTextField<User> firstnameField = new RequiredTextField<User>(
				"firstname");
		final RequiredTextField<User> lastnameField = new RequiredTextField<User>(
				"lastname");
		final RequiredTextField<User> email = new RequiredTextField<User>(
				"email");
		email.add(EmailAddressValidator.getInstance());

		// final TextField<Date> birthdateField = new TextField<Date>(
		// "birthdate", new PropertyModel<Date>(model, "birthdate"));

		// DateTextField birthdateField = new DateTextField("birthdateField",
		// new PropertyModel<Date>(
		// model, "birthdateField"), new StyleDateConverter("S-", true));
		//final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
		
		final DateTextField birthdateField = new DateTextField("birthdate",
				new PropertyModel<Date>(model, "birthdate"),
				new PatternDateConverter(new ResourceModel("dateFormat").getObject(), true));

		final PasswordTextField passwordField1 = new PasswordTextField(
				"password1", new PropertyModel<String>(model, "password"));
		final PasswordTextField passwordField2 = new PasswordTextField(
				"password2", new Model<String>(""));

		List<String> countryList = new ArrayList<String>();
		countryList.add("Country 1");
		countryList.add("Country 2");
		countryList.add("Country 3");
		countryList.add("Country 4");
		countryList.add("Country 5");
		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
				"country", countryList);

		final DropDownChoice<Gender> genderSelect = new DropDownChoice<Gender>(
				"gender", Arrays.asList(Gender.values()),
				new EnumChoiceRenderer<Gender>(this));
		genderSelect.setRequired(true);

		final TextField<User> cityField = new TextField<User>("city");

		// final FileUploadField fileUpload = new FileUploadField("imgUpload",
		// ));

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final FileUploadField fileUploadField = new FileUploadField(
				"imgUpload", (IModel) new Model<FileUpload>());
		form.add(fileUploadField);
		
		final DropDownChoice<Level> levelDDC = new DropDownChoice<Level>(
				"level", Arrays.asList(Level.values()),
				new EnumChoiceRenderer<Level>(this));

		final WebMarkupContainer levelP = new WebMarkupContainer("level_p");
		levelP.add(levelDDC);
		form.add(levelP);

		if (WiaSession.get().getLoggedInUser() == null
				|| !WiaSession.get().getLoggedInUser().isAdmin()) {
			levelP.setVisible(false);
		}

		birthdateField.setRequired(true);
		countrySelect.setRequired(true);
		// passwordField1.setRequired(true);
		// passwordField2.setRequired(true);

		usernameField.add(new UniqueUsernameValidator());
		email.add(new UniqueEmailValidator());

		birthdateField.add(new BirthdayValidator());

		form.add(new EqualPasswordInputValidator(passwordField1, passwordField2));

		form.add(usernameField);
		form.add(firstnameField);
		form.add(lastnameField);
		form.add(email);
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
				// String username =
				// usernameField.getDefaultModelObjectAsString();
				// String firstname = firstnameField
				// .getDefaultModelObjectAsString();
				// String lastname =
				// lastnameField.getDefaultModelObjectAsString();
				// String country =
				// countrySelect.getDefaultModelObjectAsString();
				// String gender = genderSelect.getDefaultModelObjectAsString();
				// String city = cityField.getDefaultModelObjectAsString();
				// String password = passwordField1
				// .getDefaultModelObjectAsString();

				String birthdate = birthdateField
						.getDefaultModelObjectAsString();
				System.out.println(birthdate);

				// User user = new User(username, password, firstname, lastname,
				// User.Level.USER);

				// System.out.println("GENDER: " + gender);

				// if ( gender.equals("Masculino") ) {
				// user.setGender(Gender.MALE);
				// }
				// else {
				// user.setGender(Gender.FEMALE);
				// }
				//
				// if ( StringUtils.isNotBlank(city) ) {
				// user.setCity(city);
				// }

				// user.setCountry(country);

				// SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				// try {
				// newUser.setBirthdate(sdf.parse(birthdate));
				// } catch (ParseException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				newUser.setLevel(Level.USER);
				// newUser.setPassword(password);

				final FileUpload uploadedFile = fileUploadField.getFileUpload();

				if (uploadedFile != null) {

					// ServletContext context = ((WebApplication)
					// getApplication()).getServletContext();
					String imgPath = ((WebApplication) getApplication())
							.getServletContext().getRealPath(IMG_DIR_NAME);

					System.out.println("REAL PATH ES " + imgPath);

					final String extension = uploadedFile.getClientFileName()
							.substring(
									uploadedFile.getClientFileName()
											.lastIndexOf('.') + 1);
					System.out.println("EXTENSION: " + extension);

					final String imgFilename = newUser.getUsername() + '.'
							+ extension;

					String filePath = imgPath + "/" + AVATAR_DIR + imgFilename;

					System.out.println("***** FILENAME> " + imgPath + '/'
							+ newUser.getUsername() + '.' + extension);

					// write to a new file
					File newFile = new File(filePath);

					if (newFile.exists()) {
						newFile.delete();
					}

					try {
						newFile.createNewFile();
						uploadedFile.writeTo(newFile);

						// info("saved file: " +
						// uploadedFile.getClientFileName());
					} catch (Exception e) {
						e.printStackTrace();
					}

					newUser.setImageURL(imgFilename);
					
				}
				
				
				//Para el proceso de registracion
				try {
					String secret = userService.generateSecret();					
					newUser.setSecret(secret);
				} catch (NoSuchAlgorithmException e) {						
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {						
					e.printStackTrace();
				}
				

				/* Insert user */
				userService.insertUser(newUser);
				System.out.println("User inserted.");
				System.out.println("Username: " + newUser.getUsername());
				System.out.println("Firstname: " + newUser.getFirstname());
				System.out.println("Lastname: " + newUser.getLastname());
				System.out.println("Country: " + newUser.getCountry());
				System.out.println("City: " + newUser.getCity());
				System.out.println("Gender: " + newUser.getGender());
				System.out.println("Birthdate: " + newUser.getBirthdate());

				if (fileUploadField != null
						&& fileUploadField.getConvertedInput() != null) {
					System.out.println("IMAGEEE : "
							+ fileUploadField.getConvertedInput().toString());
				}


				// Para el proceso de registracion				
				try {
					userService.sendRegistrationMail(newUser);
				} catch (Exception e) {					
					e.printStackTrace();
				}

				//target.add(parent);
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
		String newTitle = "Booktube - " + new ResourceModel("registerPageTitle").getObject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
