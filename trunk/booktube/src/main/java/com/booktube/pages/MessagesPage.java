package com.booktube.pages;

import java.util.Iterator;
import java.util.List;

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

import com.booktube.WiaSession;
import com.booktube.model.Message;
import com.booktube.model.User;
import com.booktube.pages.WritersPage.WriterProvider;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class MessagesPage extends BasePage {
	
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

		//parent.add(listWriters("writerList", users));
		DataView<Message> dataView = messageList("messagesList");

		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));

	}

	private DataView<Message> messageList(String label) {

		IDataProvider<Message> dataProvider = new MessageProvider();
		
		DataView<Message> dataView = new DataView<Message>("messageList", dataProvider,
				MESSAGES_PER_PAGE) {
			
			protected void populateItem(Item<Message> item) {
				final Message message = (Message) item.getModelObject();
				System.out.println("MESSAGE: " + message.getText());
				CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(message);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("user", message.getId());
				//item.add(new Label("id"));
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				item.add(new Label("text"));
				//item.add(new Label("lastname"));
				item.add(new Link("editLink", item.getModel()) {
					public void onClick() {
						//setResponsePage(new EditWriterPage(user.getId(),
						//		MessagePage.this));
					}

				});
				item.add(new Link<Message>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = -7155146615720218460L;

					public void onClick() {

						Message message = (Message) getModelObject();
						Long messageId = message.getId();

						//userService.deleteUser(message);
						//System.out.println("User " + messageId + " deleted.");

						setResponsePage(MessagesPage.this);
					}

				});
			}
			
		};
	
		return dataView;
	}
	
	class MessageProvider implements IDataProvider<Message> {

		private List<Message> messages;

		public MessageProvider() {
		}

		public Iterator<Message> iterator(int first, int count) {

			this.messages = messageService.getAllMessagesTo(user, first, count);
			return this.messages.iterator();
		}

		public int size() {
			return messageService.countMessagesTo(user);
		}

		public IModel<Message> model(Message message) {
			return new CompoundPropertyModel<Message>(message);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}


}
