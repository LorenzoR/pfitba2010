package com.booktube.pages;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Message;
import com.booktube.model.Message.Subject;
import com.booktube.model.User;
import com.booktube.model.Message.Type;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class NewContactPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	MessageService messageService;

	@SpringBean
	UserService userService;

	private final User user;
	
	private final SuccessDialog<?> dialog;

	public NewContactPage() {

		// User user = new User("username", "firstname", "lastname");
		// userService.insertUser(user);
		// WicketApplication.instance().getUserService().insertUser(user);
		// List<User> users = WicketApplication.instance().getUserService()
		// List<User> users = userService.getAllUsers();
		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", NewContactPage.class), new ResourceModel("newContactPageTitle").getObject());
		
		user = WiaSession.get().getLoggedInUser();

		Form<?> form = newContactForm(parent);
		parent.add(form);

		Label registerMessage = new Label("registerMessage",
				new ResourceModel("registerMessage"));
		parent.add(registerMessage);

//		parent.add(new BookmarkablePageLink<String>("contactsPage",
//				ContactsPage.class));

		dialog = new SuccessDialog<MessagesPage>("success_dialog", new ResourceModel("newMessageDialog").getObject(), MessagesPage.class, null);
		parent.add(dialog);
		
		if (user == null) {
			form.setVisible(false);
		} else {
			registerMessage.setVisible(false);
		}
		
		System.out.println("MSG FROM: " + messageService.countMessagesFrom(user));
		System.out.println("MSG TO: " + messageService.countMessagesTo(user));
		System.out.println("UNREAD MSG TO: " + messageService.countUnreadMessagesTo(user));

	}

	private Form<Message> newContactForm(final WebMarkupContainer parent) {
		
		/*Message message = new Message(Type.PRIVATE_MESSAGE,subject
				.getDefaultModelObjectAsString(), editor
				.getDefaultModelObjectAsString(), user);*/
		final Message message = new Message(Type.PRIVATE_MESSAGE, user);
		
		Form<Message> form = new Form<Message>("form");
		
		CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(
				message);

		form.setDefaultModel(model);

		final DropDownChoice<Subject> subject = new DropDownChoice<Subject>(
				"subject", Arrays.asList(Subject.values()),
				new EnumChoiceRenderer<Subject>(this));
		form.add(subject);
		
		final TextArea<Message> editor = new TextArea<Message>("text");
		editor.setOutputMarkupId(true);

		add(form);

		form.add(editor);
		form.add(new AjaxSubmitLink("sendMessage") {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				Map<Long, User> receiverMap = messageService.getMessagesByOperator();
				
				if ( receiverMap.size() <= 0) {
					throw new AbortWithHttpErrorCodeException(404);
				}
				
				Entry<Long,User> receiver = (Entry<Long, User>) receiverMap.entrySet().toArray()[0];
				
				//List<User> receivers = userService.getUsers(0, Integer.MAX_VALUE, Level.ADMIN);
				
				//message.setType(Type.PRIVATE_MESSAGE);
				//message.setSubject(subject.getDefaultModelObjectAsString());
				//message.setSender(user);
				
				System.out.println("+++++++ SUBJECT: " + message.getSubject());
				
				message.setReceiver(receiver.getValue());
				messageService.insertMessage(message);
				
				//messageService.sendMessages(message, receivers);
				
				dialog.open(target);
				
				//Set<MessageDetail> receiverSet = new HashSet<MessageDetail>();

				/*
				// Si el usuario es Admin
				if (true) {
					List<User> users = (List<User>) group
							.getDefaultModelObject();

					for (User receiver : users) {
						System.out.println("User: " + receiver);
						//message.addReceiver(receiver);
						receiverSet.add(new MessageDetail(receiver, message));
					}
				} else {
					User admin = userService.getUser("admin");
					receiverSet.add(new MessageDetail(admin, null));
				}

				System.out.println("Receiver: " + receiverSet.toString());
				message.setReceiver(receiverSet);
				messageService.insertMessage(message);
				*/

				// message.addReceiver(user1);

				// message.addReceiver(user2);

				// messageService.updateMessage(message);

				// comments.add(new Comment(new
				// User(ddc.getDefaultModelObjectAsString()),
				// editor.getDefaultModelObjectAsString()));
				// editor.setModel(new Model(""));
				// target.addComponent(parent);
				// target.focusComponent(editor);
				// System.out.println("ACA 1");
				/*
				 * String text = editor.getDefaultModelObjectAsString(); String
				 * username = user.getUsername(); String title =
				 * titleField.getDefaultModelObjectAsString();
				 * 
				 * //User user = userService.getUser(username); Book book = new
				 * Book(title, text, user);
				 * 
				 * 
				 * bookService.insertBook(book);
				 * System.out.println("Book inserted.");
				 * System.out.println("Title: " + title);
				 * System.out.println("Author: " + username);
				 * System.out.println("Text: " + text);
				 * 
				 * 
				 * editor.setModel(new Model("")); titleField.setModel(new
				 * Model("")); target.addComponent(parent);
				 * setResponsePage(HomePage.class);
				 */
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}
		});

		return form;
	}

	@Override
	protected void setPageTitle() {
		String newTitle = "Booktube - " + new ResourceModel("newContactPageTitle").getObject(); 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
}
