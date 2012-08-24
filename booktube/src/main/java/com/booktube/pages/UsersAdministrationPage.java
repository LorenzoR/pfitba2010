package com.booktube.pages;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.effects.sliding.SlideToggle;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.User;
import com.booktube.model.User.Gender;
import com.booktube.model.User.Level;
import com.booktube.pages.customComponents.DynamicLabel;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.service.UserService;

public class UsersAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = 837695410825256207L;

	@SpringBean
	UserService userService;

	private static SuccessDialog<?> successDialog;
	private static Dialog deleteConfirmationDialog;

	private static User deleteUser;
	private static List<User> deleteUsers;

	private DynamicLabel deleteConfirmationLabel = new DynamicLabel(
			"delete_confirmation_dialog_text", new Model<String>());

	private Long searchUserId;
	private String searchUsername;
	private Gender searchGender;
	private Integer searchLowAge;
	private Integer searchHighAge;
	private Date searchLowRegistrationDate;
	private Date searchHighRegistrationDate;
	private String searchCountry;
	private Level searchLevel;

	private final CheckGroup<User> group;

	private final DataView<User> dataView;
	private AjaxPagingNavigator footerNavigator;
	private final WebMarkupContainer searchButton;
	private final Form<User> searchUserForm;

	public UsersAdministrationPage() {
		super();
		final WebMarkupContainer parent = new WebMarkupContainer(
				"usersContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", UsersAdministrationPage.class), new ResourceModel("usersAdministrationPageTitle").getObject());

		dataView = writerList("writerList");
		
		group = new CheckGroup<User>("group", new ArrayList<User>());
		group.add(dataView);
		group.add(new CheckGroupSelector("groupSelector"));

		successDialog = new SuccessDialog<UsersAdministrationPage>(
				"success_dialog", new ResourceModel("userDeleted").getObject(), parent);
		parent.add(successDialog);

		deleteConfirmationDialog = deleteConfirmationDialog();
		parent.add(deleteConfirmationDialog);

		searchUserForm = searchUserForm(parent);

		parent.add(searchUserForm);

		searchUserForm.add(group);

		searchButton = createButtonWithEffect(
				"searchUserLink", "searchFields", new SlideToggle());
		parent.add(searchButton);
		
		parent.add(new BookmarkablePageLink<RegisterPage>("createUser", RegisterPage.class));

		if (dataView.getItemCount() > 0) {
			feedbackMessage.setVisible(false);
		} else {
			feedbackMessage.setVisible(true);
			searchUserForm.setVisible(false);
			footerNavigator.setVisible(false);
			searchButton.setVisible(false);
		}

		//String newTitle = "Booktube - Users Administration";
		//super.get("pageTitle").setDefaultModelObject(newTitle);

	}

	private DataView<User> writerList(String label) {

		IDataProvider<User> dataProvider = new WriterProvider();

		DataView<User> dataView = new DataView<User>(label, dataProvider,
				ITEMS_PER_PAGE) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(Item<User> item) {
				final User user = (User) item.getModelObject();
				final CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
						user);
				item.setDefaultModel(model);
				item.add(new Check<User>("checkbox", item.getModel()));
				final PageParameters parameters = new PageParameters();
				parameters.set("userId", user.getId());
//				item.add(new Label("id"));
				item.add(new Label("username"));
				item.add(new Label("firstname"));
				item.add(new Label("lastname"));
				item.add(new BookmarkablePageLink<Object>("detailsLink",
						ShowUserPage.class, parameters));
				item.add(new Link<User>("editLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					public void onClick() {
						PageParameters pageParameters = new PageParameters();
						pageParameters.set("userId", user.getId());
						setResponsePage(new EditWriterPage(pageParameters,
								UsersAdministrationPage.this));
					}

				});
				item.add(new AjaxLink<User>("deleteLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {

						deleteUser = user;

						deleteConfirmationDialog.open(target);
						
						deleteConfirmationLabel.setLabel(new ResourceModel("deleteUserQuestion").getObject());

						target.add(deleteConfirmationLabel);
					}

				});
			}

		};

		return dataView;
	}

	private Dialog deleteConfirmationDialog() {

		final Dialog dialog = new Dialog("delete_confirmation_dialog");

		dialog.add(deleteConfirmationLabel);

		AjaxDialogButton yesButton = new AjaxDialogButton(new ResourceModel("yes").getObject()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				
				if ( deleteUser != null ) {
					userService.deleteUser(deleteUser);
					deleteUser = null;
					successDialog.setText( new ResourceModel("userDeleted").getObject());
				}
				else if ( deleteUsers != null ) {
					successDialog.setText( new ResourceModel("usersDeleted").getObject());
					userService.deleteUsers(deleteUsers);
					deleteUsers = null;
					
				}
				
				target.add(successDialog);

				successDialog.open(target);

				dialog.close(target);
				
				showOrHideTable();
			}
		};

		DialogButton noButton = new DialogButton(new ResourceModel("no").getObject(),
				JsScope.quickScope(dialog.close().render()));

		dialog.setButtons(noButton, yesButton);

		return dialog;

	}

	private Form<User> searchUserForm(final WebMarkupContainer parent) {

		Form<User> form = new Form<User>("searchUserForm");

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				new User());

		form.setDefaultModel(model);

		footerNavigator = new AjaxPagingNavigator("footerPaginator", dataView) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(parent);
			}
		};
		form.add(footerNavigator);
		
		final WebMarkupContainer searchFields = new WebMarkupContainer(
				"searchFields");
		searchFields.add(AttributeModifier.replace("style", "display: none;"));
		form.add(searchFields);
		
		searchFields.add(feedbackMessage);

		final TextField<String> userId = new TextField<String>("userId",
				new Model<String>(""));
		searchFields.add(userId);

		final TextField<String> username = new TextField<String>("username",
				new Model<String>(""));
		searchFields.add(username);

		final DropDownChoice<Gender> genderDDC = new DropDownChoice<Gender>(
				"gender", Arrays.asList(Gender.values()),
				new EnumChoiceRenderer<Gender>(this));
		searchFields.add(genderDDC);

		final DropDownChoice<Level> levelDDC = new DropDownChoice<Level>(
				"level", Arrays.asList(Level.values()),
				new EnumChoiceRenderer<Level>(this));
		
		searchFields.add(levelDDC);
		
		final DropDownChoice<String> country = new DropDownChoice<String>(
				"country", new Model<String>(), userService.getAllCountries());

		searchFields.add(country);

		final TextField<Integer> lowAge = new TextField<Integer>("lowAge",
				new Model<Integer>(), Integer.class);
		searchFields.add(lowAge);

		final TextField<Integer> highAge = new TextField<Integer>("highAge",
				new Model<Integer>(), Integer.class);
		searchFields.add(highAge);

		final DatePicker<Date> lowRegistrationDate = createDatePicker(
				"lowRegistrationDate", dateFormat);
		searchFields.add(lowRegistrationDate);

		final DatePicker<Date> highRegistrationDate = createDatePicker(
				"highRegistrationDate", dateFormat);
		searchFields.add(highRegistrationDate);

		final AjaxSubmitLink deleteUser = new AjaxSubmitLink("deleteUser") {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				deleteConfirmationDialog.open(target);

				deleteConfirmationLabel
						.setLabel(new ResourceModel("deleteUsersQuestion").getObject());

				target.add(deleteConfirmationLabel);
				
				deleteUsers = (List<User>) group
						.getDefaultModelObject();
				
				showOrHideTable();

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
			}

		};

		form.add(deleteUser);

		searchFields.add(new AjaxSubmitLink("searchUser") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				searchFields.add(AttributeModifier.replace("style",
						"display: block;"));

				try {
					searchUserId = new Long(userId
							.getDefaultModelObjectAsString());
				} catch (NumberFormatException ex) {
					searchUserId = null;
				}

				searchCountry = new String(country
						.getDefaultModelObjectAsString());
				searchUsername = new String(username
						.getDefaultModelObjectAsString());

				searchGender = genderDDC.getModelObject();
				
				searchLevel = levelDDC.getModelObject();
				
				searchHighAge = highAge.getModelObject();
				searchLowAge = lowAge.getModelObject();

				if (!StringUtils.isBlank(lowRegistrationDate
						.getDefaultModelObjectAsString())) {
					try {
						searchLowRegistrationDate = (Date) formatter
								.parse(lowRegistrationDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						searchLowRegistrationDate = null;
					}
				} else {
					searchLowRegistrationDate = null;
				}

				if (!StringUtils.isBlank(highRegistrationDate
						.getDefaultModelObjectAsString())) {
					try {
						searchHighRegistrationDate = (Date) formatter
								.parse(highRegistrationDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						searchHighRegistrationDate = null;
					}
				} else {
					searchHighRegistrationDate = null;
				}

				if (dataView.getItemCount() <= 0) {
					deleteUser.setVisible(false);
					group.setVisible(false);
					footerNavigator.setVisible(false);
					feedbackMessage.setVisible(true);
				} else {
					deleteUser.setVisible(true);
					group.setVisible(true);
					footerNavigator.setVisible(true);
					feedbackMessage.setVisible(false);
				}

				dataView.setCurrentPage(0);
				target.add(parent);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		return form;

	}
	
	private void showOrHideTable() {
		if (dataView.getItemCount() <= 0) {
			searchUserForm.setVisible(false);
			dataView.setVisible(false);
			footerNavigator.setVisible(false);
			searchButton.setVisible(false);
			feedbackMessage.setVisible(true);
		} else {
			searchUserForm.setVisible(true);
			dataView.setVisible(true);
			footerNavigator.setVisible(true);
			searchButton.setVisible(true);
			feedbackMessage.setVisible(false);
		}
	}

	class WriterProvider implements IDataProvider<User> {

		private static final long serialVersionUID = 1L;

		public WriterProvider() {
		}

		public Iterator<User> iterator(int first, int count) {
			// this.users = userService.getAllUsers(first, count);
			// return this.users.iterator();
			return userService.getUsers(first, count, searchUserId,
					searchUsername, searchGender, searchLowAge, searchHighAge,
					searchCountry, searchLowRegistrationDate,
					searchHighRegistrationDate, searchLevel).iterator();
		}

		public int size() {
			return userService.getCount(searchUserId, searchUsername,
					searchGender, searchLowAge, searchHighAge, searchCountry,
					searchLowRegistrationDate, searchHighRegistrationDate, searchLevel);
		}

		public IModel<User> model(User user) {
			return new CompoundPropertyModel<User>(user);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		//String newTitle = "Booktube - Writers";
		//super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
