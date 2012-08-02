package com.booktube.pages;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.hibernate.StaleStateException;
import org.odlabs.wiquery.core.effects.sliding.SlideToggle;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.Message;
import com.booktube.pages.customComponents.DynamicLabel;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.service.MessageService;

public class MessagesAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = -8291402772149958339L;

	@SpringBean
	MessageService messageService;

	private static SuccessDialog<?> successDialog;
	private static Dialog deleteConfirmationDialog;

	private DynamicLabel deleteConfirmationLabel = new DynamicLabel("delete_confirmation_dialog_text", new Model<String>());
	
	private final DataView<Message> dataView;
	private AjaxPagingNavigator footerNavigator;
	private final WebMarkupContainer searchButton;
	private final Form<Message> searchForm;
	
	private final CheckGroup<Message> group;
	
	private static Message deleteMessage;
	private static List<Message> deleteMessages;

	private Long searchMessageId;
	private String searchSubject;
	private String searchSender;
	private String searchReceiver;
	private Date searchLowMessageDate;
	private Date searchHighMessageDate;

	public MessagesAdministrationPage() {
		super();
		final WebMarkupContainer parent = new WebMarkupContainer(
				"messagesContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", MessagesAdministrationPage.class), new ResourceModel("messageAdministrationPageTitle").getObject());
		
//		parent.add(new Label("pageTitle", "Messages Administration Page"));

		dataView = messageList("messageList");

		searchForm = searchMessageForm(parent);
		parent.add(searchForm);

		group = new CheckGroup<Message>("group", new ArrayList<Message>());
		group.add(dataView);
		group.add(new CheckGroupSelector("groupSelector"));

		searchForm.add(group);

		searchButton = createButtonWithEffect(
				"searchMessageLink", "searchFields", new SlideToggle());
		parent.add(searchButton);
		
		if (dataView.getItemCount() > 0) {
			feedbackMessage.setVisible(false);
		}
		else {
			feedbackMessage.setVisible(true);
			searchForm.setVisible(false);
			footerNavigator.setVisible(false);
			searchButton.setVisible(false);
		}

		successDialog = new SuccessDialog<WorksAdministrationPage>("success_dialog",  new ResourceModel("messageDeleted").getObject(), parent);
		parent.add(successDialog);

		deleteConfirmationDialog = deleteConfirmationDialog();
		parent.add(deleteConfirmationDialog);

//		String newTitle = "Booktube - Messages Administration";
//		super.get("pageTitle").setDefaultModelObject(newTitle);

	}

	private Dialog deleteConfirmationDialog() {

		final Dialog dialog = new Dialog("delete_confirmation_dialog");

//		dialog.add(new Label("delete_confirmation_dialog_text",
//				new ResourceModel("deleteMessageQuestion")));
		dialog.add(deleteConfirmationLabel);

		AjaxDialogButton yesButton = new AjaxDialogButton(new ResourceModel("yes").getObject()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				System.out.println("Borro mensaje");
				//Message message = messageService.getMessage(messageId);
				
				if ( deleteMessage != null ) {
					messageService.deleteMessage(deleteMessage);
					deleteMessage = null;
					successDialog.setText( new ResourceModel("messageDeleted").getObject());
				}
				else if ( deleteMessages != null ) {
					
					Collections.sort(deleteMessages, Message.getDateComparatorDesc());
					
					System.out.println("** REMOVED MSG: " + deleteMessages.toString());
					
					
					for (Message aMessage : deleteMessages) {
						try {
							messageService.deleteMessage(aMessage);
						} catch ( StaleStateException ex ) {
							System.out.println("Mensaje ya eliminado.");
						}
					}
					
					deleteMessages = null;
					successDialog.setText( new ResourceModel("messagesDeleted").getObject());
					
				}
				
				dialog.close(target);
				target.add(successDialog);

				successDialog.open(target);
				
				showOrHideTable();
				
//				messageService.deleteMessage(deleteMessage);
//				// JsScopeUiEvent.quickScope(deleteConfirmationdialog.close().render());
//				JsScope.quickScope(dialog.close().render());
//				// deleteConfirmationdialog.close(target);
//				successDialog.open(target);
//				// setResponsePage(MessagesAdministrationPage.class);
//				dialog.close(target);
//				
//				showOrHideTable();
			}
		};

		DialogButton noButton = new DialogButton(new ResourceModel("no").getObject(),
				JsScope.quickScope(dialog.close().render()));

		dialog.setButtons(noButton, yesButton);
		//dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close()));

		return dialog;

	}

	private DataView<Message> messageList(String label) {

		IDataProvider<Message> dataProvider = new MessageProvider();

		DataView<Message> dataView = new DataView<Message>(label, dataProvider,
				ITEMS_PER_PAGE) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(Item<Message> item) {
				final Message message = (Message) item.getModelObject();
				// final String receivers = getReceivers(message);
				System.out.println("MESSAGE: " + message.getText());
				CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(
						message);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("messageId", message.getId());
				// item.add(new Label("id"));
				item.add(new Check<Message>("checkbox", item.getModel()));
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				item.add(new Label("receiver"));
				item.add(new Label("date"));

				item.add(new Link<Message>("detailsLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					public void onClick() {
						setResponsePage(ShowMessagePage.class, parameters);
					}

				});
				item.add(new Link<Message>("editLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					public void onClick() {
						setResponsePage(ShowMessagePage.class, parameters);
						// setResponsePage(new EditWriterPage(user.getId(),
						// MessagePage.this));
					}

				});
				item.add(new AjaxLink<Message>("deleteLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {

						deleteMessage = (Message) getModelObject();
						
						deleteConfirmationDialog.open(target);

						deleteConfirmationLabel.setLabel(new ResourceModel("deleteMessageQuestion").getObject());

						target.add(deleteConfirmationLabel);
						
						//messageId = message.getId();
						// messageService.deleteMessage(message);
						// userService.deleteUser(message);
						// System.out.println("User " + messageId +
						// " deleted.");

						// setResponsePage(MessagesAdministrationPage.class);
						// dialog.open(target);
						//deleteConfirmationDialog.open(target);
					}

				});
			}

		};

		return dataView;
	}

	private Form<Message> searchMessageForm(final WebMarkupContainer parent) {

		Form<Message> form = new Form<Message>("searchMessageForm");

		CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(
				new Message());

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

		final TextField<Message> messageId = new TextField<Message>("id");
		searchFields.add(messageId);

		final TextField<Message> subject = new TextField<Message>("subject");
		searchFields.add(subject);

		final TextField<Message> sender = new TextField<Message>("sender");
		searchFields.add(sender);

		final TextField<Message> receiver = new TextField<Message>("receiver");
		searchFields.add(receiver);

		final DatePicker<Date> lowMessageDate = createDatePicker(
				"lowMessageDate", dateFormat);
		searchFields.add(lowMessageDate);

		final DatePicker<Date> highMessageDate = createDatePicker(
				"highMessageDate", dateFormat);
		searchFields.add(highMessageDate);

		final AjaxSubmitLink deleteMessage = new AjaxSubmitLink("deleteMessage") {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("selected Message(s): "
						+ group.getDefaultModelObjectAsString());

				deleteConfirmationDialog.open(target);

				deleteConfirmationLabel
						.setLabel(new ResourceModel("deleteMessagesQuestion").getObject());

				target.add(deleteConfirmationLabel);
				
				deleteMessages = (List<Message>) group
						.getDefaultModelObject();
				
				showOrHideTable();

//				Collections.sort(removedMessages, Message.getDateComparatorDesc());
//				
//				System.out.println("** REMOVED MSG: " + removedMessages.toString());
//				
//				
//				for (Message aMessage : removedMessages) {
//					try {
//						messageService.deleteMessage(aMessage);
//					} catch ( StaleStateException ex ) {
//						System.out.println("Mensaje ya eliminado.");
//					}
//				}

				
				
//				if (dataView.getItemCount() <= 0) {
//					this.setVisible(false);
//					footerNavigator.setVisible(false);
//					feedbackMessage.setVisible(true);
//				} else {
//					this.setVisible(true);
//					footerNavigator.setVisible(true);
//					feedbackMessage.setVisible(false);
//				}

				//target.add(parent);

				// System.out.println("BOOKS: " + books);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		};

		form.add(deleteMessage);

		searchFields.add(new AjaxSubmitLink("searchMessage") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				searchFields.add(AttributeModifier.replace("style",
						"display: block;"));
				// String bookIdString =
				try {
					searchMessageId = new Long(messageId
							.getDefaultModelObjectAsString());
				} catch (NumberFormatException ex) {
					searchMessageId = null;
				}

				searchSubject = new String(subject
						.getDefaultModelObjectAsString());
				searchSender = new String(sender
						.getDefaultModelObjectAsString());
				searchReceiver = new String(receiver
						.getDefaultModelObjectAsString());

				if (!StringUtils.isBlank(lowMessageDate
						.getDefaultModelObjectAsString())) {
					System.out.println("LowDate: "
							+ lowMessageDate.getDefaultModelObjectAsString());
					try {
						searchLowMessageDate = (Date) formatter
								.parse(lowMessageDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						searchLowMessageDate = null;
					}
				} else {
					searchLowMessageDate = null;
				}

				if (!StringUtils.isBlank(highMessageDate
						.getDefaultModelObjectAsString())) {
					try {
						searchHighMessageDate = (Date) formatter
								.parse(highMessageDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						searchHighMessageDate = null;
					}
				} else {
					searchHighMessageDate = null;
				}

				if (dataView.getItemCount() <= 0) {
					deleteMessage.setVisible(false);
					group.setVisible(false);
					footerNavigator.setVisible(false);
					feedbackMessage.setVisible(true);
				} else {
					deleteMessage.setVisible(true);
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
			searchForm.setVisible(false);
			dataView.setVisible(false);
			footerNavigator.setVisible(false);
			searchButton.setVisible(false);
			feedbackMessage.setVisible(true);
		} else {
			searchForm.setVisible(true);
			dataView.setVisible(true);
			footerNavigator.setVisible(true);
			searchButton.setVisible(true);
			feedbackMessage.setVisible(false);
		}
	}

	/*
	 * private String getReceivers(Message message) { String receivers = ""; for
	 * (MessageDetail aMessageDetail : message.getReceiver()) { if (
	 * aMessageDetail.getReceiver() != null ) { receivers +=
	 * aMessageDetail.getReceiver().getUsername() + ", "; } } return receivers;
	 * }
	 */

	class MessageProvider implements IDataProvider<Message> {

		private static final long serialVersionUID = 1L;

		public MessageProvider() {
		}

		public Iterator<Message> iterator(int first, int count) {
			return messageService.getMessages(first, count, searchMessageId,
					searchSubject, searchSender, searchReceiver,
					searchLowMessageDate, searchHighMessageDate).iterator();
		}

		public int size() {
			return messageService.getCount(searchMessageId, searchSubject,
					searchSender, searchReceiver, searchLowMessageDate,
					searchHighMessageDate);
		}

		public IModel<Message> model(Message message) {
			return new CompoundPropertyModel<Message>(message);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

}
