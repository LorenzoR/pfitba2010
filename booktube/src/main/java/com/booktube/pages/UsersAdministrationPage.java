package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class UsersAdministrationPage extends AdministrationPage{
	private static final long serialVersionUID = 837695410825256207L;
	public Page backPage;

	//public AdministrationPage(final PageParameters parameters) {
	public UsersAdministrationPage() {
			super();
			final WebMarkupContainer parent = new WebMarkupContainer("usersContainer");
			parent.setOutputMarkupId(true);
			add(parent);
			
			parent.add( new Label("pageTitle", "Users Administration Page"));
			
	}

}
