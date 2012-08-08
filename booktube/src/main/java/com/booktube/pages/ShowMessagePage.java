package com.booktube.pages;

import java.text.DateFormat;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Message;
import com.booktube.model.User;
import com.booktube.model.User.Level;
import com.booktube.service.MessageService;

public class ShowMessagePage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	MessageService messageService;


	private final Message message;
	
	private final User user;

	public ShowMessagePage(final PageParameters pageParameters) {

		Long messageId = pageParameters.get("messageId").toLong();

		message = messageService.getMessage(messageId);
		
		user = WiaSession.get().getLoggedInUser();

		final WebMarkupContainer parent = new WebMarkupContainer(
				"messageDetails");
		parent.setOutputMarkupId(true);
		add(parent);

		String newTitle = "Booktube - " + new ResourceModel("messagesPageTitle").getObject() + " " + message.getSubject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
		
		List<Message> messageList = message.getAllAnswers();
		
		if ( user.getLevel() == Level.ADMIN ) {
			addBreadcrumb(new BookmarkablePageLink<Object>("link", AdministrationPage.class), new ResourceModel("administrationPageTitle").getObject());
			addBreadcrumb(new BookmarkablePageLink<Object>("link", MessagesAdministrationPage.class), new ResourceModel("messageAdministrationPageTitle").getObject());
		}
		else {
			addBreadcrumb(new BookmarkablePageLink<Object>("link", MessagesPage.class, pageParameters), new ResourceModel("messageAdministrationPageTitle").getObject());
		}
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", ShowMessagePage.class, pageParameters), message.getSubject());
		
		//List<Message> messageList = getAnswers(message);
		
		//Collections.sort(messageList, Message.getDateComparator());
		
		ListView<Message> listview = new ListView<Message>("messageList", messageList) {

			private static final long serialVersionUID = 1L;
			final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, getLocale());

			protected void populateItem(ListItem<Message> item) {
				final Message message = (Message) item.getModelObject();
				CompoundPropertyModel<Message> model = new CompoundPropertyModel<Message>(message);
				item.setDefaultModel(model);
				
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				item.add(new Label("date", dateFormat.format(message.getDate())));
				item.add(new Label("text"));
			}
		};
		
		parent.add(listview);
		
		message.setRead(true);
		messageService.updateMessage(message);
		

	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
