package com.booktube.pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.model.MessageDetail;
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
		
		final WebMarkupContainer parent = new WebMarkupContainer("messageDetails");
		parent.setOutputMarkupId(true);
		add(parent);

		String newTitle = "Booktube - Message " + message.getSubject(); 
		super.get("pageTitle").setDefaultModelObject(newTitle);
		
		CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(message);
		parent.setDefaultModel(model);
		parent.add(new Label("text"));
		parent.add(new Label("sender"));
		parent.add(new Label("subject"));
		
		MessageDetail messageDetail = messageService.getMessageDetail(message, user);
		messageService.setMessageRead(messageDetail);
		
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
