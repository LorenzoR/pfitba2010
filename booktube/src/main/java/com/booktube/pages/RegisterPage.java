package com.booktube.pages;

import java.io.File;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;
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

	private User user;

	public RegisterPage() {

		user = WiaSession.get().getLoggedInUser();

		final WebMarkupContainer parent = new WebMarkupContainer("register");
		parent.setOutputMarkupId(true);
		add(parent);

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		parent.add(feedback);

		addBreadcrumb(new BookmarkablePageLink<Object>("link",
				RegisterPage.class),
				new ResourceModel("registerPageTitle").getObject());

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
		// final DateFormat dateFormat =
		// DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());

		final DateTextField birthdateField = new DateTextField("birthdate",
				new PropertyModel<Date>(model, "birthdate"),
				new PatternDateConverter(
						new ResourceModel("dateFormat").getObject(), true));

		final PasswordTextField passwordField1 = new PasswordTextField(
				"password1", new PropertyModel<String>(model, "password"));
		final PasswordTextField passwordField2 = new PasswordTextField(
				"password2", new Model<String>(""));

		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
				"country", getCountries());

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

				if (user != null && user.getLevel() == Level.ADMIN) {
					newUser.setLevel(levelDDC.getModelObject());
				} else {
					newUser.setLevel(Level.USER);
				}

				final FileUpload uploadedFile = fileUploadField.getFileUpload();

				if (uploadedFile != null) {

					String imgPath = ((WebApplication) getApplication())
							.getServletContext().getRealPath(IMG_DIR_NAME);

					final String extension = uploadedFile.getClientFileName()
							.substring(
									uploadedFile.getClientFileName()
											.lastIndexOf('.') + 1);

					final String imgFilename = newUser.getUsername() + '.'
							+ extension;

					String filePath = imgPath + "/" + AVATAR_DIR + imgFilename;

					// write to a new file
					File newFile = new File(filePath);

					if (newFile.exists()) {
						newFile.delete();
					}

					try {
						newFile.createNewFile();
						uploadedFile.writeTo(newFile);
					} catch (Exception e) {
						e.printStackTrace();
					}

					newUser.setImageURL(imgFilename);

				}

				// Para el proceso de registracion
				if (user == null || user.getLevel() != Level.ADMIN) {
					try {
						String secret = userService.generateSecret();
						newUser.setSecret(secret);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					newUser.setIsActive(true);
				}

				Logger logger = Logger.getLogger("booktube");

				/* Insert user */
				userService.insertUser(newUser);
				logger.info("User inserted.");
				logger.info("Username: " + newUser.getUsername());
				logger.info("Firstname: " + newUser.getFirstname());
				logger.info("Lastname: " + newUser.getLastname());
				logger.info("Country: " + newUser.getCountry());
				logger.info("City: " + newUser.getCity());
				logger.info("Gender: " + newUser.getGender());
				logger.info("Birthdate: " + newUser.getBirthdate());

				// Para el proceso de registracion
				if (user == null || user.getLevel() != Level.ADMIN) {
					try {
						userService.sendRegistrationMail(newUser);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// target.add(parent);
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
		String newTitle = "Booktube - "
				+ new ResourceModel("registerPageTitle").getObject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
