package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.ResourceModel;

public class SimpleContactPage extends BasePage {	
	private static final long serialVersionUID = 8580310589800311892L;
	public Page backPage;

	public SimpleContactPage() {
		final WebMarkupContainer parent = new WebMarkupContainer("simpleContactContainer");
		parent.setOutputMarkupId(true);
		add(parent);
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", SimpleContactPage.class), new ResourceModel("contactsPageTitle").getObject());
		
		parent.add(new Label("aContent", "Aca va el contenido de la Simple Contact Page."));
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		
	}
	
}
