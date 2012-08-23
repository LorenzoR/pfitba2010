package com.booktube.pages;

import java.util.Arrays;
import java.util.List;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Message;
import com.booktube.model.Message.Subject;
import com.booktube.model.User;
import com.booktube.model.Message.Type;
import com.booktube.model.User.Level;
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

	}

	private Form<Message> newContactForm(final WebMarkupContainer parent) {
		
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
				
				// Agrego los operadores que no recivieron ningun mensaje
				List<User> operators = userService.getUsers(0, Integer.MAX_VALUE, Level.OPERATOR);
				
				for ( User anOperator : operators ) {
					if ( !receiverMap.containsValue(anOperator) ) {
						receiverMap.put(0L, anOperator);
					}
				}
				
				if ( receiverMap.size() <= 0) {
					// TODO Auto-generated method stub
					//throw new AbortWithHttpErrorCodeException(404);
				}
				
				Entry<Long,User> receiver = (Entry<Long, User>) receiverMap.entrySet().toArray()[0];
				
				message.setReceiver(receiver.getValue());
				messageService.insertMessage(message);
				
				dialog.open(target);
				
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
