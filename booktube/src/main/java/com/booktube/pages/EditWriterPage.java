package com.booktube.pages;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.WicketApplication;
import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.pages.validators.BirthdayValidator;
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

	public EditWriterPage(IModel<User> model, final WebPage backPage) {

		final User user = (User) model.getObject();

		if (user == null) {
			setResponsePage(HomePage.class);
			return;
		}

		add(new Label("writerId", user.getId().toString()));

		add(editWriterForm(user));

		dialog = new SuccessDialog<EditWriterPage>("success_dialog",
				"Usuario editado con éxito!", backPage);
		add(dialog);

		// setResponsePage(backPage);
		// goToLastPage();
		String newTitle = "Booktube - Edit " + user.getUsername();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

	// private Dialog successDialog(final WebPage backPage) {
	//
	// Dialog dialog = new Dialog("success_dialog");
	//
	// dialog.add(new Label("text", "Usuario editado con exito!"));
	//
	// AjaxDialogButton ok = new AjaxDialogButton("OK") {
	//
	// private static final long serialVersionUID = 1L;
	//
	// @Override
	// protected void onButtonClicked(AjaxRequestTarget target) {
	// // do your cancel logic here
	// System.out.println("BUTTON CLICKED!!");
	// setResponsePage(backPage);
	//
	// }
	// };
	//
	// dialog.setButtons(ok);
	// dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));
	//
	// return dialog;
	// }

	private Form<User> editWriterForm(final User writer) {
		Form<User> form = new Form<User>("editWriterForm");

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				writer);

		form.setDefaultModel(model);

		final RequiredTextField<User> usernameField = new RequiredTextField<User>("username");
		form.add(usernameField);

		final RequiredTextField<User> firstnameField = new RequiredTextField<User>("firstname");
		form.add(firstnameField);

		final RequiredTextField<User> lastnameField = new RequiredTextField<User>("lastname");
		form.add(lastnameField);

		final DateTextField birthdateField = new DateTextField("birthdate",
				new PropertyModel<Date>(model, "birthdate"),
				new PatternDateConverter(WicketApplication.DATE_FORMAT, true));
		birthdateField.setRequired(true);
		form.add(birthdateField);

		form.add(new Label("date_format", WicketApplication.DATE_FORMAT_ES));

		final DropDownChoice<Level> levelDDC = new DropDownChoice<Level>(
				"level", Arrays.asList(Level.values()),
				new EnumChoiceRenderer<Level>(this));
		form.add(levelDDC);
		
		final DropDownChoice<Gender> genderSelect = new DropDownChoice<Gender>(
				"gender", Arrays.asList(Gender.values()),
				new EnumChoiceRenderer<Gender>(this));
		form.add(genderSelect);
		
		List<String> countryList = userService.getAllCountries();
		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
				"country", countryList);
		countrySelect.setRequired(true);
		form.add(countrySelect);
		
		final TextField<User> cityField = new TextField<User>("city");
		form.add(cityField);
		
		UniqueUsernameValidator usernameValidator = new UniqueUsernameValidator();
		usernameField.add(usernameValidator);
		
		BirthdayValidator birthdayValidator = new BirthdayValidator();
		birthdateField.add(birthdayValidator);
		
		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				userService.updateUser(writer);

				System.out.println("user editado");

				dialog.open(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}
		});

		return form;
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
