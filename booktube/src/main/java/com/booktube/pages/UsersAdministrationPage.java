package com.booktube.pages;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
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
import com.booktube.pages.customComponents.DynamicLabel;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.service.UserService;

public class UsersAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = 837695410825256207L;

	@SpringBean
	UserService userService;

	private static SuccessDialog<?> successDialog;
	private static Dialog deleteConfirmationDialog;

	private static Long userId;

	private static User deleteUser;

	private static String deleteUsername;

	// private Label deleteConfirmationLabel = new Label(
	// "delete_confirmation_dialog_text", new PropertyModel<String>(this,
	// "deleteConfirmationText")) {
	// private static final long serialVersionUID = 1L;
	//
	// {
	// setOutputMarkupId(true);
	// }
	// };

	// Model<String> deleteConfirmationModel = new Model<String>() {
	//
	// private static final long serialVersionUID = 1L;
	//
	// private String text;
	//
	// public String getObject() {
	// return text;
	// }
	//
	// public void setObject(String value) {
	// this.text = value;
	// }
	// };

	private DynamicLabel deleteConfirmationLabel = new DynamicLabel(
			"delete_confirmation_dialog_text", new Model<String>());

	// Model<String> successDialogModel = new Model<String>() {
	//
	// private static final long serialVersionUID = 1L;
	//
	// private String text;
	//
	// public String getObject() {
	// return text;
	// }
	//
	// public void setObject(String value) {
	// this.text = value;
	// }
	// };

	// private DynamicLabel successDialogLabel = new
	// DynamicLabel("success_dialog_text");

	// private String deleteConfirmationText;
	//
	// private Label successDialogLabel = new Label("success_dialog_text",
	// new PropertyModel<String>(this, "successDialogText")) {
	// private static final long serialVersionUID = 1L;
	//
	// {
	// setOutputMarkupId(true);
	// }
	// };
	//
	// private String successDialogText;

	private Long searchUserId;
	private String searchUsername;
	private Gender searchGender;
	private Integer searchLowAge;
	private Integer searchHighAge;
	private Date searchLowRegistrationDate;
	private Date searchHighRegistrationDate;
	private String searchCountry;

	private final CheckGroup<User> group;

	private final DataView<User> dataView;
	private final PagingNavigator footerNavigator;
	private final WebMarkupContainer searchButton;
	private final Form<User> searchUserForm;

	final LoadableDetachableModel<List<User>> resultsModel = new LoadableDetachableModel<List<User>>() {
		private static final long serialVersionUID = 1L;

		protected List<User> load() {
			return null;
		}
	};

	public UsersAdministrationPage() {
		super();
		final WebMarkupContainer parent = new WebMarkupContainer(
				"usersContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		setBreadcrumbs("Administración > Usuarios");
		
		// deleteConfirmationLabel.setOutputMarkupId(true);
		// successDialogLabel.setOutputMarkupId(true);

//		parent.add(new Label("pageTitle", "Users Administration Page"));

		dataView = writerList("writerList");
		
		group = new CheckGroup<User>("group", new ArrayList<User>());
		group.add(dataView);
		group.add(new CheckGroupSelector("groupSelector"));

		footerNavigator = new PagingNavigator("footerPaginator", dataView);
		parent.add(footerNavigator);

		successDialog = new SuccessDialog<UsersAdministrationPage>(
				"success_dialog", "Usuario eliminado.", parent);
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
		
//		parent.add(feedbackMessage);

		String newTitle = "Booktube - Users Administration";
		super.get("pageTitle").setDefaultModelObject(newTitle);

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
						setResponsePage(new EditWriterPage(model,
								UsersAdministrationPage.this));
					}

				});
				item.add(new AjaxLink<User>("deleteLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {

						// userId = user.getId();
						deleteUser = user;
						System.out.println("USERIDDDD " + userId);
						System.out.println("USER ES : " + deleteUser);

						deleteConfirmationDialog.open(target);

						deleteUsername = deleteUser.getUsername();

						deleteConfirmationLabel
								.setLabel("Esta seguro que desea eliminar el usuario "
										+ deleteUsername + " ?");

						target.add(deleteConfirmationLabel);
					}

				});
				// item.add(new Link<User>("deleteLink", item.getModel()) {
				// private static final long serialVersionUID =
				// -7155146615720218460L;
				//
				// public void onClick() {
				//
				// User user = (User) getModelObject();
				// Long userId = user.getId();
				//
				// userService.deleteUser(user);
				// System.out.println("User " + userId + " deleted.");
				//
				// dialog.open(target);
				//
				// //setResponsePage(UsersAdministrationPage.this);
				// }
				//
				// });
			}

		};

		return dataView;
	}

	// private Dialog deleteDialog(final WebMarkupContainer parent) {
	//
	// final Dialog dialog = new Dialog("success_dialog");
	//
	// dialog.add(successDialogLabel);
	//
	// AjaxDialogButton ok = new AjaxDialogButton("OK") {
	//
	// private static final long serialVersionUID = 1L;
	//
	// @Override
	// protected void onButtonClicked(AjaxRequestTarget target) {
	// //setResponsePage(UsersAdministrationPage.class);
	// dialog.close(target);
	// target.add(parent);
	// }
	// };
	//
	// dialog.setButtons(ok);
	// //dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));
	//
	// return dialog;
	//
	// }

	private Dialog deleteConfirmationDialog() {

		final Dialog dialog = new Dialog("delete_confirmation_dialog");

		// labelText = "original2";
		// dialog.add(new Label("delete_confirmation_dialog_text",
		// "Esta seguro que desea eliminar el usuario?"));
		dialog.add(deleteConfirmationLabel);
		System.out.println("USERID " + userId);
		// System.out.println("USER: " + user);
		// labelText = "original3";

		AjaxDialogButton yesButton = new AjaxDialogButton("Si") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {

				System.out.println("Borro user");

				System.out.println("USER ES : " + deleteUser);
				userService.deleteUser(deleteUser);

				successDialog.setText("Usuario eliminado.");
				target.add(successDialog);
				// JsScopeUiEvent.quickScope(deleteConfirmationdialog.close().render());
				// JsScope.quickScope(dialog.close().render());
				// deleteConfirmationdialog.close(target);
				successDialog.open(target);
				// setResponsePage(MessagesAdministrationPage.class);
				dialog.close(target);
				
				showOrHideTable();
			}
		};

		DialogButton noButton = new DialogButton("No",
				JsScope.quickScope(dialog.close().render()));

		dialog.setButtons(noButton, yesButton);
		// dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close()));

		return dialog;

	}

	private Form<User> searchUserForm(final WebMarkupContainer parent) {

		Form<User> form = new Form<User>("searchUserForm");

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				new User());

		form.setDefaultModel(model);

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

		List<String> genderList = Arrays.asList(new String[] { "Masculino",
				"Femenino" });

		final DropDownChoice<String> gender = new DropDownChoice<String>(
				"gender", new Model<String>(), genderList);

		searchFields.add(gender);

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

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("selected user(s): "
						+ group.getDefaultModelObjectAsString());

				@SuppressWarnings("unchecked")
				List<User> removedUsers = (List<User>) group
						.getDefaultModelObject();
				
				userService.deleteUsers(removedUsers);
				
//				Collection<User> removedUsers = group.getConvertedInput();
//				
//				Iterator<User> iterator = removedUsers.iterator();
//				
//				while ( iterator.hasNext() ) {
//					User deleteUser = iterator.next();
//					System.out.println("------------- USER: " + deleteUser);
//					userService.deleteUser(deleteUser);
//				}

//				for (User aUser : removedUsers) {
//					userService.deleteUser(aUser);
//				}
				
				
//				if (dataView.getItemCount() <= 0) {
//					//this.setVisible(false);
//					form.setVisible(false);
//					dataView.setVisible(false);
//					footerNavigator.setVisible(false);
//					feedbackMessage.setVisible(true);
//				} else {
//					//this.setVisible(true);
//					form.setVisible(true);
//					dataView.setVisible(true);
//					footerNavigator.setVisible(true);
//					feedbackMessage.setVisible(false);
//				}
				
				showOrHideTable();

				target.add(parent);

				// System.out.println("BOOKS: " + books);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				System.out.println("ERROR EN SEARCH USER");
			}

		};

		form.add(deleteUser);

		searchFields.add(new AjaxSubmitLink("searchUser") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("viewfalse");
				// dataView.setVisible(false);
				// footerNavigator.setVisible(false);

				searchFields.add(AttributeModifier.replace("style",
						"display: block;"));
				// String bookIdString =
				try {
					searchUserId = new Long(userId
							.getDefaultModelObjectAsString());
				} catch (NumberFormatException ex) {
					searchUserId = null;
				}

				// if
				// (!StringUtils.isBlank(userId.getDefaultModelObjectAsString()))
				// {
				// searchUserId = Long.valueOf(userId
				// .getDefaultModelObjectAsString());
				// } else {
				// searchUserId = null;
				// }

				searchCountry = new String(country
						.getDefaultModelObjectAsString());
				searchUsername = new String(username
						.getDefaultModelObjectAsString());

				if (gender.getDefaultModelObjectAsString().equals("Masculino")) {
					searchGender = Gender.MALE;
				} else if (gender.getDefaultModelObjectAsString().equals(
						"Femenino")) {
					searchGender = Gender.FEMALE;
				} else {
					searchGender = null;
				}

				
				searchHighAge = highAge.getModelObject();
				searchLowAge = lowAge.getModelObject();
				
//				try {
//					searchLowAge = new Integer(lowAge
//							.getDefaultModelObjectAsString());
//				} catch (NumberFormatException ex) {
//					searchLowAge = null;
//				}
//
//				try {
//					searchHighAge = new Integer(highAge
//							.getDefaultModelObjectAsString());
//				} catch (NumberFormatException ex) {
//					searchHighAge = null;
//				}

				if (!StringUtils.isBlank(lowRegistrationDate
						.getDefaultModelObjectAsString())) {
					System.out.println("lowRegistrationDate: "
							+ lowRegistrationDate
									.getDefaultModelObjectAsString());
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
					searchHighRegistrationDate).iterator();
		}

		public int size() {
			return userService.getCount(searchUserId, searchUsername,
					searchGender, searchLowAge, searchHighAge, searchCountry,
					searchLowRegistrationDate, searchHighRegistrationDate);
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
		String newTitle = "Booktube - Writers";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
