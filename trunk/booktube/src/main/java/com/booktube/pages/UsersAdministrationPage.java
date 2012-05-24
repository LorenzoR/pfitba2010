package com.booktube.pages;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.Campaign;
import com.booktube.model.User;
import com.booktube.pages.WritersPage.WriterProvider;
import com.booktube.service.UserService;

public class UsersAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = 837695410825256207L;

	@SpringBean
	UserService userService;

	public static final int WRITERS_PER_PAGE = 5;

	private static Dialog deleteDialog;
	private static Dialog deleteConfirmationDialog;

	private static Long userId;

	private static User deleteUser;

	private static String deleteUsername;

	private Label deleteConfirmationLabel = new Label(
			"delete_confirmation_dialog_text", new PropertyModel(this,
					"deleteConfirmationText")) {
		{
			setOutputMarkupId(true);
		}
	};
	
	private String deleteConfirmationText;
	
	private Label successDialogLabel = new Label("success_dialog_text", new PropertyModel(this, "successDialogText")) {
		{
			setOutputMarkupId(true);
		}
	};

	private String successDialogText;
	

	public UsersAdministrationPage() {
		super();
		final WebMarkupContainer parent = new WebMarkupContainer(
				"usersContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		parent.add(new Label("pageTitle", "Users Administration Page"));

		DataView<User> dataView = writerList("writerList");

		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));

		deleteDialog = deleteDialog();
		parent.add(deleteDialog);

		deleteConfirmationDialog = deleteConfirmationDialog();
		parent.add(deleteConfirmationDialog);

		String newTitle = "Booktube - Users Administration";
		super.get("pageTitle").setDefaultModelObject(newTitle);

	}

	private DataView<User> writerList(String label) {

		IDataProvider<User> dataProvider = new WriterProvider();

		DataView<User> dataView = new DataView<User>(label, dataProvider,
				WRITERS_PER_PAGE) {

			protected void populateItem(Item<User> item) {
				final User user = (User) item.getModelObject();
				CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
						user);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("userId", user.getId());
				item.add(new Label("id"));
				item.add(new Label("username"));
				item.add(new Label("firstname"));
				item.add(new Label("lastname"));
				item.add(new BookmarkablePageLink<Object>("detailsLink",
						ShowUserPage.class, parameters));
				item.add(new Link("editLink", item.getModel()) {
					public void onClick() {
						setResponsePage(new EditWriterPage(user.getId(),
								UsersAdministrationPage.this));
					}

				});
				item.add(new AjaxLink<User>("deleteLink", item.getModel()) {

					@Override
					public void onClick(AjaxRequestTarget target) {
						
						//userId = user.getId();
						deleteUser = user;
						System.out.println("USERIDDDD " + userId);
						System.out.println("USER ES : " + deleteUser);

						deleteConfirmationDialog.open(target);

						deleteUsername = deleteUser.getUsername();

						deleteConfirmationText = "Esta seguro que desea eliminar el usuario "
								+ deleteUsername + " ?";

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

	private Dialog deleteDialog() {

		Dialog dialog = new Dialog("success_dialog");

		dialog.add(successDialogLabel);

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				setResponsePage(UsersAdministrationPage.class);

			}
		};

		dialog.setButtons(ok);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));

		return dialog;

	}

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
				
				successDialogText = "Usuario " + deleteUsername + " eliminado.";
				target.add(successDialogLabel);
				// JsScopeUiEvent.quickScope(deleteConfirmationdialog.close().render());
				JsScope.quickScope(dialog.close().render());
				// deleteConfirmationdialog.close(target);
				deleteDialog.open(target);
				// setResponsePage(MessagesAdministrationPage.class);

			}
		};

		DialogButton noButton = new DialogButton("No",
				JsScope.quickScope(dialog.close().render()));

		dialog.setButtons(yesButton, noButton);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close()));

		return dialog;

	}

	class WriterProvider implements IDataProvider<User> {

		private List<User> users;

		public WriterProvider() {
		}

		public Iterator<User> iterator(int first, int count) {

			this.users = userService.getAllUsers(first, count);

			return this.users.iterator();
		}

		public int size() {
			return userService.getCount();
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
