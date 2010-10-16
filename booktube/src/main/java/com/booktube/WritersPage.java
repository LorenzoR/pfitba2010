package com.booktube;

import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.User;
import com.booktube.service.UserService;


public class WritersPage extends BasePage {

	@SpringBean
    UserService userService;
	
	public WritersPage() {

		//User user = new User("username", "firstname", "lastname");
		//userService.insertUser(user);
		//WicketApplication.instance().getUserService().insertUser(user);
		//List<User> users = WicketApplication.instance().getUserService()
		List<User> users = userService.getAllUsers();
		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		parent.add(listWriters("writerList", users));

	}

	PropertyListView<Object> listWriters(String label, List<User> users) {

		PropertyListView<Object> writersPLV = new PropertyListView<Object>(label, users) {

			protected void populateItem(ListItem<Object> item) {
				User user = (User) item.getModelObject();
				final PageParameters parameters = new PageParameters();
				parameters.put("user", user.getId());
				item.add(new Label("userId", user.getId().toString()));
				item.add(new Label("username", user.getUsername()));
				item.add(new Label("firstname", user.getFirstname()));
				item.add(new Label("lastname", user.getLastname()));
			}
		};
		
		return writersPLV;

	}

}
