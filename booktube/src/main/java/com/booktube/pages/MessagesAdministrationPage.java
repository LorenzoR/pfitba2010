package com.booktube.pages;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Page;
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

import com.booktube.model.Message;
import com.booktube.model.MessageDetail;
import com.booktube.model.User;
import com.booktube.pages.MessagesPage.MessageProvider;
import com.booktube.service.MessageService;

public class MessagesAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = -8291402772149958339L;

	@SpringBean
	MessageService messageService;

	private User user;

	public static final int MESSAGES_PER_PAGE = 5;

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

		String newTitle = "Booktube - Messages Administration";
		super.get("pageTitle").setDefaultModelObject(newTitle);

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
				item.add(new Link<Message>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = -7155146615720218460L;

					public void onClick() {

						Message message = (Message) getModelObject();
						Long messageId = message.getId();

						// userService.deleteUser(message);
						// System.out.println("User " + messageId +
						// " deleted.");

						setResponsePage(MessagesAdministrationPage.class);
					}

				});
			}

		};

		return dataView;
	}
	
	private String getReceivers(Message message) {
		String receivers = "";
		for ( MessageDetail aMessageDetail : message.getReceiver() ) {
			receivers += aMessageDetail.getReceiver().getUsername() + ", ";
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
