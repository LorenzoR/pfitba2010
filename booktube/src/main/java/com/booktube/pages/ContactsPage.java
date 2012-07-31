package com.booktube.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Message;
import com.booktube.model.User;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class ContactsPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	MessageService messageService;

	@SpringBean
	UserService userService;
	
	User user;
	
	public ContactsPage() {

		// User user = new User("username", "firstname", "lastname");
		// userService.insertUser(user);
		// WicketApplication.instance().getUserService().insertUser(user);
		// List<User> users = WicketApplication.instance().getUserService()
		//List<User> users = userService.getAllUsers();
		final WebMarkupContainer parent = new WebMarkupContainer("messages");
		parent.setOutputMarkupId(true);
		add(parent);
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", ContactsPage.class), "Contactos");
		
		user = WiaSession.get().getLoggedInUser();
		
		List<Message> messagesFrom = messageService.getAllMessagesFrom(user, 0, Integer.MAX_VALUE);
		List<Message> messagesTo = messageService.getAllMessagesTo(user, 0, Integer.MAX_VALUE);
		
		
		parent.add(listMessages("messageFromList", messagesFrom));
		parent.add(listMessages("messageToList", messagesTo));
		
		//List<Message> messagesTo = messageService.getAllMessagesTo(user, 0, Integer.MAX_VALUE);
		
		
		
		/*user = WiaSession.get().getLoggedInUser();
		
		Form<?> form = newContactForm(parent);
		parent.add(form);
		
		Label registerMessage = new Label("registerMessage", "Debe registrarse para poder contactarnos.");
		parent.add(registerMessage);
		
		if (user == null) {
			form.setVisible(false);
		} else {
			registerMessage.setVisible(false);
		}*/
		
	}

	PropertyListView<Object> listMessages(String label, List<Message> messages) {

		PropertyListView<Object> messagesPLV = new PropertyListView<Object>(label, messages) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem<Object> item) {
				Message message = (Message) item.getModelObject();
				//final PageParameters parameters = new PageParameters();
				//parameters.put("user", user.getId());
				item.add(new Label("date", message.getDate().toString()));
				item.add(new Label("subject", message.getSubject()));
				item.add(new Label("text", message.getText()));
				
				//item.add(new PropertyListView<Object>("answers", new ArrayList<Message>(message.getAnswer())) {
				item.add(new PropertyListView<Object>("answers", new ArrayList<Message>()) {	

					private static final long serialVersionUID = 1L;

					protected void populateItem(ListItem<Object> item) {
						Message message = (Message) item.getModelObject();
						//final PageParameters parameters = new PageParameters();
						//parameters.put("user", user.getId());
						item.add(new Label("date", message.getDate().toString()));
						item.add(new Label("subject", message.getSubject()));
						item.add(new Label("text", message.getText()));
						
						//item.add(new Label("answer", message.get));
						/*item.add(new Link<Object>("deleteLink", item.getModel()) {
							private static final long serialVersionUID = -7155146615720218460L;

							public void onClick() {

								User user = (User) getModelObject();
								Integer userId = user.getId();

								userService.deleteUser(user);
								System.out.println("User " + userId + " deleted.");

								setResponsePage(WritersPage.this);
							}

						});*/
					}
					});
				
				//item.add(new Label("answer", message.get));
				/*item.add(new Link<Object>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = -7155146615720218460L;

					public void onClick() {

						User user = (User) getModelObject();
						Integer userId = user.getId();

						userService.deleteUser(user);
						System.out.println("User " + userId + " deleted.");

						setResponsePage(WritersPage.this);
					}

				});*/
			}
		};
		
		return messagesPLV;

	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - Contact"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
}
