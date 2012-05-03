package com.booktube.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Message;
import com.booktube.model.MessageDetail;
import com.booktube.model.User;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class ShowUserPage extends BasePage {
	
	@SpringBean
	UserService userService;

	private final User showUser;

	public ShowUserPage(final PageParameters pageParameters) {

		Long userId = pageParameters.get("userId").toLong();

		showUser = userService.getUser(userId);

		final WebMarkupContainer parent = new WebMarkupContainer(
				"userDetails");
		parent.setOutputMarkupId(true);
		add(parent);

		String newTitle = "Booktube - User " + showUser.getUsername();
		super.get("pageTitle").setDefaultModelObject(newTitle);

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				showUser);
		parent.setDefaultModel(model);
		parent.add(new Label("id"));
		parent.add(new Label("username"));
		parent.add(new Label("firstname"));
		parent.add(new Label("lastname"));
		parent.add(new Label("gender"));
		parent.add(new Label("birthdate"));
		parent.add(new Label("country"));
		parent.add(new Label("level"));

	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
