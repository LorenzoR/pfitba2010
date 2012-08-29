package com.booktube.pages;

import java.io.File;

import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.WiaSession;
import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.pages.validators.BirthdayValidator;
import com.booktube.pages.validators.UniqueEmailValidator;
import com.booktube.pages.validators.UniqueUsernameValidator;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class EditWriterPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	private static Dialog dialog;
	
	private final String DEFAULT_AVATAR = "defaultAvatar.png";
	private final String IMG_DIR = "img/";
	private final String AVATAR_DIR = "avatar/";
	private final String IMG_HEIGHT = "116px";
	private final String IMG_WIDTH = "116px";
	private final String IMG_DIR_NAME = "img";
	
	@SuppressWarnings("unused")
	public EditWriterPage(PageParameters pageParameters, final WebPage backPage) {

		if ( pageParameters.get("userId") == null ) {
			throw new AbortWithHttpErrorCodeException(404);
		}
		
		Long userId = pageParameters.get("userId").toLong();
		
		final User user = userService.getUser(userId);
		Logger.getLogger("EditWriterPage").info("Usuario a modificar:"+user.getUsername());

		
		if (user == null) {
			setResponsePage(HomePage.class);
			return;
		}

		addBreadcrumb(new BookmarkablePageLink<Object>("link", EditWriterPage.class, pageParameters), new ResourceModel("edit").getObject() + " " + user.getUsername() );
		
		add(new Label("username", user.getUsername()));

		dialog = new SuccessDialog<EditWriterPage>("success_dialog",
				new ResourceModel("editedUserDialog").getObject(), backPage);
		add(dialog);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		
		add(editWriterForm(user, feedback));

		// setResponsePage(backPage);
		// goToLastPage();
		String newTitle = "Booktube - " + new ResourceModel("edit").getObject() + " " + user.getUsername();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
	
	public EditWriterPage(PageParameters pageParameters) {
		this(pageParameters, new WritersPage(null));
	}

	private Form<User> editWriterForm(final User writer, final WebMarkupContainer feedback) {
		Form<User> form = new Form<User>("editWriterForm");

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				writer);

		form.setDefaultModel(model);

		final RequiredTextField<User> usernameField = new RequiredTextField<User>(
				"username");
		form.add(usernameField);

		final RequiredTextField<User> firstnameField = new RequiredTextField<User>(
				"firstname");
		form.add(firstnameField);

		final RequiredTextField<User> lastnameField = new RequiredTextField<User>(
				"lastname");
		form.add(lastnameField);

		final RequiredTextField<User> email = new RequiredTextField<User>(
				"email");
		email.add(EmailAddressValidator.getInstance());
		form.add(email);

		final DateTextField birthdateField = new DateTextField("birthdate",
				new PropertyModel<Date>(model, "birthdate"),
				new PatternDateConverter(new ResourceModel("dateFormat").getObject(), true));
		birthdateField.setRequired(true);
		form.add(birthdateField);

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

		final DropDownChoice<Gender> genderSelect = new DropDownChoice<Gender>(
				"gender", Arrays.asList(Gender.values()),
				new EnumChoiceRenderer<Gender>(this));
		form.add(genderSelect);

		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
				"country", getCountries());
		countrySelect.setRequired(true);
		form.add(countrySelect);

		final TextField<User> cityField = new TextField<User>("city");
		form.add(cityField);

		usernameField.add(new UniqueUsernameValidator(writer.getUsername()));
		email.add(new UniqueEmailValidator(writer.getEmail()));
		birthdateField.add(new BirthdayValidator());

		Image avatar = new Image("avatar", new Model<String>());
		if ( writer.getImageURL() == null ) {
			avatar.add(new AttributeModifier("src", new Model<String>(IMG_DIR + DEFAULT_AVATAR)));
		}
		else {
			avatar.add(new AttributeModifier("src", new Model<String>(IMG_DIR + AVATAR_DIR + writer.getImageURL())));
		}
		avatar.add(new AttributeModifier("width", new Model<String>(IMG_WIDTH)));
		avatar.add(new AttributeModifier("height", new Model<String>(IMG_HEIGHT)));
		form.add(avatar);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final FileUploadField fileUploadField = new FileUploadField(
				"imgUpload", (IModel) new Model<FileUpload>());
		form.add(fileUploadField);

//		final BookmarkablePageLink<String> changePasswordLink = new BookmarkablePageLink<String>("changePasswordLink", ChangePasswordPage.class);
		
		Link<User> changePasswordLink = new Link<User>("changePasswordLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
//				final CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
//						WiaSession.get().getLoggedInUser());
				final CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(writer);
				setResponsePage(new ChangePasswordPage(model,
						EditWriterPage.this));
			}

		};
		form.add( changePasswordLink);
		
		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				final FileUpload uploadedFile = fileUploadField.getFileUpload();
				
				if (uploadedFile != null) {

					// ServletContext context = ((WebApplication)
					// getApplication()).getServletContext();
					String imgPath = ((WebApplication) getApplication())
							.getServletContext().getRealPath(IMG_DIR_NAME);

					final String extension = uploadedFile.getClientFileName()
							.substring(
									uploadedFile.getClientFileName()
											.lastIndexOf('.') + 1);

					final String imgFilename = writer.getUsername() + '.'
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

					writer.setImageURL(imgFilename);
				}
				else {
					// TODO
				}

				userService.updateUser(writer);
				
				target.add(welcomeLabel);
				
				dialog.open(target);
			}
			

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				target.add(feedback);
			}
		});

		return form;
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
