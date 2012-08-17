package com.booktube.pages;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.model.User;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.pages.utilities.RandomPassword;
import com.booktube.pages.validators.NonExistingEmailValidator;
import com.booktube.pages.validators.NonExistingUsernameValidator;
import com.booktube.pages.validators.UserEmailValidator;
import com.booktube.service.UserService;

public class ForgotPasswordPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	UserService userService;

	private static Dialog dialog;
	

	
	public ForgotPasswordPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("forgot");
		parent.setOutputMarkupId(true);
		add(parent);

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		parent.add(feedback);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", RegisterPage.class), new ResourceModel("forgotPasswordPageTitle").getObject());

		parent.add(forgotPasswordForm(parent, feedback));

		dialog = new SuccessDialog<HomePage>("success_dialog",
				new ResourceModel("forgotPasswordEmailSent").getObject(), HomePage.class, null);

			
		parent.add(dialog);
		
		String indications = "Para recibir los datos de su cuenta, complete el formulario.";
		parent.add(new Label("indications",indications));

	
	}

	private Form<User> forgotPasswordForm(final WebMarkupContainer parent,
			final FeedbackPanel feedback) {

		Form<User> form = new Form<User>("form");
		form.setMultiPart(true);

		final User newUser = new User();

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				newUser);

		form.setDefaultModel(model);

		
		final RequiredTextField<User> usernameField = new RequiredTextField<User>("username");
		usernameField.add(new NonExistingUsernameValidator());
		
		
		final RequiredTextField<User> email = new RequiredTextField<User>("email");
		email.add(EmailAddressValidator.getInstance());
		email.add(new NonExistingEmailValidator());
		usernameField.add( new UserEmailValidator( email ) );
		
		form.add(usernameField);
		form.add(email);		
		
		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {				
				String username = usernameField.getDefaultModelObjectAsString();
				User user = userService.getUser(username);
				String newPassword = RandomPassword.getRandomString(12);				
				try {
						userService.changeUserPassword(user, newPassword);
						userService.sendAccountInformationMail(user, newPassword);		
				} catch (Exception e) {					
					e.printStackTrace();
				}
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
		 String newTitle = "Booktube - " + new ResourceModel("forgotPasswordPageTitle").getObject();
		 super.get("pageTitle").setDefaultModelObject(newTitle);
	}
	
	

}
