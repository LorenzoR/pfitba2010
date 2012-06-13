package com.booktube.pages;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.Campaign;
import com.booktube.model.Message;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.model.Message.Type;
import com.booktube.model.User.Gender;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class NewCampaign extends BasePage {

	@SpringBean
	MessageService messageService;

	@SpringBean
	UserService userService;

	User user;

	public NewCampaign() {

		// User user = new User("username", "firstname", "lastname");
		// userService.insertUser(user);
		// WicketApplication.instance().getUserService().insertUser(user);
		// List<User> users = WicketApplication.instance().getUserService()
		// List<User> users = userService.getAllUsers();
		final WebMarkupContainer parent = new WebMarkupContainer("newCampaign");
		parent.setOutputMarkupId(true);
		add(parent);

		user = WiaSession.get().getLoggedInUser();

		Form<?> form = newContactForm(parent);
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

	private Form<?> newContactForm(final WebMarkupContainer parent) {
		Form<?> form = new Form<Object>("form");

		List<String> genders = Arrays.asList(new String[] { "Todos",
				"Masculino", "Femenino" });

		final DropDownChoice<String> genderSelect = new DropDownChoice<String>(
				"gender", new PropertyModel<String>(this, ""), genders);

		form.add(genderSelect);

		final TextField<String> lowAgeField = new TextField<String>("lowAge",
				new Model<String>(""));

		form.add(lowAgeField);

		final TextField<String> highAgeField = new TextField<String>("highAge",
				new Model<String>(""));

		form.add(highAgeField);

		final TextField<String> lowRegistrationDateField = new TextField<String>(
				"lowRegistrationDate", new Model<String>(""));

		form.add(lowRegistrationDateField);

		final TextField<String> highRegistrationDateField = new TextField<String>(
				"highRegistrationDate", new Model<String>(""));

		form.add(highRegistrationDateField);

		List<String> countryList = Arrays.asList(new String[] { "Country 1",
				"...", "Country 2" });

		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
				"country", new PropertyModel<String>(this, ""), countryList);

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
		final CheckGroup group = new CheckGroup("group", new ArrayList());

		add(form);
		form.add(group);
		group.add(new CheckGroupSelector("groupselector"));
		ListView persons = new ListView("persons", personsList) {

			protected void populateItem(ListItem item) {
				item.add(new Check("checkbox", item.getModel()));
				item.add(new Label("name", new PropertyModel(item.getModel(),
						"firstname")));
				item.add(new Label("lastName", new PropertyModel(item
						.getModel(), "lastname")));
			}

		};

		group.add(persons);

		form.add(text);
		form.add(new AjaxSubmitLink("save") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				Campaign campaign = new Campaign(subject
						.getDefaultModelObjectAsString(), text
						.getDefaultModelObjectAsString(), user);

				String country = countrySelect.getDefaultModelObjectAsString();

				String genderString = genderSelect
						.getDefaultModelObjectAsString();

				Gender gender;

				if (genderString.equals("Masculino")) {
					gender = Gender.MALE;
				} else if (genderString.equals("Femenino")) {
					gender = Gender.FEMALE;
				} else {
					gender = null;
				}

				Integer lowAge;
				
				if ( lowAgeField != null ) {
					lowAge = Integer.valueOf(lowAgeField
						.getDefaultModelObjectAsString());
				}
				else {
					lowAge = null;
				}
				
				Integer highAge;
				
				highAge = Integer.valueOf(highAgeField
						.getDefaultModelObjectAsString());
				
				Date lowDate = Date.valueOf(lowRegistrationDateField.getDefaultModelObjectAsString());
				Date highDate = Date.valueOf(highRegistrationDateField.getDefaultModelObjectAsString());
				
				System.out.println("HIGHDATESTRING: " + highRegistrationDateField.getDefaultModelObjectAsString());
				System.out.println("HIDHDATE: " + highDate);
				
				List<User> receivers = userService.getUsers(0,
						Integer.MAX_VALUE, gender, lowAge, highAge, country, lowDate, highDate);

				//campaignService.sendCampaign(campaign, receivers);

				//campaignService.insertCampaign(campaign);
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
