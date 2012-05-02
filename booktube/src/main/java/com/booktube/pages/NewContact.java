package com.booktube.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.model.MessageDetail;
import com.booktube.model.User;
import com.booktube.model.Message.Type;
import com.booktube.model.User.Level;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class NewContact extends BasePage {

	@SpringBean
	MessageService messageService;

	@SpringBean
	UserService userService;

	User user;

	public NewContact() {

		// User user = new User("username", "firstname", "lastname");
		// userService.insertUser(user);
		// WicketApplication.instance().getUserService().insertUser(user);
		// List<User> users = WicketApplication.instance().getUserService()
		// List<User> users = userService.getAllUsers();
		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		user = WiaSession.get().getLoggedInUser();

		Form<?> form = newContactForm(parent);
		parent.add(form);

		Label registerMessage = new Label("registerMessage",
				"Debe registrarse para poder contactarnos.");
		parent.add(registerMessage);

		parent.add(new BookmarkablePageLink<String>("contactsPage",
				ContactsPage.class));

		if (user == null) {
			form.setVisible(false);
		} else {
			registerMessage.setVisible(false);
		}
		
		System.out.println("MSG FROM: " + messageService.countMessagesFrom(user));
		System.out.println("MSG TO: " + messageService.countMessagesTo(user));
		System.out.println("UNREAD MSG TO: " + messageService.countUnreadMessagesTo(user));

	}

	private Form<?> newContactForm(final WebMarkupContainer parent) {
		Form<?> form = new Form("form");

		List<String> subjects = Arrays.asList(new String[] { "subject1",
				"subject2", "subject3" });

		final DropDownChoice subject = new DropDownChoice("subject",
				new PropertyModel(this, ""), subjects);

		form.add(subject);

		final TextArea editor = new TextArea("textArea", new Model());
		editor.setOutputMarkupId(true);

		add(form);

		form.add(editor);
		form.add(new AjaxSubmitLink("save") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				
				Message message = new Message(Type.PRIVATE_MESSAGE,subject
						.getDefaultModelObjectAsString(), editor
						.getDefaultModelObjectAsString(), user);
				
				List<User> receivers = userService.getUsers(0, Integer.MAX_VALUE, Level.ADMIN);
				
				messageService.sendMessages(message, receivers);
				
				
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
		// TODO Auto-generated method stub
		String newTitle = "Booktube - New Contact"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
}
