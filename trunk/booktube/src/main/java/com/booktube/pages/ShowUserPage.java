package com.booktube.pages;

import java.text.DateFormat;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.User;
import com.booktube.model.User.Level;
import com.booktube.service.UserService;

public class ShowUserPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	UserService userService;

	private final User showUser;

	public ShowUserPage(final PageParameters pageParameters) {

		Long userId = pageParameters.get("userId").toLong();

		showUser = userService.getUser(userId);

		final WebMarkupContainer parent = new WebMarkupContainer("userDetails");
		parent.setOutputMarkupId(true);
		add(parent);

		String newTitle = "Booktube - " + showUser.getUsername();
		super.get("pageTitle").setDefaultModelObject(newTitle);

		addBreadcrumb(new BookmarkablePageLink<Object>("link",
				WritersPage.class),
				new ResourceModel("writersPageTitle").getObject());
		addBreadcrumb(new BookmarkablePageLink<Object>("link",
				ShowUserPage.class, pageParameters), showUser.getUsername());

		final DateFormat dateFormat = DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.MEDIUM, getLocale());

		CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
				showUser);
		parent.setDefaultModel(model);
		WebMarkupContainer idContainer = new WebMarkupContainer("idContainer");
		idContainer.setVisible(false);
		idContainer.add(new Label("id"));
		parent.add(idContainer);
		parent.add(new Label("username"));
		parent.add(new Label("firstname"));
		parent.add(new Label("lastname"));
		//parent.add(new Label("gender"));
		parent.add(new Label("gender", new ResourceModel("Gender." + showUser.getGender().name())));
		parent.add(new Label("birthdate"));
		parent.add(new Label("country"));
		parent.add(new Label("city"));
		WebMarkupContainer levelContainer = new WebMarkupContainer(
				"levelContainer");
		levelContainer.setVisible(false);
		//levelContainer.add(new Label("level"));
		levelContainer.add(new Label("level", new ResourceModel("Level." + showUser.getLevel().name())));
		parent.add(levelContainer);
		parent.add(new Label("registrationDate", dateFormat.format(showUser
				.getRegistrationDate())));

		if (WiaSession.get().getLoggedInUser() != null
				&& WiaSession.get().getLoggedInUser().getLevel() == Level.ADMIN) {
			idContainer.setVisible(true);
			levelContainer.setVisible(true);
		}

	}

//	private static Integer getAge(Date birthdate) {
//		Calendar dob = Calendar.getInstance();
//		dob.setTime(birthdate);
//		Calendar today = Calendar.getInstance();
//		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//		if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
//			age--;
//		}
//		return age;
//	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
