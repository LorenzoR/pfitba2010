package com.booktube.pages;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import com.booktube.WiaSession;
import com.booktube.model.Campaign;
import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.pages.customComponents.AbstractAutoCompleteTextField;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class NewCampaignPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	MessageService messageService;

	@SpringBean
	UserService userService;

	private User user;

	private final SuccessDialog<?> dialog;

	final PageParameters parameters;

	private final List<String> filters = new ArrayList<String>();

	private final List<List<User>> receiverList = new ArrayList<List<User>>();

	private final Set<User> allReceiverSet = new HashSet<User>();

	private final IModel<List<User>> receiverListModel = new IModel<List<User>>() {

		private static final long serialVersionUID = 1L;

		public void detach() {
		}

		public List<User> getObject() {
			ArrayList<User> users = new ArrayList<User>(allReceiverSet);

			Collections.sort(users, new Comparator<User>() {
				public int compare(User o1, User o2) {
					return o1.getUsername().compareToIgnoreCase(
							o2.getUsername());
				}
			});

			return users;
		}

		public void setObject(List<User> object) {
		}
	};

	public NewCampaignPage() {

		// filters.add("Filtro 1");
		// filters.add("Filtro 2");
		// filters.add("Filtro 3");

		// User user = new User("username", "firstname", "lastname");
		// userService.insertUser(user);
		// WicketApplication.instance().getUserService().insertUser(user);
		// List<User> users = WicketApplication.instance().getUserService()
		// List<User> users = userService.getAllUsers();
		final WebMarkupContainer parent = new WebMarkupContainer("newCampaign");
		parent.setOutputMarkupId(true);
		add(parent);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", NewCampaignPage.class), new ResourceModel("newCampaignPageTitle").getObject());
		
		user = WiaSession.get().getLoggedInUser();

		Form<?> form = newContactForm();
		parent.add(form);

		Label registerMessage = new Label("registerMessage", new ResourceModel(
				"registerMessage"));
		parent.add(registerMessage);

		parameters = new PageParameters();
		// parameters.set("campaignId", campaign.getId());

		dialog = new SuccessDialog<ShowCampaignPage>("success_dialog",
				new ResourceModel("newCampaignDialog").getObject(), ShowCampaignPage.class,
				parameters);
		parent.add(dialog);

		if (user == null) {
			form.setVisible(false);
		} else {
			registerMessage.setVisible(false);
		}

	}

	private Form<?> newContactForm() {
		final Form<User> form = new Form<User>("form");

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				new User());

		form.setDefaultModel(model);

		final DropDownChoice<Gender> genderSelect = new DropDownChoice<Gender>(
				"gender", Arrays.asList(Gender.values()),
				new EnumChoiceRenderer<Gender>(this));
		form.add(genderSelect);

		final TextField<Integer> lowAgeField = new TextField<Integer>("lowAge",
				new Model<Integer>(), Integer.class);

		form.add(lowAgeField);

		final TextField<Integer> highAgeField = new TextField<Integer>(
				"highAge", new Model<Integer>(), Integer.class);

		form.add(highAgeField);

		final DatePicker<Date> lowRegistrationDateField = new DatePicker<Date>(
				"lowRegistrationDate", new Model<Date>(), Date.class);
		form.add(lowRegistrationDateField);

		final DatePicker<Date> highRegistrationDateField = new DatePicker<Date>(
				"highRegistrationDate", new Model<Date>(), Date.class);
		form.add(highRegistrationDateField);

		List<String> countryList = userService.getAllCountries();

		// final DropDownChoice<String> countrySelect = new
		// DropDownChoice<String>(
		// "country", new PropertyModel<String>(this, ""), countryList);
		//
		// form.add(countrySelect);
		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
				"country", countryList);
		form.add(countrySelect);

		final WebMarkupContainer newCampaignContainer = new WebMarkupContainer(
				"newCampaignContainer");
		newCampaignContainer.setVisible(false);

		final TextField<String> subject = new TextField<String>("subject",
				new Model<String>(""));

		newCampaignContainer.add(subject);

		final TextArea<String> text = new TextArea<String>("campaignText",
				new Model<String>());
		text.setOutputMarkupId(true);
		newCampaignContainer.add(text);

		final ListView<String> filtersListView = new ListView<String>(
				"filters", filters) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(final ListItem<String> item) {
				item.add(new Label("filter", item.getModelObject()));

				WebMarkupContainer td = new WebMarkupContainer("removeFilter");

				td.add(new AjaxEventBehavior("onclick") {

					private static final long serialVersionUID = 6720512493017210281L;

					@Override
					protected void onEvent(AjaxRequestTarget target) {
						int index = filters.indexOf(item.getModelObject());
						allReceiverSet.removeAll(receiverList.get(index));
						receiverList.remove(index);
						filters.remove(item.getModelObject());

						if (filters.size() == 0) {
							newCampaignContainer.setVisible(false);
						}

						if ( receiverList.size() > 0 ) {
							for (List<User> aList : receiverList) {
								allReceiverSet.addAll(aList);
							}
						}

						target.add(form);
					}

				});

				item.add(td);
			}
		};
		form.add(filtersListView);

		add(form);

		ListView<User> receiverListView = new ListView<User>("receiverList",
				receiverListModel) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(final ListItem<User> item) {

				item.setDefaultModel(new CompoundPropertyModel<User>(
						(User) item.getModelObject()));

				item.add(new Label("username"));

				WebMarkupContainer td = new WebMarkupContainer("removeReceiver");

				td.add(new AjaxEventBehavior("onclick") {

					private static final long serialVersionUID = 6720512493017210281L;

					@Override
					protected void onEvent(AjaxRequestTarget target) {
						allReceiverSet.remove(item.getModelObject());

						if (allReceiverSet.size() == 0) {
							newCampaignContainer.setVisible(false);
						}

						target.add(form);
					}

				});

				item.add(td);

			}

		};

		form.add(receiverListView);

		// form.add(text);

		form.add(new AjaxSubmitLink("addFilter") {

			private static final long serialVersionUID = 1L;
			private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				String newFilter = "";
				
				String country = countrySelect.getConvertedInput();

				if (StringUtils.isNotBlank(country)
						&& !filters.contains(country)) {
					newFilter += country + " / ";
//					filters.add(country);
//					receiverList.add(filters.indexOf(country), userService
//							.getUsers(0, Integer.MAX_VALUE, null, null, null,
//									null, null, country, null, null));
				}

				Gender gender = genderSelect.getConvertedInput();

				if (gender != null
						&& !filters.contains(new ResourceModel("Gender."
								+ gender.toString()).getObject())) {
					newFilter += new ResourceModel("Gender."
							+ gender.toString()).getObject() + " / ";
//					filters.add(genderString);
//					receiverList.add(filters.indexOf(genderString), userService
//							.getUsers(0, Integer.MAX_VALUE, null, null, gender,
//									null, null, null, null, null));
				}

				Integer lowAge = lowAgeField.getConvertedInput();
				Integer highAge = highAgeField.getConvertedInput();

				if ((lowAge != null || highAge != null)
						&& !filters.contains(lowAge + " - " + highAge)) {
					newFilter += lowAge + " - " + highAge + " / ";
					// newFilter.replace("\0", "");
//					filters.add(newFilter);
//
//					receiverList.add(filters.indexOf(newFilter), userService
//							.getUsers(0, Integer.MAX_VALUE, null, null, null,
//									lowAge, highAge, null, null, null));

				}

				Date lowDate = lowRegistrationDateField.getConvertedInput();
				Date highDate = highRegistrationDateField.getConvertedInput();
				
				if ((lowDate != null || highDate != null)
						&& !filters.contains(lowDate + " - " + highDate)) {
					newFilter += dateFormat.format(lowDate) + " - " + dateFormat.format(highDate) + " / ";
					// newFilter.replace("\0", "");
//					filters.add(newFilter);
//
//					receiverList.add(filters.indexOf(newFilter), userService
//							.getUsers(0, Integer.MAX_VALUE, null, null, null,
//									null, null, null, lowDate, highDate));
				}
			
				if ( newFilter.equals("") ) {
					newFilter = new ResourceModel("all").getObject();
				}
				
				List<User> newReceivers = userService
						.getUsers(0, Integer.MAX_VALUE, null, null, gender,
								lowAge, highAge, country, lowDate, highDate, Level.USER);
				
				if ( !filters.contains(newFilter) ) {
					filters.add(newFilter);
					
					receiverList.add(filters.indexOf(newFilter), newReceivers);

				}
				else {				
					int index = filters.indexOf(newFilter);
					List<User> oldReceivers = receiverList.get(index);
					
					if ( newReceivers.size() != oldReceivers.size() ) {
						receiverList.set(index, newReceivers);
					}
				}
				
				for (List<User> aList : receiverList) {
					allReceiverSet.addAll(aList);
				}

				if ( filters.size() > 0 ) {
					newCampaignContainer.setVisible(true);
				}

				target.add(form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
			}

		});

		final AbstractAutoCompleteTextField<User> autocompleteUser = new AbstractAutoCompleteTextField<User>(
				"addReceiver", new Model<User>(new User())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<User> getChoiceList(String searchTextInput) {

				List<User> choices = new ArrayList<User>(10);

				for (final User aUser : userService.getAllUsers(0,
						Integer.MAX_VALUE)) {
					final String username = aUser.getUsername();

					if (username.toUpperCase().startsWith(
							searchTextInput.toUpperCase())) {
						choices.add(aUser);
						if (choices.size() == 10) {
							break;
						}
					}
				}

				return choices;

			}

			@Override
			protected String getChoiceValue(User choice) throws Throwable {
				// TODO Auto-generated method stub
				return choice.getUsername();
			}

			// @Override
			// protected Iterator<User> getChoices(String searchTextInput) {
			// if (Strings.isEmpty(searchTextInput)) {
			// List<User> emptyList = Collections.emptyList();
			// return emptyList.iterator();
			// }
			//
			// List<User> choices = new ArrayList<User>(10);
			// List<User> allUsers = userService.getAllUsers(0,
			// Integer.MAX_VALUE);
			//
			//
			// for (final User aUser : getChoiceList(null)) {
			// final String username = aUser.getUsername();
			//
			// if
			// (username.toUpperCase().startsWith(searchTextInput.toUpperCase()))
			// {
			// choices.add(aUser);
			// if (choices.size() == 10) {
			// break;
			// }
			// }
			// }
			//
			// return choices.iterator();
			// }

		};

		form.add(autocompleteUser);

		form.add(new AjaxSubmitLink("addReceiverButton") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (autocompleteUser.findChoice() != null) {
					allReceiverSet.add(autocompleteUser.findChoice());
					newCampaignContainer.setVisible(true);
					target.add(form);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		newCampaignContainer.add(new AjaxSubmitLink("sendCampaign") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				Campaign campaign = new Campaign(subject
						.getDefaultModelObjectAsString(), text
						.getDefaultModelObjectAsString(), user);

				dialog.setRedirect(true);
				campaignService.sendCampaign(campaign, new ArrayList<User>(
						allReceiverSet));

				campaignService.insertCampaign(campaign);

				parameters.set("campaignId", campaign.getId());

				dialog.setText("Campaña enviada con éxito!");
				target.add(dialog);

				dialog.open(target);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
			}
		});

		form.add(newCampaignContainer);

		return form;
	}

	@Override
	protected void setPageTitle() {
		String newTitle = "Booktube - " + new ResourceModel("newCampaignPageTitle").getObject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
}
