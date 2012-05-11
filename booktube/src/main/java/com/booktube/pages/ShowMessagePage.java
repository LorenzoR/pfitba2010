package com.booktube.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.service.MessageService;

public class ShowMessagePage extends BasePage {

	@SpringBean
	MessageService messageService;

	private User user;
	private final Message message;

	public ShowMessagePage(final PageParameters pageParameters) {

		user = WiaSession.get().getLoggedInUser();
		Long messageId = pageParameters.get("messageId").toLong();

		message = messageService.getMessage(messageId);

		final WebMarkupContainer parent = new WebMarkupContainer(
				"messageDetails");
		parent.setOutputMarkupId(true);
		add(parent);

		String newTitle = "Booktube - Message " + message.getSubject();
		super.get("pageTitle").setDefaultModelObject(newTitle);

		CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(
				message);
		parent.setDefaultModel(model);
		parent.add(new Label("text"));
		parent.add(new Label("sender"));
		parent.add(new Label("subject"));

		//CampaignDetail messageDetail = messageService.getMessageDetail(message,
		//		user);
		//messageService.setMessageRead(messageDetail);
		
		List<Message> messageList = getAnswers(message);
		
		Collections.reverse(messageList);
		
		messageList.add(message);
		
		ListView<Message> listview = new ListView<Message>("messageList", messageList) {
			protected void populateItem(ListItem<Message> item) {
				final Message message = (Message) item.getModelObject();
				CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(message);
				item.setDefaultModel(model);
				
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				item.add(new Label("date"));
				item.add(new Label("text"));
			}
		};
		
		parent.add(listview);
		
		message.setRead(true);
		messageService.updateMessage(message);
		

	}

	private List<Message> getAnswers(Message aMessage) {

		List<Message> messageList = new ArrayList<Message>();

		System.out.println("DESEPUES MESSAGE: " + aMessage);

		User sender = aMessage.getSender();
		System.out.println("SENDER: " + sender.getUsername());
		User receiver = this.user;
		User auxUser;

		Message lastAnswer = aMessage;

		System.out.println("ANSWERS: " + lastAnswer.getAnswer().toString());

		do {

			Iterator<Message> messageIterator = lastAnswer.getAnswer()
					.iterator();
			
			while (messageIterator.hasNext()) {
				
				lastAnswer = messageIterator.next();
				
				System.out.println("USER ID: " + lastAnswer.getSender().getId());
				
				System.out.println("MESSAGE ID: " + lastAnswer.getId());

				if ( receiver == null || lastAnswer.getSender().getId().equals(receiver.getId())) {
					messageList.add(lastAnswer);
				}
			
			}

			auxUser = sender;
			sender = receiver;
			receiver = auxUser;

		} while (lastAnswer != null && lastAnswer.getAnswer().size() > 0);

		return messageList;
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
