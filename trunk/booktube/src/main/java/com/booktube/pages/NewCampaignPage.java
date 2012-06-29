	package com.booktube.pages;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import com.booktube.WiaSession;
import com.booktube.model.Campaign;
import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class NewCampaignPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	MessageService messageService;

	@SpringBean
	UserService userService;

	User user;

	public NewCampaignPage() {

		// User user = new User("username", "firstname", "lastname");
		// userService.insertUser(user);
		// WicketApplication.instance().getUserService().insertUser(user);
		// List<User> users = WicketApplication.instance().getUserService()
		// List<User> users = userService.getAllUsers();
		final WebMarkupContainer parent = new WebMarkupContainer("newCampaign");
		parent.setOutputMarkupId(true);
		add(parent);

		user = WiaSession.get().getLoggedInUser();

		Form<?> form = newContactForm();
		parent.add(form);

		Label registerMessage = new Label("registerMessage",
				"Debe registrarse para poder contactarnos.");
		parent.add(registerMessage);

		if (user == null) {
			form.setVisible(false);
		} else {
			registerMessage.setVisible(false);
		}

		System.out.println("MSG FROM: "
				+ messageService.countMessagesFrom(user));
		System.out.println("MSG TO: " + messageService.countMessagesTo(user));
		System.out.println("UNREAD MSG TO: "
				+ messageService.countUnreadMessagesTo(user));

	}

	private Form<?> newContactForm() {
		Form<?> form = new Form<Object>("form");

//		List<String> genders = Arrays.asList(new String[] { "Todos",
//				"Masculino", "Femenino" });

//		final DropDownChoice<String> genderSelect = new DropDownChoice<String>(
//				"gender", new PropertyModel<String>(this, ""), genders);
//
//		form.add(genderSelect);

		final DropDownChoice<Gender> genderSelect = new DropDownChoice<Gender>(
				"gender", Arrays.asList(Gender.values()),
				new EnumChoiceRenderer<Gender>(this));
		form.add(genderSelect);
		
		final TextField<String> lowAgeField = new TextField<String>("lowAge",
				new Model<String>(""));

		form.add(lowAgeField);

		final TextField<String> highAgeField = new TextField<String>("highAge",
				new Model<String>(""));

		form.add(highAgeField);

		final DatePicker<Date> lowRegistrationDateField = new DatePicker<Date>(
				"lowRegistrationDate", new Model<Date>(), Date.class);
		form.add(lowRegistrationDateField);

		final DatePicker<Date> highRegistrationDateField = new DatePicker<Date>(
				"highRegistrationDate", new Model<Date>(), Date.class);
		form.add(highRegistrationDateField);

		List<String> countryList = userService.getAllCountries();

//		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
//				"country", new PropertyModel<String>(this, ""), countryList);
//
//		form.add(countrySelect);
		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
				"country", countryList);
		form.add(countrySelect);

		final TextField<String> subject = new TextField<String>("subject",
				new Model<String>(""));

		form.add(subject);

		final TextArea<String> text = new TextArea<String>("textArea",
				new Model<String>());
		text.setOutputMarkupId(true);

		List<User> personsList = userService.getAllUsers(0, Integer.MAX_VALUE);

		/* Saco al admin actual de la lista */
		if (user != null) {
			int currentUserIndex = 0;

			for (User aUser : personsList) {
				System.out.println("USERID: " + user.getId() + " aUSERID: "
						+ aUser.getId());
				if (user.getId().equals(aUser.getId())) {
					break;
				} else {
					currentUserIndex++;
				}
			}

			personsList.remove(currentUserIndex);
		}
		final CheckGroup<Campaign> group = new CheckGroup<Campaign>("group", new ArrayList<Campaign>());

		add(form);
		form.add(group);
		group.add(new CheckGroupSelector("groupselector"));
		ListView<User> persons = new ListView<User>("persons", personsList) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem<User> item) {

				item.setDefaultModel(new CompoundPropertyModel<User>(
						(User) item.getModelObject()));
				
				item.add(new Check<User>("checkbox", item.getModel()));
				item.add(new Label("firstname"));
				item.add(new Label("lastname"));
			}

		};

		group.add(persons);

		form.add(text);
		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				Campaign campaign = new Campaign(subject
						.getDefaultModelObjectAsString(), text
						.getDefaultModelObjectAsString(), user);

				String country;

				if (!countrySelect.getDefaultModelObjectAsString().isEmpty()) {
					country = countrySelect.getDefaultModelObjectAsString();
				} else {
					country = null;
				}

				String genderString = genderSelect
						.getDefaultModelObjectAsString();

				// if (!genderSelect.getDefaultModelObjectAsString().isEmpty())
				// {
				// genderString = genderSelect.getDefaultModelObjectAsString();
				// }
				// else {
				// genderString = null;
				// }

				Gender gender;

				if (genderString.equals("Masculino")) {
					gender = Gender.MALE;
				} else if (genderString.equals("Femenino")) {
					gender = Gender.FEMALE;
				} else {
					gender = null;
				}

				Integer lowAge;

				try {
					lowAge = Integer.valueOf(lowAgeField
							.getDefaultModelObjectAsString());
				} catch (NumberFormatException nfe) {
					lowAge = null;
				}

				Integer highAge;

				try {
					highAge = Integer.valueOf(highAgeField
							.getDefaultModelObjectAsString());
				} catch (NumberFormatException nfe) {
					highAge = null;
				}

				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

				Date lowDate = null;

				if (!lowRegistrationDateField.getDefaultModelObjectAsString()
						.isEmpty()) {
					try {
						lowDate = (Date) formatter
								.parse(lowRegistrationDateField
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						lowDate = null;
					}
				} else {
					lowDate = null;
				}

				Date highDate = null;

				if (!lowRegistrationDateField.getDefaultModelObjectAsString()
						.isEmpty()) {
					try {
						highDate = (Date) formatter
								.parse(highRegistrationDateField
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						highDate = null;
					}
				} else {
					highDate = null;
				}

				// Component datePicker = getForm().get( "lowRegistrationDate"
				// );
				// System.out.println("DATE PICKER: " + datePicker);

				System.out.println("HIGHDATESTRING: "
						+ highRegistrationDateField
								.getDefaultModelObjectAsString());
				System.out.println("HIDHDATE: " + highDate);

				System.out.println("LOW DATE STRING: "
						+ lowRegistrationDateField
								.getDefaultModelObjectAsString());
				System.out.println("LOWDATE: " + lowDate);

				System.out.println("LowAge: " + lowAge);
				System.out.println("HighAge: " + highAge);

				List<User> receivers = userService.getUsers(0,
						Integer.MAX_VALUE, null, null, gender, lowAge, highAge, country,
						lowDate, highDate);

				System.out.println("RECEIVERS: ");
				System.out.println(receivers.toString());

				campaignService.sendCampaign(campaign, receivers);

				campaignService.insertCampaign(campaign);
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
		String newTitle = "Booktube - New Contact";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
}