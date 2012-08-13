package com.booktube.pages;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Message;
import com.booktube.model.User;
import com.booktube.service.MessageService;

public class MessagesPage extends BasePage {
	
	private static final long serialVersionUID = 1L;

	@SpringBean
    MessageService messageService;
	
	private User user;
	
	public static final int MESSAGES_PER_PAGE = 5;
	
	public MessagesPage() {
		user = WiaSession.get().getLoggedInUser();
		//User user = new User("username", "firstname", "lastname");
		//userService.insertUser(user);
		//WicketApplication.instance().getUserService().insertUser(user);
		//List<User> users = WicketApplication.instance().getUserService()
		//List<User> users = userService.getAllUsers(0, Integer.MAX_VALUE);
		final WebMarkupContainer parent = new WebMarkupContainer("messages");
		parent.setOutputMarkupId(true);
		add(parent);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", MessagesPage.class), new ResourceModel("messagesPageTitle").getObject());
		
		//parent.add(listWriters("writerList", users));
		DataView<Message> dataView = messageList("messagesList");

		parent.add(dataView);

		PagingNavigator footerNavigator = new PagingNavigator("footerPaginator", dataView);
		parent.add(footerNavigator);
		
		final Label feedbackMessage = new Label("feedbackMessage", new ResourceModel("noResults"));
		parent.add(feedbackMessage);
		
		if ( dataView.getItemCount() > 0 ) {
			footerNavigator.setVisible(true);
			feedbackMessage.setVisible(false);
		}
		else {
			footerNavigator.setVisible(false);
			feedbackMessage.setVisible(true);
		}

	}

	private DataView<Message> messageList(String label) {

		IDataProvider<Message> dataProvider = new MessageProvider();
		
		DataView<Message> dataView = new DataView<Message>("messageList", dataProvider,
				MESSAGES_PER_PAGE) {
			
					private static final long serialVersionUID = 1L;

			protected void populateItem(Item<Message> item) {
				final Message message = (Message) item.getModelObject();
				System.out.println("MESSAGE: " + message.getText());
				CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(message);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("messageId", message.getId());
				//item.add(new Label("id"));
				
				final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, getLocale());
				
				if ( message.isRead() || message.getSender().equals(user) ) {
					item.add(new Label("subject"));
					item.add(new Label("receiver"));
					item.add(new Label("sender"));
					item.add(new Label("date", dateFormat.format(message.getDate())));
				}
				else {
					item.add(new Label("subject", "<b>" + message.getSubject() + "</b>").setEscapeModelStrings(false));
					item.add(new Label("sender", "<b>" + message.getSender() + "</b>").setEscapeModelStrings(false));
					item.add(new Label("receiver", "<b>" + message.getReceiver() + "</b>").setEscapeModelStrings(false));
					item.add(new Label("date", "<b>" + dateFormat.format(message.getDate()) + "</b>").setEscapeModelStrings(false));
				}
				
				item.add(new Link<Message>("detailsLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					public void onClick() {
						setResponsePage(ShowMessagePage.class, parameters);
					}

				});
				item.add(new Link<Message>("answerLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					public void onClick() {
						setResponsePage(AnswerMessagePage.class, parameters);
					}

				});
				item.add(new Link<Message>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = -7155146615720218460L;

					public void onClick() {

						Message message = (Message) getModelObject();
						//Long messageId = message.getId();

						messageService.deleteMessage(message);
						//System.out.println("User " + messageId + " deleted.");

						setResponsePage(MessagesPage.this);
					}

				});
			}
			
		};
	
		return dataView;
	}
	
	class MessageProvider implements IDataProvider<Message> {

		private static final long serialVersionUID = 1L;
		private List<Message> messages;

		public MessageProvider() {
		}

		public Iterator<Message> iterator(int first, int count) {
			this.messages = messageService.getAllMessages(user, first, count);
			Collections.sort(this.messages, Message.getAnswerDateComparator());
			return this.messages.iterator();
		}

		public int size() {
			return messageService.countMessages(user);
		}

		public IModel<Message> model(Message message) {
			return new CompoundPropertyModel<Message>(message);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void setPageTitle() {
		String newTitle = "Booktube - " + new ResourceModel("messagesPageTitle").getObject(); 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}


}
