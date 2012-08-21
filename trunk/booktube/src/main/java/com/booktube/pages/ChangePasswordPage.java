package com.booktube.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.WiaSession;
import com.booktube.model.User;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.pages.validators.SamePasswordValidator;


import com.booktube.service.UserService;

public class ChangePasswordPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	UserService userService;

	private static Dialog dialog;
	private User user;
	

	
	public ChangePasswordPage(IModel<User> model, final WebPage backPage) {

		user = (User) model.getObject();

		if (user == null) {
			setResponsePage(HomePage.class);
			return;
		}
		Logger.getLogger("ChangePasswordPage").info("Usuario a modificar:"+user.getUsername());

		final WebMarkupContainer parent = new WebMarkupContainer("changePassword");
		parent.setOutputMarkupId(true);
		add(parent);

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		parent.add(feedback);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", RegisterPage.class), new ResourceModel("changePasswordPageTitle").getObject());

		parent.add(changePasswordForm(user, feedback));

		dialog = new SuccessDialog<HomePage>("success_dialog",
				new ResourceModel("changePasswordDone").getObject(), HomePage.class, null);

			
		parent.add(dialog);
		
		String indications = "Ingrese la nueva contraseña, y repítala para su seguridad.";
		parent.add(new Label("indications",indications));

	
	}

	private Form<User> changePasswordForm(final User writer,
			final FeedbackPanel feedback) {

		Form<User> form = new Form<User>("form");
		form.setMultiPart(true);

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(writer);
		form.setDefaultModel(model);
		
		final PasswordTextField passwordField1 = new PasswordTextField(
				"password1", new PropertyModel<String>(model, "password"));
		final PasswordTextField passwordField2 = new PasswordTextField(
				"password2", new Model<String>(""));

		
		passwordField1.add(new SamePasswordValidator(passwordField2));
		
		form.add(passwordField1);
		form.add(passwordField2);
		
		
		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {		
//								
				userService.updateUser(writer);
				
				Logger.getLogger("ChangePasswordPage.onSubmit()").info("Usuario "+writer.getUsername()+" ha cambiado su contraseña." );

				target.add(welcomeLabel);
				
				if( WiaSession.get().getLoggedInUser().getUsername().compareToIgnoreCase(writer.getUsername()) == 0 )
					WiaSession.get().logInUser(writer);
				
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
