package com.booktube.pages;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Message;
import com.booktube.model.User;
import com.booktube.model.Message.Type;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class AnswerMessagePage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	MessageService messageService;

	@SpringBean
	UserService userService;

	private User user;
	
	private Message message;

	public AnswerMessagePage(PageParameters pageParameters) {

		// User user = new User("username", "firstname", "lastname");
		// userService.insertUser(user);
		// WicketApplication.instance().getUserService().insertUser(user);
		// List<User> users = WicketApplication.instance().getUserService()
		// List<User> users = userService.getAllUsers();
		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", AnswerMessagePage.class, pageParameters), new ResourceModel("answerMessagePageTitle").getObject());
		
		Long messageId = pageParameters.get("messageId").toLong();
		message = messageService.getMessage(messageId);

		user = WiaSession.get().getLoggedInUser();

		List<Message> messageList = message.getAllAnswers();
		
		Form<Message> form = answerForm(parent, messageList);
		parent.add(form);

		Label registerMessage = new Label("registerMessage",
				new ResourceModel("registerMessage"));
		parent.add(registerMessage);

		if (user == null) {
			form.setVisible(false);
		} else {
			registerMessage.setVisible(false);
		}
		
		//Collections.reverse(messageList);
		//messageList.add(message);

		PropertyListView<Message> propertyListView = new PropertyListView<Message>("messageList", messageList) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem<Message> item) {
				final Message message = (Message) item.getModelObject();
				CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(message);
				
				if ( !user.getUsername().equals(message.getSender().getUsername()) ) {
					message.setRead(true);
				}
				messageService.updateMessage(message);
				
				item.setDefaultModel(model);
				
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				item.add(new Label("date"));
				item.add(new Label("text"));
				
			}
		};
		
		parent.add(propertyListView);

	}

	private Form<Message> answerForm(final WebMarkupContainer parent, final List<Message> messages) {
		Form<Message> form = new Form<Message>("form");

		final Message answer = new Message();
		
		CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(
				answer);

		form.setDefaultModel(model);
		
		final TextArea<Message> editor = new TextArea<Message>("text");
		editor.setOutputMarkupId(true);

		add(form);

		form.add(editor);
		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				
				
				//Message answer = new Message(type, "RE: " + message.getSubject(), editor
				//		.getDefaultModelObjectAsString(), user, message.getSender());
				answer.setType(Type.ANSWER);
				answer.setSubject("RE: " + message.getSubject());
				answer.setSender(user);
				answer.setReceiver(message.getSender());
				answer.setRead(false);
				
				//Set<Message> answers = new HashSet<Message>();
				//answers.add(answer);
				
				message.getLastAnswer().setAnswer(answer);
				
				messageService.updateMessage(message);
				
				messages.add(0, answer);
				
				target.add(parent);

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
		String newTitle = "Booktube - " + new ResourceModel("answerMessagePageTitle").getObject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
