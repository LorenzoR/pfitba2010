package com.booktube.pages;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.Message;
import com.booktube.model.MessageDetail;
import com.booktube.model.User;
import com.booktube.pages.MessagesPage.MessageProvider;
import com.booktube.service.MessageService;

public class MessagesAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = -8291402772149958339L;

	@SpringBean
	MessageService messageService;

	private static Dialog deleteDialog;
	private static Dialog deleteConfirmationDialog;

	public static final int MESSAGES_PER_PAGE = 5;
	
	private static Long messageId;

	public MessagesAdministrationPage() {
		super();
		final WebMarkupContainer parent = new WebMarkupContainer(
				"messagesContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		parent.add(new Label("pageTitle", "Messages Administration Page"));

		DataView<Message> dataView = messageList("messagesList");

		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));
		
		deleteDialog = deleteDialog();
		parent.add(deleteDialog);
		
		deleteConfirmationDialog = deleteConfirmationDialog();
		parent.add(deleteConfirmationDialog);

		String newTitle = "Booktube - Messages Administration";
		super.get("pageTitle").setDefaultModelObject(newTitle);

	}

	private Dialog deleteDialog() {

		Dialog dialog = new Dialog("success_dialog");
		
		dialog.add(new Label("success_dialog_text", "Mensaje eliminado!"));
		
		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				setResponsePage(MessagesAdministrationPage.class);

			}
		};

		dialog.setButtons(ok);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));
		
		return dialog;

	}

	private Dialog deleteConfirmationDialog() {

		final Dialog dialog = new Dialog("delete_confirmation_dialog");

		dialog.add(new Label("delete_confirmation_dialog_text", "Esta seguro que desea eliminar el mensaje?"));
		
		AjaxDialogButton yesButton = new AjaxDialogButton("Si") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				System.out.println("Borro mensaje");
				Message message = messageService.getMessage(messageId);
				messageService.deleteMessage(message);
				// JsScopeUiEvent.quickScope(deleteConfirmationdialog.close().render());
				JsScope.quickScope(dialog.close().render());
				// deleteConfirmationdialog.close(target);
				deleteDialog.open(target);
				// setResponsePage(MessagesAdministrationPage.class);

			}
		};

//		AjaxDialogButton noButton = new AjaxDialogButton("No") {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onButtonClicked(AjaxRequestTarget target) {
//				System.out.println("No borro mensaje");
//				dialog.close();
//				// setResponsePage(MessagesAdministrationPage.class);
//
//			}
//		};
		
		DialogButton noButton = new DialogButton("No", 
                JsScope.quickScope(dialog.close().render()));

		dialog.setButtons(yesButton, noButton);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close()));

		return dialog;

	}

	private DataView<Message> messageList(String label) {

		IDataProvider<Message> dataProvider = new MessageProvider();

		DataView<Message> dataView = new DataView<Message>("messageList",
				dataProvider, MESSAGES_PER_PAGE) {

			protected void populateItem(Item<Message> item) {
				final Message message = (Message) item.getModelObject();
				final String receivers = getReceivers(message);
				System.out.println("MESSAGE: " + message.getText());
				CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(
						message);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("messageId", message.getId());
				// item.add(new Label("id"));
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				item.add(new Label("receiver", receivers));
				item.add(new Label("date"));

				item.add(new Link("detailsLink", item.getModel()) {
					public void onClick() {
						setResponsePage(ShowMessagePage.class, parameters);
					}

				});
				item.add(new Link("editLink", item.getModel()) {
					public void onClick() {
						setResponsePage(ShowMessagePage.class, parameters);
						// setResponsePage(new EditWriterPage(user.getId(),
						// MessagePage.this));
					}

				});
				item.add(new AjaxLink<Message>("deleteLink", item.getModel()) {

					@Override
					public void onClick(AjaxRequestTarget target) {

						Message message = (Message) getModelObject();
						messageId = message.getId();
						messageService.deleteMessage(message);
						// userService.deleteUser(message);
						// System.out.println("User " + messageId +
						// " deleted.");

						// setResponsePage(MessagesAdministrationPage.class);
						// dialog.open(target);
						deleteConfirmationDialog.open(target);
					}

				});
			}

		};

		return dataView;
	}

	private String getReceivers(Message message) {
		String receivers = "";
		for (MessageDetail aMessageDetail : message.getReceiver()) {
			if ( aMessageDetail.getReceiver() != null ) {
				receivers += aMessageDetail.getReceiver().getUsername() + ", ";
			}
		}
		return receivers;
	}

	class MessageProvider implements IDataProvider<Message> {

		private List<Message> messages;

		public MessageProvider() {
		}

		public Iterator<Message> iterator(int first, int count) {

			this.messages = messageService.getAllMessages(first, count);
			return this.messages.iterator();
		}

		public int size() {
			return messageService.countMessages();
		}

		public IModel<Message> model(Message message) {
			return new CompoundPropertyModel<Message>(message);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

}
