package com.booktube.pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.User;
import com.booktube.service.UserService;

public class ShowUserPage extends BasePage {
	
	private static final long serialVersionUID = 1L;

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
		parent.add(new Label("city"));
		parent.add(new Label("level"));
		parent.add(new Label("registrationDate"));

	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
