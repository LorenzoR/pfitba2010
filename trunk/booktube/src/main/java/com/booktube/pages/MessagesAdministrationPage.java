package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class MessagesAdministrationPage extends AdministrationPage{	
	private static final long serialVersionUID = -8291402772149958339L;
	public Page backPage;

	//public AdministrationPage(final PageParameters parameters) {
	public MessagesAdministrationPage() {
			super();
			final WebMarkupContainer parent = new WebMarkupContainer("messagesContainer");
			parent.setOutputMarkupId(true);
			add(parent);
			
			parent.add( new Label("pageTitle", "Messages Administration Page"));
			
			String newTitle = "Booktube - Messages Administration"; 
			super.get("pageTitle").setDefaultModelObject(newTitle);
			
	}

}
